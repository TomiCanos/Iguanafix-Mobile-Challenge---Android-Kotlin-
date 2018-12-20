package com.example.admin.iguanafixmobilechallenge_androidkotlin.Retrofit

import com.example.admin.iguanafixmobilechallenge_androidkotlin.Model.Contact
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

class ContactDAO {
    private val retrofit: Retrofit

    init {
        val httpClient = OkHttpClient.Builder()

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://private-d0cc1-iguanafixtest.apiary-mock.com/")
            .addConverterFactory(GsonConverterFactory.create())

        retrofit = retrofitBuilder.client(httpClient.build()).build()
    }

    //hago el pedido asincronico a la api, si falla paso una lista vacia para que no rompa
    fun getContactsAsync(vmlistener: ResultListener<List<Contact>>) {
        val contactService = retrofit.create(ContactService::class.java)
        val call = contactService.contacts
        call.enqueue(object : Callback<List<Contact>> {
            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                vmlistener.finish(response.body()!!)
            }

            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                vmlistener.finish(ArrayList<Contact>())
            }
        })
    }
}
