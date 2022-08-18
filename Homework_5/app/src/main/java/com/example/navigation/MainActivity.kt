package com.example.navigation

import android.Manifest
import android.content.ContentProviderOperation
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.navigation.data.Contact
import com.example.navigation.databinding.ActivityMainBinding
import com.example.navigation.listener.Listener


class MainActivity : AppCompatActivity(), Listener {

    private var binding: ActivityMainBinding? = null
    lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>
    private var contact: Contact? = null

    companion object {
        private val PERMISSIONS = listOfNotNull(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        launchPermission()
        if (hasPermission().not()) {
            requestPermissions()
        } else {
            navigationList()
            contact = savedInstanceState?.getParcelable<Contact>("1")
            val flag = savedInstanceState?.getBoolean("2")
            if (contact != null && flag != null) {
                if (flag) {
                    showDetailFragment(contact!!)
                }
            }
        }
    }

    private fun launchPermission() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionToGrantedMap: Map<String, Boolean> ->
            if (permissionToGrantedMap.values.all { it }) {
                Toast.makeText(this, "Разрешение включено!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "Необходимо разрешение для действия!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun hasPermission(): Boolean {
        return PERMISSIONS.all {
            ActivityCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        requestPermissionLauncher.launch(PERMISSIONS.toTypedArray())
    }

    private fun getContacts(): List<Contact> {
        val list = mutableListOf<Contact>()
        this.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )?.use { cursor ->
            if (cursor?.moveToFirst().not()) return emptyList()
            do {
                val nameIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val name = cursor.getString(nameIndex).orEmpty()

                val idIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)
                val id = cursor.getLong(idIndex)

                val photoIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                val photoUri = cursor.getString(photoIndex)

                val numberIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val number = cursor.getString(numberIndex)

                list.add(Contact(id = id, name = name, phone = number, photoUri = photoUri))
            } while (cursor.moveToNext())
            return list
        }
        return list
    }

    private fun saveContacts(contact: Contact) {
        val ops = ArrayList<ContentProviderOperation>()
        ops.add(
            ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(
                    ContactsContract.Data._ID.toString() + "=?",
                    arrayOf(java.lang.String.valueOf(contact.id))
                )
                .withValue(ContactsContract.Data.DATA1, contact.phone)
                .build()
        )
        contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        contact = null
    }

    override fun showDetailFragment(data: Contact) {
        val orientation = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        supportFragmentManager.findFragmentByTag("DETAIL")
            ?.let { supportFragmentManager.beginTransaction().remove(it).commit() }
        if (!orientation) {
            supportFragmentManager.beginTransaction().addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container1, DetailFragment.newInstance(data), "DETAIL").commit()
        } else {
            supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container2, DetailFragment.newInstance(data), "DETAIL").commit()
        }
        contact = data
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("1", contact)
        outState.putBoolean("2", supportFragmentManager.findFragmentByTag("DETAIL") != null)
    }

    override fun updateListFragment(newData: Contact) {
        contact = newData
        saveContacts(newData)
        Handler().postDelayed(object : Runnable {
            override fun run() {
                navigationList()
            }
        }, 500)
    }

    override fun deleteContact(deleteData: Contact) {
        delete(deleteData)
        Handler().postDelayed(object : Runnable {
            override fun run() {
                navigationList()
            }
        }, 500)
    }

    private fun delete(contact: Contact) {
        val ops = ArrayList<ContentProviderOperation>()
        ops.add(
            ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
                .withSelection(
                    ContactsContract.Data._ID.toString() + "=?",
                    arrayOf(java.lang.String.valueOf(contact.id))
                )
                .build()
        )
        contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
    }

    private fun navigationList() {
        val fragment = supportFragmentManager.findFragmentByTag("TAG_LIST_FRAGMENT")
        if (fragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, ListFragment.newInstance(getContacts()), "TAG_LIST_FRAGMENT")
                .commit()
        } else {
            (fragment as? ListFragment)?.updateContact(getContacts())
        }
    }
}