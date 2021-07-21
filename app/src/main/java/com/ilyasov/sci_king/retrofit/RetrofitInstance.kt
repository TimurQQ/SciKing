package com.ilyasov.sci_king.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy<Retrofit> {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl("http://export.arxiv.org/api/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build()
        }

        val api: SciArticlesAPI by lazy {
            retrofit.create(SciArticlesAPI::class.java)
        }
    }
}