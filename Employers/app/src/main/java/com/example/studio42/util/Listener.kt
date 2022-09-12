package com.example.studio42.util

import com.example.studio42.domain.entity.EmloyerType
import com.example.studio42.domain.entity.RequestEmployer

interface Listener {

    fun listenerType(data: RequestEmployer)
}

interface ActivityListener {
    fun listen(data: RequestEmployer)
}