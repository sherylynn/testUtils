package com.sherylynn.testUtils

import android.app.*
import android.os.*
import android.widget.Toast
import kotlinx.android.synthetic.main.main.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        button.setOnClickListener{
            Toast.makeText(this@MainActivity,toastMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    fun toastMessage():String{
        return "I am not hooked";
    }
}
