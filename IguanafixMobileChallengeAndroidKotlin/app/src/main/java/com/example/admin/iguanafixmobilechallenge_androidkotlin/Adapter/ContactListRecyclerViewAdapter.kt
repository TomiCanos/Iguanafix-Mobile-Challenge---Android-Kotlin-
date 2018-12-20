package com.example.admin.iguanafixmobilechallenge_androidkotlin.Adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.admin.iguanafixmobilechallenge_androidkotlin.Model.Contact
import com.example.admin.iguanafixmobilechallenge_androidkotlin.Model.RecyclerViewFastScroller
import com.example.admin.iguanafixmobilechallenge_androidkotlin.R
import java.util.ArrayList

class ContactListRecyclerViewAdapter(private val onClickContactCellNotifier: OnClickContactCellNotifier) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), RecyclerViewFastScroller.BubbleTextGetter {


    private var contacts: List<Contact>? = null

    init {
        contacts = ArrayList<Contact>()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val view = inflater.inflate(R.layout.contact_list_cell, viewGroup, false)
        return ViewHolder(view)
    }

    //elije a cual contanto de la vista inflar
    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val contact = contacts!![p1]
        val viewHolder: ViewHolder = p0 as ViewHolder
        viewHolder.bindContact(contact)    }

    override fun getItemCount(): Int {
        return contacts!!.size
    }

    fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    //le da el texto para mostrar a la burbujita del fastscroller
    override fun getTextToShowInBubble(pos: Int): String {

        val result = contacts!![pos].first_name.first()
        return Character.toString(result).toUpperCase()
    }

    //une los datos de la vista con los del contacto recibido
    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contactName: TextView
        private val contactPhoto: ImageView

        init {
            contactName = itemView.findViewById(R.id.contactCellNameTextView)
            contactPhoto = itemView.findViewById(R.id.contactCellPhotoImageView)

            itemView.setOnClickListener { onClickContactCellNotifier.openClickedContact(contacts!![adapterPosition]) }
        }

        fun bindContact(contact: Contact) {
            contactName.setText(contact.first_name + " " + contact.last_name)
            val thumbDrawable = Glide.with(itemView).load(contact.thumb)

            //Hay un contacto que no tiene foto y como thumb trae un archivo svg en vez de un png
            if (!contact.thumb.contains(".png")) {
                Glide.with(itemView).load(R.drawable.wonder_woman_thumbnail).into(contactPhoto)
            } else {
                Glide.with(itemView).load(contact.photo).thumbnail(thumbDrawable)
                    .into(contactPhoto)
            }
        }
    }

    // funcion para abrir el detalle del contacto
    interface OnClickContactCellNotifier {
        fun openClickedContact(contact: Contact)
    }
}
