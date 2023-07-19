package com.achmadss.domain.model

import com.achmadss.domain.entity.Contacts

data class ContactModel(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String?,
    val dob: String?,
)

fun ContactModel.toContacts() = Contacts(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    dob = dob,
)