package com.achmadss.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_app_data")
data class Contacts (
    @PrimaryKey
    var id: String,
    var firstName: String = "",
    var lastName: String = "",
    var email: String? = null,
    var dob: String? = null,
)