package com.example.android.meetingscheduler.presentation

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.meetingscheduler.R
import com.example.android.meetingscheduler.remote.RemoteDataFetchClass
import com.example.android.meetingscheduler.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

internal const val NO_MEETINGS_KEY = "NO_MEETINGS"

class HomeActivity : AppCompatActivity() {
    private val cal = Calendar.getInstance()

    private val viewModel: HomeActivityViewModel by lazy { ViewModelProviders.of(this).get(HomeActivityViewModel::class.java) }


    companion object {
        fun errorScenario(meetingFlag: Boolean) {
            val intent = Intent()
            intent.putExtra(NO_MEETINGS_KEY, meetingFlag)
        }

        var recycler_view: RecyclerView? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler_view = recycler_view_meetings
        RemoteDataFetchClass.date = date_edit_text.hint.toString()
        val process = RemoteDataFetchClass()
        process.execute()

        recycler_view_meetings.apply {
            this.visibility = View.VISIBLE
            this?.layoutManager = LinearLayoutManager(applicationContext)
            this.itemAnimator = DefaultItemAnimator()
            this.addItemDecoration(DividerItemDecoration(this@HomeActivity, LinearLayoutManager.VERTICAL))
        }

        date_edit_text.hint = SimpleDateFormat(getString(R.string.date_format)).format(System.currentTimeMillis())

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Toast.makeText(this, getString(R.string.sunday_error_message), Toast.LENGTH_LONG).show()
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val sdf = SimpleDateFormat(getString(R.string.date_format), Locale.US)
            date_edit_text.hint = sdf.format(cal.time)
        }

        date_edit_text.setOnClickListener {
            this.hideKeyboard(it)
            DatePickerDialog(this@HomeActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        fetch_meetings.setOnClickListener {
            val meetingStatus = intent.getBooleanExtra(NO_MEETINGS_KEY, true)
            if (!meetingStatus) {
                date_layout.error = getString(R.string.gerenic_meeting_error)
            }
            RemoteDataFetchClass.date = date_edit_text.hint.toString()
            val getResponse = RemoteDataFetchClass()
            getResponse.execute()
            viewModel.initialize(date_edit_text.hint.toString())
        }

        previous_action.setOnClickListener {
            val c = Calendar.getInstance()
            val splitDate = date_edit_text.hint.toString().split("/")
            c.set(Calendar.DATE, splitDate[0].toInt() - 1)
            val sdf = SimpleDateFormat(getString(R.string.date_format), Locale.US)
            date_edit_text.hint = sdf.format(c.time)
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                Toast.makeText(this, getString(R.string.sunday_error_message), Toast.LENGTH_LONG).show()
                recycler_view_meetings.visibility = View.GONE
            } else {
                recycler_view_meetings.visibility = View.VISIBLE
                recycler_view_meetings.adapter?.notifyDataSetChanged()
                RemoteDataFetchClass.date = date_edit_text.hint.toString()
                val getResponse = RemoteDataFetchClass()
                getResponse.execute()
            }
        }

        next_action.setOnClickListener {
            val c = Calendar.getInstance()
            val splitDate = date_edit_text.hint.toString().split("/")
            c.set(Calendar.DATE, splitDate[0].toInt() + 1)
            date_edit_text.hint = SimpleDateFormat(getString(R.string.date_format), Locale.US).format(c.time)
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                Toast.makeText(this, getString(R.string.sunday_error_message), Toast.LENGTH_LONG).show()
                recycler_view_meetings.visibility = View.GONE
            } else {
                recycler_view_meetings.visibility = View.VISIBLE
                RemoteDataFetchClass.date = date_edit_text.hint.toString()
                val getResponse = RemoteDataFetchClass()
                getResponse.execute()
            }
        }

        fab.setOnClickListener {
            startActivity(Intent(this, AddMeetingActivity::class.java))
        }

    }
}

