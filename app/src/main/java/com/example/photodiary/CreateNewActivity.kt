package com.example.photodiary

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.photodiary.databinding.ActivityCreateNewBinding
import com.example.photodiary.databinding.ActivityMainBinding
import com.example.photodiary.databinding.ActivityStoryviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

@Suppress("DEPRECATION")
class CreateNewActivity : AppCompatActivity() {
    private lateinit var myBinding: ActivityCreateNewBinding
    lateinit var auth : FirebaseAuth
    lateinit var databaseRef: DatabaseReference
    private var imageUri  : Uri? = null

    companion object{
        val IMAGE_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myBinding = ActivityCreateNewBinding.inflate(layoutInflater)
        setContentView(myBinding.root)

        myBinding.cardViewAddImage.setOnClickListener { pickImage() }
        myBinding.buttonPost.setOnClickListener { createPost() }

//        myBinding.cardViewAddTitle.setOnClickListener { pickTitle() }
    }

    private fun pickImage() {
        val imageIntent = Intent(Intent.ACTION_PICK)
        imageIntent.type = "image/*"
        startActivityForResult(imageIntent, IMAGE_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            myBinding.imageViewImageNew.setImageURI(data?.data)
            imageUri = data?.data
        }
    }


    private fun createPost() {
        val storyTitle = myBinding.editTextTitle.text.toString()
        val storyLocation = myBinding.editTextLocation.text.toString()
        val storyDescription = myBinding.editTextDescription.text.toString()
        val storyImage = imageUri
        val storyRating = myBinding.ratingBarPersonal.rating
        val myPost = Posts(storyTitle, storyLocation,
            storyDescription, storyImage, storyRating)

        if (storyTitle.isEmpty() || storyLocation.isEmpty() || storyDescription.isEmpty()){
            Toast.makeText(this, "Empty Field(s) Found", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this, "TRYING!!!", Toast.LENGTH_SHORT).show()
            auth = FirebaseAuth.getInstance()
            databaseRef = FirebaseDatabase.getInstance().getReference("Posts") //.child(auth.currentUser?.uid.toString())
            databaseRef.setValue(myPost).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "Story Posted Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error while posting", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}