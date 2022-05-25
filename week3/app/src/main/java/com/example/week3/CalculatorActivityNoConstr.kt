package com.example.week3

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CalculatorActivityNoConstr: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculater_no_constraint)
    }
}