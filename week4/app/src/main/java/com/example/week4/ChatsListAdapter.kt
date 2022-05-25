package ru.alexanderklimov.recyclerviewdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.week4.ChatData
import com.example.week4.CurrentChatActivity
import com.example.week4.R
import java.io.Serializable

class ChatsListAdapter(private var chatList: MutableList<ChatData>, private var activityResultLauncher: ActivityResultLauncher<Intent>) : RecyclerView
.Adapter<ChatsListAdapter.MyViewHolder>() {
    //адаптер для cписокв чатов

    // Для вычисления разницы
    private val diffCallback: DiffUtil.ItemCallback<ChatData> = object : DiffUtil.ItemCallback<ChatData>() {
        override fun areItemsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
            return oldItem.mChatId == newItem.mChatId

        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ChatData, newItem: ChatData): Boolean {
            return oldItem.currentChatRepository == newItem.currentChatRepository
        }
    }

    //хранение исходного списка
    private var mDiffer: AsyncListDiffer<ChatData> = AsyncListDiffer(this, diffCallback)


    //обновление данных
    fun submitList(data: List<ChatData>) {
         mDiffer.submitList(data)
    }

    override fun getItemCount() = mDiffer.getCurrentList().size;


    fun getList(): MutableList<ChatData> = mDiffer.getCurrentList();

    // для обновления текста
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var imageChat: ImageView
        lateinit var tvNameChat: TextView
        lateinit var  tvLastMessage: TextView

        // задаем даннные в окне
        fun setData(chatData: ChatData, activityResultLauncher: ActivityResultLauncher<Intent>, position: Int) {
            imageChat = itemView.findViewById(R.id.iv_image)
            tvNameChat = itemView.findViewById(R.id.tv_name)
            tvLastMessage = itemView.findViewById(R.id.tv_first_message)

            imageChat.setImageResource(chatData.mChatImage)
            tvNameChat.text = chatData.mChatName
            tvLastMessage.text = chatData.currentChatRepository.getCurChat(0).mMessage

            // задаем onClick для элемента, открывающий чат
            val mContext = itemView.context
            itemView.isClickable = true
            itemView.setOnClickListener{
                val intent = Intent(
                    mContext,
                    CurrentChatActivity::class.java).apply {
                        putExtra("position", position)
                        putExtra("name", chatData.mChatName)
                        putExtra("firstMessage", chatData.mChatName)
                        putExtra("image", chatData.mChatImage)
                        putExtra("currentChatRepository", chatData.currentChatRepository as Serializable)
                    }

                activityResultLauncher.launch(intent)
            }
        }
    }

    // задаем значение в элементе
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(mDiffer.currentList[position], activityResultLauncher, position)
    }

    //указываем идентификато макета для элемента
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }
}