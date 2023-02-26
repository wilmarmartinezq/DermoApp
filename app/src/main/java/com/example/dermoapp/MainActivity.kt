package com.example.dermoapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.dermoapp.adapter.VolleyAdapter
import com.example.dermoapp.data.Consultation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.dermoapp.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private lateinit var requestQueue: RequestQueue
    private val url = "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/consultations"
    private lateinit var adapter: VolleyAdapter


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


        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.recycler.layoutManager = LinearLayoutManager(this)

        requestQueue = Volley.newRequestQueue(this)
        VolleyLog.DEBUG = true
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            {
                val type = object : TypeToken<List<Consultation>>() {}.type
                val list: List<Consultation> = Gson().fromJson(it.toString(), type)

                adapter = VolleyAdapter(list)
                mainBinding.recycler.adapter = adapter
            },
            {

            }
        )

        requestQueue.add(jsonArrayRequest)



    }



    private fun consultationActivityIntent() = startActivity(Intent(this, CreateConsultationActivity::class.java))


    }



