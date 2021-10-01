package com.provider.unobridge.network.core

import android.content.Intent
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.provider.unobridge.base.MainApplication
import com.provider.unobridge.base.Prefs
import com.provider.unobridge.data.LoginParams
import com.provider.unobridge.data.SubscriptionParams
import com.provider.unobridge.data.responeClasses.BaseResponse
import com.provider.unobridge.ui.activities.AuthHandlerActivity
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


interface APIService {

    @POST("api/user/login")
    suspend fun login(@Body body: LoginParams): BaseResponse

    @POST("api/user/logout")
    suspend fun logout(): BaseResponse

    @POST("api/subscription/subscribe")
    suspend fun subscribe(@Body body: SubscriptionParams): BaseResponse

    @GET("api/user/getProfile")
    suspend fun getProfile(): BaseResponse

    @GET("api/state/listStates")
    suspend fun getStatesList(): BaseResponse

    @GET("api/state/getPlanList")
    suspend fun getSubscriptionPlans(): BaseResponse


    @Multipart
    @POST("api/user/register")
    suspend fun addProfile(
        @PartMap request: HashMap<String, RequestBody>,
        @Part profileImage: MultipartBody.Part?,
        @Part aadharFrontImage: MultipartBody.Part?,
        @Part aadharBackImage: MultipartBody.Part?,
        @Part licenseFrontImage: MultipartBody.Part?,
        @Part licenseBackImage: MultipartBody.Part?,
    ): BaseResponse

    @Multipart
    @POST("api/user/update-profile")
    suspend fun updateProfile(
        @PartMap request: HashMap<String, RequestBody>,
        @PartMap children_ids: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part profile_pictures: Array<MultipartBody.Part>?,
        @Part voice: MultipartBody.Part?,
    ): BaseResponse


    companion object {


        //        const val BASE_URL = "http://192.168.3.174:7001/" //Local
        const val BASE_URL = "http://18.221.60.82:3002/" //Staging Server

        //        const val BASE_URL = "http://1.6.98.142:4009/" //Live
        private fun getAPIService(): APIService {
            val client =
                OkHttpClient.Builder().addInterceptor(provideHeaderInterceptor()!!)
                    .addInterceptor(ForbiddenInterceptor())
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(provideHttpLoggingInterceptor()!!)
                    .addInterceptor(ExceptionInterceptor()).build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(APIService::class.java)
        }


        fun getErrorMessageFromGenericResponse(
            exception: Exception,
        ): String? {
            var error = ""
            try {
                when (exception) {
                    is HttpException -> {
                        val body = exception.response()?.errorBody()
                        val adapter = Gson().getAdapter(BaseResponse::class.java)
                        val errorParser = adapter.fromJson(body?.string())
                        val msg = errorParser.message ?: exception.message()
                        if (msg == "Unauthenticated.") {
                            error = "Your account has been logged into another device."
                        } else {
                            error = msg
                        }
                    }
                    is ConnectException -> {
                        error =
                            "Your internet connection appears to be offline. Please try again."
                    }
                    is SocketTimeoutException -> {
                        error = "Something went wrong. Please try again later."
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                return error
            }
        }

        private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor? {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.d("API Logging", "response => $message")
                    }
                })
//            httpLoggingInterceptor.setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            return httpLoggingInterceptor
        }

        private fun provideHeaderInterceptor(): Interceptor? {
            return object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    //*Code for request with auth token
                    val accessToken: String = Prefs.init().accessToken
                    return if (!TextUtils.isEmpty(accessToken)) {
                        val request: Request = chain.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Authorization", "Bearer $accessToken").build()
                        chain.proceed(request)
                    } else {
                        val request: Request = chain.request().newBuilder()
                            .addHeader("Accept", "application/json").build()
                        chain.proceed(request)
                    }
                }
            }
        }


        class ForbiddenInterceptor : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request = chain.request()
                return chain.proceed(request)
            }
        }

        class ExceptionInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val response = chain.proceed(request)

                if (response.code == 500) {
                    return response
                } else if (response.code == 402) {
                    return response
                } else if (response.code == 401) {
                    /* Prefs.init().isLogIn = "false"
                     Prefs.init().isVisitPreferences = "false"
                     Prefs.init().isEditProfile = "false"
                     Prefs.init().shakeId = ""*/
                    Prefs.init().clear()

                    android.os.Handler(Looper.getMainLooper()).post {
                        val toast = Toast.makeText(
                            MainApplication.get().applicationContext,
                            "Your account has been logged into another device.",
                            Toast.LENGTH_SHORT
                        )
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show()
                    }
                    MainApplication.get().applicationContext.startActivity(
                        Intent(
                            MainApplication.get().applicationContext,
                            AuthHandlerActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    return response
                }

                return response
            }

        }

        operator fun invoke() = getAPIService()
    }
}