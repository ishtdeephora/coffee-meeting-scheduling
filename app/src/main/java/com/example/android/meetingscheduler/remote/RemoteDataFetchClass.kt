package com.example.android.meetingscheduler.remote

import android.os.AsyncTask
import com.example.android.meetingscheduler.dataadapter.MeetingAdapter
import com.example.android.meetingscheduler.entities.SchedulingEntity
import com.example.android.meetingscheduler.presentation.HomeActivity
import com.example.android.meetingscheduler.presentation.HomeActivity.Companion.recycler_view
import com.example.android.meetingscheduler.presentation.HomeActivityViewModel
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

/**
 * This class will be responsible to fetch the remote data and convert it to core entity
 * which can then be used to show the list on the presentation layer
 */
class RemoteDataFetchClass : AsyncTask<Void, Void, Void>() {

    companion object {
        var date: String = ""
    }

    private var schedulingEvents = arrayListOf<SchedulingEntity>()
    override fun doInBackground(vararg voids: Void): Void? {
        try {
            val date = date
            val baseUrl = "http://fathomless-shelf-5846.herokuapp.com/api/schedule?date=\"$date\""

            val url = URL(baseUrl)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            val storedData = httpURLConnection.inputStream.bufferedReader().readText()

            val entityArray = JSONArray(storedData)


            val eventParticipantList = arrayListOf<String>()


            /**
             * looper to iterate over the json array
             */
            for (i in 0 until entityArray.length()) {

                val c = entityArray.getJSONObject(i)
                val startTime = c.getString("start_time")
                val endTime = c.getString("end_time")
                val description = c.getString("description")
                val participants = c.getJSONArray("participants")

                for (participant in 0 until participants.length()) {
                    eventParticipantList.add(participants[participant] as String)
                }
                schedulingEvents.add(SchedulingEntity(start_time = startTime, end_time = endTime, description = description, participants = eventParticipantList as List<String>))
                if (schedulingEvents.size < 0) {
                    HomeActivity.errorScenario(false)
                }
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        return null //return null to the post execute if no data is found, can be handled as nullable type in onPostExecute aVoid
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        println("Hello")
        HomeActivityViewModel().onSuccess(schedulingEvents)

        val meetingAdapter = MeetingAdapter(schedulingEvents)
        meetingAdapter.notifyDataSetChanged()
        recycler_view?.adapter = meetingAdapter
        recycler_view?.invalidate()
    }


}




