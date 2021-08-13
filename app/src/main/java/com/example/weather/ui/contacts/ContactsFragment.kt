package com.example.weather.ui.contacts

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.example.weather.R
import com.example.weather.databinding.FragmentContactsBinding

private const val READ_CONTACTS_REQUEST_CODE = 111

class ContactsFragment : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = ContactsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when {
            requireContext().checkSelfPermission(Manifest.permission.READ_CONTACTS)
                    == PackageManager.PERMISSION_GRANTED -> {
                getContacts()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                showDialog()
            }
            else -> {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    READ_CONTACTS_REQUEST_CODE
                )
            }
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(context)
            .setTitle(R.string.contacts_alert_title)
            .setMessage(R.string.contacts_alert_message)
            .setPositiveButton(R.string.contacts_alert_btn_yes) { _, _ ->
                requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    READ_CONTACTS_REQUEST_CODE
                )
            }
            .setNegativeButton(R.string.contacts_alert_btn_no) { _, _ ->
                binding.containerForContacts.addView(
                    AppCompatTextView(requireContext()).apply {
                        text = getString(R.string.contacts_access_error)
                        setTextColor(context.getColor(R.color.design_default_color_error))
                        textAlignment = TEXT_ALIGNMENT_CENTER
                    }
                )
            }
            .create()
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == READ_CONTACTS_REQUEST_CODE) {
            val pos = permissions.indexOf(Manifest.permission.READ_CONTACTS)
            if (grantResults[pos] == PackageManager.PERMISSION_GRANTED) {
                getContacts()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun getContacts() {
        context?.let {
            val contentResolver = it.contentResolver

            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )

            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        val name = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                        )
                        binding.containerForContacts.addView(
                            AppCompatTextView(requireContext()).apply { text = name }
                        )
                    }
                }
            }
            cursorWithContacts?.close()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}