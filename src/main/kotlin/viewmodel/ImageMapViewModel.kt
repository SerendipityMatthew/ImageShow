package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import image.ImageMapMarkerInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import utils.Utils

class ImageMapViewModel : KoinComponent, ViewModel() {
    val imageGpsInfoGsonFlow = MutableStateFlow("")

    init {
        imageMarkerInfoEncode()
    }

    private fun imageMarkerInfoEncode() {
        viewModelScope.launch {
            Utils.imageGpsInfoFlow.collect { markerInfos ->
                val markersJson = Json.encodeToString(markerInfos)
                imageGpsInfoGsonFlow.emit(markersJson)
            }
        }
    }


}

