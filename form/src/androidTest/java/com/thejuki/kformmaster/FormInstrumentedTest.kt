package com.thejuki.kformmaster

import android.text.InputType
import android.widget.DatePicker
import android.widget.ProgressBar
import android.widget.TimePicker
import androidx.appcompat.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.thejuki.kformmaster.item.ContactItem
import com.thejuki.kformmaster.token.ItemsCompletionView
import com.thejuki.kformmaster.widget.SegmentedGroup
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Form Instrumented Test
 *
 * The Great Form Instrumented Test
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class FormInstrumentedTest {

    @get:Rule
    val activityRule = ActivityTestRule(FormActivityTest::class.java)

    @Test
    fun header_isDisplayed() {
        // Check if Header 1 is displayed on the form
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(0))
        onView(withText("Header 1"))
                .check(matches(isDisplayed()))

        // Check if Header 2 is displayed on the form
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(4))
        onView(withText("Header 2"))
                .check(matches(isDisplayed()))

        // Check if Header 3 is displayed on the form
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(8))
        onView(withText("Header 3"))
                .check(matches(isDisplayed()))

        // Check if Header 4 is displayed on the form
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(12))
        onView(withText("Header 4"))
                .check(matches(isDisplayed()))

        // Check if Header 5 is displayed on the form
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(18))
        onView(withText("Header 5"))
                .check(matches(isDisplayed()))
    }

    @Test
    fun editTextElement_shouldHaveInputType() {
        // Check Email type
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(1))
        onView(allOf(`is`(instanceOf(AppCompatEditText::class.java)),
                hasTextViewInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)))
                .check(matches(isDisplayed()))

        // Check Password type
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(2))
        onView(allOf(`is`(instanceOf(AppCompatEditText::class.java)),
                hasTextViewInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)))
                .check(matches(isDisplayed()))

        // Check Phone type
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(3))
        onView(allOf(`is`(instanceOf(AppCompatEditText::class.java)),
                hasTextViewInputType(InputType.TYPE_CLASS_PHONE or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)))
                .check(matches(isDisplayed()))

        // Check Single Line type
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(5))
        onView(allOf(`is`(instanceOf(AppCompatEditText::class.java)),
                hasTextViewInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS),
                hasTextViewSingleLine()))
                .check(matches(isDisplayed()))

        // Check Multi Line type
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(6))
        onView(allOf(`is`(instanceOf(AppCompatEditText::class.java)),
                hasTextViewInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_MULTI_LINE)))
                .check(matches(isDisplayed()))

        // Check Number type
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(7))
        onView(allOf(`is`(instanceOf(AppCompatEditText::class.java)),
                hasTextViewInputType(InputType.TYPE_CLASS_NUMBER)))
                .check(matches(isDisplayed()))
    }

    @Test
    fun text_shouldNotBeDisplayed_whenVisibleIsFalse() {
        // Text element's visible property is set to false and should not show up on the form
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(25))
        onView(withText("Hidden"))
                .check(matches(not(isDisplayed())))
    }

    @Test
    fun progress_changes_whenProgressed() {
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(22))
        onView(withClassName(Matchers.equalTo(ProgressBar::class.java.name)))
                .perform(setProgressBarProgress(75))
                .check(matches(withProgressBarProgress(75)))
    }

    @Test
    fun segmented_changes_whenClicked() {
        onView(withId(R.id.recyclerView)).perform(scrollToPosition<RecyclerView.ViewHolder>(23))

        onView(withClassName(Matchers.equalTo(SegmentedGroup::class.java.name)))
                .check(matches(hasRadioButtonCheck(0)))

        // Select "Mango"
        onView(withClassName(Matchers.equalTo(SegmentedGroup::class.java.name)))
                .perform(setRadioButtonCheck(2))

        // Check each RadioButton
        onView(withClassName(Matchers.equalTo(SegmentedGroup::class.java.name)))
                .check(matches(not(hasRadioButtonCheck(0))))
        onView(withClassName(Matchers.equalTo(SegmentedGroup::class.java.name)))
                .check(matches(not(hasRadioButtonCheck(1))))
        onView(withClassName(Matchers.equalTo(SegmentedGroup::class.java.name)))
                .check(matches(hasRadioButtonCheck(2)))
        onView(withClassName(Matchers.equalTo(SegmentedGroup::class.java.name)))
                .check(matches(not(hasRadioButtonCheck(3))))
    }

    @Test
    fun button_disabled_shouldDoNothing_whenClicked() {
        // Click button to verify that nothing happens because it is disabled
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(26, click()))
        onView(withId(R.id.recyclerView))
                .check(matches(not(withText("Disabled?"))))
    }

    @Test
    fun picker_openDialog_whenClicked() {
        // Open SingleItem Dialog and select "Orange"
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(13, click()))
        onView(withText("SingleItem Dialog"))
                .inRoot(RootMatchers.isDialog())
                .check(matches(isDisplayed()))
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Orange")))
                .perform(click())

        // Open MultiItems Dialog, select "Orange", click "OK"
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(14, click()))
        onView(withText("MultiItems Dialog"))
                .inRoot(RootMatchers.isDialog())
                .check(matches(isDisplayed()))
        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Orange")))
                .perform(click())
        onView(withId(android.R.id.button1)).perform(click())

        // Open Date Dialog, enter 2/25/2018, click "OK"
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(9, click()))
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
                .inRoot(RootMatchers.isDialog())
                .check(matches(isDisplayed())).perform(PickerActions.setDate(2018, 2, 25))
        onView(withId(android.R.id.button1)).perform(click())

        // Open Time Dialog, enter 12:30 AM, click "OK"
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(10, click()))
        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name)))
                .inRoot(RootMatchers.isDialog())
                .check(matches(isDisplayed())).perform(PickerActions.setTime(12, 30))
        onView(withId(android.R.id.button1)).perform(click())

        //** DateTime Dialog */

        // Open Date Dialog, enter 2/25/2018, click "OK"
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(11, click()))
        onView(withClassName(Matchers.equalTo(DatePicker::class.java.name)))
                .inRoot(RootMatchers.isDialog())
                .check(matches(isDisplayed())).perform(PickerActions.setDate(2018, 2, 25))
        onView(withId(android.R.id.button1)).perform(click())

        // Open Time Dialog, enter 12:30 AM, click "OK"
        onView(withClassName(Matchers.equalTo(TimePicker::class.java.name)))
                .inRoot(RootMatchers.isDialog())
                .check(matches(isDisplayed())).perform(PickerActions.setTime(12, 30))
        onView(withId(android.R.id.button1)).perform(click())
    }

    @Test
    fun autoComplete_providesSuggestions_whenTextIsTyped() {
        // Enter text in the autoComplete field, click on the suggestion, and verify it is displayed in the field
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(15, click()))

        onView(withClassName(Matchers.equalTo(AppCompatAutoCompleteTextView::class.java.name)))
                .perform(typeText("Kotlin"))
        onData(equalTo(ContactItem(id = 3, value = "Kotlin Contact", label = "Kotlin Contact (Coder)")))
                .inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withText("Kotlin Contact (Coder)"))
                .check(matches(isDisplayed()))
    }

    @Test
    fun autoCompleteToken_providesSuggestions_whenTextIsTyped() {
        // Enter text in the autoCompleteToken field, click on the suggestion, and verify it exists in the options list
        val contactItem = ContactItem(id = 3, value = "Kotlin.Contact@mail.com", label = "Kotlin Contact (Coder)")

        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(16, click()))

        onView(withClassName(Matchers.equalTo(ItemsCompletionView::class.java.name)))
                .perform(typeText("Kotlin"))
        onData(equalTo(contactItem))
                .inRoot(RootMatchers.isPlatformPopup()).perform(click())
        onView(withClassName(Matchers.equalTo(ItemsCompletionView::class.java.name)))
                .check(matches(hasItemsCompletionViewObject(contactItem)))
    }

    /*
    @Test
    fun button_openDialog_whenClicked() {
        // Click button to verify the value observer Unit action works and displays an alert dialog
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(24, click()))
        onView(withText("Confirm?"))
                .inRoot(RootMatchers.isDialog())
                .check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())
    }
    */

    @Test
    fun slider_changes_whenProgressed() {
        // Change slider to check progress value
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(20, click()))
        onView(withClassName(Matchers.equalTo(AppCompatSeekBar::class.java.name)))
                .perform(setSeekBarProgress(10))
                .check(matches(withSeekBarProgress(10)))
    }

    @Test
    fun checkBox_becomeChecked_whenClicked() {
        // Check it
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(21, click()))
        onView(`is`(instanceOf(AppCompatCheckBox::class.java)))
                .check(matches(isChecked()))

        // UnCheck it
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(21, click()))
        onView(`is`(instanceOf(AppCompatCheckBox::class.java)))
                .check(matches(isNotChecked()))
    }

    @Test
    fun switch_becomeChecked_whenClicked() {
        // Check it
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(19, click()))
        onView(`is`(instanceOf(SwitchCompat::class.java)))
                .check(matches(isChecked()))

        // UnCheck it
        onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(19, click()))
        onView(`is`(instanceOf(SwitchCompat::class.java)))
                .check(matches(isNotChecked()))
    }
}
