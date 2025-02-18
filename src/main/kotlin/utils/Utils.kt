package utils

import java.io.File
import java.util.Base64
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

object Utils {
    fun encodeImageToBase64(imagePath: String): String {
        val file = File(imagePath)
        val bytes = file.readBytes()
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun createThumbnail(
        imagePath: String,
        thumbnailWidth: Int,
        thumbnailHeight: Int
    ): BufferedImage {
        val originalImage = ImageIO.read(File(imagePath))
        val thumbnail =
            originalImage.getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH)
        val bufferedThumbnail =
            BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_ARGB)
        val graphics = bufferedThumbnail.createGraphics()
        graphics.drawImage(thumbnail, 0, 0, null)
        graphics.dispose()
        return bufferedThumbnail
    }

    fun thumbnailToBase64(thumbnail: BufferedImage): String {
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(thumbnail, "png", outputStream)
        val bytes = outputStream.toByteArray()
        val base64 = Base64.getEncoder().encodeToString(bytes)
        return base64
    }

    fun ByteArray.toBase64(): String {
        return Base64.getEncoder().encodeToString(this)
    }

}