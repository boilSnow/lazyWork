package com.boilsnow.lib.common.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.boilsnow.lib.common.entente.ViewEntente
import com.boilsnow.lib.common.ui.assist.ToastAss
import com.boilsnow.lib.common.util.LL

/**
 * Description:定义扩展
 * Remark:
 */
abstract class BaseFragment : Fragment() {

    private var mView: View? = null
    protected var mIsViewLoad = false
    protected var mFragmentAction: ViewEntente.OnSkipAction? = null

    protected abstract fun getLayoutID(): Int

    protected abstract fun setupViews(view: View?)

    protected abstract fun initialized()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (container == null) return null
        mView = inflater.inflate(getLayoutID(), container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preliminary()
    }

    private fun preliminary() {
        mIsViewLoad = true
        setupViews(mView)

        initialized()
    }

    fun reloadData(eValue: Int = -1) {
        if (!mIsViewLoad) return
        loadData(eValue)
    }

    fun setOnFragmentActionListener(action: ViewEntente.OnSkipAction) {
        mFragmentAction = action
    }

    protected open fun loadData(eValue: Int = -1) {
    }

    protected fun log(text: String) = LL.d(tag = this.javaClass.name, msg = text)

    protected fun toast(text: String, isLong: Boolean = false) =
        ToastAss.show(activity!!, text, isLong)

    protected fun toActivity(name: Class<*>, extras: Bundle? = null, requestCode: Int = -1) {
        val intent = Intent(activity, name)
        if (extras != null) intent.putExtras(extras)
        if (requestCode < 0) startActivity(intent) else startActivityForResult(intent, requestCode)
    }

}