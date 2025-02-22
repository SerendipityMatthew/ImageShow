package component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import ui.screen.ImageMapScreen
import ui.screen.MainScreen
import ui.screen.SingleImageScreen


@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {
    MaterialTheme {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            Children(
                stack = component.stack,
                modifier = Modifier.fillMaxSize(),
                animation = stackAnimation(fade() + scale())
            ) {
                when (val instance = it.instance) {
                    is RootComponent.Child.ImageMap -> ImageMapScreen(imageMapComponent = instance.component)
                    is RootComponent.Child.Main -> MainScreen(mainComponent = instance.component)
                    is RootComponent.Child.SingleImage -> SingleImageScreen(singleImageComponent = instance.component)
                }
            }
        }
    }
}
