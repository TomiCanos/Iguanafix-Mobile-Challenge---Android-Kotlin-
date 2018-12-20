package com.example.admin.iguanafixmobilechallenge_androidkotlin.Retrofit

import com.example.admin.iguanafixmobilechallenge_androidkotlin.Model.Contact
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ContactService {

    @get:GET("/contacts")
    val contacts: Call<List<Contact>>

    @GET("/contacts/{contactId}")
    fun getContact(@Path("contactId") contactId: String): Call<Contact>
}
