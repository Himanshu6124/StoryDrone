package com.himanshu.storydrone

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.himanshu.storydrone.databinding.RvMyStoryBinding
import com.himanshu.storydrone.databinding.StoryLayoutBinding

class MyStoryAdapter(var context: Context ,var list: ArrayList<String>) : RecyclerView.Adapter<MyStoryAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var binding = RvMyStoryBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyStoryAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.rv_my_story,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val item = list[position]

        holder.itemView.setOnClickListener {
            holder.itemView.setOnClickListener {
                val intent = Intent(context, StoryScreenActivty::class.java)
                intent.putExtra("URI", item)
                context.startActivity(intent)


//                Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show()
            }
        }

        GlideLoader(context).loadUserPicture(item, holder.binding.tvIconStory)

    }

}