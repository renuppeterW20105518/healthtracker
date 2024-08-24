package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Activity
import ie.setu.domain.FitnessGoal
import ie.setu.domain.Image
import org.junit.jupiter.api.TestInstance
import kong.unirest.Unirest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Nested
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import ie.setu.domain.User
import ie.setu.helpers.*
import ie.setu.utils.jsonToObject
import ie.setu.utils.jsonNodeToObject
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HealthTrackerControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    @Nested
    inner class CreateUsers{
        @Test
        fun `add a user with correct details returns a 201 response`() {

            //Arrange & Act & Assert
            //    add the user and verify return code (using fixture data)
            val addResponse = addUser(validName, validEmail)
            assertEquals(201, addResponse.status)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse= retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //Assert - verify the contents of the retrieved user
            val retrievedUser : User = jsonToObject(addResponse.body.toString())
            assertEquals(validEmail, retrievedUser.email)
            assertEquals(validName, retrievedUser.name)

            //After - restore the db to previous state by deleting the added user
            val deleteResponse = deleteUser(retrievedUser.id)
            assertEquals(204, deleteResponse.status)
        }
    }

    @Nested
    inner class UpdateUsers {
        @Test
        fun `updating a user when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do an update on

            val addedResponse = addUser(validName, validEmail)
            val addedUser : User = jsonToObject(addedResponse.body.toString())

            //Act & Assert - update the email and name of the retrieved user and assert 204 is returned
            assertEquals(204, updateUser(addedUser.id, updatedName, updatedEmail).status)

            //Act & Assert - retrieve updated user and assert details are correct
            val updatedUserResponse = retrieveUserById(addedUser.id)
            val updatedUser : User = jsonToObject(updatedUserResponse.body.toString())
            assertEquals(updatedName, updatedUser.name)
            assertEquals(updatedEmail, updatedUser.email)

            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.id)
        }

        @Test
        fun `updating a user when it doesn't exist, returns a 404 response`() {

            //Arrange - creating some text fixture data
            val updatedName = "Updated Name"
            val updatedEmail = "Updated Email"

            //Act & Assert - attempt to update the email and name of user that doesn't exist
            assertEquals(404, updateUser(-1, updatedName, updatedEmail).status)
        }
    }

    @Nested
    inner class ReadUsers {
        @Test
        fun `get all users from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/users/").asString()
            assertEquals(200, response.status)
        }

        @Test
        fun `get user by id when user does not exist returns 404 response`() {

            //Arrange - test data for user id
            val id = Integer.MIN_VALUE

            // Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/users/${id}").asString()

            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `get user by email when user does not exist returns 404 response`() {
            // Arrange & Act - attempt to retrieve the non-existent user from the database
            val retrieveResponse = Unirest.get(origin + "/api/users/email/${nonExistingEmail}").asString()
            // Assert -  verify return code
            assertEquals(404, retrieveResponse.status)
        }

        @Test
        fun `getting a user by id when id exists, returns a 200 response`() {

            //Arrange - add the user
            val addResponse = addUser(validName, validEmail)
            val addedUser : User = jsonToObject(addResponse.body.toString())

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveUserById(addedUser.id)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            deleteUser(addedUser.id)
        }

        @Test
        fun `getting a user by email when email exists, returns a 200 response`() {

            //Arrange - add the user
            addUser(validName, validEmail)

            //Assert - retrieve the added user from the database and verify return code
            val retrieveResponse = retrieveUserByEmail(validEmail)
            assertEquals(200, retrieveResponse.status)

            //After - restore the db to previous state by deleting the added user
            val retrievedUser : User = jsonToObject(retrieveResponse.body.toString())
            deleteUser(retrievedUser.id)
        }

    }

    @Nested
    inner class DeleteUsers {
        @Test
        fun `deleting a user when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteUser(-1).status)
        }

        @Test
        fun `deleting a user when it exists, returns a 204 response`() {

            //Arrange - add the user that we plan to do a delete on
            val addedResponse = addUser(validName, validEmail)
            val addedUser : User = jsonToObject(addedResponse.body.toString())

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted user --> 404 response
            assertEquals(404, retrieveUserById(addedUser.id).status)
        }
    }

    //--------------------------------------------------------------
    // Activities specifics
    //-------------------------------------------------------------

    @Nested
    inner class CreateActivities {

        @Test
        fun `add an activity when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addActivityResponse = addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id
            )
            assertEquals(201, addActivityResponse.status)

            //After - delete the user (Activity will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add an activity when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addActivityResponse = addActivity(
                activities.get(0).description, activities.get(0).duration,
                activities.get(0).calories, activities.get(0).started, userId
            )
            assertEquals(404, addActivityResponse.status)
        }
    }

    @Nested
    inner class UpdateActivities {

        @Test
        fun `updating an activity by activity id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val activityID = -1

            //Arrange - check there is no user for -1 id
            assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an activity/user that doesn't exist
            assertEquals(
                404, updateActivity(
                    activityID, updatedDescription, updatedDuration,
                    updatedCalories, updatedStarted, userId
                ).status
            )
        }

        @Test
        fun `updating an activity by activity id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated activity that we plan to do an update on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addActivityResponse = addActivity(
                activities[0].description,
                activities[0].duration, activities[0].calories,
                activities[0].started, addedUser.id)
            assertEquals(201, addActivityResponse.status)
            val addedActivity = jsonNodeToObject<Activity>(addActivityResponse)

            //Act & Assert - update the added activity and assert a 204 is returned
            val updatedActivityResponse = updateActivity(addedActivity.id, updatedDescription,
                updatedDuration, updatedCalories, updatedStarted, addedUser.id)
            assertEquals(204, updatedActivityResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedActivityResponse = retrieveActivityByActivityId(addedActivity.id)
            val updatedActivity = jsonNodeToObject<Activity>(retrievedActivityResponse)
            assertEquals(updatedDescription,updatedActivity.description)
            assertEquals(updatedDuration, updatedActivity.duration, 0.1)
            assertEquals(updatedCalories, updatedActivity.calories)
            assertEquals(updatedStarted, updatedActivity.started )

            //After - delete the user
            deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class ReadActivities {

        @Test
        fun `get all activities from the database returns 200 or 404 response`() {
            val response = retrieveAllActivities()
            if (response.status == 200){
                val retrievedActivities = jsonNodeToObject<Array<Activity>>(response)
                assertNotEquals(0, retrievedActivities.size)
            }
            else{
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all activities by user id when user and activities exists returns 200 response`() {
            //Arrange - add a user and 3 associated activities that we plan to retrieve
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id)
            addActivity(
                activities[1].description, activities[1].duration,
                activities[1].calories, activities[1].started, addedUser.id)
            addActivity(
                activities[2].description, activities[2].duration,
                activities[2].calories, activities[2].started, addedUser.id)

            //Assert and Act - retrieve the three added activities by user id
            val response = retrieveActivitiesByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedActivities = jsonNodeToObject<Array<Activity>>(response)
            assertEquals(3, retrievedActivities.size)

            //After - delete the added user and assert a 204 is returned (activities are cascade deleted)
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all activities by user id when no activities exist returns 404 response`() {
            //Arrange - add a user
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the activities by user id
            val response = retrieveActivitiesByUserId(addedUser.id)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all activities by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve activities by user id
            val response = retrieveActivitiesByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get activity by activity id when no activity exists returns 404 response`() {
            //Arrange
            val activityId = -1
            //Assert and Act - attempt to retrieve the activity by activity id
            val response = retrieveActivityByActivityId(activityId)
            assertEquals(404, response.status)
        }


        @Test
        fun `get activity by activity id when activity exists returns 200 response`() {
            //Arrange - add a user and associated activity
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addActivityResponse = addActivity(
                activities[0].description,
                activities[0].duration, activities[0].calories,
                activities[0].started, addedUser.id)
            assertEquals(201, addActivityResponse.status)
            val addedActivity = jsonNodeToObject<Activity>(addActivityResponse)

            //Act & Assert - retrieve the activity by activity id
            val response = retrieveActivityByActivityId(addedActivity.id)
            assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class DeleteActivities {

        @Test
        fun `deleting an activity by activity id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteActivityByActivityId(-1).status)
        }

        @Test
        fun `deleting activities by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteActivitiesByUserId(-1).status)
        }

        @Test
        fun `deleting an activity by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated activity that we plan to do a delete on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addActivityResponse = addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id)
            assertEquals(201, addActivityResponse.status)

            //Act & Assert - delete the added activity and assert a 204 is returned
            val addedActivity = jsonNodeToObject<Activity>(addActivityResponse)
            assertEquals(204, deleteActivityByActivityId(addedActivity.id).status)

            //After - delete the user
            deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all activities by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated activities that we plan to do a cascade delete
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addActivityResponse1 = addActivity(
                activities[0].description, activities[0].duration,
                activities[0].calories, activities[0].started, addedUser.id)
            assertEquals(201, addActivityResponse1.status)
            val addActivityResponse2 = addActivity(
                activities[1].description, activities[1].duration,
                activities[1].calories, activities[1].started, addedUser.id)
            assertEquals(201, addActivityResponse2.status)
            val addActivityResponse3 = addActivity(
                activities[2].description, activities[2].duration,
                activities[2].calories, activities[2].started, addedUser.id)
            assertEquals(201, addActivityResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted activities
            val addedActivity1 = jsonNodeToObject<Activity>(addActivityResponse1)
            val addedActivity2 = jsonNodeToObject<Activity>(addActivityResponse2)
            val addedActivity3 = jsonNodeToObject<Activity>(addActivityResponse3)
            assertEquals(404, retrieveActivityByActivityId(addedActivity1.id).status)
            assertEquals(404, retrieveActivityByActivityId(addedActivity2.id).status)
            assertEquals(404, retrieveActivityByActivityId(addedActivity3.id).status)
        }
    }

    //--------------------------------------------------------------
    // Fitness Goal specifics
    //--------------------------------------------------------------

    @Nested
    inner class CreateFitnessGoals {

        @Test
        fun `add an fitness goal when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated fitness goal that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addFitnessGoalResponse = addFitnessGoal(
                fitnessGoals[0].goal, fitnessGoals[0].duration, fitnessGoals[0].target,
                fitnessGoals[0].status, fitnessGoals[0].started, fitnessGoals[0].ended, addedUser.id
            )
            assertEquals(201, addFitnessGoalResponse.status)

            //After - delete the user (FitnessGoal will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add an fitness goal when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addFitnessGoalResponse = addFitnessGoal(
                fitnessGoals.get(0).goal, fitnessGoals.get(0).duration,fitnessGoals.get(0).target,
                fitnessGoals.get(0).status, fitnessGoals.get(0).started,fitnessGoals.get(0).ended, userId
            )
            assertEquals(404, addFitnessGoalResponse.status)
        }
    }

    @Nested
    inner class UpdateFitnessGoals {

        @Test
        fun `updating an fitness goal by fitness id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val fitnessGoalID = -1

            //Arrange - check there is no user for -1 id
            assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of a fitnessGoal/user that doesn't exist
            assertEquals(
                404, updateFitnessGoal(
                    fitnessGoalID, updatedGoal, updatedDuration, updatedTarget,
                    updatedStatus, updatedStarted, updatedEnded, userId
                ).status
            )
        }

        @Test
        fun `updating an fitness goal by fitness id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated fitness Goal that we plan to do an update on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addFitnessGoalResponse = addFitnessGoal(
                fitnessGoals[0].goal, fitnessGoals[0].duration,
                fitnessGoals[0].target, fitnessGoals[0].status,
                fitnessGoals[0].started,fitnessGoals[0].ended, addedUser.id)
            assertEquals(201, addFitnessGoalResponse.status)
            val addedFitnessGoal = jsonNodeToObject<FitnessGoal>(addFitnessGoalResponse)

            //Act & Assert - update the added fitness goal and assert a 204 is returned
            val updatedFitnessGoalResponse = updateFitnessGoal(addedFitnessGoal.id, updatedGoal,
                updatedDuration, updatedTarget, updatedStatus, updatedStarted, updatedEnded, addedUser.id)
            assertEquals(204, updatedFitnessGoalResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedFitnessGoalResponse = retrieveFitnessGoalByFitnessGoalId(addedFitnessGoal.id)
            val updatedFitnessGoal = jsonNodeToObject<FitnessGoal>(retrievedFitnessGoalResponse)
            assertEquals(updatedGoal,updatedFitnessGoal.goal)
            assertEquals(updatedDuration, updatedFitnessGoal.duration, 0.1)
            assertEquals(updatedTarget, updatedFitnessGoal.target)
            assertEquals(updatedStatus, updatedFitnessGoal.status)
            assertEquals(updatedStarted, updatedFitnessGoal.started)
            assertEquals(updatedEnded, updatedFitnessGoal.ended )

            //After - delete the user
            deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class ReadFitnessGoals {

        @Test
        fun `get all fitness goals from the database returns 200 or 404 response`() {
            val response = retrieveAllFitnessGoals()
            if (response.status == 200){
                val retrievedFitnessGoals = jsonNodeToObject<Array<FitnessGoal>>(response)
                assertNotEquals(0, retrievedFitnessGoals.size)
            }
            else{
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all fitness goals by user id when user and fitnessGoals exists returns 200 response`() {
            //Arrange - add a user and 3 associated fitnessGoals that we plan to retrieve
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            addFitnessGoal(
                fitnessGoals[0].goal, fitnessGoals[0].duration, fitnessGoals[0].target,
                fitnessGoals[0].status, fitnessGoals[0].started,fitnessGoals[0].ended, addedUser.id)
            addFitnessGoal(
                fitnessGoals[1].goal, fitnessGoals[1].duration, fitnessGoals[1].target,
                fitnessGoals[1].status, fitnessGoals[1].started,fitnessGoals[1].ended, addedUser.id)
            addFitnessGoal(
                fitnessGoals[2].goal, fitnessGoals[2].duration, fitnessGoals[2].target,
                fitnessGoals[2].status, fitnessGoals[2].started,fitnessGoals[2].ended, addedUser.id)

            //Assert and Act - retrieve the three added fitness goals by user id
            val response = retrieveFitnessGoalsByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedFitnessGoals = jsonNodeToObject<Array<FitnessGoal>>(response)
            assertEquals(3, retrievedFitnessGoals.size)

            //After - delete the added user and assert a 204 is returned (fitness goals are cascade deleted)
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all fitness goals by user id when no fitness goals exist returns 404 response`() {
            //Arrange - add a user
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the fitness goals by user id
            val response = retrieveFitnessGoalsByUserId(addedUser.id)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all fitness goals by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve fitness goals by user id
            val response = retrieveFitnessGoalsByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get fitness goal by fitness goal id when no fitness goal exists returns 404 response`() {
            //Arrange
            val fitnessGoalId = -1
            //Assert and Act - attempt to retrieve the fitness goal by fitness goal id
            val response = retrieveFitnessGoalByFitnessGoalId(fitnessGoalId)
            assertEquals(404, response.status)
        }


        @Test
        fun `get fitness goal by fitness goal id when fitness goal exists returns 200 response`() {
            //Arrange - add a user and associated fitness goal
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addFitnessGoalResponse = addFitnessGoal(
                fitnessGoals[0].goal,
                fitnessGoals[0].duration, fitnessGoals[0].target,
                fitnessGoals[0].status, fitnessGoals[0].started,
                fitnessGoals[0].ended, addedUser.id)
            assertEquals(201, addFitnessGoalResponse.status)
            val addedFitnessGoal = jsonNodeToObject<FitnessGoal>(addFitnessGoalResponse)

            //Act & Assert - retrieve the fitness goal by FitnessGoal id
            val response = retrieveFitnessGoalByFitnessGoalId(addedFitnessGoal.id)
            assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class DeleteFitnessGoals {

        @Test
        fun `deleting an fitness goal by fitness goal id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteFitnessGoalByFitnessGoalId(-1).status)
        }

        @Test
        fun `deleting fitness goals by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteFitnessGoalsByUserId(-1).status)
        }

        @Test
        fun `deleting an fitness goal by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated fitness goal that we plan to do a delete on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addFitnessGoalResponse = addFitnessGoal(
                fitnessGoals[0].goal, fitnessGoals[0].duration, fitnessGoals[0].target ,
                fitnessGoals[0].status, fitnessGoals[0].started, fitnessGoals[0].ended, addedUser.id)
            assertEquals(201, addFitnessGoalResponse.status)

            //Act & Assert - delete the added fitness goal and assert a 204 is returned
            val addedFitnessGoal = jsonNodeToObject<FitnessGoal>(addFitnessGoalResponse)
            assertEquals(204, deleteFitnessGoalByFitnessGoalId(addedFitnessGoal.id).status)

            //After - delete the user
            deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all fitness goals by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated fitness goals that we plan to do a cascade delete
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addFitnessGoalResponse1 = addFitnessGoal(
                fitnessGoals[0].goal, fitnessGoals[0].duration, fitnessGoals[0].target,
                fitnessGoals[0].status, fitnessGoals[0].started, fitnessGoals[0].ended, addedUser.id)
            assertEquals(201, addFitnessGoalResponse1.status)
            val addFitnessGoalResponse2 = addFitnessGoal(
                fitnessGoals[1].goal, fitnessGoals[1].duration, fitnessGoals[1].target,
                fitnessGoals[1].status, fitnessGoals[1].started, fitnessGoals[1].ended, addedUser.id)
            assertEquals(201, addFitnessGoalResponse2.status)
            val addFitnessGoalResponse3 = addFitnessGoal(
                fitnessGoals[2].goal, fitnessGoals[2].duration, fitnessGoals[2].target,
                fitnessGoals[2].status, fitnessGoals[2].started, fitnessGoals[2].ended, addedUser.id)
            assertEquals(201, addFitnessGoalResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted fitness goals
            val addedFitnessGoal1 = jsonNodeToObject<FitnessGoal>(addFitnessGoalResponse1)
            val addedFitnessGoal2 = jsonNodeToObject<FitnessGoal>(addFitnessGoalResponse2)
            val addedFitnessGoal3 = jsonNodeToObject<FitnessGoal>(addFitnessGoalResponse3)
            assertEquals(404, retrieveFitnessGoalByFitnessGoalId(addedFitnessGoal1.id).status)
            assertEquals(404, retrieveFitnessGoalByFitnessGoalId(addedFitnessGoal2.id).status)
            assertEquals(404, retrieveFitnessGoalByFitnessGoalId(addedFitnessGoal3.id).status)
        }
    }

    //--------------------------------------------------------------
    // Image specifics
    //--------------------------------------------------------------

    @Nested
    inner class CreateImages {

        @Test
        fun `add an image when a user exists for it, returns a 201 response`() {

            //Arrange - add a user and an associated image that we plan to do a delete on
            val addedUser: User = jsonToObject(addUser(validName, validEmail).body.toString())

            val addImageResponse = addImage(
                images[0].title, images[0].description,
                images[0].image_file_path, addedUser.id
            )
            assertEquals(201, addImageResponse.status)

            //After - delete the user (Image will cascade delete in the database)
            deleteUser(addedUser.id)
        }

        @Test
        fun `add an image when no user exists for it, returns a 404 response`() {

            //Arrange - check there is no user for -1 id
            val userId = -1
            assertEquals(404, retrieveUserById(userId).status)

            val addImageResponse = addImage(
                images.get(0).title, images.get(0).description,
                images.get(0).image_file_path, userId
            )
            assertEquals(404, addImageResponse.status)
        }
    }

    @Nested
    inner class UpdateImages {

        @Test
        fun `updating an image by image id when it doesn't exist, returns a 404 response`() {
            val userId = -1
            val imageID = -1

            //Arrange - check there is no user for -1 id
            assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the details of an image/user that doesn't exist
            assertEquals(
                404, updateImage(
                    imageID, updatedTitle, updatedDescription,
                    updatedImagefilepath, userId
                ).status
            )
        }

        @Test
        fun `updating an image by image id when it exists, returns 204 response`() {

            //Arrange - add a user and an associated image that we plan to do an update on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addImageResponse = addImage(
                images[0].title,
                images[0].description, images[0].image_file_path, addedUser.id)
            assertEquals(201, addImageResponse.status)
            val addedImage = jsonNodeToObject<Image>(addImageResponse)

            //Act & Assert - update the added image and assert a 204 is returned
            val updatedImageResponse = updateImage(addedImage.id, updatedTitle,
                updatedDescription, updatedImagefilepath, addedUser.id)
            assertEquals(204, updatedImageResponse.status)

            //Assert that the individual fields were all updated as expected
            val retrievedImageResponse = retrieveImageByImageId(addedImage.id)
            val updatedImage = jsonNodeToObject<Image>(retrievedImageResponse)
            assertEquals(updatedTitle,updatedImage.title)
            assertEquals(updatedDescription,updatedImage.description)
            assertEquals(updatedImagefilepath, updatedImage.image_file_path)
            //After - delete the user
            deleteUser(addedUser.id)
        }
    }

    @Nested
    inner class ReadImages {

        @Test
        fun `get all images from the database returns 200 or 404 response`() {
            val response = retrieveAllImages()
            if (response.status == 200){
                val retrievedImages = jsonNodeToObject<Array<Image>>(response)
                assertNotEquals(0, retrievedImages.size)
            }
            else{
                assertEquals(404, response.status)
            }
        }

        @Test
        fun `get all images by user id when user and images exists returns 200 response`() {
            //Arrange - add a user and 3 associated images that we plan to retrieve
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            addImage(
                images[0].title, images[0].description,
                images[0].image_file_path, addedUser.id)
            addImage(
                images[1].title, images[1].description,
                images[1].image_file_path, addedUser.id)
            addImage(
                images[2].title, images[2].description,
                images[2].image_file_path, addedUser.id)

            //Assert and Act - retrieve the three added images by user id
            val response = retrieveImagesByUserId(addedUser.id)
            assertEquals(200, response.status)
            val retrievedImages = jsonNodeToObject<Array<Image>>(response)
            assertEquals(3, retrievedImages.size)

            //After - delete the added user and assert a 204 is returned (images are cascade deleted)
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all images by user id when no images exist returns 404 response`() {
            //Arrange - add a user
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())

            //Assert and Act - retrieve the images by user id
            val response = retrieveImagesByUserId(addedUser.id)
            assertEquals(404, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

        @Test
        fun `get all images by user id when no user exists returns 404 response`() {
            //Arrange
            val userId = -1

            //Assert and Act - retrieve images by user id
            val response = retrieveImagesByUserId(userId)
            assertEquals(404, response.status)
        }

        @Test
        fun `get image by image id when no image exists returns 404 response`() {
            //Arrange
            val imageId = -1
            //Assert and Act - attempt to retrieve the image by image id
            val response = retrieveImageByImageId(imageId)
            assertEquals(404, response.status)
        }


        @Test
        fun `get image by image id when image exists returns 200 response`() {
            //Arrange - add a user and associated image
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addImageResponse = addImage(
                images[0].title,
                images[0].description,
                images[0].image_file_path,
                addedUser.id)
            assertEquals(201, addImageResponse.status)
            val addedImage = jsonNodeToObject<Image>(addImageResponse)

            //Act & Assert - retrieve the image by image id
            val response = retrieveImageByImageId(addedImage.id)
            assertEquals(200, response.status)

            //After - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)
        }

    }

    @Nested
    inner class DeleteImages {

        @Test
        fun `deleting an image by image id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteImageByImageId(-1).status)
        }

        @Test
        fun `deleting images by user id when it doesn't exist, returns a 404 response`() {
            //Act & Assert - attempt to delete a user that doesn't exist
            assertEquals(404, deleteImagesByUserId(-1).status)
        }

        @Test
        fun `deleting an image by id when it exists, returns a 204 response`() {

            //Arrange - add a user and an associated image that we plan to do a delete on
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addImageResponse = addImage(
                                   images[0].title,
                                   images[0].description,
                                   images[0].image_file_path,
                                   addedUser.id)
            assertEquals(201, addImageResponse.status)

            //Act & Assert - delete the added image and assert a 204 is returned
            val addedImage = jsonNodeToObject<Image>(addImageResponse)
            assertEquals(204, deleteImageByImageId(addedImage.id).status)

            //After - delete the user
            deleteUser(addedUser.id)
        }

        @Test
        fun `deleting all images by userid when it exists, returns a 204 response`() {

            //Arrange - add a user and 3 associated images that we plan to do a cascade delete
            val addedUser : User = jsonToObject(addUser(validName, validEmail).body.toString())
            val addImageResponse1 = addImage(
                images[0].title,
                images[0].description,
                images[0].image_file_path,
                addedUser.id)
            assertEquals(201, addImageResponse1.status)
            val addImageResponse2 = addImage(
                images[1].title,
                images[1].description,
                images[1].image_file_path,
                addedUser.id)
            assertEquals(201, addImageResponse2.status)
            val addImageResponse3 = addImage(
                images[2].title,
                images[2].description,
                images[2].image_file_path,
                addedUser.id)
            assertEquals(201, addImageResponse3.status)

            //Act & Assert - delete the added user and assert a 204 is returned
            assertEquals(204, deleteUser(addedUser.id).status)

            //Act & Assert - attempt to retrieve the deleted images
            val addedImage1 = jsonNodeToObject<Image>(addImageResponse1)
            val addedImage2 = jsonNodeToObject<Image>(addImageResponse2)
            val addedImage3 = jsonNodeToObject<Image>(addImageResponse3)
            assertEquals(404, retrieveImageByImageId(addedImage1.id).status)
            assertEquals(404, retrieveImageByImageId(addedImage2.id).status)
            assertEquals(404, retrieveImageByImageId(addedImage3.id).status)
        }
    }

    //helper function to add a test user to the database
    private fun addUser (name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/users")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }

    //helper function to delete a test user from the database
    private fun deleteUser (id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/$id").asString()
    }

    //helper function to retrieve a test user from the database by email
    private fun retrieveUserByEmail(email : String) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/email/${email}").asString()
    }

    //helper function to retrieve a test user from the database by id
    private fun retrieveUserById(id: Int) : HttpResponse<String> {
        return Unirest.get(origin + "/api/users/${id}").asString()
    }

    //helper function to add a test user to the database
    private fun updateUser (id: Int, name: String, email: String): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/users/$id")
            .body("{\"name\":\"$name\", \"email\":\"$email\"}")
            .asJson()
    }

    //helper function to add an activity
    private fun addActivity(description: String, duration: Double, calories: Int,
                            started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/activities")
            .body("""
                {
                   "description":"$description",
                   "duration":$duration,
                   "calories":$calories,
                   "started":"$started",
                   "userId":$userId
                }
            """.trimIndent())
            .asJson()
    }

    //helper function to add a test user to the database
    private fun updateActivity(id: Int, description: String, duration: Double, calories: Int,
                               started: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/activities/$id")
            .body("""
                {
                  "description":"$description",
                  "duration":$duration,
                  "calories":$calories,
                  "started":"$started",
                  "userId":$userId
                }
            """.trimIndent()).asJson()
    }

    private fun retrieveActivityByActivityId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/activities/${id}").asJson()
    }

    //helper function to retrieve all activities
    private fun retrieveAllActivities(): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/activities").asJson()
    }

    //helper function to retrieve activities by user id
    private fun retrieveActivitiesByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/activities").asJson()
    }

    //helper function to delete an activity by activity id
    private fun deleteActivityByActivityId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/activities/$id").asString()
    }

    //helper function to delete an activity by activity id
    private fun deleteActivitiesByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/$id/activities").asString()
    }

    private fun addFitnessGoal(goal: String, duration: Double, target: String,  status: String,
                            started: DateTime, ended: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/fitness")
            .body("""
                {
                   "goal":"$goal",
                   "duration":$duration,
                   "target":"$target",
                   "status":$status,
                   "started":"$started",
                   "ended":"$ended",
                   "userId":$userId
                }
            """.trimIndent())
            .asJson()
    }

    private fun updateFitnessGoal(id: Int, goal: String, duration: Double, target: String, status: String,
        started: DateTime, ended: DateTime, userId: Int
    ): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/fitness/$id")
            .body("""
                {
                  "goal":"$goal",
                  "duration":$duration,
                  "target":"$target",
                  "status":"$status",
                  "started":"$started",
                  "ended":"$ended",
                  "userId":$userId
                }
            """.trimIndent()).asJson()
    }

    private fun retrieveFitnessGoalByFitnessGoalId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/fitness/${id}").asJson()
    }

    private fun retrieveAllFitnessGoals(): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/fitness").asJson()
    }

    private fun retrieveFitnessGoalsByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/fitness").asJson()
    }

    private fun deleteFitnessGoalsByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/$id/fitness").asString()
    }

    private fun deleteFitnessGoalByFitnessGoalId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/fitness/$id").asString()
    }

    private fun addImage(title: String, description: String, image_file_path: String, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post(origin + "/api/images")
            .body("""
                {
                   "title":"$title",
                   "description":"$description",
                   "image_file_path":"$image_file_path",
                   "userId":$userId
                }
            """.trimIndent())
            .asJson()
    }

    private fun updateImage(id: Int, title: String, description: String, image_file_path: String, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/images/$id")
            .body("""
                {
                  "title":"$title",
                  "description":"$description",
                  "image_file_path":"$image_file_path",
                  "userId":$userId
                }
            """.trimIndent()).asJson()
    }

    private fun retrieveImageByImageId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/images/${id}").asJson()
    }

    private fun retrieveAllImages(): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/images").asJson()
    }

    private fun retrieveImagesByUserId(id: Int): HttpResponse<JsonNode> {
        return Unirest.get(origin + "/api/users/${id}/images").asJson()
    }

    private fun deleteImageByImageId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/images/$id").asString()
    }

    private fun deleteImagesByUserId(id: Int): HttpResponse<String> {
        return Unirest.delete(origin + "/api/users/$id/images").asString()
    }

}