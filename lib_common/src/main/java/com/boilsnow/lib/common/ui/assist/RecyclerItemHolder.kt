package com.boilsnow.lib.common.ui.assist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.boilsnow.lib.common.R

/**
 * Description:Recycle条目视图定义
 * Remark:
 */
abstract class BaseRecyclerItemHolder<in D>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindData(data: D)
}

/**
 * Description:Recycle底部条目视图定义
 * Remark:
 */
open class RecyclerFootItemHolder<D>(itemView: View) : BaseRecyclerItemHolder<D>(itemView) {

    enum class TYPE {
        DATA_LOADING,       //加载中
        DATA_NULL,          //无数据
        DATA_END,           //数据末尾
        VAIN                //无效字段
    }

    private var tvText: TextView = itemView.findViewById(R.id.tvText)
    private var pbBar: ProgressBar = itemView.findViewById(R.id.pbBar)

    override fun bindData(data: D) {
    }

    open fun updateView(type: TYPE) {
        pbBar.visibility = View.GONE
        tvText.text = when (type) {
            TYPE.DATA_LOADING -> {
                pbBar.visibility = View.VISIBLE
                itemView.context.resources.getString(R.string.l00_list_data_loading)
            }
            TYPE.DATA_NULL -> itemView.context.resources.getString(R.string.l00_list_data_null)
            TYPE.DATA_END -> itemView.context.resources.getString(R.string.l00_list_data_end)
            else -> ""
        }
    }
}

/**
 * Description:无效条目
 * Remark:
 */
open class InvalidItemHolder<D>(context: Context, parent: ViewGroup) : BaseRecyclerItemHolder<D>(
    LayoutInflater.from(context).inflate(R.layout.l00_item_invalid, parent, false)
) {

    override fun bindData(data: D) {
    }
}