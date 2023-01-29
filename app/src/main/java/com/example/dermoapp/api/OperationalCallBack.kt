package com.example.dermoapp.api

interface OperationalCallBack {
    fun onSuccess(message: String)
    fun onFailure(message: String)
}