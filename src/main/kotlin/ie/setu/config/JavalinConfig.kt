package ie.setu.config

import ie.setu.controllers.*
import ie.setu.utils.jsonObjectMapper
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import io.javalin.vue.VueComponent

class JavalinConfig {

    fun startJavalinService(): Javalin{

        val app = Javalin.create{
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.staticFiles.enableWebjars()
            it.vue.vueAppName = "app" // only required for Vue 3, is defined in layout.html
        }.apply {
                exception(Exception::class.java){e,_ -> e.printStackTrace()}
                error(404){ctx -> ctx.json("404 - Not found")}
            }.start(getRemoteAssignedPort())

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
                    path("fitness"){
                        get(FitnessController::getFitnessByUserId)
                        delete(FitnessController::deleteFitnessByUserId)
                    }
                    path("images"){
                        get(ImageController::getImagesByUserId)
                        delete(ImageController::deleteImageByUserId)
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
            path("/api/fitness"){
                get(FitnessController::getAllFitness)
                post(FitnessController::addFitness)
                path("{fitness-id}"){
                    get(FitnessController::getFitnessByFitnessId)
                    delete(FitnessController::deleteFitnessByFitnessId)
                    patch(FitnessController::updateFitness)
                }
            }
            path("/api/images") {
                get(ImageController::getAllImages)
                post(ImageController::addImage)
                path("{image-id}"){
                    get(ImageController::getImagesByImageId)
                    delete(ImageController::deleteImageByImageId)
                    patch(ImageController::updateImage)
                }
            }
            path("/api/registration-gym"){
                post(TraineeController::addTrainee)
            }
            path("/api/login-gym"){
                post(TraineeController::login)
            }

            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside the VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
            get("/activities", VueComponent("<activity-overview></activity-overview>"))
            get("/activities/{activity-id}", VueComponent("<activity-profile></activity-profile>"))
            get("/fitness", VueComponent("<fitness-goal-overview></fitness-goal-overview>"))
            get("/fitness/{fitness-id}", VueComponent("<fitness-goal-profile></fitness-goal-profile>"))
            get("/users/{user-id}/fitness", VueComponent("<user-fitness-overview></user-fitness-overview>"))
            get("/trainee-registration", VueComponent("<registration-gym></registration-gym"))
            get("/trainee-login", VueComponent("<login-gym></login-gym"))
            get("/images", VueComponent("<image-overview></image-overview>"))
            get("/images/{image-id}", VueComponent("<image-profile></image-profile>"))
        }
    }
}