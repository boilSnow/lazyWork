package com.boilsnow.kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.kotlin_activity_main.*

/**
 * Description:
 * Remark:
 */
@Route(path = "/kotlin/demo/a")
class DemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kotlin_activity_main)

        btnGO.setOnClickListener {
            ARouter.getInstance().build("/java/demo/a").navigation()
        }
    }
}
