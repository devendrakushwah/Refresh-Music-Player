package example.com.refresh.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import example.com.refresh.R
import android.support.v4.app.DialogFragment


/**
 * A simple [Fragment] subclass.
 */
class LicenseFragment :  DialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_license, container, false)
        getDialog().setTitle("Open Source Licenses");
        return view
    }

}// Required empty public constructor
