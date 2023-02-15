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
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
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
    private lateinit var shape:String
    private lateinit var numberOfInjuries:String
    private lateinit var distribution:String
    private lateinit var comment:String




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


    }

    fun createConsultationBackIntent(view: View) {
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
        shape=editTextForma.text.toString()
        numberOfInjuries=editTextNumeroLesiones.text.toString()
        distribution=editTextDistribucion.text.toString()
        comment=editTextComentario.text.toString()



        if(shape.isBlank() || numberOfInjuries.isBlank() || distribution.isBlank() || comment.isBlank() ) {
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
                        consultationDetails["shape"] = shape
                        consultationDetails["numberOfInjuries"] = numberOfInjuries
                        consultationDetails["distribution"] = distribution
                        consultationDetails["comment"] = comment
                        consultationDetails["image"] = imageurl



                        return consultationDetails

                    }
                    val queue= Volley.newRequestQueue(this)
                    val consultationDetails=makeHashMapConsultation().toMap()
                    val jsonObjectRequest=object : JsonObjectRequest(
                        Method.POST,
                        "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/consultations",
                        JSONObject(consultationDetails),
                        Response.Listener {
                            try {
                                mainActivityIntent()
                                mProgressDialog.dismiss()
                            }catch (e: Exception){
                                mProgressDialog.dismiss()
                                Toast.makeText(this, "Un error inesperado ha ocurrido", Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener {
                            mProgressDialog.dismiss()
                        }) {
                    }
                    queue.add(jsonObjectRequest)
            }
        }

            }
        else{
            Toast.makeText(this, "Debes seleccionar una imagen", Toast.LENGTH_SHORT).show()
        }

    }



    private fun mainActivityIntent() = startActivity(Intent(this, MainActivity::class.java))}



