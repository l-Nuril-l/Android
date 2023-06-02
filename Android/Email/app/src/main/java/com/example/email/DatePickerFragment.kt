package com.example.email

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "ARG_DATE";

class DatePickerFragment : DialogFragment() {

    val dateListener = DatePickerDialog.OnDateSetListener{
        _ : DatePicker, year: Int,month: Int, day: Int ->
        var resultDate : Date = GregorianCalendar(year,month,day).time
        targetFragment?.let {
            fragment -> (fragment as Callbacks).onDateSelected(resultDate)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        return DatePickerDialog(
            requireContext(),
            dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    interface Callbacks {
        fun onDateSelected(date: Date)
    }

    companion object {
        fun newInstance(date: Date):DatePickerFragment {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)

            return DatePickerFragment().apply {
                arguments = args;
            }
        }
    }
}