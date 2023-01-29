package com.example.dermoapp.api

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.dermoapp.models.Patient
import org.json.JSONObject

class VolleyHandler(context: Context) {
    private var requestQueue: RequestQueue = Volley.newRequestQueue(context)

    fun userRegistration(patient: Patient, callBack: OperationalCallBack): String? {
        val url = "${Constants.BASE_URL}${Constants.REGISTER_END_POINT}"
        val data = JSONObject()
        var message: String? = null

        data.put("full_name", patient.name)
        data.put("email_id", patient.email)
        data.put("password", patient.password)
        data.put("birthDate", patient.birthDate)
        data.put("country", patient.country)
        data.put("skinType", patient.skinType)
        data.put("profilePicture", patient.profilePicture)


        val request = JsonObjectRequest(Request.Method.POST, url, data, { response: JSONObject ->
            message = response.getString("message")
            Log.i(TAG, message.toString())
            callBack.onSuccess(message.toString())
        }, { error: VolleyError ->
            error.printStackTrace()
            message = error.message.toString()
            Log.i(TAG, error.toString())
            callBack.onFailure(message.toString())
        })
        requestQueue.add(request)
        return message
    }

    companion object {
        const val TAG = "TAG"
    }
}