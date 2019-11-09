package com.example.android.meetingscheduler.entities

/**
 * entity to capture the Remote entity to Scheduling Core Entity
 */
data class SchedulingEntity(
        val start_time: String?,
        val end_time: String?,
        val description: String?,
        val participants: List<String>?
)