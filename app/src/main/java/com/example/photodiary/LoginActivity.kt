package com.example.photodiary

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.photodiary.databinding.ActivityLoginBinding
import com.example.photodiary.databinding.ActivityWelcomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var myBinding : ActivityLoginBinding
    private lateinit var myFirebaseAuth : FirebaseAuth
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(myBinding.root)
        myFirebaseAuth = FirebaseAuth.getInstance()

        myBinding.buttonLogin.setOnClickListener {
            manualLogin()
        }

        val GSO = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Resource value given
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, GSO)

        myBinding.imageViewGoogleSignIn.setOnClickListener {
            signInGoogle()
        }

        myBinding.buttomBackSignup.setOnClickListener {
            val myIntent = Intent(this, SignupActivity::class.java)
            startActivity(myIntent)
        }

    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent

        val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            if (result.resultCode == Activity.RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }
        }

        launcher.launch(signInIntent)
    }
    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            } else {
                Toast.makeText(this, "Login Failed!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val myCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        myFirebaseAuth.signInWithCredential(myCredential).addOnCompleteListener{
            if (it.isSuccessful){
                val myIntent2 = Intent(this, MainActivity::class.java)
                startActivity(myIntent2)
            } else{
                Toast.makeText(this, "Login Failed for Unknown Reason!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun manualLogin() {
        val email = myBinding.editTextLoginEmail.text.toString()
        val password: String = myBinding.editTextLoginPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty() ){
            myFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if (it.isSuccessful){
                    val myIntent = Intent(this, MainActivity::class.java)
                    startActivity(myIntent)
                } else{
                    Toast.makeText(this, "Email and Password does not match", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Toast.makeText(this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show()
        }
    }
}


