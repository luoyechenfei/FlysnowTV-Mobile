package com.iptv.tvplayer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import kotlin.math.max

/**
 * 内置文字台标兜底：当订阅源没有 tvg-logo、或网络台标加载失败时，
 * 用频道名绘制一张「像台标」的圆角图，保证每张卡都有台标而非单个首字母。
 *
 * 设计：深色玻璃底 + 品牌蓝描边 + 居中白色粗体文字（自动按字数缩放字号），
 * intrinsic 尺寸 320x160，与 Coil 网络台标解码尺寸一致，交给 LogoFitView 等比居中。
 */
class TextLogoDrawable(private val text: String) : Drawable() {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#2A1B4D")
    }
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#6E9ED8")
        strokeWidth = 4f
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#EAF1FF")
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }
    private val rect = RectF()

    override fun draw(canvas: Canvas) {
        val b = bounds
        if (b.isEmpty) return
        val radius = minOf(b.width(), b.height()) * 0.18f
        rect.set(
            b.left + borderPaint.strokeWidth,
            b.top + borderPaint.strokeWidth,
            b.right - borderPaint.strokeWidth,
            b.bottom - borderPaint.strokeWidth
        )
        canvas.drawRoundRect(rect, radius, radius, bgPaint)
        canvas.drawRoundRect(rect, radius, radius, borderPaint)

        // 精简显示：取前 8 个字符，去掉常见后缀词让台标更像真实 logo
        val label = simplify(text)
        // 按字符数与可用宽度自适应字号
        val availW = b.width() * 0.82f
        var size = b.height() * 0.52f
        textPaint.textSize = size
        val maxLen = max(1, label.length)
        val measured = textPaint.measureText(label)
        if (measured > availW) size *= availW / measured
        textPaint.textSize = size
        val fm = textPaint.fontMetrics
        val baseline = b.centerY().toFloat() - (fm.ascent + fm.descent) / 2f
        canvas.drawText(label, b.centerX().toFloat(), baseline, textPaint)
    }

    private fun simplify(name: String): String {
        var s = name.trim()
        // 去掉括号里的地区/高清后缀与常见描述词，只留主名
        s = s.replace(Regex("[（(].*?[）)]"), "")
            .replace(Regex("[\\s\\-_]*(高清|标清|HD|SD|频道|频道1)$", RegexOption.IGNORE_CASE), "")
            .trim()
        if (s.length > 8) s = s.take(8)
        return s.ifEmpty { name.take(8) }
    }

    override fun setAlpha(alpha: Int) {
        bgPaint.alpha = alpha
        borderPaint.alpha = alpha
        textPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        bgPaint.colorFilter = colorFilter
        borderPaint.colorFilter = colorFilter
        textPaint.colorFilter = colorFilter
    }

    @Suppress("DeprecatedCallableAddReplaceWith")
    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = android.graphics.PixelFormat.TRANSLUCENT

    override fun getIntrinsicWidth(): Int = 320
    override fun getIntrinsicHeight(): Int = 160
}
