package com.example.kotlin_facebook_login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlin_facebook_login.databinding.ActivityMainBinding
import com.facebook.appevents.AppEventsLogger

import com.facebook.login.LoginResult

import android.R
import android.view.View
import com.facebook.login.LoginManager

import com.facebook.login.widget.LoginButton
import java.util.*
import android.content.Intent
import android.util.Log
import com.facebook.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //Facebook
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginManager: LoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Facebook
        callbackManager = CallbackManager.Factory.create()
        loginManager = LoginManager.getInstance()

        binding.tvFbLogin.setOnClickListener {
            loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))

            loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) {
                        // App code
                        val graphRequest = GraphRequest.newMeRequest(loginResult?.accessToken) { f_object, response ->
                            // {token: loginResult.accessToken / userObject: f_object}
                            binding.tvFbResult.text = "onSuccess: token: ${loginResult?.accessToken} \n\n userObject: ${f_object}}"
                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "id,name,email,gender,birthday")
                        graphRequest.parameters = parameters
                        graphRequest.executeAsync()
                    }

                    override fun onCancel() {
                        // App code
                    }

                    override fun onError(exception: FacebookException) {
                        // App code
                        binding.tvFbResult.text = "onError: ${exception.printStackTrace()}"
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}