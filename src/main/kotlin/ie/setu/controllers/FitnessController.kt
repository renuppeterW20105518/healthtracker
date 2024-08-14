package ie.setu.controllers

import ie.setu.domain.FitnessGoal
import ie.setu.domain.repository.FitnessGoalDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.utils.jsonToObject
import io.javalin.http.Context

object FitnessController {
    private val userDao = UserDAO()
    private val fitnessGoalDAO = FitnessGoalDAO()

    fun getAllFitness(ctx: Context) {
        val fitnessGoal = fitnessGoalDAO.getAll()
        if(fitnessGoal.size != 0){
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(fitnessGoal)
    }

    fun getFitnessByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val fitness = fitnessGoalDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (fitness.isNotEmpty()) {
                ctx.json(fitness)
                ctx.status(200)
            } else {
                ctx.status(404)
            }
        } else{
            ctx.status(404)
        }
    }

    fun getFitnessByFitnessId(ctx: Context) {
        val fitness = fitnessGoalDAO.findByFitnessId((ctx.pathParam("fitness-id").toInt()))
        if (fitness != null){
            ctx.json(fitness)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addFitness(ctx: Context) {
        val fitness : FitnessGoal = jsonToObject(ctx.body())
        val userId = userDao.findById(fitness.userId)
        if (userId != null) {
            val fitnessId = fitnessGoalDAO.save(fitness)
            fitness.id = fitnessId
            ctx.json(fitness)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    fun deleteFitnessByFitnessId(ctx: Context) {
        if(fitnessGoalDAO.deleteByFitnessId(ctx.pathParam("fitness-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteFitnessByUserId(ctx: Context){
        if(fitnessGoalDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateFitness(ctx: Context){
        val fitness : FitnessGoal = jsonToObject(ctx.body())
        if (fitnessGoalDAO.updateByFitnessId(
                fitnessId = ctx.pathParam("fitness-id").toInt(),
                fitnessToUpdate = fitness) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}