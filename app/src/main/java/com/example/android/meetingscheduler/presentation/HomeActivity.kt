package com.example.android.meetingscheduler.presentation


import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.meetingscheduler.R
import com.example.android.meetingscheduler.remote.RemoteDataFetchClass
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class HomeActivity : AppCompatActivity() {
    private val cal = Calendar.getInstance()

    private val viewModel: HomeActivityViewModel by lazy { ViewModelProviders.of(this).get(HomeActivityViewModel::class.java) }


    companion object {
        fun errorScenario(meetingFlag: Boolean) {
            val intent = Intent()
            intent.putExtra("NO_MEETINGS", meetingFlag)
        }

        var recycler_view: RecyclerView? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RemoteDataFetchClass.date = date_edit_text.hint.toString()
        val process = RemoteDataFetchClass()
        process.execute()
        recycler_view = recycler_view_meetings

        recycler_view_meetings?.layoutManager = LinearLayoutManager(applicationContext)
        recycler_view_meetings.itemAnimator = DefaultItemAnimator()

        recycler_view_meetings.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        date_edit_text.hint = SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis())

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Toast.makeText(this, "Oops!! Sunday, no meetings. Enjoy!!", Toast.LENGTH_LONG).show()
        }
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            date_edit_text.hint = sdf.format(cal.time)

        }


        date_edit_text.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
            DatePickerDialog(this@HomeActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        fetch_meetings.setOnClickListener {
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                Toast.makeText(this, "Oops!! Sunday, no meetings. Enjoy!!", Toast.LENGTH_LONG).show()
            }
            val meetingStatus = intent.getBooleanExtra("NO_MEETINGS", true)
            if (!meetingStatus) {
                date_layout.error = "Oops!! No meetings"
            }
            RemoteDataFetchClass.date = date_edit_text.hint.toString()
            val getResponse = RemoteDataFetchClass()
            getResponse.execute()
            viewModel.initialize(date_edit_text.hint.toString())
        }

        previous_action.setOnClickListener {
            DatePickerDialog(this@HomeActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        next_action.setOnClickListener {
/*            var dt = Date()
            val c = Calendar.getInstance()
            c.time = dt
            c.add(Calendar.DATE, 1)
            val myFormat = "dd/MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            date_edit_text.hint = sdf.format(c.time)*/
            DatePickerDialog(this@HomeActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        fab.setOnClickListener {
            startActivity(Intent(this, AddMeetingActivity::class.java))
        }


    }


}






