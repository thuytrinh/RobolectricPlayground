package thuytrinh.robolectricplayground

import android.app.Application
import android.content.Context
import androidx.work.*
import java.io.File

class App : Application() {
  override fun onCreate() {
    super.onCreate()

    WorkManager.initialize(
      this,
      Configuration.Builder()
        .build()
    )

    val workManager = WorkManager.getInstance(this)
    workManager.enqueue(
      OneTimeWorkRequest.Builder(WriteFileWorker::class.java)
        .build()
    )
  }
}

class WriteFileWorker(
  context: Context,
  workerParams: WorkerParameters
) : Worker(context, workerParams) {
  override fun doWork(): Result {
    File("build/work-manager.log").writeText("(づ｡◕‿‿◕｡)づ")
    return Result.success()
  }
}
