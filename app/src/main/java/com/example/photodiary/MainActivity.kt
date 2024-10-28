package com.example.photodiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AbsListView.RecyclerListener
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import com.example.photodiary.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
    private lateinit var myBinding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    lateinit var myRecylerView: RecyclerView
    lateinit var myArrayList: ArrayList<Story>
    lateinit var auth : FirebaseAuth
    lateinit var databaseRef: DatabaseReference
    lateinit var uid : String
    var posts : Posts? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(myBinding.root)

        supportFragmentManager.commit {
            add(R.id.frame_content, AllviewFragment())
        }

        myBinding.bottomNav.setOnItemSelectedListener(this)

        myBinding.addNewStory.setOnClickListener { goToCreateNewActivity() }

        ////////////////////////////////////////////////////////////////////////////////
        userData()
        ////////////////////////////////////////////////////////////////////////////////

    }

    private fun userData() {
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().getReference("Posts")
        if (uid.isNotEmpty()){
            getUserData()
        }
    }

    private fun getUserData() {
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                posts = snapshot.getValue(Posts::class.java)
                Toast.makeText(this@MainActivity, "GOT THE DATA!!!", Toast.LENGTH_SHORT).show()
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "CAN'T FETCH DATA!!!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToCreateNewActivity() {
        val myIntent = Intent(this, CreateNewActivity::class.java)
        startActivity(myIntent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.nav_allview -> {onAllviewClicked(); true}
            R.id.nav_onlyphotos -> {onOnlyphotosClicked(); true}
            else -> false
        }
    }

    private fun onOnlyphotosClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, OnlyphotosFragment())
        }
    }
    private fun onAllviewClicked() {
        supportFragmentManager.commit {
            replace(R.id.frame_content, AllviewFragment())
        }
    }

}