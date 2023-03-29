package com.himanshu.storydrone

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.himanshu.storydrone.databinding.StoryLayoutBinding

class StoryShowAdapter(var context: Context , var list : ArrayList<StoryView> ) :
    RecyclerView.Adapter<StoryShowAdapter.MyViewHolder>() {


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var binding = StoryLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return StoryShowAdapter.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.story_layout,
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
                intent.putExtra("URI", item.uri)
                context.startActivity(intent)


//                Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show()
            }
        }

            //        Log.i("jkjjkk",item.uri)
            GlideLoader(context).loadUserPicture(item.uri, holder.binding.tvMyStory)


//        val fullName =
//            FireStoreClass().getUserNameById(item.uid)
            getUserNameById(item.uid, holder.binding.tvName)

            Log.i("fullname", item.uid)

//        holder.binding.tvName.text = fullName
            holder.binding.tvTime.text = item.time

        }
        fun getUserNameById(uid: String, tvName: TextView) {
            var fullName: String = ""
            val mFireStore = FirebaseFirestore.getInstance()

            mFireStore.collection(Constants.USERS).get().addOnSuccessListener { documents ->
                for (i in documents) {
                    if (uid.equals(i.id)) {
                        val firstName = i["firstName"]
                        val lastName = i["lastName"]
                        fullName = "$firstName  $lastName"
                        tvName.text = fullName


                    }

                }


            }


        }




}