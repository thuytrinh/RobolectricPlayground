package thuytrinh.robolectricplayground

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class AppTest {
  @Test
  fun `works fine`() {
    // Given
    // When
    // Then
    assertThat(File("build/work-manager.log")).exists()
  }
}
