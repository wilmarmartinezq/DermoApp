package com.example.dermoapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.dermoapp.utils.ConnectionManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_create_consultation.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.io.IOException
import java.lang.Exception
import java.util.*

class CreateConsultationActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val spFileName="dermoappFile"

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var imagePreview: ImageView
    lateinit var btn_choose_image: Button
    private lateinit var username:String
    private lateinit var typeofinjuryvalue:String
    private lateinit var shapetypevalue:String
    private lateinit var numberofinjuriesvalue:String
    private lateinit var distributionvalue:String
    private lateinit var comment:String
    private lateinit var creationdate:String
    private lateinit var speciality:String
    private var idvalue = UUID.randomUUID().toString()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_consultation)
        sharedPreferences = getSharedPreferences(spFileName, Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "").toString()
        btn_choose_image = findViewById(R.id.btn_choose_image)
        imagePreview = findViewById(R.id.image_preview)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        btn_choose_image.setOnClickListener { launchGallery() }



        val typeofinjury = resources.getStringArray(R.array.injuryType_array)

        val injuryspinner = findViewById<Spinner>(R.id.TypeOfInjury)
        if (injuryspinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, typeofinjury)
            injuryspinner.adapter = adapter

            injuryspinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    typeofinjuryvalue = parent.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }



        val shapetype = resources.getStringArray(R.array.shapeType_array)

        val shapespinner = findViewById<Spinner>(R.id.shapeType)
        if (shapespinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, shapetype)
            shapespinner.adapter = adapter

            shapespinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    shapetypevalue = parent.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        val numberofinjuries = resources.getStringArray(R.array.numberOfInjuries_array)

        val numberofinjuriesspinner = findViewById<Spinner>(R.id.numberOfInjuries)
        if (numberofinjuriesspinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, numberofinjuries)
            numberofinjuriesspinner.adapter = adapter

            numberofinjuriesspinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    numberofinjuriesvalue = parent.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        val distribution = resources.getStringArray(R.array.distribution_array)

        val distributionspinner = findViewById<Spinner>(R.id.distribution)
        if (distributionspinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, distribution)
            distributionspinner.adapter = adapter

            distributionspinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    distributionvalue = parent.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }



    }

    fun createConsultationBackIntent(view:View) {
        onBackPressed()
    }



    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imagePreview.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    fun consultationUpConfirm(view: View) {
        comment=editTextComentario.text.toString()
        creationdate= ""

        if(typeofinjuryvalue=="M??cula") {
            speciality = "pediatrics"
        }

        if(typeofinjuryvalue=="P??pula") {
            speciality = "elderly"
        }

        if(typeofinjuryvalue=="Parche") {
            speciality = "cosmetic"
        }

        if(typeofinjuryvalue=="Placa") {
            speciality = "oncology"
        }

        if(typeofinjuryvalue=="N??dulo") {
            speciality = "oncology"
        }

        if(typeofinjuryvalue=="Ampolla") {
            speciality = "cosmetic"
        }

        if(typeofinjuryvalue=="??lcera") {
            speciality = "oncology"
        }

        if(typeofinjuryvalue=="Ves??cula") {
            speciality = "cosmetic"
        }

        if(typeofinjuryvalue.isBlank() || shapetypevalue.isBlank() || numberofinjuriesvalue.isBlank() || distributionvalue.isBlank() || comment.isBlank() ) {
            Toast.makeText(this, "Todos los campos deben ser diligenciados", Toast.LENGTH_SHORT).show()
        }

        else{
            if(!ConnectionManager().isNetworkAccessActive(this)){
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setIcon(R.drawable.no_connection)
                    .setTitle("Sin Internet")
                    .setMessage("Favor revisar el acceso a internet")
                    .setPositiveButton("Retry") { dialog, _ ->
                        if (ConnectionManager().isNetworkAccessActive(this)) {
                            dialog.dismiss()
                            registerConsultation()
                        } else {
                            Toast.makeText(this, "Todav??a desconectado :(", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Open Settings") { _, _ ->
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }.show()
            }else{
                registerConsultation()

            }
        }
    }

    private fun registerConsultation() {


        if(filePath != null){
            val mProgressDialog = ProgressDialog(this)
            mProgressDialog.setTitle("")
            mProgressDialog.setMessage("Registrando consulta")

            val ref = storageReference?.child("images_consultations/" + UUID.randomUUID().toString())
            ref?.putFile(filePath!!)?.addOnSuccessListener {

                val result = it.metadata!!.reference!!.downloadUrl;
                result.addOnSuccessListener {
                    mProgressDialog.show()

                    var imageurl = it.toString()


                    fun makeHashMapConsultation():Map<String,String> {

                        val consultationDetails=HashMap<String,String>()
                        consultationDetails["id"] = idvalue
                        consultationDetails["shape"] = shapetypevalue
                        consultationDetails["numberOfInjuries"] = numberofinjuriesvalue
                        consultationDetails["distribution"] = distributionvalue
                        consultationDetails["comment"] = comment
                        consultationDetails["image"] = imageurl
                        consultationDetails["creationDate"] = creationdate
                        consultationDetails["typeOfInjury"] = typeofinjuryvalue
                        consultationDetails["specialty"] = speciality
                        consultationDetails["diagnosis"] = "diagnosis"
                        consultationDetails["asigned"] = "false"
                        consultationDetails["acceptDiagnosis"] = "false"



                        return consultationDetails

                    }

                    fun makeHashMapConsultationPatient():Map<String,String> {

                        val consultationPatientDetails=HashMap<String,String>()



                        return consultationPatientDetails

                    }

                    val queue= Volley.newRequestQueue(this)
                    val consultationDetails=makeHashMapConsultation().toMap()
                    val jsonObjectRequest=object : JsonObjectRequest(
                        Method.POST,
                        "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/consultations",

                        JSONObject(consultationDetails),
                        Response.Listener {
                            try {

                            }catch (e: Exception){
                                mProgressDialog.dismiss()
                                Toast.makeText(this, "Un error inesperado ha ocurrido", Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener {
                            Toast.makeText(this, "Verificar la conexi??n a internet", Toast.LENGTH_SHORT).show()
                            mProgressDialog.dismiss()
                        }) {
                    }

                    val queuepatient= Volley.newRequestQueue(this)
                    val consultationPatientDetails=makeHashMapConsultationPatient().toMap()
                    val jsonObjectRequestPatient=object : JsonObjectRequest(
                        Method.POST,
                        "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/patients/"+username+"/consultations/"+idvalue,

                        JSONObject(consultationPatientDetails),
                        Response.Listener {
                            try {
                                mProgressDialog.dismiss()
                                mainActivityIntent()
                            }catch (e: Exception){
                                mProgressDialog.dismiss()
                                Toast.makeText(this, "Un error inesperado ha ocurrido", Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener {
                            mProgressDialog.dismiss()
                            btn_register_consultation.setPressed(true)

                        }) {
                    }
                    queue.add(jsonObjectRequest)
                    queuepatient.add(jsonObjectRequestPatient)

                }
        }

            }
        else{
            Toast.makeText(this, "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show()
        }

    }



    private fun mainActivityIntent() = startActivity(Intent(this, MainActivity::class.java))

    fun consultationAutomaticUpConfirm(view: View) {
        comment=editTextComentario.text.toString()
        creationdate= ""

        if(typeofinjuryvalue=="M??cula") {
            speciality = "pediatrics"
        }

        if(typeofinjuryvalue=="P??pula") {
            speciality = "elderly"
        }

        if(typeofinjuryvalue=="Parche") {
            speciality = "cosmetic"
        }

        if(typeofinjuryvalue=="Placa") {
            speciality = "oncology"
        }

        if(typeofinjuryvalue=="N??dulo") {
            speciality = "oncology"
        }

        if(typeofinjuryvalue=="Ampolla") {
            speciality = "cosmetic"
        }

        if(typeofinjuryvalue=="??lcera") {
            speciality = "oncology"
        }

        if(typeofinjuryvalue=="Ves??cula") {
            speciality = "cosmetic"
        }

        if(typeofinjuryvalue.isBlank() || shapetypevalue.isBlank() || numberofinjuriesvalue.isBlank() || distributionvalue.isBlank()) {
            Toast.makeText(this, "Todos los campos deben ser diligenciados", Toast.LENGTH_SHORT).show()
        }

        else{
            if(!ConnectionManager().isNetworkAccessActive(this)){
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setIcon(R.drawable.no_connection)
                    .setTitle("Sin Internet")
                    .setMessage("Favor revisar el acceso a internet")
                    .setPositiveButton("Retry") { dialog, _ ->
                        if (ConnectionManager().isNetworkAccessActive(this)) {
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this, "Todav??a desconectado :(", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Open Settings") { _, _ ->
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }.show()
            }else{
                registerConsult()
                registerConsultAutomatic()
                registerConsultationPatient()
            }
        }



    }



    private fun registerConsult() {

        val queue=Volley.newRequestQueue(this)
        val userDetails=makeHashMapConsult().toMap()
        val jsonObjectRequest=object : JsonObjectRequest(
            Method.POST,
            "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/consultations",
            JSONObject(userDetails),
            Response.Listener {
                try {
                    Toast.makeText(this, "Registrando consulta automatica", Toast.LENGTH_SHORT).show()

                }catch (e:Exception){
                    Toast.makeText(this, "Un error inesperado ha ocurrido", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
            }) {
        }
        queue.add(jsonObjectRequest)
    }


    private fun registerConsultAutomatic() {

        val queue=Volley.newRequestQueue(this)
        val userDetails=makeHashMapConsultAutomatic().toMap()
        val jsonObjectRequest=object : JsonObjectRequest(
            Method.POST,
            "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/autodiagnosis",
            JSONObject(userDetails),
            Response.Listener {
                try {
                }catch (e:Exception){
                    Toast.makeText(this, "Un error inesperado ha ocurrido", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {

            }) {
        }
        queue.add(jsonObjectRequest)
    }


    private fun registerConsultationPatient() {

        val queue=Volley.newRequestQueue(this)
        val userDetails=makeHashMapConsultationPatient().toMap()
        val jsonObjectRequest=object : JsonObjectRequest(
            Method.POST,
            "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/patients/$username/consultations/$idvalue",
            JSONObject(userDetails),
            Response.Listener {
                try {
                    mainActivityIntent()
                    finishAffinity()
                }catch (e:Exception){
                    Toast.makeText(this, "Un error inesperado ha ocurrido", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                btn_automatic_consultation.setPressed(true)


            }) {
        }
        queue.add(jsonObjectRequest)
    }



    private fun makeHashMapConsult():Map<String,String> {
        val userDetails=HashMap<String,String>()
        userDetails["id"] = idvalue
        userDetails["shape"] = shapetypevalue
        userDetails["numberOfInjuries"] = numberofinjuriesvalue
        userDetails["distribution"] = distributionvalue
        userDetails["comment"] = "Diagn??stico Generado autom??tica"
        userDetails["image"] = "https://firebasestorage.googleapis.com/v0/b/dermoappfront.appspot.com/o/images_consultations%2FheaderLogo.png?alt=media&token=07204193-f80a-4d57-be8d-e6d1234205dd"
        userDetails["creationDate"] = creationdate
        userDetails["typeOfInjury"] = typeofinjuryvalue
        userDetails["specialty"] = speciality
        userDetails["diagnosis"] = "diagnosis"
        userDetails["asigned"] = "false"
        userDetails["acceptDiagnosis"] = "false"

        return userDetails
    }

    private fun makeHashMapConsultAutomatic():Map<String,String> {
        val userDetails=HashMap<String,String>()
        userDetails["question"] = "Tengo una lesi??n en la piel en forma de $typeofinjuryvalue, y con n??mero de lesiones $numberofinjuriesvalue, en forma de $shapetypevalue y con distribuci??n $distributionvalue. Me podr??as indicar un diagn??stico para mi lesi??n de la piel\n\n\n"
        userDetails["consultationId"] = idvalue

        return userDetails
    }

    private fun makeHashMapConsultationPatient():Map<String,String> {
        val userDetails=HashMap<String,String>()

        return userDetails
    }


}



