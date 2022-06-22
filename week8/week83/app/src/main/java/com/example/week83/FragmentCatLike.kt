package com.example.week83

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.week83.model.CatDataItem
import com.example.week83.model.VoteData
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.content.*
import io.ktor.http.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val BASE_URL = "https://api.thecatapi.com/v1"
const val SEARCH_URL = "/images/search"
const val API_KEY = "bcaeec60-01cd-4050-aa32-431a6e6a064b"
const val BASE_USER = "test" //пользователь по умолчанию

/**
 * Фрагмент оценки котов
 */
@DelicateCoroutinesApi
class FragmentCatLike : Fragment() {
    private lateinit var myView: View
    var catImageData: List<CatDataItem>? = null
    lateinit var curUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fresco.initialize(activity)//задаем fresco
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myView = inflater.inflate(R.layout.fragment_cat_like, container, false)
        return myView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).isAbout = false
        //пользователь по умолчанию
        curUser = BASE_USER
        val etUsername = myView.findViewById<EditText>(R.id.et_username)
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

        // обновить по нажатию на картинку
        val draweeView = myView.findViewById<View>(R.id.iv_favorite) as SimpleDraweeView
        draweeView.setOnClickListener {
            setCatImage(-1)
        }

        //кнопка для лайка
        val ibLike = myView.findViewById<ImageButton>(R.id.ib_like)
        ibLike.setOnClickListener {
            setCatImage(1)
        }

        //кнопка для дизлайка
        val ibDislike = myView.findViewById<ImageButton>(R.id.ib_dislike)
        ibDislike.setOnClickListener {
            setCatImage(0)
        }

        //избранное
        val btnFavorite: Button = myView.findViewById(R.id.btn_favorite)

        btnFavorite.setOnClickListener() {
            //текущий пользователь
            val bundle = bundleOf("curUser" to curUser)

            //переходим к лайкнутым
            val activity = activity as MainActivity
            activity.navController?.navigate(R.id.fragmentFavorite, bundle)
        }

        setCatImage(-1)
    }

    /**
     * Получаем изображение кота
     *
     * @param vote - какая оценка 1-лайк, 0-диздайк, -1-ничего
     */
    private fun setCatImage(vote: Int) {
        val connectivity =
            activity?.applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        val info = connectivity.activeNetwork

        if (info != null) {
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    //если надо проголосовать
                    if (vote != -1) {
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

                    //получаем случайного кота
                    val clientGet = HttpClient()
                    //строка с новым котом
                    val jsonText = clientGet.get<String>(BASE_URL + SEARCH_URL)

                    //получаем переменную с данными кота
                    catImageData = Json.decodeFromString(jsonText)

                    //задаем картинку
                    val uri: Uri =
                        Uri.parse(catImageData!![0].url)
                    val draweeView = myView.findViewById<View>(R.id.iv_favorite) as SimpleDraweeView
                    draweeView.setImageURI(uri)
                    draweeView.isClickable = false
                } catch (e: Exception) {
                }
            }

        }
    }
}