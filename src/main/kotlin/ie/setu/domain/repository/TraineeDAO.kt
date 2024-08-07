package ie.setu.domain.repository

import ie.setu.domain.Trainee
import ie.setu.domain.db.Trainees
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

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
}