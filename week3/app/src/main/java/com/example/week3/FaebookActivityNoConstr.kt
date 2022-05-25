package com.example.week3

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class FaebookActivityNoConstr: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_no_constr)
        val btn = findViewById<Button>(R.id.btn_facebook_sign_in)
        btn.setBackgroundColor(getColor(R.color.facebook))
    }
}