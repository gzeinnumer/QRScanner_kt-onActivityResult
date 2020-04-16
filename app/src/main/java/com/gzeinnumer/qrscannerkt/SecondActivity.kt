package com.gzeinnumer.qrscannerkt

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        ed_qr_1.setOnClickListener {
            startActivityForResult(Intent(applicationContext, MainActivity::class.java).apply {
                putExtra("index", 1)
            }, 1)
        }

        ed_qr_2.setOnClickListener {
            startActivityForResult(Intent(applicationContext, MainActivity::class.java).apply {
                putExtra("index", 2)
            }, 2)
        }

        ed_qr_3.setOnClickListener {
            startActivityForResult(Intent(applicationContext, MainActivity::class.java).apply {
                putExtra("index", 3)
            }, 3)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            Log.d(
                "MyZein",
                "Second value " + it.getStringExtra("value") + " Index " + it.getIntExtra("index", 0)
            )
            when (requestCode) {
                1 -> {
                    ed_qr_1.setText(it.getStringExtra("value"))
                }
                2 -> {
                    ed_qr_2.setText(it.getStringExtra("value"))
                }
                3 -> {
                    ed_qr_3.setText(it.getStringExtra("value"))
                }
            }
        }
    }

}
