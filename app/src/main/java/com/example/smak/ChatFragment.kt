package com.example.smak

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smak.databinding.FragmentChatBinding
import com.example.smak.databinding.ItemMessageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


/*class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ChatAdapter
    private lateinit var database: DatabaseReference
    private lateinit var currentUser: FirebaseUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ChatAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().reference
        currentUser = FirebaseAuth.getInstance().currentUser!!

        listenForMessages()

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(currentUser.uid, messageText)
                binding.messageEditText.text.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendMessage(senderId: String, messageText: String) {
        val message = Message(senderId, messageText)
        database.child("chats").push().setValue(message)
    }

    private fun listenForMessages() {
        database.child("chats").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    adapter.addMessage(message)
                    binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
        })
    }
}
data class Message(val senderId: String, val messageText: String) {
    constructor() : this("", "")
}

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private val messageList = mutableListOf<Message>()

    fun addMessage(message: Message) {
        messageList.add(message)
        notifyItemInserted(messageList.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMessageBinding.inflate(layoutInflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount(): Int = messageList.size

    inner class MessageViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.senderTextView.text = message.senderId
            binding.messageTextView.text = message.messageText
        }
    }
}*/

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ChatAdapter
    private lateinit var database: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var recipientId: String
    private lateinit var chatId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ChatAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().reference
        currentUser = FirebaseAuth.getInstance().currentUser!!

        recipientId = "ID_DEL_DESTINATARIO"

        chatId = "CHAT_${currentUser.uid}_${recipientId}"

        listenForMessages()

        binding.sendButton.setOnClickListener {
            val messageText = binding.messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(currentUser.uid, recipientId, messageText)
                binding.messageEditText.text.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun sendMessage(senderId: String, recipientId: String, messageText: String) {
        val message = Message(senderId, recipientId, messageText)
        database.child("chats").child(chatId).push().setValue(message)
    }

    private fun listenForMessages() {
        database.child("chats").child(chatId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)
                if (message != null) {
                    adapter.addMessage(message)
                    binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
        })
    }
}

data class Message(val senderId: String, val recipientId: String, val messageText: String) {
    constructor() : this("", "", "")
}

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
    private val messageList = mutableListOf<Message>()

    fun addMessage(message: Message) {
        messageList.add(message)
        notifyItemInserted(messageList.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMessageBinding.inflate(layoutInflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount(): Int = messageList.size

    inner class MessageViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.senderTextView.text = message.senderId
            binding.messageTextView.text = message.messageText
        }
    }
}