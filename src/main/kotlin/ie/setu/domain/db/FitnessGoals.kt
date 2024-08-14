package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object FitnessGoals: Table("fitnessgoals") {
    val id = integer("id").autoIncrement().primaryKey()
    val goal = varchar("goal", 100)
    val duration = double("duration")
    val target = varchar("target", 100)
    val status = varchar("status", 15)
    val started = datetime("started")
    val ended = datetime("ended")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
}