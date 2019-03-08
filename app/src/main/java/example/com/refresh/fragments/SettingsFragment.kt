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
import android.widget.TextView
import example.com.refresh.R
import android.content.Intent
import android.content.ActivityNotFoundException
import android.net.Uri


/**
 * Created by ADMIN on 6/21/2017.
 */

class SettingsFragment : Fragment() {

    object Statified {
        var MY_PREFS_NAME = "ShakeFeature"
    }

    var shakeSwitch: Switch? = null
    var _activity: Activity? = null
    var rate : TextView? = null
    var share : TextView? = null

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
        rate = view.findViewById(R.id.settings_rate) as TextView
        share = view.findViewById(R.id.settings_share) as TextView

        rate?.setOnClickListener({

            //Handle Rate us
            val uri = Uri.parse("market://details?id=" + activity?.getPackageName())
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + activity?.getPackageName())))
            }
        })

        share?.setOnClickListener({
            val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            var shareBody = "Download Refresh Music Player now!!!\nWith in-built lyrics search & equalizer\nLink: "
            shareBody += "http://play.google.com/store/apps/details?id=" + activity?.getPackageName()
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Download Refresh Music Player")
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        })
        return view
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.action_sort)
        val item1 = menu.findItem(R.id.action_search)
        item.isVisible = false
        item1.isVisible = false
    }

}
