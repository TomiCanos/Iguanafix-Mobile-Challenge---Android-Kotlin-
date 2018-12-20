package com.example.admin.iguanafixmobilechallenge_androidkotlin.View

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.admin.iguanafixmobilechallenge_androidkotlin.Model.Contact
import com.example.admin.iguanafixmobilechallenge_androidkotlin.R

class ContactDetailFragment : Fragment() {

    private var contact: Contact? = null
    private var photo: ImageView? = null
    private var name: TextView? = null
    private var birthDate: TextView? = null
    private var homeNumber: TextView? = null
    private var cellphoneNumber: TextView? = null
    private var officeNumber: TextView? = null
    private var backgroundImage: ImageView? = null
    private var phonesCardView: CardView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.contact_detail_fragment, container, false)

        //agarro el contacto que paso por bundle y lo utilizo para la vista
        val bundle = arguments

        contact = bundle!!.getSerializable(CONTACT_ID) as Contact
        photo = view.findViewById(R.id.contactDetailPhotoImageView)
        name = view.findViewById(R.id.ContactDetailNameTextView)
        birthDate = view.findViewById(R.id.contactDetailBirthDateTextView)
        homeNumber = view.findViewById(R.id.contactDetailHomeNumberTextView)
        cellphoneNumber = view.findViewById(R.id.contactDetailCellphoneNumberTextView)
        officeNumber = view.findViewById(R.id.contactDetailofficeNumberTextView)
        backgroundImage = view.findViewById(R.id.contactDetailBackgroundImageView)
        phonesCardView = view.findViewById(R.id.contactDetailPhonesCardView)

        //Hay un contacto que no tiene foto y como thumb trae un archivo svg en vez de un png
        if (!contact!!.thumb.contains(".png")) {
            Glide.with(view).load(R.drawable.wonder_woman_thumbnail).into(photo!!)
        } else {
            val thumbDrawable = Glide.with(view).load(contact!!.thumb)
            Glide.with(view).load(contact!!.photo).thumbnail(thumbDrawable).into(photo!!)
        }
        name!!.setText(contact!!.first_name + " " + contact!!.last_name)
        birthDate!!.setText(contact!!.birth_date)
        Glide.with(view).load(R.drawable.contactdetailbackground2).into(backgroundImage!!)

        for (phone in contact!!.phones) {
            if (phone.number != null) {
                when (phone.type) {
                    "Home" -> {
                        homeNumber!!.visibility = View.VISIBLE
                        homeNumber!!.text = "🏠: " + phone.number
                        cellphoneNumber!!.visibility = View.VISIBLE
                        cellphoneNumber!!.text = "📱: " + phone.number
                        officeNumber!!.visibility = View.VISIBLE
                        officeNumber!!.text = "🏢: " + phone.number
                    }

                    "Cellphone" -> {
                        cellphoneNumber!!.visibility = View.VISIBLE
                        cellphoneNumber!!.text = "📱: " + phone.number
                        officeNumber!!.visibility = View.VISIBLE
                        officeNumber!!.text = "🏢: " + phone.number
                    }

                    "Office" -> {
                        officeNumber!!.visibility = View.VISIBLE
                        officeNumber!!.text = "🏢: " + phone.number
                    }
                }
            } else {
                phonesCardView!!.visibility = View.GONE
            }

        }

        return view
    }

    companion object {

        val CONTACT_ID = "CONTACT_ID"


        fun newInstance(): ContactDetailFragment {
            return ContactDetailFragment()
        }
    }

}
