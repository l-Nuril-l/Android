package com.example.taskintent

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
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
import androidx.lifecycle.Observer
import java.util.*

private const val ARG_TASK_ID = "task_id"
private const val TAG = "TaskFragment"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
private const val REQUEST_CONTACT = 1
private const val DATE_FORMAT = "EEE, MMM, dd"

class TaskFragment : Fragment(), DatePickerFragment.Callbacks {
    private lateinit var task: Task
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var shareTaskButton: Button
    private lateinit var chooseReceiverButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProvider(this).get(TaskDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()
        val taskId: UUID = arguments?.getSerializable(ARG_TASK_ID) as UUID
        taskDetailViewModel.loadTask(taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        titleField = view.findViewById(R.id.task_title) as EditText
        dateButton = view.findViewById(R.id.task_date) as Button
        shareTaskButton = view.findViewById(R.id.share_task) as Button
        chooseReceiverButton = view.findViewById(R.id.task_receiver) as Button
        solvedCheckBox = view.findViewById(R.id.task_solved) as CheckBox


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskDetailViewModel.taskLiveData.observe(
            viewLifecycleOwner,
            Observer { task ->
                task?.let {
                    this.task = task
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                task.title = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }
        titleField.addTextChangedListener(titleWatcher)

        solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                task.isSolved = isChecked
            }
        }

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(task.date).apply {
                setTargetFragment(this@TaskFragment, REQUEST_DATE)
                show(this@TaskFragment.parentFragmentManager, DIALOG_DATE)
            }
        }

        shareTaskButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getTaskInfo())
                putExtra(Intent.EXTRA_SUBJECT, "Task Info")
            }.also { intent ->
                startActivity(intent)
            }
        }

        chooseReceiverButton.apply {
            val pickContactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            setOnClickListener {
                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        taskDetailViewModel.saveTask(task)
    }

    private fun updateUI() {
        titleField.setText(task.title)
        dateButton.text = task.date.toString()
        solvedCheckBox.isChecked = task.isSolved
        solvedCheckBox.jumpDrawablesToCurrentState()

        if (task.receiver.isNotEmpty()) {
            chooseReceiverButton.text = task.receiver
        }
    }

    private fun getTaskInfo(): String {
        val solvedString = if (task.isSolved) {
            getString(R.string.task_info_solved)
        } else {
            getString(R.string.task_info_unsolved)
        }

        val dateString = DateFormat.format(DATE_FORMAT, task.date).toString()

        val receiver = if (task.receiver.isBlank()) {
            getString(R.string.task_info_no_receiver)
        } else {
            getString(R.string.task_info_receiver)
        }

        return getString(R.string.task_info, task.title, dateString, solvedString, receiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_CONTACT && data != null -> {
                val contactUri: Uri = data.data!!
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)

                val cursor = requireActivity().contentResolver
                    .query(contactUri, queryFields, null, null, null)

                cursor?.use {
                    if (it.count == 0) {
                        return
                    }

                    it.moveToFirst()
                    val receiver = it.getString(0)
                    task.receiver = receiver
                    taskDetailViewModel.saveTask(task)
                    chooseReceiverButton.text = receiver
                }

            }
        }
    }

    companion object {
        fun newInstance(taskId: UUID): TaskFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TASK_ID, taskId)
            }
            return TaskFragment().apply {
                arguments = args
            }
        }
    }

    override fun onDateSelected(date: Date) {
        task.date = date
        updateUI()
    }
}