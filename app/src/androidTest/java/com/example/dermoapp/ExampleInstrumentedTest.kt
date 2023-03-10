package com.example.dermoapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_main.*


import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.regex.Pattern.matches
import androidx.test.espresso.action.ViewActions.typeText

import android.R
import android.widget.Button
import androidx.test.espresso.Espresso

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import java.util.EnumSet.allOf
import org.junit.After

import android.widget.EditText
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation

import org.junit.Before

import androidx.test.rule.ActivityTestRule

import org.junit.Rule
import java.lang.Exception


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.dermoapp", appContext.packageName)
    }

}
/*
class LoginActivityTest {
    @Rule
    var mActivityTestRule = ActivityTestRule(
        LoginActivity::class.java
    )
    var loginActivity: LoginActivity? = null
    @Before
    @Throws(Exception::class)
    fun setUp() {
        loginActivity = mActivityTestRule.activity
    }

    @Test
    fun testLogin() {
        getInstrumentation().runOnMainSync(Runnable {

            val email = loginActivity!!.findViewById<EditText>(R.id.edit)
            val pass = loginActivity!!.findViewById<EditText>(R.id.password)
            email.setText("email")
            pass.setText("pass")
            val loginBtn: Button = loginActivity!!.findViewById(R.id.login)
            loginBtn.performClick()
            assertTrue(loginActivity.isCurUserLoggedIn())
        })
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        loginActivity = null
    }
}

 */