package com.example.week73

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.week73.DB.MyDatabase
import com.example.week73.model.FavoritesUrl
import kotlinx.coroutines.*

const val VOTES_URL = "v1/votes"

@DelicateCoroutinesApi
class FavoritesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageGalleryAdapter: ImageGalleryAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var curUser: String //текущий пользователь
    private lateinit var myDatabase: MyDatabase //бд
    private lateinit var favoriteRepository: FavoriteRepository //репозиторий
    private lateinit var mHandler: Handler
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

        //создаем БД
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
                        } catch (e: Exception) {
                        }
                    }
                }
            }
        }
        swipeRefreshLayout.setOnRefreshListener {
            getFavoriteCats()
        }

        //получаем котов
        getFavoriteCats()
    }

    /**
     *  Получаем ссылки на фото лайкнутых котов
     */
    @SuppressLint("HandlerLeak")
    private fun getFavoriteCats() {//если пустое
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
            // получаем котов
            job.start()
            job.join()
            //задаем адаптер
            mHandler.post {
                imageGalleryAdapter = ImageGalleryAdapter(images)
                recyclerView.adapter = imageGalleryAdapter
                mHandler.sendEmptyMessage(0)
                isShowing = false
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