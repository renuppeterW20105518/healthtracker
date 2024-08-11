package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*
import ie.setu.domain.db.Trainees.autoIncrement
import ie.setu.domain.db.Trainees.primaryKey
import org.jetbrains.exposed.sql.ResultRow

fun mapToUser (it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email]
)

fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

fun mapToTrainee(it: ResultRow) = Trainee(
    traineeId = it[Trainees.traineeId],
    firstName = it[Trainees.firstName],
    lastName = it[Trainees.lastName],
    address = it[Trainees.address],
    eircode = it[Trainees.eircode],
    county = it[Trainees.county],
    phoneNumber = it[Trainees.phoneNumber],
    gender = it[Trainees.gender],
    dateOfBirth = it[Trainees.dateOfBirth],
    height = it[Trainees.height],
    weight = it[Trainees.weight],
    paymentMethod = it[Trainees.paymentMethod],
    amount = it[Trainees.amount],
    username = it[Trainees.username],
    password = it[Trainees.password],
    emergency_contacts_first_name = it[Trainees.emergency_contacts_first_name],
    emergency_contacts_last_name = it[Trainees.emergency_contacts_last_name],
    emergency_contacts_phone_number = it[Trainees.emergency_contacts_phone_number],
    emergency_contacts_relation = it[Trainees.emergency_contacts_relation]
)

fun mapToFitnessGoal(it: ResultRow) = FitnessGoal(
    id = it[FitnessGoals.id],
    goal = it[FitnessGoals.goal],
    duration = it[FitnessGoals.duration],
    target = it[FitnessGoals.target],
    status = it[FitnessGoals.status],
    started = it[FitnessGoals.started],
    ended = it[FitnessGoals.ended],
    userId = it[FitnessGoals.userId]
)
