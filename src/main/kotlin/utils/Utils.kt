package utils

import java.io.File
import java.util.Base64
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

import java.io.FileOutputStream
import java.io.IOException

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


    fun createThumbnailFileAndSave(imagePath: String, thumbnailPath: String, width: Int, height: Int):String {
        // 读取原始图片
        val originalImage: BufferedImage = ImageIO.read(File(imagePath))

        // 创建缩略图
        val thumbnailImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g = thumbnailImage.createGraphics()
        g.drawImage(originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null)
        g.dispose()

        // 保存缩略图到本地
        ImageIO.write(thumbnailImage, "png", File(thumbnailPath))
        return thumbnailPath
    }


    fun ByteArray.toBase64(): String {
        return Base64.getEncoder().encodeToString(this)
    }

}