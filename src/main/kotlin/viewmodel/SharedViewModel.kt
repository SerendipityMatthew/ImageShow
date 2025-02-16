package viewmodel

import androidx.lifecycle.ViewModel
import com.ashampoo.kim.Kim
import com.ashampoo.kim.common.convertToPhotoMetadata
import extension.isImageFile
import image.CameraBodyInfo
import image.ImageGPSInfo
import image.ImageMeta
import image.ImageSizeInfo
import image.LensInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import java.io.File

const val IMAGE_FILE_PATH = "/Users/matthew/Downloads/github/Matthew/ImageShow/"

class SharedViewModel() : KoinComponent, ViewModel() {
    private val _imageMetaList = MutableStateFlow<List<ImageMeta>>(listOf())
    val imageList: StateFlow<List<ImageMeta>> = _imageMetaList.asStateFlow()

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val viewModelScope = CoroutineScope(SupervisorJob() + ioDispatcher)

    init {
        readImages()
    }

    fun refreshImages() {
        viewModelScope.launch {
            delay(1000)
            _imageMetaList.emit(emptyList())
            readImages()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun readImages() {
        viewModelScope.launch {
            val list = readImages(IMAGE_FILE_PATH)
            _imageMetaList.emit(list)
        }
    }

    suspend fun readImages(path: String): List<ImageMeta> {
        val file = File(path)

        val imageList = if (file.isFile) {
            if (file.isImageFile()) {
                val imageMeta = ImageMeta(
                    filePath = file.path,
                    imageFileName = file.name,
                )
                listOf(imageMeta)
            } else {
                emptyList()
            }
        } else if (file.isDirectory) {
            file.walk()
                .filter {
                    it.isImageFile()
                }
                .map { f ->
                    val photoMetadata = Kim.readMetadata(f.readBytes())?.convertToPhotoMetadata()
                    if (photoMetadata != null) {
                        ImageMeta(
                            filePath = f.path,
                            imageFileName = f.name,
                            imageGPSInfo = ImageGPSInfo(
                                latitude = photoMetadata.gpsCoordinates?.latitude,
                                longitude = photoMetadata.gpsCoordinates?.longitude
                            ),
                            imageSizeInfo = ImageSizeInfo(
                                widthInPx = photoMetadata.widthPx,
                                heightInPx = photoMetadata.heightPx,
                            ),
                            cameraBodyInfo = CameraBodyInfo(
                                cameraMake = photoMetadata.cameraMake,
                                cameraModel = photoMetadata.cameraModel,
                            ),
                            lensInfo = LensInfo(
                                name = photoMetadata.lensName ?: "",
                                lensMake = photoMetadata.lensMake,
                                lensModel = photoMetadata.lensModel,
                            )
                        )

                    } else {
                        ImageMeta(
                            filePath = f.path,
                            imageFileName = f.name,
                        )
                    }

                }.toMutableList()
        } else {
            emptyList()
        }
        return imageList
    }

}
