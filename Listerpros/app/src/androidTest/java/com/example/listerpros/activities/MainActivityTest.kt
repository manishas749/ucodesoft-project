package com.example.listerpros.activities

import android.os.SystemClock
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.example.listerpros.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest
{
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup()
    {

        scenario= ActivityScenario.launch(MainActivity::class.java)
        scenario.moveToState(Lifecycle.State.RESUMED)

    }

    @Test
    fun timeSheetPerform()
    {
        onView(withId(R.id.timeSheetsFragment)).perform(click())

    }

    @Test
    fun onJobPerform()
    {
        onView(withId(R.id.myJobsFragment)).perform(click())

    }


}