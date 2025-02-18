package image

import com.ashampoo.kim.model.ImageSize
import utils.Utils.toBase64

data class ImageMeta(
    val filePath: String,
    val imageFileName: String,
    val lensInfo: LensInfo? = null,
    val cameraBodyInfo: CameraBodyInfo? = null,
    val imageSizeInfo: ImageSizeInfo? = null,
    val imageFocalInfo: ImageFocalInfo? = null,
    val imageGPSInfo: ImageGPSInfo? = null,
    val thumbnailImageInfo: ThumbnailImageInfo? = null
)

data class ImageFocalInfo(
    val focalLength: Double? = null,
    val exposureTime: Double? = null,
    val iso: Int? = null,
)

data class ImageSizeInfo(
    val widthInPx: Int? = null,
    val heightInPx: Int? = null,
)

data class LensInfo(
    val name: String,
    val lensMake: String? = null,
    val lensModel: String? = null,
)

data class CameraBodyInfo(
    val cameraMake: String? = null,
    val cameraModel: String? = null,
)

data class ImageGPSInfo(
    val latitude: Double? = null,
    val longitude: Double? = null,
)

data class ThumbnailImageInfo(
    val thumbnailImageSize: ImageSizeInfo? = null,
    val thumbnailBytes: ByteArray? = null,
    val thumbnailBase64: String? = null,

) {

}