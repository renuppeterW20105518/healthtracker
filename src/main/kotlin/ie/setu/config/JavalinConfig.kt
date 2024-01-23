package ie.setu.config

import ie.setu.controllers.ActivityController
import ie.setu.controllers.UserController
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson

class JavalinConfig {

    fun startJavalinService(): Javalin{

        val app = Javalin.create{it.jsonMapper(JavalinJackson(jsonObjectMapper()))}
            .apply {
                exception(Exception::class.java){e,_ -> e.printStackTrace()}
                error(404){ctx -> ctx.json("404 - Not found")}
            }
            .start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun getRemoteAssignedPort(): Int{
        val remotePort = System.getenv("PORT")
        return if (remotePort != null){
            Integer.parseInt(remotePort)
        }else 7000
    }

    private fun registerRoutes(app: Javalin){
        app.routes{
            path("/api/users"){
                get(UserController::getAllUsers)
                post(UserController::addUser)
                path("{user-id}"){
                    get(UserController::getUserByUserId)
                    delete(UserController::deleteUser)
                    patch(UserController::updateUser)
                    path("activities"){
                        get(ActivityController::getActivitiesByUserId)
                        delete(ActivityController::deleteActivityByUserId)
                    }
                }
                path("/email/{email}"){
                    get(UserController::getUserByEmail)
                }
            }
            path("/api/activities") {
                get(ActivityController::getAllActivities)
                post(ActivityController::addActivity)
                path("{activity-id}"){
                    get(ActivityController::getActivitiesByActivityId)
                    delete(ActivityController::deleteActivityByActivityId)
                    patch(ActivityController::updateActivity)
                }
            }
        }
    }
}