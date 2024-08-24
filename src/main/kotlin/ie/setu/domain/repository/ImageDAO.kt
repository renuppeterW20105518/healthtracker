package ie.setu.domain.repository

import ie.setu.domain.Activity
import ie.setu.domain.Image
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Images
import ie.setu.utils.mapToActivity
import ie.setu.utils.mapToImage
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ImageDAO {
    fun getAll(): ArrayList<Image> {
        val imagesList: ArrayList<Image> = arrayListOf()
        transaction {
            Images.selectAll().map {
                imagesList.add(mapToImage(it)) }
        }
        return imagesList
    }

    fun findByImageId(id: Int): Image?{
        return transaction {
            Images
                .select() { Images.id eq id}
                .map{mapToImage(it)}
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<Image>{
        return transaction {
            Images
                .select { Images.userId eq userId}
                .map { mapToImage(it) }
        }
    }

    fun save(image: Image): Int{
        return transaction {
            Images.insert {
                it[title] = image.title
                it[description] = image.description
                it[image_file_path] = image.image_file_path
                it[userId] = image.userId
            }
        }get Images.id
    }

    fun updateByImageId(imageId: Int, imageToUpdate: Image): Int{
        return transaction {
            Images.update ({
                Images.id eq imageId}) {
                it[title] = imageToUpdate.title
                it[description] = imageToUpdate.description
                it[image_file_path] = imageToUpdate.image_file_path
                it[userId] = imageToUpdate.userId
            }
        }
    }

    fun deleteByImageId (imageId: Int): Int{
        return transaction{
            Images.deleteWhere { Images.id eq imageId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            Images.deleteWhere { Images.userId eq userId }
        }
    }
}