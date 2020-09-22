package thuytrinh.robolectricplayground

import android.app.Application
import android.content.Context
import androidx.work.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.io.File

class App : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@App)
      modules(
        listOf(
          module {
            single { WorkManager.getInstance(get()) }
          }
        )
      )
    }

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
