package com.example.week83

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation

class MainActivity : AppCompatActivity() {
    var navController: NavController? = null
    var isAbout = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // задаем контроллер
        navController =
            Navigation.findNavController(this, R.id.my_nav_host_fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // переход к "о приложении"
            R.id.action_about -> {
                if (!isAbout) {
                    isAbout = true
                    navController?.navigate(R.id.aboutFragment, null)
                }
                return true
            }
        }
        return true
    }
}