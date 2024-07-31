package com.example.listerpros.fragments


import android.os.SystemClock
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.listerpros.adapter.CalendarAdapter
import com.example.listerpros.constants.Constants
import com.example.listerpros.preferences.LoginTokenManager
import org.junit.Before
import org.junit.Test


class MyJobsFragmentTest {
    private lateinit var scenario: FragmentScenario<MyJobsFragment>
    private var loginKey = LoginTokenManager(ApplicationProvider.getApplicationContext())

    @Before
    fun setup() {

        Constants.BEARER_TOKEN = loginKey.getToken().toString()
        scenario =
            launchFragmentInContainer(themeResId = com.example.listerpros.R.style.Theme_Listerpros)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun weekDateCheck() {

        SystemClock.sleep(2500)
        onView(withId(com.example.listerpros.R.id.prevArrow)).perform(click())
        SystemClock.sleep(2500)
        var count = 0

        while (count < 6) {
            for (i in 0..6) {
                onView(withId(com.example.listerpros.R.id.dateRecycleView))
                    .perform(
                        RecyclerViewActions.actionOnItemAtPosition<CalendarAdapter.ViewHolder>(
                            i,
                            click()
                        )
                    )
            }
            count++
        }

    }
}