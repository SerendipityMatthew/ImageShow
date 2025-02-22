package component

import com.arkivanov.decompose.ComponentContext
import image.ImageMeta

interface SingleImageComponent {
    val imageMeta: ImageMeta

    fun onBackClicked()
}

class DefaultSingleImageComponent(
    override val imageMeta: ImageMeta,
    componentContext: ComponentContext,
    private val onBackClick: () -> Unit,
) : SingleImageComponent, ComponentContext by componentContext {

    override fun onBackClicked() {
        onBackClick()
    }
}
