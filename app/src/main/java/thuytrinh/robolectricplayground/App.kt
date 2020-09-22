package thuytrinh.robolectricplayground

import android.app.Application
import androidx.work.WorkManager

class App : Application() {
  override fun onCreate() {
    super.onCreate()

    val workManager = WorkManager.getInstance(this)
  }
}
