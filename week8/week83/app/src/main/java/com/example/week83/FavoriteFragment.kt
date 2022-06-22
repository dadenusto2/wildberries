package com.example.week83

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.week83.adapter.ImageGalleryAdapter
import com.example.week83.db.MyDatabase
import com.example.week83.model.FavoritesUrl
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "curUser"
const val VOTES_URL = "v1/votes"

/**
 * Фрагмент лайкнутых котов пользователя
 */
@DelicateCoroutinesApi
class FavoriteFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageGalleryAdapter: ImageGalleryAdapter
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    var curUser: String? = null
    private lateinit var myDatabase: MyDatabase
    lateinit var favoriteRepository: FavoriteRepository
    lateinit var mHandler: Handler
    var isShowing: Boolean = false
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.actionBar?.hide()
        arguments?.let {
            curUser = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).isAbout = false

        favoriteRepository = FavoriteRepository()
        // получаем текущего пользователя

        val tvUsername = view.findViewById<TextView>(R.id.tv_username)
        tvUsername.text = curUser

        val layoutManager = GridLayoutManager(activity!!.applicationContext, 2)
        recyclerView = view.findViewById(R.id.rv_images)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager

        swipeRefreshLayout = view.findViewById(R.id.sfl_favorite_cats)

        //создаем БД
        myDatabase = Room.databaseBuilder(
            activity!!.applicationContext,
            MyDatabase::class.java, MyDatabase.NAME
        ).fallbackToDestructiveMigration().build()

        //для открытия/закрытия alert dialog
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
                activity
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
        val ll = LinearLayout(activity)
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
        val progressBar = ProgressBar(activity)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER

        val tvText = TextView(activity)
        tvText.text = "Загрузка красивых котиков ..."
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 14f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
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