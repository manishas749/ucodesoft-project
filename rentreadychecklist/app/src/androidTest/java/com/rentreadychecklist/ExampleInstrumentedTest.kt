package com.rentreadychecklist

import android.content.Context
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rentreadychecklist.utils.ImageUpload
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        val uri: Uri? = null
        val context: Context = InstrumentationRegistry.getInstrumentation().context
        val value = ImageUpload().getImagePath(context,uri!!)
        assertEquals("",value)


    }
}