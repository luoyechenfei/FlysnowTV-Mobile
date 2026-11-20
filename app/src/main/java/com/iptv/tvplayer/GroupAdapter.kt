package com.iptv.tvplayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * 手机/平板顶部分类导航：标准横滑标签栏。
 * - 全部分类横向排列，手指左右滑动即可浏览，分类再多也能滑到（不再有「更多」弹窗）；
 * - 点选高亮（底部指示条 + 亮色文字），切换由 Activity 的 onSelect 处理；
 * - 无遥控器焦点依赖、无按屏宽截断逻辑——纯触摸交互。
 */
class GroupAdapter(
    private val context: Context,
    private val onSelect: (String?) -> Unit
) : RecyclerView.Adapter<GroupAdapter.TabHolder>() {

    /** 全部分类（含首位空串「全部」） */
    private val all = ArrayList<String>()
    private var selected: String? = null

    fun submit(groups: List<String>) {
        all.clear()
        all.add("")
        all.addAll(groups)
        notifyDataSetChanged()
    }

    fun setSelected(group: String?) {
        if (group == selected) return
        val oldPos = selectedPosition()
        selected = group
        notifyItemChanged(oldPos)
        notifyItemChanged(selectedPosition())
    }

    /** 选中项在标签栏中的位置；用于自动滚动到可见区域 */
    fun selectedPosition(): Int {
        val key = selected ?: ""
        val index = all.indexOf(key)
        return if (index >= 0) index else 0
    }

    /** 换源后旧分组可能已不存在，用来判断是否需要回退 */
    fun hasGroup(group: String?): Boolean = group == null || all.contains(group)

    private fun label(group: String): String = if (group.isBlank()) "全部" else group

    override fun getItemCount(): Int = all.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_group_chip, parent, false) as TextView
        return TabHolder(view)
    }

    override fun onBindViewHolder(holder: TabHolder, position: Int) {
        val group = all.getOrNull(position) ?: ""
        holder.bind(
            label(group),
            group == selected || (group.isBlank() && selected == null)
        )
    }

    inner class TabHolder(textView: TextView) : RecyclerView.ViewHolder(textView) {
        private val chip: TextView = textView

        init {
            chip.setOnClickListener {
                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                onSelect((all.getOrNull(position) ?: "").ifBlank { null })
            }
        }

        fun bind(label: String, isSelected: Boolean) {
            chip.setBackgroundResource(R.drawable.bg_group_tab)
            ContextCompat.getColorStateList(chip.context, R.color.tab_chip_text)
                ?.let { chip.setTextColor(it) }
            chip.text = label
            chip.isSelected = isSelected
        }
    }
}
