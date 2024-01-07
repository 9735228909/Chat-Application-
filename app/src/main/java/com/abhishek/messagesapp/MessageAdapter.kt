package com.abhishek.messagesapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context,val messagelist:ArrayList<Message>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEVIE = 1
    val ITEM_SENT = 2

    class SentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val sendmessage = itemView.findViewById<TextView>(R.id.txtsend_message)
    }

    class RecevieViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val receviemessage = itemView.findViewById<TextView>(R.id.txtrecevie_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1){
            val view = LayoutInflater.from(context).inflate(R.layout.recevie,parent,false)
            return RecevieViewHolder(view)
        }else{
            val view = LayoutInflater.from(context).inflate(R.layout.send,parent,false)
            return SentViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messagelist.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messagelist[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEVIE
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var currentMessage = messagelist[position]

        if (holder.javaClass == SentViewHolder::class.java){
            val viewholder = holder as SentViewHolder
            holder.sendmessage.text = currentMessage.message
        }
        else{
            val viewHolder = holder as RecevieViewHolder
            holder.receviemessage.text = currentMessage.message
        }

    }
}