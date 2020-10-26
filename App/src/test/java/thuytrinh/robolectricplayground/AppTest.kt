package thuytrinh.robolectricplayground

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.model.Statement
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import java.io.File

class WorkManagerRule : TestRule {
  override fun apply(base: Statement?, description: Description?): Statement {
    return object : Statement() {
      override fun evaluate() {
        val config = Configuration.Builder()
          .setExecutor(SynchronousExecutor())
          .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(
          InstrumentationRegistry.getInstrumentation().targetContext,
          config
        )

        base?.evaluate()
      }
    }
  }
}

@RunWith(AndroidJUnit4::class)
class AppTest : KoinTest {
  @get:Rule
  val rule: TestRule = RuleChain
    .outerRule(
      WorkManagerRule()
    )
    .around(
      KoinTestRule.create {
        modules(
          getAppModules() + getFakeAppModules()
        )
      }
    )
    .around(InstantTaskExecutorRule())
  private val appController: AppController by inject()

  @Test
  fun `works fine`() {
    // When
    appController.initialize()

    // Then
    assertThat(File("build/work-manager.log")).exists()
  }
}

fun getFakeAppModules(): List<Module> = listOf(
  module {
    single<Context> { ApplicationProvider.getApplicationContext() }
    single(override = true) {
      WorkManager.getInstance(
        InstrumentationRegistry.getInstrumentation().targetContext
      )
    }
  }
)
