package com.example.dermoapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AutomaticConsultationActivity  : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_automatic_consultation)


    }

    fun detailBackIntent(view: View) {
        onBackPressed()
    }
}