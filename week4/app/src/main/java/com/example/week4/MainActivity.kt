package com.example.week4
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.alexanderklimov.recyclerviewdemo.ChatsListAdapter


class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var customAdapter: ChatsListAdapter
    lateinit var chatsRepository: ChatsRepository
    lateinit var chatsList: MutableList<ChatData>
    var swipeRefresh: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        // генерируем чаты
        chatsRepository = ChatsRepository()
        chatsRepository.generateChats()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        chatsList = chatsRepository.getChats()
        //для перехода к чату без адаптеров
        val btnChatsWithoutList = findViewById<Button>(R.id.btn_chat_without_adapter)
        btnChatsWithoutList.setOnClickListener {
            val intent = Intent(
                this,
                ChatsWithoutListActivity::class.java)
            startActivity(intent)
        }
        // контракт для перехода в конкретный чат
        val simpleContractRegistration = registerForActivityResult(ActivityResultContracts.StartActivityForResult() ) { resultStr ->
            // получаем новый репозиторий с сообщениями
            val newCurrentChatRepository : CurrentChatRepository = resultStr.data?.extras?.getSerializable("data") as CurrentChatRepository
            val position : Int? = resultStr.data?.extras?.getInt("position")
            val newChatList = mutableListOf<ChatData>()
            // генерируем репозиторий для сравнения и обновления
            for(i:Int in 0 until chatsRepository.getChatsSize()){
                val newChatRepository = CurrentChatRepository()
                newChatRepository.setNewList(chatsRepository.getChatItem(i).currentChatRepository.getCurrentChatList())

                var newChatData: ChatData
                if(i==position)//если полученый чат, то обновляем сообщения
                    newChatData = ChatData(chatsRepository.getChatItem(i).mChatId, chatsRepository.getChatItem(i).mChatImage,
                        chatsRepository.getChatItem(i).mChatName, newCurrentChatRepository)
                else//иначе олстовляем
                    newChatData = ChatData(chatsRepository.getChatItem(i).mChatId, chatsRepository.getChatItem(i).mChatImage,
                        chatsRepository.getChatItem(i).mChatName, newChatRepository)

                newChatList.add(newChatData)
            }
            customAdapter.submitList(newChatList.toMutableList())
        }

        // адаптер через DiffUtils
        customAdapter = ChatsListAdapter(chatsList, simpleContractRegistration)
        customAdapter.submitList(chatsList)
        recyclerView.adapter = customAdapter

        // scroll
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    val layoutManager =
                    LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                    val visibleItemCount = layoutManager.childCount;
                    val totalItemCount = layoutManager.itemCount
                    val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        GlobalScope.launch {
                            Thread.sleep(1_000)
                            chatsRepository.generateChats()
                            val newChatList = chatsRepository.getChats()
                            customAdapter.submitList(newChatList.toMutableList())
                        }
                    }
                }
            }
        })

        // swipe to refresh для новых сообщений
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.srl_chats)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefresh = true
            swipeRefreshLayout.isRefreshing = true
            lifecycleScope.launch {
                val position : Int = (0 until chatsList.size).random()
                delay((1000..2000).random().toLong())// моделируем загрузку

                // сооздаем новый репозиторий для последющего сравнения в адаптере
                val newChatsRepository = ChatsRepository()

                //довавляем список чатов
                newChatsRepository.setList(customAdapter.getList())

                //в выбраном случайно чате добавляем новое сообщение
                newChatsRepository.getChatItem(position).currentChatRepository.addMessage(false)
                // переносим этот сат вверх
                val tmp = newChatsRepository.getChatItem(position)
                newChatsRepository.delFromChats(position)
                newChatsRepository.setChatList(tmp, 0)
                //сравниваем в адаптере
                recyclerView.scrollToPosition(0)
                customAdapter.submitList(newChatsRepository.getChats())
                swipeRefreshLayout.isRefreshing = false
                recyclerView.scrollToPosition(0)
            }
        }
    }
}
