package component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import image.ImageMeta
import kotlinx.serialization.Serializable

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>
    fun onBackClicked(toIndex: Int)

    sealed class Child {
        class ImageMap(val component: ImageMapComponent) : Child()
        class Main(val component: MainComponent) : Child()
        class SingleImage(val component: SingleImageComponent) : Child()
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

            is Config.MainConfig -> RootComponent.Child.Main(mainComponent(childComponentContext, config = config))

            is Config.SingleImageConfig -> RootComponent.Child.SingleImage(
                singleImageComponent(
                    childComponentContext,
                    config
                )
            )
        }

    private fun imageMapComponent(componentContext: ComponentContext): ImageMapComponent =
        DefaultImageMapComponent(
            componentContext = componentContext,
            onShowMainScreen = { navigation.pop() },
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun mainComponent(componentContext: ComponentContext, config: Config.MainConfig): MainComponent =
        DefaultMainComponent(
            componentContext = componentContext,
            onShowImageGPS = { navigation.push(Config.ImageMapConfig) },
            onShowSingleImageClick = { navigation.push(Config.SingleImageConfig(imageMeta = it)) },
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun singleImageComponent(
        componentContext: ComponentContext,
        config: Config.SingleImageConfig
    ): SingleImageComponent =
        DefaultSingleImageComponent(
            componentContext = componentContext,
            imageMeta = config.imageMeta,
            onBackClick = { navigation.pop() },
        )


    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    @Serializable
    private sealed class Config {
        @Serializable
        data object ImageMapConfig : Config()

        @Serializable
        data object MainConfig : Config()

        @Serializable
        data class SingleImageConfig(val imageMeta: ImageMeta) : Config()
    }
}








