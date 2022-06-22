package com.example.week53

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.week53.model.CatDataItem
import com.example.week53.model.VoteData
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val BASE_URL = "https://api.thecatapi.com/v1"
const val SEARCH_URL = "/images/search"
const val API_KEY = "bcaeec60-01cd-4050-aa32-431a6e6a064b"
const val BASE_USER = "test"

class MainActivity : AppCompatActivity() {
    var catImageData: List<CatDataItem>? = null
    lateinit var curUser: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(this)//задаем fresco
        setContentView(R.layout.activity_main)

        //ользователь по умолчанию
        curUser = BASE_USER
        val etUsername = findViewById<EditText>(R.id.et_username)
        etUsername.setText(curUser)

        //изменения пользователя
        etUsername.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!etUsername.text.isEmpty()) {
                    curUser = etUsername.text.toString()
                } else {
                    curUser = BASE_USER
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        //задаем картинку кота
        setCatImage(-1)

        val draweeView = findViewById<View>(R.id.iv_favorite) as SimpleDraweeView
        draweeView.setOnClickListener {
            setCatImage(-1)
        }

        //кнопка для лайка
        val ibLike = findViewById<ImageButton>(R.id.ib_like)
        ibLike.setOnClickListener {
            setCatImage(1)
        }

        //кнопка для дизлайка
        val ibDislike = findViewById<ImageButton>(R.id.ib_dislike)
        ibDislike.setOnClickListener {
            setCatImage(0)
        }

        //избранное
        val btnFavorite: Button = findViewById(R.id.btn_favorite)
        btnFavorite.setOnClickListener() {
            val intent = Intent(
                this,
                FavoritesActivity::class.java
            )
            intent.putExtra("username", curUser)
            startActivity(intent)
        }
    }

    /**
     * Получаем изображение кота
     *
     * @param vote - какая оценка 1-лайк, 0-диздайк, -1-ничего
     */
    fun setCatImage(vote: Int) {
        val connectivity =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        val info = connectivity.activeNetwork

        if (info != null) {
            GlobalScope.launch(Dispatchers.IO) {
                if (vote != -1) {//если надо проголосовать
                    val voteData = VoteData(catImageData?.get(0)?.id, curUser, vote)
                    val client = HttpClient()
                    client.post<Any> {//отправляем POST запрос
                        body = TextContent(
                            Json.encodeToString(voteData),
                            contentType = ContentType.Application.Json
                        )
                        header("x-api-key", API_KEY)//api ключ
                        url {
                            protocol = URLProtocol.HTTPS
                            host = "api.thecatapi.com"
                            path(VOTES_URL)
                        }
                    }
                }
                val clientGet = HttpClient()
                //получаем случайного кота
                val jsonText = clientGet.get<String>(BASE_URL + SEARCH_URL)

                //получаем пременную с данными кота
                catImageData = Json.decodeFromString(jsonText)

                //задаем картинку
                val uri: Uri =
                    Uri.parse(catImageData!![0].url)
                val draweeView = findViewById<View>(R.id.iv_favorite) as SimpleDraweeView
                draweeView.setImageURI(uri)
                draweeView.isClickable = false
                Log.d("--", draweeView.display.isValid.toString())
            }
        }
    }
}
