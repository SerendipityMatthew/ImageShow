package extension

import java.io.File
import java.util.Locale

fun File.isImageFile(): Boolean {
    return this.isFile
            && (imageExtensionList.contains(this.extension.uppercase(Locale.ROOT)))
            || imageExtensionList.contains(
        this.extension.lowercase(Locale.ROOT)
    )
}

val imageExtensionList = listOf(
    ImageExtension.JPEG.extension,
    ImageExtension.PNG.extension,
    ImageExtension.GIF.extension,
    ImageExtension.WEBP.extension,
    ImageExtension.AVIF.extension,
    ImageExtension.HEIC.extension,
    ImageExtension.TIFF.extension,
)