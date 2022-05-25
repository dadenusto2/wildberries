package com.example.week4

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// активити для отдельного чата
class CurrentChatActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var customAdapter: MessageAdapter
    lateinit var currentChatRepository: CurrentChatRepository
    lateinit var sendButton: ImageButton
    var position: Int? = 0

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.messenger_activity)
        // получаем данные о чате
        position = intent.extras?.getInt("position")
        currentChatRepository = intent.extras?.getSerializable("currentChatRepository") as CurrentChatRepository

        //имя пользователя
        val chatName = findViewById<TextView>(R.id.tv_chat_name)
        chatName.text = intent.extras?.getString("name")

        //аватар
        val chatImage = findViewById<ImageView>(R.id.iv_chat_image)
        chatImage.setImageResource(intent.extras!!.getInt("image"))

        //задаем адаптер для recyclerView
        recyclerView = findViewById(R.id.rv_messages)

        val mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mLayoutManager.reverseLayout = true
        mLayoutManager.stackFromEnd = true
        recyclerView.layoutManager = mLayoutManager
        customAdapter = MessageAdapter(currentChatRepository.getCurrentChatList(), currentChatRepository)
        (recyclerView.layoutManager as LinearLayoutManager).stackFromEnd = true
        recyclerView.adapter = customAdapter

        // удаление сообщений при пролистовании вправо или влево
        recyclerView.smoothScrollToPosition(0)
        recyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener{
            override fun onLayoutChange(
                p0: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                if (bottom < oldBottom) {
                    recyclerView.postDelayed(java.lang.Runnable {
                        recyclerView.smoothScrollToPosition(0)
                    }, 100)
                }
            }
        })

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        // кнопка отправки сообщения
        sendButton = findViewById(R.id.btn_send)
        sendButton.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
            val message = findViewById<EditText>(R.id.et_message).text
            if (!message.isEmpty()) {
                findViewById<EditText>(R.id.et_message).setText("", TextView.BufferType.EDITABLE)
                currentChatRepository.addMessage(true, message.toString())
            }
            customAdapter.notifyDataSetChanged()

        }
        //добавление новых сообщений
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.srl_messages)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            lifecycleScope.launch {
                delay((1000..3000).random().toLong())
                currentChatRepository.generateChats()
                currentChatRepository.getCurrentChatList()
                swipeRefreshLayout.isRefreshing = false
                customAdapter.notifyDataSetChanged()
            }
        }
        repeater()
    }

    // свайп сообщения вбок для удаления
    val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.adapterPosition
            val chatData = currentChatRepository.getCurChat(viewHolder.adapterPosition)
            currentChatRepository.removeItem(pos)
            // появляется сообщения, что можно восстановтьб удаленное сообщение
            Snackbar.make(recyclerView, "Вернуть сообщение?", Snackbar.LENGTH_LONG)
                .setAction("Да",
                    View.OnClickListener {
                        currentChatRepository.addChatBack(pos, chatData)
                        customAdapter.notifyItemInserted(pos)
                    }).show()
            customAdapter.notifyItemRemoved(pos)
        }
    }
    // при нажатиии кнопки назад передаем новый список сообщениц для данного чата
    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("position", position)
        intent.putExtra("data", currentChatRepository)
        setResult(Activity.RESULT_OK, intent)
        finish()
        super.onBackPressed()
    }
    // новые сообщения
    fun repeater() {
        lifecycleScope.launch { ->
            repeat(100) {
                delay((10000..20000).random().toLong())
                currentChatRepository.addMessage(false)
                customAdapter.notifyDataSetChanged()
            }
        }
    }

}