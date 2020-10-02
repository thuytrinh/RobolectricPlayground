package thuytrinh.robolectricplayground

import android.icu.text.RelativeDateTimeFormatter
import android.icu.text.RelativeDateTimeFormatter.Direction
import android.icu.text.RelativeDateTimeFormatter.RelativeUnit
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements

@RunWith(AndroidJUnit4::class)
@Config(shadows = [RelativeDateTimeFormatterShadow::class])
class RelativeDateTimeFormatterTest {
  @Test
  fun `should work for days`() {
    val relativeTime = RelativeDateTimeFormatter.getInstance()
      .format(
        2.0,
        Direction.LAST,
        RelativeUnit.DAYS
      )
    assertThat(relativeTime).isEqualTo("2 days ago")
  }

  @Test
  fun `should work for hours`() {
    val relativeTime = RelativeDateTimeFormatter.getInstance()
      .format(
        3.0,
        Direction.LAST,
        RelativeUnit.HOURS
      )
    assertThat(relativeTime).isEqualTo("3 hours ago")
  }
}

@Implements(RelativeDateTimeFormatter::class)
class RelativeDateTimeFormatterShadow {
  companion object {
    private val mock = mockk<RelativeDateTimeFormatter>().apply {
      val ibmFormatter = IbmRelativeDateTimeFormatter.getInstance()
      every { format(any(), eq(Direction.LAST), any()) }.answers {
        ibmFormatter.format(
          args[0] as Double,
          // TODO: Make it work for all directions
          com.ibm.icu.text.RelativeDateTimeFormatter.Direction.LAST,
          (args[2] as RelativeUnit).toIbmRelativeUnit()
        )
      }
    }

    @JvmStatic
    @Implementation
    fun getInstance(): RelativeDateTimeFormatter = mock
  }
}

private typealias IbmRelativeDateTimeFormatter = com.ibm.icu.text.RelativeDateTimeFormatter
private typealias IbmRelativeUnit = com.ibm.icu.text.RelativeDateTimeFormatter.RelativeUnit

private fun RelativeUnit.toIbmRelativeUnit(): IbmRelativeUnit = when (this) {
  RelativeUnit.SECONDS -> IbmRelativeUnit.SECONDS
  RelativeUnit.MINUTES -> IbmRelativeUnit.MINUTES
  RelativeUnit.HOURS -> IbmRelativeUnit.HOURS
  RelativeUnit.DAYS -> IbmRelativeUnit.DAYS
  RelativeUnit.WEEKS -> IbmRelativeUnit.WEEKS
  RelativeUnit.MONTHS -> IbmRelativeUnit.MONTHS
  RelativeUnit.YEARS -> IbmRelativeUnit.YEARS
}
