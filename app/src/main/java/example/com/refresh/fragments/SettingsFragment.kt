package example.com.refresh.fragments

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import example.com.refresh.R


/**
 * Created by ADMIN on 6/21/2017.
 */

class SettingsFragment : Fragment() {

    object Statified {
        var MY_PREFS_NAME = "ShakeFeature"
    }

    var shakeSwitch: Switch? = null
    var _activity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        _activity = context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        _activity = activity
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val prefs = _activity?.getSharedPreferences(SettingsFragment.Statified.MY_PREFS_NAME, MODE_PRIVATE)
        val isAllowed = prefs?.getBoolean("feature", false)
        if (isAllowed as Boolean) {
            shakeSwitch?.setChecked(true)
        } else {

        }
        shakeSwitch?.setOnCheckedChangeListener({ compoundButton, b ->
            if (b) {
                val editor = activity?.getSharedPreferences(SettingsFragment.Statified.MY_PREFS_NAME, MODE_PRIVATE)?.edit()
                editor?.putBoolean("feature", true)
                editor?.apply()
            } else {
                val editor = activity?.getSharedPreferences(SettingsFragment.Statified.MY_PREFS_NAME, MODE_PRIVATE)?.edit()
                editor?.putBoolean("feature", false)
                editor?.apply()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)
        activity?.title = "Settings"
        shakeSwitch = view.findViewById(R.id.switchShake) as Switch
        return view
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.action_sort)
        if (item == null) {

        } else {
            item.isVisible = false
        }

    }

}
