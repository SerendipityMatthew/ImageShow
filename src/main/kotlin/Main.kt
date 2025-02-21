import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import dev.datlag.kcef.KCEF
import org.koin.core.KoinApplication
import utils.Utils.runOnUiThread


fun main() = application {
    KoinApplication.init().modules(allModules)
    val lifecycle = LifecycleRegistry()

    val root = runOnUiThread {
        DefaultRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
        )
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

