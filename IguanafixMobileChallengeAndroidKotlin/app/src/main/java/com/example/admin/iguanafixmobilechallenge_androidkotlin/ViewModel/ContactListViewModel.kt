package com.example.admin.iguanafixmobilechallenge_androidkotlin.ViewModel

import android.arch.lifecycle.ViewModel
import android.os.Build
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.admin.iguanafixmobilechallenge_androidkotlin.Adapter.ContactListRecyclerViewAdapter
import com.example.admin.iguanafixmobilechallenge_androidkotlin.Model.Contact
import com.example.admin.iguanafixmobilechallenge_androidkotlin.Retrofit.ContactDAO
import com.example.admin.iguanafixmobilechallenge_androidkotlin.Retrofit.ResultListener
import java.nio.channels.Selector
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class ContactListViewModel : ViewModel() {

    private var adapter: ContactListRecyclerViewAdapter? = null
    private var contacts: List<Contact>? = null

    init {
        contacts = ArrayList()
    }

    fun setRecyclerView(contactsRecyclerView: RecyclerView, fragmentActivity: FragmentActivity) {
        contactsRecyclerView.layoutManager = LinearLayoutManager(fragmentActivity, LinearLayoutManager.VERTICAL, false)
        contactsRecyclerView.setAdapter(adapter)
    }

    //cuando traigo los contactos le notifico que cambio el set de datos al adapter para que los muestre
    fun getContactsfromAPIandNotifyAdapter() {
        val contactDAO = ContactDAO()

        contactDAO.getContactsAsync(object : ResultListener<List<Contact>> {
            override fun finish(result: List<Contact>) {
                contacts = result
                setsortedListToAdapter(contacts!!)
            }
        })
    }

    fun inicializeAdapterAndSetOnClickContactSell(adapter: ContactListRecyclerViewAdapter) {
        this.adapter = adapter
    }

    fun searchContactbyName(contactName: String): List<Contact> {
        val results = ArrayList<Contact>()

        for (contact in contacts!!) {
            val fullNameToLowerCase = contact.first_name.toLowerCase() + contact.last_name.toLowerCase()

            //paso t0d0 a lower case para que no haya problema con mayusculas y minusculas
            if (fullNameToLowerCase.contains(contactName.toLowerCase())) {
                results.add(contact)
            }
        }

        return results
    }


    fun setsortedListToAdapter(contacts: List<Contact>) {
         this.contacts = contacts.sortedWith(compareBy({it.first_name + it.last_name}, {it.first_name + it.last_name}))
        adapter!!.setContacts(this.contacts!!)
    }
}