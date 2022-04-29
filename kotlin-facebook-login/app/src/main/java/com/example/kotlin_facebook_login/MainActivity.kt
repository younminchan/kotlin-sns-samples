package com.example.kotlin_facebook_login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlin_facebook_login.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.util.*

/** MainActivity.kt*/
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //Facebook
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginManager: LoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Facebook-Login
        callbackManager = CallbackManager.Factory.create() //로그인 응답 처리할 CallbackManager
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
                    }
                    override fun onError(exception: FacebookException) {
                        binding.tvFbResult.text = "onError: ${exception.printStackTrace()}"
                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}