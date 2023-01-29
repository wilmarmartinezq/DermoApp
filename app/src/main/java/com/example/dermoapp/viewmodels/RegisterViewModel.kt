package com.example.dermoapp.viewmodels

import com.example.dermoapp.api.OperationalCallBack
import com.example.dermoapp.api.VolleyHandler
import com.example.dermoapp.models.Patient

interface RegisterView {
    fun setResult(message: String)
    fun onLoad(isLoading: Boolean)
}

interface RegisterPresenter {
    fun registerUser(user: Patient): String
}

class RegisterViewModel (
    private val volleyHandler: VolleyHandler,
    private val registrationView: RegisterView
) : RegisterPresenter {

    override fun registerUser(user: Patient): String {
        registrationView.onLoad(true)
        val message = volleyHandler.userRegistration(user, object : OperationalCallBack {
            override fun onSuccess(message: String) {
                registrationView.onLoad(false)
                registrationView.setResult(message)
            }

            override fun onFailure(message: String) {
                registrationView.onLoad(false)
                registrationView.setResult(message)
            }
        })
        return message ?: DEFAULT_MESSAGE
    }

    companion object {
        const val DEFAULT_MESSAGE = "default message"
    }
}