package ie.setu.domain

import org.joda.time.DateTime

data class Trainee(
    var traineeId: Int,
    var firstName: String,
    var lastName: String,
    var address: String,
    var eircode: String,
    var county: String,
    var phoneNumber: String,
    var gender: String,
    var dateOfBirth: DateTime,
    var height: Int,
    var weight: Int,
    var paymentMethod: String,
    var amount: Double,
    var username: String,
    var password: String,
    var emergency_contacts_first_name: String,
    var emergency_contacts_last_name: String,
    var emergency_contacts_phone_number: String,
    var emergency_contacts_relation: String,
)
