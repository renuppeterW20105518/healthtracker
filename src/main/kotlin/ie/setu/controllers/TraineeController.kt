package ie.setu.controllers

import ie.setu.domain.*
import ie.setu.domain.repository.TraineeDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import io.javalin.http.UnauthorizedResponse

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

    fun login(ctx: Context) {
        val loginData: Map<String, String> = jsonToObject(ctx.body())
        val username = loginData["username"]
        val password = loginData["password"]

        if (username == null || password == null) {
            throw UnauthorizedResponse("Username and password must be provided")
        }

        val trainee: Trainee? = traineeDao.findByUsernameandPassword(username, password)

        if (trainee != null) {
            trainee.traineeId = trainee.traineeId
            ctx.json(trainee)
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }
}
