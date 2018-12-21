package com.example.admin.iguanafixmobilechallenge_androidkotlin.Model

import java.io.Serializable

class Contact(
    val user_id: String, val birth_date: String, val first_name: String, val last_name: String,
    val phones: List<Phone>, val thumb: String, val photo: String
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Contact

        if (user_id != other.user_id) return false
        if (birth_date != other.birth_date) return false
        if (first_name != other.first_name) return false
        if (last_name != other.last_name) return false
        if (phones != other.phones) return false
        if (thumb != other.thumb) return false
        if (photo != other.photo) return false

        return true
    }

}
