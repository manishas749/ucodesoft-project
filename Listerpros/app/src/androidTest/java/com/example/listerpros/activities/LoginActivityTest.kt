package com.example.listerpros.activities



import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.listerpros.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {


    private lateinit var mActivityScenarioRule : ActivityScenario<LoginActivity>

    @Before
    fun setup(){
        mActivityScenarioRule = ActivityScenario.launch(LoginActivity::class.java)
        mActivityScenarioRule.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun loginActivityTest() {
        onView(withId(R.id.emailAddressField)).perform(click())
        onView(withId(R.id.emailAddressField)).perform(typeText("rajnish157@gmail.com"))

        onView(withId(R.id.passwordField)).perform(click())
        onView(withId(R.id.passwordField)).perform(typeText("Pass"))

        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

    }

    @Test
    fun loginActivityTest1() {
        onView(withId(R.id.emailAddressField)).perform(click())
        onView(withId(R.id.emailAddressField)).perform(typeText("rajnish157"))

        onView(withId(R.id.passwordField)).perform(click())
        onView(withId(R.id.passwordField)).perform(typeText(""))

        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

    }

    @Test
    fun loginActivityTest2() {
        onView(withId(R.id.emailAddressField)).perform(click())
        onView(withId(R.id.emailAddressField)).perform(typeText("rajnish157@gmail.com"))

        onView(withId(R.id.passwordField)).perform(click())
        onView(withId(R.id.passwordField)).perform(typeText("Passw0rd"))

        onView(isRoot()).perform(ViewActions.closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

    }


}
