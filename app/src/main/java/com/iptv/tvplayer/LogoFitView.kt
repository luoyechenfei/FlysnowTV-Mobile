package com.iptv.tvplayer

import android.content.Context
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

/**
 * 台标专用 ImageView：完整显示、不裁剪、不变形（与 TV 版实现一致，真机已验证）。
 *  - scaleType=MATRIX：按 drawable 真实尺寸与自身可用区域等比缩放，
 *    显示尺寸 ≤ 本框 82%（放大封顶 2.2 倍防发虚）；
 *  - 不裁剪、不裁圆角，任何来源台标都完整显示、不溢出台标槽；
 *  - onSizeChanged / setImageDrawable 双入口自动重算，网络 / 内置 / 兜底共用入口。
 */
class LogoFitView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        scaleType = ScaleType.MATRIX
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        applyLogo()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        applyLogo()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        applyLogo()
    }

    /** 按当前 drawable 与自身尺寸重算等比缩放矩阵：完整显示、≤ 框 82%、放大 ≤ 2.2 倍 */
    fun applyLogo() {
        val d = drawable ?: return
        val iw = d.intrinsicWidth
        val ih = d.intrinsicHeight
        val aw = width - paddingLeft - paddingRight
        val ah = height - paddingTop - paddingBottom
        if (iw <= 0 || ih <= 0 || aw <= 0 || ah <= 0) return

        val fill = min(aw.toFloat() / iw, ah.toFloat() / ih)
        var s = fill * LOGO_MAX_RATIO
        if (s > LOGO_MAX_UPSCALE) s = LOGO_MAX_UPSCALE
        if (s <= 0f) return

        val dispW = iw * s
        val dispH = ih * s
        val m = Matrix()
        m.setScale(s, s)
        m.postTranslate(
            paddingLeft + (aw - dispW) / 2f,
            paddingTop + (ah - dispH) / 2f
        )
        imageMatrix = m
    }

    companion object {
        /** 显示尺寸上限：台标槽的 82%（比 TV 版 78% 略大，手机卡片更扁需更充分利用） */
        private const val LOGO_MAX_RATIO = 0.82f
        private const val LOGO_MAX_UPSCALE = 2.2f
    }
}
