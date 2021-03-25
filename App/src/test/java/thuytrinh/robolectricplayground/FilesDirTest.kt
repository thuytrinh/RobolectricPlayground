package thuytrinh.robolectricplayground

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class FilesDirTest {
  @Test
  fun test() {
    // Given
    val context = ApplicationProvider.getApplicationContext<Context>()
    val dir = File(context.filesDir, "logs").also { it.mkdirs() }
    val fileName = "data.log"
    val file = File(dir, fileName)

    // When
    val expectedContent = "Hi!"
    file.writeText(expectedContent)
    println(file.absolutePath)

    // Then
    val actualContent = File(dir, fileName).readText()
    assertThat(actualContent).isEqualTo(expectedContent)
  }
}
