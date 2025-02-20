import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>
    fun onBackClicked(toIndex: Int)

    sealed class Child {
        class ImageMap(val component: ImageMapComponent) : Child()
        class Main(val component: MainComponent) : Child()
    }
}


class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.MainConfig,
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(
        config: Config,
        childComponentContext: ComponentContext
    ): RootComponent.Child =
        when (config) {
            is Config.ImageMapConfig -> RootComponent.Child.ImageMap(
                imageMapComponent(
                    childComponentContext
                )
            )

            is Config.MainConfig -> RootComponent.Child.Main(mainComponent(childComponentContext))
        }

    private fun imageMapComponent(componentContext: ComponentContext): ImageMapComponent =
        DefaultImageMapComponent(
            componentContext = componentContext,
            onShowMainScreen = { navigation.pop() },
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun mainComponent(componentContext: ComponentContext): MainComponent =
        DefaultMainComponent(
            componentContext = componentContext,
            onShowImageGPS = { navigation.push(Config.ImageMapConfig) },
        )


    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object ImageMapConfig : Config

        @Serializable
        data object MainConfig : Config
    }
}


interface ImageMapComponent {
    fun onShowMainScreenClicked()
}

interface MainComponent {
    fun onShowImageGPSScreenClicked()
}


class DefaultImageMapComponent(
    componentContext: ComponentContext,
    private val onShowMainScreen: () -> Unit,
) : ImageMapComponent, ComponentContext by componentContext {

    override fun onShowMainScreenClicked() {
        onShowMainScreen()
    }
}

class DefaultMainComponent(
    componentContext: ComponentContext,
    private val onShowImageGPS: () -> Unit,
) : MainComponent, ComponentContext by componentContext {

    override fun onShowImageGPSScreenClicked() {
        onShowImageGPS()
    }
}


