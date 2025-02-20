package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashampoo.kim.Kim
import com.ashampoo.kim.common.convertToPhotoMetadata
import dev.datlag.kcef.KCEF
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
        initKcef()
    }

    private fun initKcef() {
        viewModelScope.launch(ioDispatcher) {
            KCEF.init(builder = {
                installDir(File("kcef-bundle"))
                progress {
                    onDownloading {}
                    onInitialized {
                    }
                }

                settings {
                    cachePath = File("cache").absolutePath
                }
            }, onError = {
                it?.printStackTrace()
            }, onRestartRequired = {})
        }
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
                .filter {
                    !it.path.contains("thumbnail")
                }
                .map { imageFile ->
                    val photoMetadata = Kim.readMetadata(imageFile.readBytes())?.convertToPhotoMetadata()
                    if (photoMetadata != null) {
                        ImageMeta(
                            filePath = imageFile.path,
                            imageFileName = imageFile.name,
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
                                thumbnailFilePath = "../../../" + Utils.createThumbnailFileAndSave(
                                    imageFile.path,
                                    "images/thumbnail/${imageFile.name}",
                                    100,
                                    100
                                )
                            ),

                            )

                    } else {
                        ImageMeta(
                            filePath = imageFile.path,
                            imageFileName = imageFile.name,
                        )
                    }

                }.toMutableList()
        } else {
            emptyList()
        }
        return imageList
    }

}
