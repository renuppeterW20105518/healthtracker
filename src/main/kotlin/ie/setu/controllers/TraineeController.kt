package ie.setu.controllers

import ie.setu.domain.*
import ie.setu.domain.repository.TraineeDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object TraineeController {

    private val traineeDao = TraineeDAO()
    fun addTrainee(ctx: Context) {
        val trainee : Trainee = jsonToObject(ctx.body())

        val traineeId = traineeDao.save(trainee)
        if (traineeId != null) {
            trainee.traineeId = traineeId
            ctx.json(trainee)
            ctx.status(201)
        }
    }
}
