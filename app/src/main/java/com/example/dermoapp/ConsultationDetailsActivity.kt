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
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.dermoapp.data.Consultation
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.dermoapp.utils.ConnectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_consultation_details.*
import kotlinx.android.synthetic.main.activity_create_consultation.*
import org.json.JSONObject




class ConsultationDetailsActivity : AppCompatActivity(){

    private lateinit var sharedPreferences: SharedPreferences
    private val spFileName = "dermoappFile"
    private lateinit var username:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultation_details)

        sharedPreferences = getSharedPreferences(spFileName, Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()

        val getData = intent.getParcelableExtra<Consultation>("android")
        if (getData != null) {
            val detailDiagnosis: TextView = findViewById(R.id.DetailsTV)
            val detailAccept: TextView = findViewById(R.id.DetailsAcceptTV)
            val detailWithOutDiagnosis: TextView = findViewById(R.id.DetailsDiagnosisTV)
            val detailImage: ImageView = findViewById(R.id.DetailsIv)
            detailDiagnosis.text = getData.diagnosis
            Picasso.get().load(getData.image).into(detailImage)
            if ((getData.acceptDiagnosis=="false") and (getData.diagnosis!="diagnosis") and (getData.comment!="Diagnóstico Generado automática")) {
                detailAccept.text = "Aceptar diagnóstico"

            }

            if (getData.diagnosis=="diagnosis") {
                detailWithOutDiagnosis.text = "Sin diagnóstico"

            }

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

        val getData = intent.getParcelableExtra<Consultation>("android")

        val queue=Volley.newRequestQueue(this)
        val userDetails=makeHashMapPatients().toMap()
        val jsonObjectRequest=object : JsonObjectRequest(
            Method.PUT,
            "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/consultations/${getData?.id}",
            JSONObject(userDetails),
            Response.Listener {
                try {
                    Toast.makeText(this,"Aceptando diagnóstico ...",Toast.LENGTH_SHORT).show()
                    mainActivityIntent()

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

        val getData = intent.getParcelableExtra<Consultation>("android")

        val userDetails=HashMap<String,String>()
        userDetails["shape"] = getData!!.shape
        userDetails["numberOfInjuries"] = getData.numberOfInjuries
        userDetails["distribution"] = getData.distribution
        userDetails["comment"] = getData.comment
        userDetails["image"] = getData.image
        userDetails["creationDate"] = getData.creationDate
        userDetails["typeOfInjury"] = getData.typeOfInjury
        userDetails["specialty"] = getData.specialty
        userDetails["diagnosis"] = getData.diagnosis
        userDetails["asigned"] = "true"
        userDetails["acceptDiagnosis"] = "true"

        return userDetails
    }

    private fun mainActivityIntent() = startActivity(Intent(this, MainActivity::class.java))

}

