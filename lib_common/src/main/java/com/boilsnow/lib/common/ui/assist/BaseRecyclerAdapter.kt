package com.boilsnow.lib.common.ui.assist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.boilsnow.lib.common.R

/**
 * Description: Recycler适配器扩展
 * Remark:
 */
abstract class BaseRecyclerAdapter<D>(protected var mContext: Context) :
    RecyclerView.Adapter<BaseRecyclerItemHolder<D>>() {

    companion object {
        //底部视图
        private const val FOOT_VIEW = -9
    }

    protected var mData: ArrayList<D>? = null
    private var mIsShowFootView = false
    private var mFootType = RecyclerFootItemHolder.TYPE.VAIN

    fun setIsShowFootView(isShow: Boolean) {
        mIsShowFootView = isShow
        this.notifyDataSetChanged()
    }

    fun setData(
        data: ArrayList<D>?,
        footType: RecyclerFootItemHolder.TYPE = RecyclerFootItemHolder.TYPE.VAIN
    ) {
        mFootType = footType
        this.mData = data ?: ArrayList()
        this.notifyDataSetChanged()
    }

    fun addData(
        data: ArrayList<D>?,
        footType: RecyclerFootItemHolder.TYPE = RecyclerFootItemHolder.TYPE.VAIN
    ) {
        mFootType = footType
        if (data != null) {
            if (mData == null) mData = ArrayList()
            this.mData!!.addAll(data)
        }
        this.notifyDataSetChanged()
    }

    fun getData(): ArrayList<D>? = mData

    fun getItemData(position: Int): D? = if (position < mData!!.size) mData!![position] else null

    //校验条目视图显示类型
    protected open fun checkItemViewType(position: Int) = super.getItemViewType(position)

    //创建条目视图
    protected abstract fun onCreateItemViewHolder(
        parent: ViewGroup, viewType: Int
    ): BaseRecyclerItemHolder<D>

    //创建底部条目视图
    protected open fun createFootViewHolder(parent: ViewGroup): RecyclerFootItemHolder<D> =
        RecyclerFootItemHolder(
            LayoutInflater.from(mContext).inflate(R.layout.l00_item_foot, parent, false)
        )

    //绑定条目视图数据
    protected open fun onBindItemViewHolder(
        holder: BaseRecyclerItemHolder<D>, position: Int, viewType: Int
    ) = if (viewType == FOOT_VIEW) (holder as RecyclerFootItemHolder).updateView(mFootType)
    else holder.bindData(mData?.get(position)!!)

    override fun getItemCount(): Int =
        if (mIsShowFootView) (mData?.size ?: 0) + 1 else (mData?.size ?: 0)

    override fun getItemViewType(position: Int): Int =
        if (mIsShowFootView && position == itemCount - 1) FOOT_VIEW else checkItemViewType(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (mIsShowFootView && viewType == FOOT_VIEW) createFootViewHolder(parent)
        else onCreateItemViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: BaseRecyclerItemHolder<D>, position: Int) =
        onBindItemViewHolder(holder, position, getItemViewType(position))
}