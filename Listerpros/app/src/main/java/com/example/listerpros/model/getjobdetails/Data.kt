package com.example.listerpros.model.getjobdetails

data class Data(
    val access_method: String,
    val additional_info: String,
    val customer: Customer,
    val end: String,
    val gate_code: String,
    val id: Int,
    val key_info: String,
    val location: String,
    val location_status: LocationStatus,
    val mechanical_box_code: String,
    val notes: List<Note>,
    val occupancy: String,
    val occupant_contact: String,
    val occupant_name: String,
    val preferred_shoot_date: String,
    val property_square_footage: Int,
    val schedule_time: String,
    val start: String,
    val status: String,
    val summary: String,
    val supra_lockbox: String,
    val tasks: List<Task>,
    val title: String,
    val unit_number: String
)