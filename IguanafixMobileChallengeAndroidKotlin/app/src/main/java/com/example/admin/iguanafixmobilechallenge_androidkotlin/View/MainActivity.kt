package com.example.admin.iguanafixmobilechallenge_androidkotlin.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.admin.iguanafixmobilechallenge_androidkotlin.Model.Contact
import com.example.admin.iguanafixmobilechallenge_androidkotlin.R

class MainActivity : AppCompatActivity(), ContactListFragment.OnClickContactCellNotifier {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.mainActivityContainer, ContactListFragment(), "tag")
        fragmentTransaction.commit()
    }

    override fun openClickedContact(contact: Contact) {
        val bundle = Bundle()
        bundle.putSerializable(ContactDetailFragment.CONTACT_ID, contact)
        val contactDetailFragment = ContactDetailFragment()
        contactDetailFragment.setArguments(bundle)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainActivityContainer, contactDetailFragment, "tag")
        fragmentTransaction.addToBackStack(null).commit()
    }
}