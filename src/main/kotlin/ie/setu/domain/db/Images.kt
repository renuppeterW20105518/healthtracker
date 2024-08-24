package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Images: Table("images") {
    val id = integer("id").autoIncrement().primaryKey()
    val title = varchar("title",100)
    val description = varchar("description", 200)
    val image_file_path = varchar("image_file_path",400)
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}