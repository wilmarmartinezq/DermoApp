package com.example.dermoapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.dermoapp.data.Consultation
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.dermoapp.utils.ConnectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_consultation_details.*
import org.json.JSONObject




class ConsultationDetailsActivity : AppCompatActivity(){

    private lateinit var sharedPreferences: SharedPreferences
    private val spFileName = "dermoappFile"
    private lateinit var username:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultation_details)


        val getData = intent.getParcelableExtra<Consultation>("android")
        if (getData != null) {
            val detailTitle: TextView = findViewById(R.id.DetailsTV)
            val detailImage: ImageView = findViewById(R.id.DetailsIv)
            detailTitle.text = getData.diagnosis
            Picasso.get().load(getData.image).into(detailImage)
        }
    }



    fun detailBackIntent(view: View) {
        onBackPressed()
    }

    fun acceptDiagnosis(view: View) {

        if(!ConnectionManager().isNetworkAccessActive(this)){
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setIcon(R.drawable.no_connection)
                    .setTitle("Sin Internet")
                    .setMessage("Favor revisar el acceso a internet")
                    .setPositiveButton("Retry") { dialog, _ ->
                        if (ConnectionManager().isNetworkAccessActive(this)) {
                            dialog.dismiss()
                            register()
                        } else {
                            Toast.makeText(this, "Todavía desconectado :(", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Open Settings") { _, _ ->
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }.show()
            }else{
                register()
            }

    }


    private fun register() {

        val sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE)
        val value = sharedPreferences.getString("value", "")

        val queue=Volley.newRequestQueue(this)
        val userDetails=makeHashMapPatients().toMap()
        val jsonObjectRequest=object : JsonObjectRequest(
            Method.PUT,
            "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/patients/$username/consultations/$value",
            JSONObject(userDetails),
            Response.Listener {
                try {
                    Toast.makeText(this,"Aceptando diagnóstico ...",Toast.LENGTH_SHORT).show()

                }catch (e:Exception){
                    Toast.makeText(this, "Un error inesperado ha ocurrido", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(this, "Un error inesperado ha ocurrido", Toast.LENGTH_SHORT).show()


            }) {
        }
        queue.add(jsonObjectRequest)
    }





    private fun makeHashMapPatients():Map<String,String> {
        val userDetails=HashMap<String,String>()
        userDetails["acceptDiagnosis"] = "Si"

        return userDetails
    }



/*
    private fun getData() {

        val id = intent.getStringExtra("key")


        var url = "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/patients/$username/consultations/$id"

        val queue = Volley.newRequestQueue(this@ConsultationDetailsActivity)

        val request =
            JsonArrayRequest(Request.Method.GET, url, null, { response ->
                try {
                    for (i in 0 until response.length()) {
                        val respObj = response.getJSONObject(i)
                        val idd = respObj.getString("id")
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
                        val acceptDiagnosis = respObj.getString("acceptDiagnosis")

                        consultationDetail.add(Consultation(idd,shape,numberOfInjuries,distribution,comment,image,creationDate,typeOfInjury,specialty,diagnosis,asigned,acceptDiagnosis))

                        consultationDetailRVAdapter.notifyDataSetChanged()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }, { error ->
                Toast.makeText(this@ConsultationDetailsActivity, "Error al recibir datos", Toast.LENGTH_SHORT)
                    .show()
            })
        queue.add(request)
    }

 */

}

private fun ImageView.setImageResource(image: String) {

}
