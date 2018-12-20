package com.example.admin.iguanafixmobilechallenge_androidkotlin.View

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.admin.iguanafixmobilechallenge_androidkotlin.Adapter.ContactListRecyclerViewAdapter
import com.example.admin.iguanafixmobilechallenge_androidkotlin.Model.Contact
import com.example.admin.iguanafixmobilechallenge_androidkotlin.Model.RecyclerViewFastScroller
import com.example.admin.iguanafixmobilechallenge_androidkotlin.R
import com.example.admin.iguanafixmobilechallenge_androidkotlin.ViewModel.ContactListViewModel

class ContactListFragment : Fragment() {

    private var searchContactByNameEditText: EditText? = null
    private var contactsRecyclerView: RecyclerView? = null
    private var contactsRecyclerViewFastScroller: RecyclerViewFastScroller? = null
    private var mViewModel: ContactListViewModel? = null
    private var onClickContactCellNotifier: OnClickContactCellNotifier? = null
    private var inflatedView: View? = null
    private var search: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (inflatedView != null) {
            return inflatedView
        }
        val view1 = inflater.inflate(R.layout.contact_list_fragment, container, false)
        mViewModel = ViewModelProviders.of(this).get(ContactListViewModel::class.java!!)

        searchContactByNameEditText = view1.findViewById(R.id.contactListNameEditText)
        contactsRecyclerView = view1.findViewById(R.id.contactListRecyclerView)
        contactsRecyclerViewFastScroller = view1.findViewById(R.id.contactListRecyclerViewFastScroller)
        search = ""

        mViewModel!!.getContactsfromAPIandNotifyAdapter()

        mViewModel!!.inicializeAdapterAndSetOnClickContactSell(
            ContactListRecyclerViewAdapter(
                object : ContactListRecyclerViewAdapter.OnClickContactCellNotifier {
                    override fun openClickedContact(contact: Contact) {
                        onClickContactCellNotifier!!.openClickedContact(contact)
                    }
                })
        )


        contactsRecyclerViewFastScroller!!.setRecyclerView(contactsRecyclerView!!)
        contactsRecyclerViewFastScroller!!.setViewsToUse(
            R.layout.recycler_view_fast_scroller__fast_scroller,
            R.id.fastscroller_bubble,
            R.id.fastscroller_handle
        )
        mViewModel!!.setRecyclerView(contactsRecyclerView!!, activity!!)

        searchContactByNameEditText!!.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    mViewModel!!.getContactsfromAPIandNotifyAdapter()
                    contactsRecyclerViewFastScroller!!.setVisibility(View.VISIBLE)
                } else {
                    contactsRecyclerViewFastScroller!!.setVisibility(View.INVISIBLE)
                    search = s.toString()
                }
                mViewModel!!.setsortedListToAdapter(mViewModel!!.searchContactbyName(search!!))
            }

            override fun afterTextChanged(s: Editable) {}
        })

        return view1
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onClickContactCellNotifier = activity as OnClickContactCellNotifier?
    }

    //funcion para abrir el detalle del contacto
    interface OnClickContactCellNotifier {
        fun openClickedContact(contact: Contact)
    }

    // esto es para que cuando minimicen la app no vuelva a cargar los datos de la vista
    override fun onPause() {
        inflatedView = getView()
        super.onPause()
    }

}
