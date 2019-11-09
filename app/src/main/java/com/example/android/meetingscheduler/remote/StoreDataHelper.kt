package com.example.android.meetingscheduler.remote

import android.os.AsyncTask

import java.io.DataOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

//can be used for post query
class StoreDataHelper : AsyncTask<String, Void, String>() {
    private var httpURLConnection: HttpURLConnection? = null
    private var data = " "
    public override fun doInBackground(vararg params: String): String {
        try {
            httpURLConnection = URL(params[0]).openConnection() as HttpURLConnection
            httpURLConnection!!.requestMethod = "POST"

            httpURLConnection!!.doOutput = true

            val wr = DataOutputStream(httpURLConnection!!.outputStream)
            wr.writeBytes("PostData= " + params[1])
            wr.flush()
            wr.close()

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return data
    }
}
