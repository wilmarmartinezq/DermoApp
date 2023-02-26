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

        if(typeofinjuryvalue=="Mácula") {
            speciality = "pediatrics"
        }

        if(typeofinjuryvalue=="Pápula") {
            speciality = "elderly"
        }

        if(typeofinjuryvalue=="Parche") {
            speciality = "cosmetic"
        }

        if(typeofinjuryvalue=="Placa") {
            speciality = "oncology"
        }

        if(typeofinjuryvalue=="Nódulo") {
            speciality = "oncology"
        }

        if(typeofinjuryvalue=="Ampolla") {
            speciality = "cosmetic"
        }

        if(typeofinjuryvalue=="Úlcera") {
            speciality = "oncology"
        }

        if(typeofinjuryvalue=="Vesícula") {
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
                            Toast.makeText(this, "Todavía desconectado :(", Toast.LENGTH_SHORT).show()
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
                            Toast.makeText(this, "Verificar la conexión a internet", Toast.LENGTH_SHORT).show()
                            mProgressDialog.dismiss()
                        }) {
                    }
                    queue.add(jsonObjectRequest)


                    val queuepatient= Volley.newRequestQueue(this)
                    val consultationPatientDetails=makeHashMapConsultationPatient().toMap()
                    val jsonObjectRequestPatient=object : JsonObjectRequest(
                        Method.POST,
                        "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/patients/$username/consultations/$idvalue",

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
                        }) {
                    }
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
        Toast.makeText(this, "Proceso en desarrollo", Toast.LENGTH_SHORT).show()

    }



}



