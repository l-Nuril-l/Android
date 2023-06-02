package com.example.email

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.sql.Time
import java.util.*

private const val ARG_TIME = "ARG_TIME";

class TimePickerFragment : DialogFragment() {
    val dateListener = TimePickerDialog.OnTimeSetListener{
            _ : TimePicker, hour: Int, min: Int ->

        val date = arguments?.getSerializable(ARG_TIME) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date

        var resultDate : Date = GregorianCalendar(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),hour,min).time

        targetFragment?.let {
                fragment -> (fragment as Callbacks).onTimeSelected(resultDate)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_TIME) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        return TimePickerDialog(
            requireContext(),
            dateListener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true)
    }

    interface Callbacks {
        fun onTimeSelected(date: Date)
    }

    companion object {
        fun newInstance(date: Date):TimePickerFragment {
            val args = Bundle()
            args.putSerializable(ARG_TIME, date)

            return TimePickerFragment().apply {
                arguments = args;
            }
        }
    }
}