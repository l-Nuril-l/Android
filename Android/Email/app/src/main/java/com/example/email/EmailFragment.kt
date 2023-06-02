package com.example.email

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.*

private const val ARG_EMAIL_ID = "email_id"
private const val TAG = "EmailFragment"
private const val DIALOG_DATE = "DIALOG_DATE"
private const val DIALOG_TIME = "DIALOG_TIME"
private const val REQUEST_DATE = 1
private const val REQUEST_TIME = 2
private const val REQUEST_CONTACT = 3
private const val REQUEST_NUMBER = 4
private const val DATE_FORMAT ="EEE, MMM, dd"

class EmailFragment : Fragment(), DatePickerFragment.Callbacks,TimePickerFragment.Callbacks {
    private lateinit var email: Email
    private lateinit var titleField: EditText
    private lateinit var senderField: EditText
    private lateinit var textField: EditText
    private lateinit var dateButton: Button
    private lateinit var receiverButton: Button
    private lateinit var callButton: Button
    private lateinit var shareButton: Button
    private lateinit var isImportantCheckBox: CheckBox
    private val emailDetailViewModel: EmailDetailViewModel by lazy {
        ViewModelProvider(this).get(EmailDetailViewModel::class.java)
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateUI()
    {
        titleField.setText(email.title)
        senderField.setText(email.sender)
        textField.setText(email.text)
        dateButton.text = SimpleDateFormat("EEEE, MMM dd, yyyy, hh:mm").format(email.date)
        isImportantCheckBox.isChecked = email.isImportant

        if(email.receiver.isNotEmpty()){
            receiverButton.text = email.receiver;
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailDetailViewModel.emailLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                it?.let {
                    this.email = it
                    updateUI()
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        email = Email()
        val emailId: UUID = arguments?.getSerializable(ARG_EMAIL_ID) as UUID
        emailDetailViewModel.loadEmail(emailId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_email, container, false)
        titleField = view.findViewById(R.id.title)
        senderField = view.findViewById(R.id.sender)
        textField = view.findViewById(R.id.text)
        dateButton = view.findViewById(R.id.date)
        receiverButton = view.findViewById(R.id.email_receiver)
        shareButton = view.findViewById(R.id.email_share)
        callButton = view.findViewById(R.id.email_call)
        isImportantCheckBox = view.findViewById(R.id.isImportant)


        return view
    }

    override fun onDateSelected(date: Date) {
        email.date = date

        TimePickerFragment.newInstance(email.date).apply {
            setTargetFragment(this@EmailFragment, REQUEST_TIME)
            show(this@EmailFragment.parentFragmentManager, DIALOG_TIME)
        }

        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_CONTACT && data != null ->{
                val contactUri: Uri = data.data!!
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                val cursor = requireActivity().contentResolver
                    .query(contactUri,queryFields,null,null,null)
                cursor?.use {
                    if(it.count == 0)
                    {
                        return
                    }
                    it.moveToFirst()
                    val receiver = it.getString(0)
                    email.receiver = receiver
                    emailDetailViewModel.saveEmail(email)
                    receiverButton.text = receiver
                }
            }
            requestCode == REQUEST_NUMBER && data != null->{
                val contactUri: Uri = data.data!!
                val queryFields = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val cursor = requireActivity().contentResolver
                    .query(contactUri,queryFields,null,null,null)
                cursor?.use {
                    if(it.count == 0)
                    {
                        return
                    }
                    it.moveToFirst()
                    val number = it.getString(0)
                    Intent(Intent.ACTION_DIAL).apply {
                        setData(Uri.parse("tel:$number"))
                    }.also {
                        startActivity(it)
                    }
                }



            }
        }
    }

    override fun onStop() {
        super.onStop()
        emailDetailViewModel.saveEmail(email)
    }

    private fun getEmailInfo(): String {
        val isImportant = if (email.isImportant) getString(R.string.email_important) else getString(R.string.email_not_important)
        val dateString = DateFormat.format(DATE_FORMAT, email.date);
        val receiver = if (email.receiver.isBlank()) getString(R.string.email_info_no_receiver) else getString(R.string.email_info_receiver)
        return getString(R.string.email_info, email.title, dateString, isImportant, receiver);
    }



    override fun onStart() {
        super.onStart()

        titleField.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    email.title = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

        senderField.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    email.sender = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })
        textField.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    email.text = p0.toString()
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

        dateButton.setOnClickListener {
                DatePickerFragment.newInstance(email.date).apply {
                    setTargetFragment(this@EmailFragment, REQUEST_DATE)
                    show(this@EmailFragment.parentFragmentManager, DIALOG_DATE)
                }
        }

        shareButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT,getEmailInfo())
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title))
            }.also {
                startActivity(it)
            }
        }

        val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        receiverButton.setOnClickListener {
            startActivityForResult(pickContactIntent, REQUEST_CONTACT)
        }

        val pickNumberIntent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        callButton.setOnClickListener {
            startActivityForResult(pickNumberIntent, REQUEST_NUMBER)
        }

        isImportantCheckBox.apply {
            setOnCheckedChangeListener { _, x ->
                email.isImportant = x;
            }
        }

    }
    companion object

    fun newInstance(emailId : UUID): EmailFragment{
        val args = Bundle().apply {
            putSerializable(ARG_EMAIL_ID,emailId)
        }
        
        val fragment = EmailFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onTimeSelected(date: Date) {
        email.date.time = date.time
        updateUI()
    }
}