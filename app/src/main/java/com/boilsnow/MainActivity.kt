package com.boilsnow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnJava.setOnClickListener(this)
        btnKotlin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnJava -> ARouter.getInstance().build("/java/demo/a").navigation()
            R.id.btnKotlin -> ARouter.getInstance().build("/kotlin/demo/a").navigation()
        }
    }

}