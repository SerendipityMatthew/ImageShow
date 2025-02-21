package image

import kotlinx.serialization.Serializable

@Serializable
data class ImageMeta(
    val filePath: String,
    val imageFileName: String,
    val lensInfo: LensInfo? = null,
    val cameraBodyInfo: CameraBodyInfo? = null,
    val imageSizeInfo: ImageSizeInfo? = null,
    val imageFocalInfo: ImageFocalInfo? = null,
    val imageGPSInfo: ImageGPSInfo? = null,
    val thumbnailImageInfo: ThumbnailImageInfo? = null,
) {
    val imageMapMarkerInfo: ImageMapMarkerInfo
        get() {
            return ImageMapMarkerInfo(
                imageName = imageFileName,
                latitude = imageGPSInfo?.latitude ?: 0.0,
                longitude = imageGPSInfo?.longitude ?: 0.0,
                imageUrl = thumbnailImageInfo?.thumbnailFilePath ?: ""
            )
        }
    var index: Int = 0
}

@Serializable
data class ImageFocalInfo(
    val focalLength: Double? = null,
    val exposureTime: Double? = null,
    val iso: Int? = null,
)

@Serializable
data class ImageSizeInfo(
    val widthInPx: Int? = null,
    val heightInPx: Int? = null,
)

@Serializable
data class LensInfo(
    val name: String,
    val lensMake: String? = null,
    val lensModel: String? = null,
)

@Serializable
data class CameraBodyInfo(
    val cameraMake: String? = null,
    val cameraModel: String? = null,
)

@Serializable
data class ImageGPSInfo(
    val latitude: Double? = null,
    val longitude: Double? = null,
)

@Serializable
data class ThumbnailImageInfo(
    val thumbnailImageSize: ImageSizeInfo? = null,
    val thumbnailBytes: ByteArray? = null,
    val thumbnailBase64: String? = null,
    val thumbnailFilePath: String? = null,

    ) {

}

@Serializable
data class ImageMapMarkerInfo(
    val imageName: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null
)