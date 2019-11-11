package com.boilsnow.lib.common.entente

/**
 * Description:mvp框架定义
 * Remark:
 */
interface BaseMvpEntente {
    //view层定义
    interface IView {
        //显示进度条
        fun showProgress(text: String = "")

        //隐藏进度条
        fun hideProgress()

        //显示提示信息
        fun showHintText(text: String, isWarn: Boolean = false)
    }

    //控制层定义
    interface IPresenter {
        //资源销毁
        fun onDestroy()
    }

    //列表数据    eValue: 扩展保留字段
    interface IListDataView<D> : IView {
        fun updateListData(data: ArrayList<D>?, isFirstPage: Boolean = true, eValue: Int = 0)
    }

    //列表事件
    interface IListDataPresenter : IPresenter {
        fun updateListData(eValue: Int = 0)

        fun loadListData(eValue: Int = 0)
    }

    //更新数据
    interface IUpdateDataView<D> : IView {
        fun updateData(data: D)
    }

    //返回成功
    interface ISuccessView : IView {
        fun onSuccess()
    }

}