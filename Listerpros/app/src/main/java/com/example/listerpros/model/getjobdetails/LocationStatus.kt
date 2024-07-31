package com.example.listerpros.model.getjobdetails

data class LocationStatus(
    val added_by: String,
    val created: String,
    val id: Int,
    val location_status: String,
    val modified: String,
    val order_id: Int,
    val status_added_by: StatusAddedBy
)