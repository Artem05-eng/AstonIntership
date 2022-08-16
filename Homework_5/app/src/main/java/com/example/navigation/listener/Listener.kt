package com.example.navigation.listener

import com.example.navigation.data.Contact

interface Listener {

    fun showDetailFragment(data: Contact)

    fun updateListFragment(data: Contact)
}