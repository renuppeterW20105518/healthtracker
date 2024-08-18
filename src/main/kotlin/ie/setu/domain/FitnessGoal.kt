package ie.setu.domain

import org.joda.time.DateTime

data class FitnessGoal(
    var id: Int,
    var goal:String,
    var duration: Double,
    var target: String,
    var status: String,
    var started: DateTime,
    var ended: DateTime,
    var userId: Int
)
