package com.example.memoapp_1
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.rv_item.view.*

class RecyclerViewAdapter(val context: Context, db: MemoDatabase,
                          var memos: List<Memo>)
//class RecyclerViewAdapter(var memos: ArrayList<Memo>)
    :RecyclerView.Adapter <RecyclerViewAdapter.Viewholder>() {

    class Viewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var view: View = itemView
        fun bind(context: Context,  memo : Memo,listener: View.OnClickListener){
            view.rv_title.text = memo.title
            view.rv_content.text = memo.contents
            view.setOnClickListener(listener)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerViewAdapter.Viewholder {
        val context = parent.context
        val view = LayoutInflater.from(context)
                .inflate(R.layout.rv_item,parent,false)
        return RecyclerViewAdapter.Viewholder(view)
    }

    override fun getItemCount(): Int {
        return memos.size
    }

    override fun getItemId(position: Int): Long {
        return memos[position].id!!.toLong()
    }



    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val memo = memos[position]

        val listener = View.OnClickListener { it ->
            //Toast.makeText(it.context, "Clicked ${memo.title}",Toast.LENGTH_SHORT).show()
        }

        holder.apply {
            bind(context, memo,listener)
            itemView.tag = memo
        }
    }

}

