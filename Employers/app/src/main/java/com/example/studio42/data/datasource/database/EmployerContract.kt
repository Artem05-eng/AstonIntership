package com.example.studio42.data.datasource.database

object EmployerContract {

    const val TABLE_NAME_EMPLOYER = "employers"

    object CollumnEmployer {
        const val ID = "id"
        const val NAME = "name"
        const val OPEN_VACANCIES = "open_vacancies"
        const val PREV_KEY = "prev_key"
        const val NEXT_KEY = "next_key"
    }
}