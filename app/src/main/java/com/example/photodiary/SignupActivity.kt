package com.example.photodiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.photodiary.databinding.ActivitySignupBinding
import com.example.photodiary.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var myBinding : ActivitySignupBinding
    private lateinit var myFirebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(myBinding.root)
        myFirebaseAuth = FirebaseAuth.getInstance()

        myBinding.buttonSignup.setOnClickListener{
            val email = myBinding.editTextSignupEmail.text.toString()
            val password = myBinding.editTextSignupPassword.text.toString()
            val confirmPassword = myBinding.editTextSignupConfirmPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
                if (password == confirmPassword){
                    myFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if (it.isSuccessful){
                            val myIntent = Intent(this, LoginActivity::class.java)
                            startActivity(myIntent)
                        } else{
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                } else{
                    Toast.makeText(this, "Password is not matching!", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show()
            }
        }

        myBinding.buttonBackLogin.setOnClickListener {
            val myIntent = Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
        }



    }
}