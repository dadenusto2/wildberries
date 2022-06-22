package com.example.week53

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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.week53.model.CatDataItem
import com.example.week53.model.FavoriteData
import com.example.week53.model.FavoritesUrl
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

const val VOTES_URL = "v1/votes"

@DelicateCoroutinesApi
class FavoritesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageGalleryAdapter: ImageGalleryAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var curUser: String
    lateinit var mHandler: Handler
    var isShowing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        // получаем текущего пользователя
        curUser = intent.getStringExtra("username").toString()

        val tvUsername = findViewById<TextView>(R.id.tv_username)
        tvUsername.text = curUser

        val layoutManager = GridLayoutManager(applicationContext, 2)
        recyclerView = findViewById(R.id.rv_images)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        swipeRefreshLayout = findViewById(R.id.sfl_favorite_cats)

        swipeRefreshLayout.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.IO) {
                getFavoriteCats()
            }
        }
        GlobalScope.launch(Dispatchers.IO) {
            getFavoriteCats()
        }

        var loading: AlertDialog? = null
        mHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
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
    }

    /**
     *  Получаем ссылки на фото лайкнутых котов
     */
    suspend fun getFavoriteCats() {
        if (!isShowing) {
            isShowing = true
            mHandler.sendEmptyMessage(1)
        }
        swipeRefreshLayout.isRefreshing = false
        // возвращаемый список
        var favoriteCats: MutableList<FavoritesUrl?> = mutableListOf()
        //список из API
        var favoriteCatsFromApi = mutableListOf<FavoritesUrl?>()

        //получаем список
        val jobGet = GlobalScope.launch {
            favoriteCatsFromApi = getFavoriteFromApi(curUser)
        }
        jobGet.start()
        jobGet.join()

        val job: Job
        // если список бд не нулевой и совпадет со список API
        if (favoriteCatsFromApi.size > 0) {
            job = lifecycleScope.launch {
                favoriteCats = favoriteCatsFromApi
                //обновляем список бд
                Toast.makeText(
                    this@FavoritesActivity,
                    "Данные из API!",
                    Toast.LENGTH_LONG
                ).show()
                lifecycleScope.launch {
                    //задаем адаптер
                    imageGalleryAdapter = ImageGalleryAdapter(favoriteCats)
                    recyclerView.adapter = imageGalleryAdapter
                    mHandler.sendEmptyMessage(0)
                    isShowing = false
                }
            }
        } else {
            job = lifecycleScope.launch {
                Toast.makeText(
                    this@FavoritesActivity,
                    "Ошибка подключения!",
                    Toast.LENGTH_LONG
                ).show()
                mHandler.sendEmptyMessage(0)
                isShowing = false
            }

        }
        job.start()
        job.join()
    }

    /**
     * Получаем сслыки из API
     *
     * @param curUser - текущий пользователь
     *
     * @return ссылки из API
     */
    private suspend fun getFavoriteFromApi(curUser: String?): MutableList<FavoritesUrl?> {
        try {
            //создаем запрос на все оценки у данного пользователя
            val clientPost = HttpClient(Android) {}
            val get = clientPost.get<String> {
                header("x-api-key", API_KEY)
                parameter("sub_id", curUser)
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.thecatapi.com"
                    path(VOTES_URL)
                }
            }
            val favoriteCatsFromApi = Json.decodeFromString<MutableList<FavoriteData>>(get)
            return getImages(favoriteCatsFromApi)
        } catch (e: Exception) {//если ошибка подключения
            return mutableListOf()
        }
    }

    /**
     * Получаем сслыки на фото из API
     *
     * @param favoriteImages - лайкнутые каты
     *
     * @return ссылки из API
     */
    private suspend fun getImages(favoriteImages: MutableList<FavoriteData>): MutableList<FavoritesUrl?> {
        // Хранение ссылок на объекты котов, котьорые были пролайканы
        val imageUrls = mutableListOf<String>()
        val subId = favoriteImages[0].sub_id
        for (element in favoriteImages) {//получаем ссылка на фото с оценокой 1(лайк)
            if (element.value == 1) {
                imageUrls.add(
                    "https://api.thecatapi.com/v1/images/" +
                            element.image_id

                )
            }
        }

        //получаем фото всех лайкнутых котов из объектов котов
        var images = mutableListOf<FavoritesUrl?>()
        try {
            for (element in imageUrls) {
                val clientGet = HttpClient(Android) {}
                val jsonText = clientGet.get<String>(element)
                val favoriteImage = Json.decodeFromString<CatDataItem>(jsonText)
                images.add(0, FavoritesUrl(0, subId, favoriteImage.url))
            }
        } catch (e: Exception) {
            images = mutableListOf()
        }
        return images
    }

    /**
     * Задаем окно загрузки
     *
     * @return окно загрузки
     */
    fun setProgressDialog(): AlertDialog {
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
}