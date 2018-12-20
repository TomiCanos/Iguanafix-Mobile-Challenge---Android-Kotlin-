package com.example.admin.iguanafixmobilechallenge_androidkotlin.Retrofit

interface ResultListener<T> {
    fun finish(resultado: T)
}