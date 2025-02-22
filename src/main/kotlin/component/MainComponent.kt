package component

import com.arkivanov.decompose.ComponentContext
import image.ImageMeta

interface MainComponent {
    fun onShowImageGPSScreenClicked()
    fun onShowSingleImageClick(meta: ImageMeta)
}


class DefaultMainComponent(
    componentContext: ComponentContext,
    private val onShowImageGPS: () -> Unit,
    private val onShowSingleImageClick: (ImageMeta) -> Unit,
) : MainComponent, ComponentContext by componentContext {

    override fun onShowImageGPSScreenClicked() {
        onShowImageGPS()
    }

    override fun onShowSingleImageClick(meta: ImageMeta) {
        onShowSingleImageClick.invoke(meta)
    }
}