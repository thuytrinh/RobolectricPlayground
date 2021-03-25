package thuytrinh.robolectricplayground

import android.app.Application
import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

class App : Application() {
  private val appController: AppController by inject()

  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@App)
      modules(getAppModules())
    }

    appController.initialize()
  }
}

class AppController(
  private val workManager: WorkManager
) {
  fun initialize() {
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

fun getAppModules(): List<Module> = listOf(
  module {
    single { WorkManager.getInstance(get()) }
    single { AppController(get()) }
  }
)
