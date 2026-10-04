package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.AvatarConfig
import com.example.model.GraphicsProfile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Nexa World", appName)
  }

  @Test
  fun `verify avatar default configuration`() {
    val config = AvatarConfig()
    assertNotNull(config.hairStyle)
    assertEquals("Cyber Spikes", config.hairStyle)
    assertEquals("Nexus Scout", config.outfitStyle)
  }

  @Test
  fun `verify graphics profiles`() {
    assertEquals(30, GraphicsProfile.LOW.targetFps)
    assertEquals(60, GraphicsProfile.MEDIUM.targetFps)
    assertEquals(60, GraphicsProfile.HIGH.targetFps)
  }
}
