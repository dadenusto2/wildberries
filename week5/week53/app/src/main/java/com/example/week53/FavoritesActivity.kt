package com.example.week53

import android.app.AlertDialog
import android.content.Context
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val VOTES_URL = "v1/votes"
class FavoritesActivity: AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageGalleryAdapter: ImageGalleryAdapter
    lateinit var swipeRefreshLayout : SwipeRefreshLayout
    lateinit var curUser: String
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

        swipeRefreshLayout  = findViewById(R.id.sfl_favorite_cats)

        swipeRefreshLayout.setOnRefreshListener {
            getFavoriteCats()
        }

        getFavoriteCats()
    }

    fun getFavoriteCats() {
        val connectivity =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        val info = connectivity.activeNetwork

        if (info != null) {
            if (recyclerView.isEmpty()) {//если пустое
                //задаем ркно загрузки
                val loading = setProgressDialog()
                loading.show()
                GlobalScope.launch(Dispatchers.IO) {
                    //создаем запрос на все оцени у данного пользователя
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

                    //все лайкнутые записи
                    val favoriteImage = Json.decodeFromString<List<FavoriteData>>(get)
                    val imageUrls = mutableListOf<String>()
                    for (element in favoriteImage) {//получаем ссылка на фото с оценокой 1(лайк)
                        if (element.value == 1)
                            imageUrls.add(
                                "https://api.thecatapi.com/v1/images/" +
                                        element.image_id
                            )
                    }
                    Log.d("---", imageUrls.toString())
                    //получаем фото всех лайкнутых котов
                    val images = mutableListOf<String?>()
                    for (element in imageUrls) {
                        val clientGet = HttpClient(Android) {}
                        val jsonText = clientGet.get<String>(element)
                        Log.d("--+", element)
                        val favoriteImage = Json.decodeFromString<CatDataItem>(jsonText)
                        images.add(0, favoriteImage.url)
                    }
                    Log.d("---", images.toString())
                    //задаем фото в адаптере
                    val mHandler = Handler(Looper.getMainLooper())
                    mHandler.post {
                        imageGalleryAdapter = ImageGalleryAdapter(images)
                        recyclerView.adapter = imageGalleryAdapter
                        loading.dismiss()
                    }
                }
            }
        }
        else{
            Toast.makeText(this, "Нет соединения!", Toast.LENGTH_LONG).show()
        }
        swipeRefreshLayout.isRefreshing = false
    }

    fun setProgressDialog() :AlertDialog{
        // Creating a Linear Layout
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

        // Creating a TextView inside the layout
        val tvText = TextView(this)
        tvText.text = "Загрузка красивых котиков ..."
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 14f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)

        // Setting the AlertDialog Builder view
        // as the Linear layout created above
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(ll)

        // Displaying the dialog
        val dialog: AlertDialog = builder.create()
        dialog.show()
        return dialog
    }
}