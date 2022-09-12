package com.example.studio42.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class EmloyerType(
    val id: String,
    val name: String
) : Parcelable

data class EmployerTypeWrapper<T>(
    val employer_type: List<T>
)