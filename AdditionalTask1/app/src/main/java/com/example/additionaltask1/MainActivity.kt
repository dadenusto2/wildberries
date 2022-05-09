package com.example.additionaltask1

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
     fun arraySort(view: View){

         val fm: FragmentManager = supportFragmentManager

         val countString = findViewById<EditText>(R.id.editTextNumber).text.toString()

         var fragment = fm.findFragmentById(R.id.fragmentContainer)

         if (countString != "") {
             if(fragment != null)
                 fm.beginTransaction().remove(fragment).commit();
             val countOfELem = Integer.parseInt(countString)

             val bundle = Bundle()
             bundle.putInt("param1", countOfELem)

             fragment = ArrayFragment()

             fragment.setArguments(bundle)
             fm.beginTransaction()
                 .add(R.id.fragmentContainer, fragment)
                 .commit()
         }
         else{
             if(countString == "")
                Log.v("string","empty")
         }
     }
}