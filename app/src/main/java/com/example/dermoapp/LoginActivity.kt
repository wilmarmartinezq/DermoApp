package com.example.dermoapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.dermoapp.utils.ConnectionManager
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private val spFileName="dermoappFile"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var username:String
    private lateinit var password:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences=getSharedPreferences(spFileName, Context.MODE_PRIVATE)

        username=""
        password=""

        if(sharedPreferences.getBoolean("isLoggedIn",false))
            mainActivityIntent()

        setContentView(R.layout.activity_login)

        btnSignIn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        username=editTextEmail.text.toString()
        password=editTextPassword.text.toString()
        if(username.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Ingresar su email y password", Toast.LENGTH_SHORT).show()
            editTextEmail.setText("")
            editTextPassword.setText("")
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
                            login()
                        } else {
                            Toast.makeText(this, "Todavía desconectado :(", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("Open Settings") { _, _ ->
                        startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }.show()
            }else{
                login()
            }
        }
    }

    private fun login() {
        val queue= Volley.newRequestQueue(this)
        val userDetails= JSONObject()
        userDetails.put("username", username)
        userDetails.put("password",password)
        val jsonObjectRequest=object : JsonObjectRequest(
            Method.POST,
            "https://dermoapp-backend-nest-z4o5lll72a-uw.a.run.app/Api/V1/auth/login",
            userDetails,
            Response.Listener {
                try {
                        setSharedPreferences(username)
                        Toast.makeText(this,"Ingresando...",Toast.LENGTH_SHORT).show()
                        mainActivityIntent()
                        setLoggedIn()
                        finishAffinity()
                }catch (e:Exception){
                    Toast.makeText(this, "Un error inesperado ha ocurrido", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener {
                Toast.makeText(this, "Favor verificar su email y contraseña", Toast.LENGTH_SHORT).show()
                Log.e("dermoapp",it.message.toString())
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
//                headers["token"] = getString(R.string.token)
                return headers


            }
        }
        queue.add(jsonObjectRequest)

    }


    private fun setLoggedIn() =sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()

    private fun mainActivityIntent() {
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
    }

    fun signUpIntent(view: View) =startActivity(Intent(this, RegisterActivity::class.java))


    private fun setSharedPreferences(
        username: String,
    ) = sharedPreferences.edit()
        .putString("username",username)
        .apply()

}
