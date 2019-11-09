package com.example.android.meetingscheduler.presentation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.android.meetingscheduler.R
import kotlinx.android.synthetic.main.activity_add_meeting.*
import java.util.*


class AddMeetingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meeting)

        supportActionBar?.title = "Add a meeting"
        supportActionBar?.setHomeButtonEnabled(true)

        meeting_date_field.hint = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
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
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)

            DatePickerDialog(this@AddMeetingActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        start_time_field.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            TimePickerDialog(this@AddMeetingActivity, listener(it), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        end_time_field.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            TimePickerDialog(this@AddMeetingActivity, listener(it), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        submit_add_meeting.setOnClickListener {
            val intent = Intent(this@AddMeetingActivity, HomeActivity::class.java)
            startActivity(intent)
        }

    }
}
