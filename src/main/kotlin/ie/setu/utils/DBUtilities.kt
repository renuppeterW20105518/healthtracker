package ie.setu.utils

import ie.setu.domain.Activity
import ie.setu.domain.FitnessGoal
import ie.setu.domain.db.Users
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.FitnessGoals
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