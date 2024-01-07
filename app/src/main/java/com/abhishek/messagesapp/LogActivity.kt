package com.abhishek.messagesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isInvisible
import com.abhishek.messagesapp.databinding.ActivityLogBinding
import com.google.firebase.auth.FirebaseAuth

class LogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogBinding
    private lateinit var auth:FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.txtLogin.setOnClickListener {
            val intent = Intent(this,SingUpActivity::class.java)
            startActivity(intent)
        }
        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            validation(email,password)
            
            singin(email,password)

        }
    }

    private fun singin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Authentication success",
                        Toast.LENGTH_SHORT,
                    ).show()
                    val intent = Intent(this,HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {

                    Toast.makeText(
                        this,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun validation(email: String, password: String):Boolean {

        if ( email.isBlank() || password.isBlank()) {
            if (email.isBlank()) {
                binding.edtEmail.error = "please enter your Email"
            }
            if (password.isBlank()) {
                binding.edtPassword.error = "please enter your Password"
            }
            return false
        }
        else {
            return true
        }
    }

}