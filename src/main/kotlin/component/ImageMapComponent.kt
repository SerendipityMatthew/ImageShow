package component

import com.arkivanov.decompose.ComponentContext

interface ImageMapComponent {
    fun onShowMainScreenClicked()
}

class DefaultImageMapComponent(
    componentContext: ComponentContext,
    private val onShowMainScreen: () -> Unit,
) : ImageMapComponent, ComponentContext by componentContext {

    override fun onShowMainScreenClicked() {
        onShowMainScreen()
    }
}