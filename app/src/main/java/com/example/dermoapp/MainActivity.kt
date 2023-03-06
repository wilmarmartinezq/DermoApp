package com.example.dermoapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.content.SharedPreferences
import android.app.AlertDialog
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.dermoapp.adapter.ConsultationRVAdapter
import com.example.dermoapp.data.Consultation
import kotlinx.android.synthetic.main.item_consultation.*

    class MainActivity : AppCompatActivity() {

        private lateinit var sharedPreferences: SharedPreferences
        private val spFileName = "dermoappFile"
        private lateinit var username:String

        lateinit var consultationRV: RecyclerView
        lateinit var consultationRVAdapter: ConsultationRVAdapter
        lateinit var consultationList: ArrayList<Consultation>

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            sharedPreferences = getSharedPreferences(spFileName, Context.MODE_PRIVATE)
            username = sharedPreferences.getString("username", "").toString()


            consultationRV = findViewById(R.id.idRVConsultations)
            consultationList = ArrayList()
            consultationRVAdapter = ConsultationRVAdapter(consultationList)
            consultationRV.adapter = consultationRVAdapter

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


            getData()

            btnConsultation.setOnClickListener {
                consultationActivityIntent()
            }





        }



        private fun getData() {
            var url = "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/consultations"

            val queue = Volley.newRequestQueue(this@MainActivity)

            val request =
                JsonArrayRequest(Request.Method.GET, url, null, { response ->
                    try {
                        for (i in 0 until response.length()) {
                            val respObj = response.getJSONObject(i)
                            val id = respObj.getString("id")
                            val shape = respObj.getString("shape")
                            val numberOfInjuries = respObj.getString("numberOfInjuries")
                            val distribution = respObj.getString("distribution")
                            val comment = respObj.getString("comment")
                            val image = respObj.getString("image")
                            val creationDate = respObj.getString("creationDate")
                            val typeOfInjury = respObj.getString("typeOfInjury")
                            val specialty = respObj.getString("specialty")
                            val diagnosis = respObj.getString("diagnosis")
                            val asigned = respObj.getBoolean("asigned")
                            val acceptDiagnosis = respObj.getBoolean("acceptDiagnosis")

                            consultationList.add(Consultation(id,shape,numberOfInjuries,distribution,comment,image,creationDate,typeOfInjury,specialty,diagnosis,asigned,acceptDiagnosis))

                            consultationRVAdapter.notifyDataSetChanged()
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }, { error ->
                    Toast.makeText(this@MainActivity, "Error al recibir datos", Toast.LENGTH_SHORT)
                        .show()
                })
            queue.add(request)
        }

        private fun consultationActivityIntent() = startActivity(Intent(this, CreateConsultationActivity::class.java))


        fun consultationDetailsConfirm(view: View) {
            val id = findViewById<TextView>(R.id.id)
            val Id = id.text.toString()
            intent.putExtra("id_key", Id)
            startActivity(Intent(this, ConsultationDetailsActivity::class.java))

        }

    }




