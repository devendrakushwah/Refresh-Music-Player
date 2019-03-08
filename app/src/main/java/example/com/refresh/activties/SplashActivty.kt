package example.com.refresh.activties

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import example.com.refresh.R

/**
 * Created by ADMIN on 6/19/2017.
 */
class SplashActivty : AppCompatActivity() {
    object Staticated {
        var SPLASH_TIME_OUT = 1000
    }


    var permissionsString = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splash_screen)
            if (!hasPermissions(this@SplashActivty, *permissionsString)) {
                ActivityCompat.requestPermissions(this@SplashActivty, permissionsString, 131)
                //Do nothing here because permissions aren't granted

            } else {
                Handler().postDelayed({
                    val startAct = Intent(this@SplashActivty, MainActivity::class.java)
                    startActivity(startAct)
                    // close this activity
                    this.finish()
                }, SplashActivty.Staticated.SPLASH_TIME_OUT.toLong())
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            131 -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Handler().postDelayed({
                        /* Create an Intent that will start the Menu-Activity. */
                        val mainIntent = Intent(this@SplashActivty, MainActivity::class.java)
                        this.startActivity(mainIntent)
                        this.finish()
                    }, SplashActivty.Staticated.SPLASH_TIME_OUT.toLong())


                } else {
                    Toast.makeText(this, "Please grant all permissions to continue", Toast.LENGTH_SHORT).show()
                    this.finish()
                }
                return
            }
            else -> {
                Toast.makeText(this@SplashActivty, "Something went wrong", Toast.LENGTH_SHORT).show()
                this.finish()
                return
            }


        }
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        var hasAllPermissions = true
        for (permission in permissions) {
            //return false instead of assigning, but with this you can log all permission values
            val res = context.checkCallingOrSelfPermission(permission)
            if (res != PackageManager.PERMISSION_GRANTED) {
                hasAllPermissions = false
            }
        }

        return hasAllPermissions

    }
}

