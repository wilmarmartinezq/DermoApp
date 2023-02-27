package com.example.dermoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.dermoapp.adapter.ConsultationRVAdapter
import com.example.dermoapp.data.Consultation
import kotlinx.android.synthetic.main.item_consultation.*

    class MainActivity : AppCompatActivity() {

        lateinit var consultationRV: RecyclerView
        lateinit var consultationRVAdapter: ConsultationRVAdapter
        lateinit var consultationList: ArrayList<Consultation>

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            consultationRV = findViewById(R.id.idRVConsultations)
            consultationList = ArrayList()
            consultationRVAdapter = ConsultationRVAdapter(consultationList)
            consultationRV.adapter = consultationRVAdapter

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
                            // on below line we are extracting
                            // data from each json object
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

                            consultationList.add(Consultation(id,shape,numberOfInjuries,distribution,comment,image,creationDate,typeOfInjury,specialty))

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

    }




