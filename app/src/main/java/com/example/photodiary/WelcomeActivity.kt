package com.example.photodiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.photodiary.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var myBinding : ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(myBinding.root)

        myBinding.cardViewLogin.setOnClickListener {
            val myIntent = Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
        }
        myBinding.cardViewSignup.setOnClickListener {
            val myIntent = Intent(this, SignupActivity::class.java)
            startActivity(myIntent)
        }

    }
}