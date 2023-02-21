package com.example.dermoapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.dermoapp.utils.ConnectionManager
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.lang.Exception


class RegisterActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private val spFileName="dermoappFile"
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var name:String
    private lateinit var birthday:String
    private lateinit var country:String
    private lateinit var skintypevalue:String
    private lateinit var profilepicture:String
    private lateinit var username:String
    private lateinit var role:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sharedPreferences=getSharedPreferences(spFileName, Context.MODE_PRIVATE)


        val skintype = resources.getStringArray(R.array.skinType_array)

        val spinner = findViewById<Spinner>(R.id.skinType)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, skintype)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    skintypevalue = parent.getItemAtPosition(position).toString()

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


    }

    fun signUpBackIntent() {
        onBackPressed()
    }

    fun singUpConfirm(view: View) {
        username=editTextEmail.text.toString()
        name=editTextName.text.toString()
        email=editTextEmail.text.toString()
        password=editTextPassword.text.toString()
        birthday=editTextBirthDay.text.toString()
        country=editTextNacionality.text.toString()
        role="patient"
        profilepicture= "https://www.si.edu/sites/default/files/newsdesk/fact_sheets/nhb2015-02891.jpg"


        if(email.isBlank() || password.isBlank() || name.isBlank() || birthday.isBlank() || country.isBlank() || skintypevalue.isBlank()) {
            Toast.makeText(this, "Todos los campos deben ser diligenciados", Toast.LENGTH_SHORT).show()
        }

        else if (!isEmailValid(email)){
            Toast.makeText(this,"Email invalido",Toast.LENGTH_SHORT).show()
        }else if (password.length<8){
            Toast.makeText(this,"Password debe contener mínimo 8 caracteres o números",Toast.LENGTH_SHORT).show()
        }else{
            if(!ConnectionManager().isNetworkAccessActive(this)){
                AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setIcon(R.drawable.no_connection)
                    .setTitle("Sin Internet")
                    .setMessage("Favor revisar el acceso a internet")
                    .setPositiveButton("Retry") { dialog, _ ->
                        if (ConnectionManager().isNetworkAccessActive(this)) {
                            dialog.dismiss()
                            registerPatients()
                            registerUsername()
                        } else {
                            Toast.makeText(this, "Todavía desconectado :(", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Open Settings") { _, _ ->
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }.show()
            }else{
                registerPatients()
                registerUsername()
            }
        }
    }


    private fun registerPatients() {
        val queue=Volley.newRequestQueue(this)
        val userDetails=makeHashMapPatients().toMap()
        val jsonObjectRequest=object : JsonObjectRequest(
            Method.POST,
            "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/patients",
            JSONObject(userDetails),
            Response.Listener {
                try {
                    Toast.makeText(this,"Registrado paciente ...",Toast.LENGTH_SHORT).show()

                }catch (e:Exception){
                    Toast.makeText(this, "Un error inesperado ha ocurrido", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
            }) {
        }
        queue.add(jsonObjectRequest)
    }




    private fun registerUsername() {
        val queue=Volley.newRequestQueue(this)
        val userDetails=makeHashMapUsername().toMap()
        val jsonObjectRequest=object : JsonObjectRequest(
            Method.POST,
            "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/users/signup",
            JSONObject(userDetails),
            Response.Listener {
                try {
                    setSharedPreferences(username)
                    Toast.makeText(this,"Ingresando ...",Toast.LENGTH_SHORT).show()
                    mainActivityIntent()
                    setLoggedIn()
                    finishAffinity()
                }catch (e:Exception){
                    Toast.makeText(this, "Un error inesperado ha ocurrido", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(this, "El email ya se encuentra registrado", Toast.LENGTH_SHORT).show()

            }) {
        }
        queue.add(jsonObjectRequest)
    }



    private fun makeHashMapPatients():Map<String,String> {
        val userDetails=HashMap<String,String>()
        userDetails["name"] = name
        userDetails["email"] = email
        userDetails["password"] = password
        userDetails["birthDate"] = birthday
        userDetails["country"] = country
        userDetails["skinType"] = skintypevalue
        userDetails["profilePicture"] = profilepicture

        return userDetails
    }


    private fun makeHashMapUsername():Map<String,String> {
        val userDetails=HashMap<String,String>()
        userDetails["email"] = email
        userDetails["password"] = password
        userDetails["roles"] = role

        return userDetails
    }



    private fun setSharedPreferences(
        username: String,
    ) = sharedPreferences.edit()
        .putString("username",username)
        .apply()


    private fun isEmailValid(email: CharSequence) =Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun setLoggedIn() =sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()

    private fun mainActivityIntent() = startActivity(Intent(this, MainActivity::class.java))}
