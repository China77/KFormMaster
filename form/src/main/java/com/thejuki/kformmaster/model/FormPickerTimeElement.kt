package com.thejuki.kformmaster.model

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.thejuki.kformmaster.R
import com.thejuki.kformmaster.helper.FormBuildHelper
import com.thejuki.kformmaster.listener.OnFormElementValueChangedListener
import java.io.Serializable
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Form Picker Time Element
 *
 * Form element for AppCompatEditText (which on click opens a Time dialog)
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
class FormPickerTimeElement(tag: Int = -1) : FormPickerElement<FormPickerTimeElement.TimeHolder>(tag) {

    /**
     * Date Format part of TimeHolder
     */
    var dateFormat: DateFormat = SimpleDateFormat.getDateInstance()
        set(value) {
            field = value
            this.value = FormPickerTimeElement.TimeHolder(dateValue, dateFormat)
            reInitDialog()
        }

    /**
     * Date Value part of TimeHolder
     */
    var dateValue: Date? = null
        set(value) {
            field = value
            this.value = FormPickerTimeElement.TimeHolder(dateValue, dateFormat)
            reInitDialog()
        }

    /**
     * Alert Dialog Builder
     * Used to call reInitDialog without needing context again.
     */
    private var alertDialogBuilder: AlertDialog.Builder? = null

    /**
     * Hold the [OnFormElementValueChangedListener] from [FormBuildHelper]
     */
    private var listener: OnFormElementValueChangedListener? = null

    override fun clear() {
        this.value?.useCurrentTime()
        (this.editView as? TextView)?.text = ""
        this.valueObservers.forEach { it(this.value, this) }
    }

    /**
     * Time Holder
     *
     * Holds the date fields for [FormPickerTimeElement]
     */
    class TimeHolder : Serializable {

        var isEmptyTime: Boolean = false
        var hourOfDay: Int? = null
        var minute: Int? = null
        var dateFormat: DateFormat = SimpleDateFormat.getTimeInstance()

        constructor(hourOfDay: Int, minute: Int) {
            this.hourOfDay = hourOfDay
            this.minute = minute
        }

        constructor() {
            useCurrentTime()
        }

        constructor(date: Date?, dateFormat: DateFormat = SimpleDateFormat.getDateInstance()) {
            if (date != null) {
                val calendar = Calendar.getInstance()
                calendar.time = date
                this.hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
                this.minute = calendar.get(Calendar.MINUTE)
            } else {
                isEmptyTime = true
            }
            this.dateFormat = dateFormat
        }

        override fun toString(): String {
            return if (isEmptyTime || hourOfDay == null || minute == null) ""
            else dateFormat.format(getTime())
        }

        fun getTime(): Date? {
            if (isEmptyTime || hourOfDay == null || minute == null) {
                return null
            }
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay ?: 0)
            calendar.set(Calendar.MINUTE, minute ?: 0)

            return calendar.time
        }

        fun getTime(zone: TimeZone,
                    aLocale: Locale): Date? {
            if (hourOfDay == null || minute == null) {
                return null
            }
            val calendar = Calendar.getInstance(zone, aLocale)
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay ?: 0)
            calendar.set(Calendar.MINUTE, minute ?: 0)

            return calendar.time
        }

        fun validOrCurrentTime() {
            if (hourOfDay == null || minute == null) {
                useCurrentTime()
            }
        }

        fun useCurrentTime() {
            val calendar = Calendar.getInstance()
            this.hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
            this.minute = calendar.get(Calendar.MINUTE)
            isEmptyTime = true
        }

        fun equals(another: TimeHolder): Boolean {
            return another.isEmptyTime == this.isEmptyTime &&
                    another.minute == this.minute &&
                    another.hourOfDay == this.hourOfDay
        }
    }

    override val isValid: Boolean
        get() = validityCheck()

    override var validityCheck = { !required || (value != null && value?.getTime() != null) }

    /**
     * Re-initializes the dialog
     * Should be called value changes by user
     */
    fun reInitDialog(formBuilder: FormBuildHelper? = null) {

        if (formBuilder != null) {
            listener = formBuilder.listener
        }

        val editTextView = this.editView as? AppCompatEditText

        if (editTextView?.context == null) {
            return
        }

        val timePickerDialog = TimePickerDialog(editTextView.context,
                timeDialogListener(editTextView),
                value?.hourOfDay ?: 0,
                value?.minute ?: 0,
                false)

        if (alertDialogBuilder == null) {
            alertDialogBuilder = AlertDialog.Builder(editTextView.context)
            if (this.confirmTitle == null) {
                this.confirmTitle = editTextView.context.getString(R.string.form_master_confirm_title)
            }
            if (this.confirmMessage == null) {
                this.confirmMessage = editTextView.context.getString(R.string.form_master_confirm_message)
            }
        }

        // display the dialog on click
        val listener = View.OnClickListener {
            if (!confirmEdit || valueAsString.isEmpty()) {
                timePickerDialog.show()
            } else if (confirmEdit && value != null) {
                alertDialogBuilder
                        ?.setTitle(confirmTitle)
                        ?.setMessage(confirmMessage)
                        ?.setPositiveButton(android.R.string.ok) { _, _ ->
                            timePickerDialog.show()
                        }?.setNegativeButton(android.R.string.cancel) { _, _ -> }?.show()
            }
        }

        itemView?.setOnClickListener(listener)
        editTextView.setOnClickListener(listener)
    }

    private fun timeDialogListener(editTextValue: AppCompatEditText): TimePickerDialog.OnTimeSetListener {
        return TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            with(value)
            {
                this?.hourOfDay = hourOfDay
                this?.minute = minute
                this?.isEmptyTime = false
            }

            error = null
            listener?.onValueChanged(this)
            valueObservers.forEach { it(value, this) }
            editTextValue.setText(valueAsString)
        }
    }
}