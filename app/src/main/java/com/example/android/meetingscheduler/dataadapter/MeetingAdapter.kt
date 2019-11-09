package com.example.android.meetingscheduler.dataadapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.meetingscheduler.R
import com.example.android.meetingscheduler.entities.SchedulingEntity
import java.util.ArrayList

/**
 * This adapter will help to build the container for the scheduling entity list
 * @param meetingsList - will be passed by the activity
 */
class MeetingAdapter(private val meetingsList: ArrayList<SchedulingEntity>?) : RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder>() {

    inner class MeetingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var startTimeView: TextView = view.findViewById(R.id.start_time)
        var endTimeView: TextView = view.findViewById(R.id.end_time)
        var descriptionView: TextView = view.findViewById(R.id.description_text)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item_layout, parent, false)

        return MeetingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val meeting = meetingsList?.get(position)
        holder.startTimeView.text = meeting?.start_time.toString()
        holder.endTimeView.text = " - ".plus(meeting?.end_time)
        holder.descriptionView.text = meeting?.description
    }

    override fun getItemCount(): Int {
        return meetingsList?.size?:0
    }
}