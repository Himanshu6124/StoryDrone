package com.himanshu.storydrone

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.himanshu.storydrone.databinding.ActivityPrivacyBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PrivacyActivity : BaseActivity()
{
    private lateinit var list : ArrayList<Pair<String, Any>>
    private var selectedStrings = ArrayList<String>()
    private lateinit var binding : ActivityPrivacyBinding
    var uri: String? = null
    override fun onCreate(savedInstanceState: Bundle?)
    {
             uri = intent.getStringExtra("URI")

        super.onCreate(savedInstanceState)
        binding =DataBindingUtil.setContentView(this,R.layout.activity_privacy)

        FireStoreClass().getListOfUsers(this)

        binding.button2.setOnClickListener {

            FireStoreClass().uploadImageToCloudStorage(this, Uri.parse(uri),"story")
//
//            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("URI", uri.toString())
//            startActivity(intent)


        }


    }

    fun userDetailGetSuccess(mp: ArrayList<Pair<String, Any>>)
    {
        list = mp
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        val adapter = PrivacyAdapter(this,list,selectedStrings)

        binding.recyclerView.adapter = adapter

    }

    fun imageUploadSuccess(imageURL: Uri) {

        // Hide the progress dialog
//        hideProgressDialog()

        uri = imageURL.toString()
        Log.i("jodjod4", uri.toString())

        uploadStory()
    }

    fun uploadStory() {
        val uid = FireStoreClass().getCurrentUserID()
        //This method returns the time in millis
        val date = Date()
        val timeMilli: Long = date.time
        val uri = uri.toString()
        val formatter = SimpleDateFormat("hh:mm a")
        val time = formatter.format(Date())
        val story = Story(uid, uri, timeMilli.toString(), time.toString(), selectedStrings)

        FireStoreClass().uploadStoryToFireStore(this, story)

    }
    fun uploadStoryToFireStoreSuccess() {
//        hideProgressDialog()

        Log.i("HPHP", "succ")

        Toast.makeText(
            this,
            resources.getString(R.string.story_uploaded),
            Toast.LENGTH_SHORT
        ).show()
    }


}