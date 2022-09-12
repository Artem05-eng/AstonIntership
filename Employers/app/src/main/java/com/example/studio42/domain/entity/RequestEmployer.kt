package com.example.studio42.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestEmployer(
    var text: String,
    var type: String,
    var flag: Boolean
) : Parcelable