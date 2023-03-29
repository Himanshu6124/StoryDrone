package com.himanshu.storydrone

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.himanshu.storydrone.databinding.ActivityMainBinding
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mSelectedImageFileUri: Uri? = null
    var list = kotlin.collections.ArrayList<String>()
    var uri: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        uri = intent.getStringExtra("URI").toString()

        Log.i("ghghghg", uri!!)

        if (uri != null) {
            GlideLoader(this).loadUserPicture(uri!!, binding.tvMyStory)

        }

        FireStoreClass().getStoriesListforCurrentUser(this)
        FireStoreClass().getMyStories(this)


        binding.tvAddStory.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
//                showErrorSnackBar("You Already have permission",false)
                Constants.showImageChooser(this)

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
//                showErrorSnackBar("You don't have permission",true)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                showErrorSnackBar("Permission Granted",false)
                Constants.showImageChooser(this)
            } else {
                showErrorSnackBar("Permission Denied", true)
            }
        }

    }


    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!
                        GlideLoader(this).loadUserPicture(
                            mSelectedImageFileUri!!,
                            binding.tvMyStory
                        )
                        val intent = Intent(this, PrivacyActivity::class.java)
                        intent.putExtra("URI", mSelectedImageFileUri.toString())
                        startActivity(intent)


                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@MainActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            showErrorSnackBar("Image selection cancelled", false)
            Log.e("Request Cancelled", "Image selection cancelled")
        }
    }

    fun fetchStorySuccess(storiesList: ArrayList<StoryView>)
    {
        Log.i("fetchedad",storiesList.toString())
        val validStoryList: ArrayList<StoryView> = ArrayList<StoryView>()
        for(i in storiesList){
            val time = Date().time.toLong()
            if( time - i.timeStamp.toLong() < 600000){
                validStoryList.add(i)
            }
        }

        val adapter = StoryShowAdapter(this,validStoryList)
        binding.recyclerStory.layoutManager = LinearLayoutManager(this)
        binding.recyclerStory.setHasFixedSize(true)
        binding.recyclerStory.adapter = adapter

    }

    fun fetchMyStorySuccess(myStorysList: ArrayList<String>)
    {
        if(myStorysList.size > 0)
        {
            GlideLoader(this).loadUserPicture(
                myStorysList[0],
                binding.tvMyStory
            )

        }

    }


}