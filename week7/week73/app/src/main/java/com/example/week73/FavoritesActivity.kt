package com.example.week73

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.*

const val VOTES_URL = "v1/votes"

@DelicateCoroutinesApi
class FavoritesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageGalleryAdapter: ImageGalleryAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var curUser: String
    private lateinit var myDatabase: MyDatabase
    lateinit var favoriteRepository: FavoriteRepository
    lateinit var mHandler: Handler
    var isShowing: Boolean = false

    @SuppressLint("HandlerLeak")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favoriteRepository = FavoriteRepository()
        // получаем текущего пользователя
        curUser = intent.getStringExtra("username").toString()

        val tvUsername = findViewById<TextView>(R.id.tv_username)
        tvUsername.text = curUser

        val layoutManager = GridLayoutManager(applicationContext, 2)
        recyclerView = findViewById(R.id.rv_images)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        swipeRefreshLayout = findViewById(R.id.sfl_favorite_cats)

        //счоздаем БД
        myDatabase = Room.databaseBuilder(
            this,
            MyDatabase::class.java, MyDatabase.NAME
        ).fallbackToDestructiveMigration().build()

        //для открытия/закрытия alert dialog
        var loading: AlertDialog? = null
        mHandler = object : Handler() {
            @SuppressLint("HandlerLeak")
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1 -> {//показывать
                        if (isShowing) {
                            if (loading == null)//если не сущесвует создаем
                                loading = setProgressDialog()
                            else//если сущесвует, показываем
                                loading!!.show()
                        }
                    }
                    0 -> {//не показывать
                        try {
                            loading!!.dismiss()
                        } catch (e: Exception) { }
                    }
                }
            }
        }
        swipeRefreshLayout.setOnRefreshListener {
            getFavoriteCats()
        }

        //плучаем котов
        getFavoriteCats()
    }

    /**
     *  Получаем ссылки на фото лайкнутых котов
     */
    @SuppressLint("HandlerLeak")
    private fun getFavoriteCats() {
        val connectivity =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        val info = connectivity.activeNetwork
        //проверяем соединение
        if (info != null) {
            if (recyclerView.isEmpty()) {//если пустое
                if (!isShowing) {
                    isShowing = true
                    mHandler.sendEmptyMessage(1)
                }
                //задаем окно загрузки
                var images = mutableListOf<FavoritesUrl?>()
                val job: Job = GlobalScope.launch(Dispatchers.IO) {
                    val favoriteRepository = FavoriteRepository()
                    // получаем ссылки на картинки лайкнутых котов
                    images = favoriteRepository.getFavoriteCats(
                        curUser,
                        myDatabase,
                        this@FavoritesActivity
                    )
                }
                GlobalScope.launch {
                    job.start()
                    job.join()
                    mHandler.post {
                        //задаем адаптер
                        imageGalleryAdapter = ImageGalleryAdapter(images)
                        recyclerView.adapter = imageGalleryAdapter
                        mHandler.sendEmptyMessage(0)
                        isShowing = false
                    }
                }
            }
        } else {
            mHandler.post {
                Toast.makeText(this, "Нет соединения!", Toast.LENGTH_LONG).show()
            }
        }
        swipeRefreshLayout.isRefreshing = false
    }

    /**
     * Задаем окно загрузки
     *
     * @return окно загрузки
     */
    private fun setProgressDialog(): AlertDialog {
        val llPadding = 30
        val ll = LinearLayout(this)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        // Creating a ProgressBar inside the layout
        val progressBar = ProgressBar(this)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER

        val tvText = TextView(this)
        tvText.text = "Загрузка красивых котиков ..."
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 14f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(ll)

        val dialog: AlertDialog = builder.create()
        dialog.show()
        return dialog
    }

    override fun onPause() {
        super.onPause()
        mHandler.sendEmptyMessage(0)
    }

    override fun onResume() {
        super.onResume()
        mHandler.sendEmptyMessage(1)
    }
}