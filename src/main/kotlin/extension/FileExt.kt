package extension

import java.io.File
import java.util.Locale

fun File.isImageFile(): Boolean {
    return this.isFile
            && (imageFileExtensionList.contains(this.extension.uppercase(Locale.ROOT)))
            || imageFileExtensionList.contains(
        this.extension.lowercase(Locale.ROOT)
    )
}

val imageFileExtensionList = listOf(
    ImageFileExtension.JPEG.extension,
    ImageFileExtension.JPG.extension,
    ImageFileExtension.PNG.extension,
    ImageFileExtension.GIF.extension,
    ImageFileExtension.WEBP.extension,
    ImageFileExtension.AVIF.extension,
    ImageFileExtension.HEIC.extension,
    ImageFileExtension.TIFF.extension,
)