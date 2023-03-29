package com.himanshu.storydrone

import android.app.Activity
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireStoreClass {
    // Access a Cloud Firestore instance.
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!


                when (activity) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess(user)
                    }

                }
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    fun uploadStoryToFireStore(activity: PrivacyActivity, story: Story) {
        mFireStore.collection("stories")
            // Document ID for users fields. Here the document it is the User ID.
            .document(story.hashCode().toString())
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(story, SetOptions.merge())
            .addOnSuccessListener {


                // Here call a function of base activity for transferring the result to it.
                activity.uploadStoryToFireStoreSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getStoriesListforCurrentUser(activity: MainActivity) {
        val arr = mutableListOf<DocumentSnapshot>()
        val storiesList = ArrayList<StoryView>()

        mFireStore.collection("stories").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    arr.add(document)
                    Log.i("ssdfsf", document.id)
                }

                for (i in arr) {
//                    for(x in i["visible"])

                    if (i["visibleTo"] != null) {
                        val list: List<String> = i["visibleTo"] as List<String>

                        for (x in list) {
                            val myUid = FireStoreClass().getCurrentUserID()

                            if (x.equals(FireStoreClass().getCurrentUserID())) {


                                val stor = StoryView(
                                    i["uri"] as String,
                                    i["uploaderId"] as String,
                                    i["uploadTime"] as String,
                                    i["uploadTimeStamp"] as String
                                )
                                storiesList.add(stor)
                            }
                        }

                    }

                    Log.i("dgdgsf", i["visible"].toString())
                }

                activity.fetchStorySuccess(storiesList)


                        Log.i("sgdgd", storiesList.toString())

            }
            .addOnFailureListener {

            }
    }

    fun uploadImageToCloudStorage(activity: PrivacyActivity, imageFileURI: Uri?, imageType: String) {
        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )

        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                Log.i("jodjod2", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())


                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())

                        Log.i("jodjod3", uri.toString())


                        // Here call a function of base activity for transferring the result to it.

                        activity.imageUploadSuccess(uri)


                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.


                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }


    fun getListOfUsers(activity: PrivacyActivity) {
        var mp: ArrayList<Pair<String, Any>> = ArrayList<Pair<String, Any>>()



        mFireStore.collection(Constants.USERS).get().addOnSuccessListener { documents ->

            for (doc in documents) {
                val firstName = doc["firstName"]
                val lastName = doc["lastName"]
                val id = doc.id

                val fullName = "$firstName $lastName"

                val userHashMap = HashMap<String, Any>()
                userHashMap[id] = fullName

                mp.add(Pair(id, fullName))


//                Log.i("hjhjhhj",mp[0].toString())

            }
            activity.userDetailGetSuccess(mp)



            for (i in mp) {
//                val x = i.f

                Log.i("hjhjhhj", i.second.toString())

            }
        }
            .addOnFailureListener {
            }

    }

    fun getUserNameById(uid : String)
    {
        var fullName : String = ""
        mFireStore.collection(Constants.USERS).get().addOnSuccessListener {documents->
            for(i in documents)
            {
                if(uid.equals(i.id))
                {
                    val firstName = i["firstName"]
                    val lastName = i["lastName"]
                    fullName =  "$firstName  $lastName"
                    Log.i("fullname",fullName)


                }

            }


        }.addOnFailureListener {

        }
        Log.i("fullname",fullName)
//        return fullName


    }
    fun getMyStories(activity: MainActivity) {
        val arr = mutableListOf<DocumentSnapshot>()
        val myStorysList = ArrayList<String>()

        mFireStore.collection("stories").get().addOnSuccessListener { documents ->

            for (doc in documents) {
                val uploaderId = doc["uploaderId"]

                if(uploaderId == getCurrentUserID())
                {
                    myStorysList.add(doc["uri"] as String)
                }

                }

            }
            activity.fetchMyStorySuccess(myStorysList)


    }


}