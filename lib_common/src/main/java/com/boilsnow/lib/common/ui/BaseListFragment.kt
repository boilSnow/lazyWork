package com.boilsnow.lib.common.ui

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.boilsnow.lib.common.R
import com.boilsnow.lib.common.entente.ViewEntente
import com.boilsnow.lib.common.ui.assist.BaseRecyclerAdapter
import com.boilsnow.lib.common.ui.assist.RecyclerFootItemHolder
import java.util.*

/**
 * Description:列表fragment
 * Remark:
 */
abstract class BaseListFragment<D> : BaseFragment() {

    protected var isUpdateOn = false     //开启刷新
    protected var isLoadOn = false       //开启加载
    protected var isShowFoot = false    //开启显示底部视图

    protected var mRvList: RecyclerView? = null
    protected var mSrView: SwipeRefreshLayout? = null

    protected lateinit var mAdapter: BaseRecyclerAdapter<D>
    protected var mDataType = RecyclerFootItemHolder.TYPE.VAIN
    protected var mAction: ViewEntente.OnListAction? = null

    override fun getLayoutID() = R.layout.l00_include_list_recycler

    override fun setupViews(view: View?) {
        mRvList = view?.findViewById(R.id.rvList)
        mSrView = view?.findViewById(R.id.srView)

        mRvList?.layoutManager = listLayoutManager()
    }

    override fun initialized() {
        mAdapter = listAdapter()
        mAdapter.setIsShowFootView(isShowFoot)

        mRvList?.adapter = mAdapter
        mSrView?.isEnabled = isUpdateOn
        if (isUpdateOn) mSrView?.setOnRefreshListener { mAction?.onUpdateListData(this) }
        if (isLoadOn) mRvList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState != RecyclerView.SCROLL_STATE_IDLE) return
                val layoutManager = recyclerView.layoutManager
                val lastVisiblePosition = when (layoutManager) {
                    is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                    is StaggeredGridLayoutManager -> {
                        val into = IntArray(layoutManager.spanCount)
                        layoutManager.findLastVisibleItemPositions(into)
                        Collections.max(into.toList())
                    }
                    else -> (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                }
                if (mDataType == RecyclerFootItemHolder.TYPE.DATA_LOADING           //还有更多数据
                    && layoutManager.childCount > 0                                 //子条目数大于0
                    && lastVisiblePosition >= layoutManager.itemCount - 1           //当前屏幕显示的最后视图是最后的数据
                    && layoutManager.itemCount >= layoutManager.childCount
                ) mAction?.onLoadListData(this@BaseListFragment)
            }
        })
    }

    open fun setOnListAction(action: ViewEntente.OnListAction) {
        mAction = action
    }

    open fun setListData(
        data: ArrayList<D>?, isUpdate: Boolean = true, min: Int = 0, size: Int = 10
    ) {
        mDataType = if (data?.size ?: 0 >= size) RecyclerFootItemHolder.TYPE.DATA_LOADING else {
            if (isUpdate && data?.size ?: 0 == min) RecyclerFootItemHolder.TYPE.DATA_NULL
            else RecyclerFootItemHolder.TYPE.DATA_END
        }

        if (isUpdate) {
            mAdapter.setData(data, mDataType)
            hideLoading()
        } else mAdapter.addData(data, mDataType)
    }

    fun hideLoading() {
        if (isUpdateOn) mSrView?.isRefreshing = false
    }

    protected abstract fun listLayoutManager(): LinearLayoutManager

    protected abstract fun listAdapter(): BaseRecyclerAdapter<D>
}