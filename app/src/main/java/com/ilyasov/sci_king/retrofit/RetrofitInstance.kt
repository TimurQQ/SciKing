package com.ilyasov.sci_king.retrofit

import com.ilyasov.sci_king.util.Constants.Companion.BASE_URL
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RetrofitInstance {
    companion object {
        private val retrofit by lazy<Retrofit> {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(TikXmlConverterFactory.create())
                .client(client)
                .build()
        }
        val api: SciArticlesAPI by lazy { retrofit.create(SciArticlesAPI::class.java) }
    }
}