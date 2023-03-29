package com.himanshu.storydrone

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.himanshu.storydrone.databinding.RecyclerPrivacyLayoutBinding

class PrivacyAdapter(var context: Context , var list : ArrayList<Pair<String, Any>> , val selectedStrings :  ArrayList<String>) :
    RecyclerView.Adapter<PrivacyAdapter.MyViewHolder>() {


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var binding =RecyclerPrivacyLayoutBinding.bind(view)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.recycler_privacy_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int
    {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        val item = list[position]

        if(FireStoreClass().getCurrentUserID() == item.first)
        {
            holder.binding.checkBox.visibility = View.GONE
            holder.binding.personName.visibility = View.GONE
        }

        else
        {
            holder.binding.personName.text = item.second.toString()

            holder.binding.checkBox.setOnClickListener {

                if(holder.binding.checkBox.isChecked)
                {
                    selectedStrings.add(item.first)
                }

                else if(!holder.binding.checkBox.isChecked)
                {
                    selectedStrings.remove(item.first)
                }

            }


        }




    }

}