package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

object Trainees : Table("gymtrainees") {
    val traineeId = integer("traineeId").autoIncrement().primaryKey()
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val address = varchar("address", 225)
    val eircode = varchar("eircode", 10)
    val county = varchar("county", 50)
    val phoneNumber = varchar("phone_number", 15)
    val gender = varchar("gender", 10)
    val dateOfBirth = datetime("date_of_birth")
    val height = integer("height")
    val weight = integer("weight")
    val paymentMethod = varchar("payment_method", 25)
    val amount = double("amount")
    val username = varchar("username", 50)
    val password = varchar("password", 255)
    val emergency_contacts_first_name = varchar("emergency_contacts_first_name", 50)
    val emergency_contacts_last_name = varchar("emergency_contacts_last_name", 50)
    val emergency_contacts_phone_number = varchar("emergency_contacts_phone_number", 15)
    val emergency_contacts_relation = varchar("emergency_contacts_relation", 50)
}
