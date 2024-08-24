package ie.setu.controllers

import ie.setu.domain.Image
import ie.setu.domain.repository.ImageDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object ImageController {

    private val userDao = UserDAO()
    private val imageDAO = ImageDAO()

    fun getAllImages(ctx: Context) {
        val images = imageDAO.getAll()
        if(images.size != 0){
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(images)
    }

    fun getImagesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val images = imageDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (images.isNotEmpty()) {
                ctx.json(images)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else{
            ctx.status(404)
        }
    }

    fun getImagesByImageId(ctx: Context) {
        val image = imageDAO.findByImageId((ctx.pathParam("image-id").toInt()))
        if (image != null){
            ctx.json(image)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addImage(ctx: Context) {
        val image : Image = jsonToObject(ctx.body())
        val userId = userDao.findById(image.userId)
        if (userId != null) {
            val imageId = imageDAO.save(image)
            image.id = imageId
            ctx.json(image)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    fun deleteImageByImageId(ctx: Context) {
        if(imageDAO.deleteByImageId(ctx.pathParam("image-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteImageByUserId(ctx: Context){
        if(imageDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateImage(ctx: Context){
        val image : Image = jsonToObject(ctx.body())
        if (imageDAO.updateByImageId(
                imageId = ctx.pathParam("image-id").toInt(),
                imageToUpdate = image) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

}