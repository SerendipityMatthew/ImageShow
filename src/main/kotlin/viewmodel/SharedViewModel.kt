package viewmodel

import androidx.lifecycle.ViewModel
import com.ashampoo.kim.Kim
import com.ashampoo.kim.common.convertToPhotoMetadata
import extension.isImageFile
import image.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent
import utils.Utils
import java.io.File

const val IMAGE_FILE_PATH = "images"

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
                            ),
                            thumbnailImageInfo = ThumbnailImageInfo(
                                thumbnailBase64 = Utils.thumbnailToBase64(Utils.createThumbnail(f.path, 50, 50))
                            ),

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
