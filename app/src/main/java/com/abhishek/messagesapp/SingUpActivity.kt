package com.abhishek.messagesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.abhishek.messagesapp.databinding.ActivitySingUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SingUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySingUpBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var mdata:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


        binding.txtview.setOnClickListener {
            val intent = Intent(this,LogActivity::class.java)
            startActivity(intent)
            finish()
        }

        auth = FirebaseAuth.getInstance()

        binding.btnSingup.setOnClickListener {
            val name = binding.edtName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val number = binding.edtNumber.text.toString()

            singup(name,email,password)

            validation(name,email,password,number)

        }

    }

    private fun validation(name: String, email: String, password: String, number: String):Boolean {

        if (name.isBlank() || email.isBlank() || password.isBlank() || number.isBlank()) {
            if (name.isBlank()) {
                binding.edtName.error = "please enter your Nmae"
            }
            if (email.isBlank()) {
                binding.edtEmail.error = "please enter your Email"
            }
            if (password.isBlank()) {
                binding.edtPassword.error = "please enter your Password"
            }
            if (number.isBlank()) {
                binding.edtNumber.error = "please enter your Number"
            }
            return false
        }
        else {
            return true
         }
    }

    private fun singup(name: String,email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Authentication success",
                        Toast.LENGTH_SHORT,
                    ).show()
                    addUserToDatabase(name,email,auth.currentUser?.uid!!)
                    val intent = Intent(this,HomeActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {

        mdata= FirebaseDatabase.getInstance().getReference()
        mdata.child("User").child(uid).setValue(User(name,email,uid))

    }
}