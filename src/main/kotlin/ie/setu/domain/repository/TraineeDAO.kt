package ie.setu.domain.repository

import ie.setu.domain.Trainee
import ie.setu.domain.db.Trainees
import ie.setu.utils.mapToTrainee
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.and

class TraineeDAO() {
    fun save(trainee: Trainee): Int? {
        return transaction {
            Trainees.insert {
                it[firstName] = trainee.firstName
                it[lastName] = trainee.lastName
                it[address] = trainee.address
                it[eircode] = trainee.eircode
                it[county] = trainee.county
                it[phoneNumber] = trainee.phoneNumber
                it[gender] = trainee.gender
                it[dateOfBirth] = trainee.dateOfBirth
                it[height] = trainee.height
                it[weight] = trainee.weight
                it[paymentMethod] = trainee.paymentMethod
                it[amount] = trainee.amount
                it[username] = trainee.username
                it[password] = trainee.password
                it[emergency_contacts_first_name] = trainee.emergency_contacts_first_name
                it[emergency_contacts_last_name] = trainee.emergency_contacts_last_name
                it[emergency_contacts_phone_number] = trainee.emergency_contacts_phone_number
                it[emergency_contacts_relation] = trainee.emergency_contacts_relation
            } get Trainees.traineeId
        }
    }

//    fun findByUsernameandPassword(username: String, password: String): Trainee? {
//        return transaction {
//            Trainees.select() {
//                Trainees.username eq username
//                Trainees.password eq password
//            }
//                .map { mapToTrainee(it) }
//                .firstOrNull()
//        }
//    }

    fun findByUsernameandPassword(username: String, password: String): Trainee? {
        return transaction {
            Trainees.select {
                (Trainees.username eq username) and (Trainees.password eq password)
            }
                .map { mapToTrainee(it) }
                .firstOrNull()
        }
    }
}