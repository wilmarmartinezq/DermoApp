package com.example.dermoapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val spFileName = "dermoappFile"
    private lateinit var username:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences(spFileName, Context.MODE_PRIVATE)


        username = sharedPreferences.getString("username", "").toString()

        btnSignOut.setOnClickListener {
            AlertDialog
                .Builder(this)
                .setTitle("Confirmación")
                .setMessage("Seguro que quieres cerrar sesión?")
                .setPositiveButton("Yes") { dialog, _ ->
                    //log out
                    sharedPreferences.edit().clear().apply()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()

        }

        btnConsultation.setOnClickListener {
            consultationActivityIntent()
        }




    }






    private fun consultationActivityIntent() = startActivity(Intent(this, CreateConsultationActivity::class.java))


    }



