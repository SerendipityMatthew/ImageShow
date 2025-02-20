import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import dev.datlag.kcef.KCEF
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinApplication
import utils.Utils.runOnUiThread
import java.io.File


fun main() = application {
    val isShowWebview = remember { mutableStateOf(false) }
    KoinApplication.init().modules(allModules)
    val lifecycle = LifecycleRegistry()

    val root = runOnUiThread {
        DefaultRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
        )
    }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            KCEF.init(builder = {
                installDir(File("kcef-bundle"))
                progress {
                    onDownloading {}
                    onInitialized {
                        isShowWebview.value = true
                    }
                }

                settings {
                    cachePath = File("cache").absolutePath
                }
            }, onError = {
                it?.printStackTrace()
            }, onRestartRequired = {})
        }
    }
    Window(title = "ImageShow", onCloseRequest = ::exitApplication) {
        RootContent(root)
    }


    DisposableEffect(Unit) {
        onDispose {
            KCEF.disposeBlocking()
        }
    }
}

