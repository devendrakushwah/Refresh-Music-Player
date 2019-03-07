package example.com.refresh.fragments


import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import example.com.refresh.R


/**
 * Created by ADMIN on 6/21/2017.
 */
class AboutUsFragment : Fragment() {
    var aboutUs: TextView? = null
    var btn : Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)
        activity?.title = "About us"
        btn=view.findViewById(R.id.liscence)
        aboutUs = view.findViewById(R.id.about_us_text) as TextView
        (aboutUs as TextView).text = ""
        btn?.setOnClickListener(View.OnClickListener {
            val pop=LicenseFragment()
            //pop.dialog.setTitle("Open Source Licenses")
            val fm=fragmentManager
            pop.show(fm,"Open Source Licenses")
        }
        )
        return view
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val item = menu.findItem(R.id.action_sort)
        val item1 = menu.findItem(R.id.action_search)
        item.isVisible = false
        item1.isVisible = false
    }
}

