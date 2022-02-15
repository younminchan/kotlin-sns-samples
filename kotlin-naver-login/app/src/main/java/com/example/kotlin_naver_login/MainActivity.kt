package com.example.kotlin_naver_login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.kotlin_naver_login.databinding.ActivityMainBinding
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //네이버 로그인
    var mNaverLoginModule : OAuthLogin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /** Naver Login Module Initialize */
        mNaverLoginModule = OAuthLogin.getInstance()
        val naverClientId = getString(R.string.social_login_info_naver_client_id)
        val naverClientSecret = getString(R.string.social_login_info_naver_client_secret)
        val naverClientName = getString(R.string.social_login_info_naver_client_name)
        mNaverLoginModule?.init(this, naverClientId, naverClientSecret, naverClientName)

        binding.tvNaverLogin.setOnClickListener {
            startNaverLogin()
        }
    }
    private fun startNaverLogin(){
        // 2. userId 조회(access)
        fun getNaverUserInfo(tokenType : String?, accessToken: String?){
            val urlStr = "https://openapi.naver.com/v1/nid/me"
            val authorization = "$tokenType $accessToken"

            AndroidNetworking.get(urlStr)
                .addHeaders("Authorization", authorization)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject?) {
                        val res = response?.get("response") as JSONObject?
                        val userId = res?.get("id") as String?
                        binding.tvResult.text = "userId: ${userId} \n\n accessToken: ${accessToken}"
                    }

                    override fun onError(anError: ANError?) {

                    }
                })
        }

//        // 1. 네이버 로그인 요청
        mNaverLoginModule?.startOauthLoginActivity(this, object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                if (success) {
                    val accessToken: String? = mNaverLoginModule?.getAccessToken(this@MainActivity)
                    //val refreshToken: String? = mNaverOAuthLoginModule?.getRefreshToken(getActivity())
                    val tokenType = mNaverLoginModule?.getTokenType(this@MainActivity)

                    getNaverUserInfo(tokenType, accessToken)

                } else {
                    val errorDesc = mNaverLoginModule?.getLastErrorDesc(this@MainActivity)
                }
            }
        })
    }
}