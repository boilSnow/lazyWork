package com.boilsnow.kotlin

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.view.View

/**
 * Description:
 * Remark:
 */
class KotlinMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.btnShow).setOnClickListener {
            val intent = Intent()
            intent.setClass(this@KotlinMainActivity, DemoActivity::class.java)

            startActivity(intent)
        }
    }
}
