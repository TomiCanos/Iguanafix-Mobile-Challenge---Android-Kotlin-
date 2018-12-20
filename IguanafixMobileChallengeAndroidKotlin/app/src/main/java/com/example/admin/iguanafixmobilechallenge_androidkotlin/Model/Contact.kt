package com.example.admin.iguanafixmobilechallenge_androidkotlin.Model

import java.io.Serializable

class Contact(
    val user_id: String, val birth_date: String, val first_name: String, val last_name: String,
    val phones: List<Phone>, val thumb: String, val photo: String
) : Serializable, Comparable<Contact> {

    override fun compareTo(other: Contact): Int {
        var result: Int = 0
        if (other.equals(this)) {
            return result
        } else {
            val charArray1: CharArray = (first_name + last_name).toLowerCase().toCharArray()
            val charArray2: CharArray = (other.first_name + other.last_name).toLowerCase().toCharArray()
            var i: Int = 0
            var contact1: Int = 0
            var contact2: Int = 0

            for (char: Char in charArray1) {
                if (i < (charArray2.size - 1)) {
                    if (char > charArray2[i]) {
                        contact2++
                    } else {
                        contact1++
                    }
                    i++
                }
            }

            if (contact1 < contact2) {
                result = 1
            } else if (contact1 > contact2) {
                result = -1
            }
        }
        return result
    }

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
