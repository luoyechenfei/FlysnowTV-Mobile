package com.iptv.tvplayer

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import kotlin.math.abs

/**
 * 手机 / 平板触摸手势层（手机版专用，TV 版不使用）。
 *
 * 挂在播放画面上，提供与遥控器等价、且更贴合触屏的操作：
 *  - 单击：显隐播放信息浮层（信息卡 + 底部控制条）
 *  - 双击：播放 / 暂停
 *  - 左右滑：上一个 / 下一个频道
 *  - 上下滑：右半屏调音量、左半屏调屏幕亮度（带居中提示）
 *
 * 设计成自包含辅助类：只通过回调把「显隐 / 播放暂停 / 换台」交回 Activity，
 * 音量与亮度由本类直接操作系统服务，避免侵入 3000+ 行的 MainActivity。
 */
class TouchGestures(
    private val activity: Activity,
    private val onSingleTap: () -> Unit,
    private val onDoubleTap: () -> Unit,
    private val onNextChannel: () -> Unit,
    private val onPrevChannel: () -> Unit,
) {
    private val audioManager = activity.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    private val screenW = activity.resources.displayMetrics.widthPixels

    // 是否响应手势：仅播放模式为 true（墙面模式交给 RecyclerView 点击）
    var enabled = false

    // 竖向滑动状态：起始 Y、起始 X（判断左亮右音量）、累计量、当前类型
    private var sliding = false
    private var startX = 0f
    private var lastY = 0f
    private var slideKind = 0 // 1=音量 2=亮度
    private var slideValue = 0f

    private var hintToast: Toast? = null

    private val detector = GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean = true

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            if (enabled) onSingleTap()
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            if (enabled) onDoubleTap()
            return true
        }

        override fun onFling(
            e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float
        ): Boolean {
            if (!enabled || e1 == null) return false
            val dx = e2.x - e1.x
            val dy = e2.y - e1.y
            // 水平意图明确时才换台，避免与竖向音量滑动互相误触
            if (abs(dx) > abs(dy) * 1.4f && abs(dx) > screenW * 0.12f) {
                if (dx < 0) onNextChannel() else onPrevChannel()
                return true
            }
            return false
        }
    })

    /** 挂到 playerView 的 OnTouchListener。返回 true 表示消费，避免穿透到底层。 */
    fun attach(view: View) {
        view.setOnTouchListener { v, event ->
            val handled = detector.onTouchEvent(event)
            handleVertical(event)
            handled || sliding || event.action == MotionEvent.ACTION_UP
        }
    }

    /** 竖向连续滑动：右侧音量、左侧亮度。上滑增大、下滑减小。 */
    private fun handleVertical(event: MotionEvent) {
        if (!enabled) return
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                lastY = event.y
                sliding = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (!sliding) {
                    val dx = abs(event.x - startX)
                    val dy = abs(event.y - lastY)
                    // 达到阈值且竖向占优才进入滑动模式
                    if (dy > 12f && dy > dx * 1.3f) {
                        sliding = true
                        slideKind = if (startX > screenW / 2f) 1 else 2
                        slideValue = if (slideKind == 1) {
                            audioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
                        } else {
                            activity.window.attributes.screenBrightness.let {
                                if (it < 0f) 0.5f else it
                            } * 100f
                        }
                    }
                }
                if (sliding) {
                    val deltaY = lastY - event.y // 上滑为正
                    lastY = event.y
                    if (slideKind == 1) {
                        val step = deltaY / 8f
                        slideValue = (slideValue + step).coerceIn(0f, maxVolume.toFloat())
                        val v = slideValue.toInt()
                        // flags=0：静默设置，滑动时不弹系统音量条、不发声（本类自带居中提示）
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, v, 0)
                        showHint("音量 ${v * 100 / maxVolume}%")
                    } else {
                        val step = deltaY / screenW * 1.5f * 100f
                        slideValue = (slideValue + step).coerceIn(5f, 100f)
                        val lp = activity.window.attributes
                        lp.screenBrightness = slideValue / 100f
                        activity.window.attributes = lp
                        showHint("亮度 ${slideValue.toInt()}%")
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                sliding = false
            }
        }
    }

    /** 居中淡出提示：复用同一个 Toast，滑动过程不断刷新文本而非叠一堆。 */
    private fun showHint(text: String) {
        hintToast?.setText(text)
        if (hintToast == null) {
            hintToast = Toast.makeText(activity, text, Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, 0, 0)
            }
        }
        hintToast?.show()
    }
}
