package com.example.navigation.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(
    val id: Long,
    val name: String,
    val phone: String,
    val photoUri: String?
) : Parcelable