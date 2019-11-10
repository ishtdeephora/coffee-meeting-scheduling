package com.example.android.meetingscheduler.presentation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.android.meetingscheduler.R
import com.example.android.meetingscheduler.utils.hideKeyboard
import kotlinx.android.synthetic.main.activity_add_meeting.*
import java.util.*


class AddMeetingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meeting)

        supportActionBar?.title = getString(R.string.add_meeting_header)
        supportActionBar?.setHomeButtonEnabled(true)

        meeting_date_field.hint = SimpleDateFormat(getString(R.string.date_format)).format(System.currentTimeMillis())
        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = getString(R.string.date_format) // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            meeting_date_field.hint = sdf.format(cal.time)
        }

        fun listener(editText: View): TimePickerDialog.OnTimeSetListener {
            return TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                (editText as EditText).hint = SimpleDateFormat("HH:mm a").format(cal.time)
            }
        }


        meeting_date_field.setOnClickListener {
            this.hideKeyboard(it)
            DatePickerDialog(this@AddMeetingActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        start_time_field.setOnClickListener {
            this.hideKeyboard(it)
            TimePickerDialog(this@AddMeetingActivity, listener(it), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        end_time_field.setOnClickListener {
            this.hideKeyboard(it)
            TimePickerDialog(this@AddMeetingActivity, listener(it), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        submit_add_meeting.setOnClickListener {
            startActivity(Intent(this@AddMeetingActivity, HomeActivity::class.java))
        }

        container_layout.setOnClickListener {
            this.hideKeyboard(it)
        }

    }
}
