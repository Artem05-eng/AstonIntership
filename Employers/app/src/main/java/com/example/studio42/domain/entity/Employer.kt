package com.example.studio42.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.studio42.data.datasource.database.EmployerContract
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = EmployerContract.TABLE_NAME_EMPLOYER)
data class Employer(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = EmployerContract.CollumnEmployer.ID)
    val id: String,
    @ColumnInfo(name = EmployerContract.CollumnEmployer.NAME)
    val name: String,
    @ColumnInfo(name = EmployerContract.CollumnEmployer.OPEN_VACANCIES)
    val open_vacancies: String,
    @Embedded(prefix = "url") val logo_urls: ObjectUrl?,
    @ColumnInfo(name = EmployerContract.CollumnEmployer.PREV_KEY)
    var prev_key: Int?,
    @ColumnInfo(name = EmployerContract.CollumnEmployer.NEXT_KEY)
    var next_key: Int?
) : Parcelable

data class EmployerFound(
    val items: List<Employer>,
    val found: Int
)

@Parcelize
data class ObjectUrl(
    val original: String
) : Parcelable