package thuytrinh.robolectricplayground

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@RunWith(AndroidJUnit4::class)
class DataStoreTest {
  @Test
  fun `should work`() = runBlockingTest {
    // Given
    val dataStore = DataStoreFactory.create(
      serializer = ItemsSerializer,
      scope = this,
      produceFile = {
        File(ApplicationProvider.getApplicationContext<Context>().filesDir, "data")
      }
    )

    // When
    dataStore.updateData { listOf(0, 1, 2, 3) }

    // Then
    val actual = dataStore.data.first()
    assertThat(actual).containsExactly(0, 1, 2, 3)
  }
}

private object ItemsSerializer : Serializer<List<Int>> {
  override suspend fun writeTo(t: List<Int>, output: OutputStream) {
    output.writer().write(
      Json.encodeToString(t)
    )
  }

  override suspend fun readFrom(input: InputStream): List<Int> {
    val json = input.reader().readText()
    return when {
      json.isEmpty() -> emptyList()
      else -> Json.decodeFromString(json)
    }
  }

  override val defaultValue: List<Int>
    get() = emptyList()
}
