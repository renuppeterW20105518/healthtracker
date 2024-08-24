package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.FitnessGoal
import ie.setu.domain.Image
import ie.setu.domain.User
import ie.setu.domain.db.Activities
import ie.setu.domain.db.FitnessGoals
import ie.setu.domain.db.Images
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.FitnessGoalDAO
import ie.setu.domain.repository.ImageDAO
import ie.setu.domain.repository.UserDAO
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val updatedName = "Updated Name"
val updatedEmail = "Updated Email"

val updatedDescription = "Updated Description"
val updatedDuration = 0.00;
val updatedCalories = -1;
val updatedStarted = DateTime.parse("2023-12-27T14:45:11.087Z");

val updatedGoal = "Updated Goal"
val updatedTarget = "Updated Target";
val updatedStatus = "Updated Status";
val updatedEnded = DateTime.parse("2023-12-27T14:45:11.087Z");

val updatedTitle = "Updated title"
val updatedImagefilepath = "Updated image file path";


val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4)
)

val activities = arrayListOf<Activity>(
    Activity(id = 1, description = "Running", duration = 22.0, calories = 230, started = DateTime.now(), userId = 1),
    Activity(id = 2, description = "Hopping", duration = 10.5, calories = 80, started = DateTime.now(), userId = 1),
    Activity(id = 3, description = "Walking", duration = 12.0, calories = 120, started = DateTime.now(), userId = 2)
)

val fitnessGoals = arrayListOf<FitnessGoal>(
    FitnessGoal(id = 1, goal = "Running", duration = 22.0, target = "increase speed", status = "incomplete", started = DateTime.now(), ended = DateTime.now(), userId = 1),
    FitnessGoal(id = 2, goal = "Hopping", duration = 10.5, target = "reduce weight", status = "incomplete", started = DateTime.now(), ended = DateTime.now(), userId = 1),
    FitnessGoal(id = 3, goal = "Walking", duration = 12.0, target = "boost energy", status = "incomplete", started = DateTime.now(), ended = DateTime.now(), userId = 2)
)

val images = arrayListOf<Image>(
    Image(id = 1, title = "Jogging", description = "Jogging Image", image_file_path = "Downloads/fffff.jpg", userId = 1),
    Image(id = 2, title = "Swimming", description = "Swimming Image", image_file_path = "Downloads/fffff.jpg", userId = 1),
    Image(id = 3, title = "Skipping", description = "Skipping Image", image_file_path = "Downloads/fffff.jpg", userId = 2)
)

fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activities.get(0))
    activityDAO.save(activities.get(1))
    activityDAO.save(activities.get(2))
    return activityDAO
}

fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(users.get(0))
    userDAO.save(users.get(1))
    userDAO.save(users.get(2))
    return userDAO
}

fun populateFitnessGoalTable(): FitnessGoalDAO {
    SchemaUtils.create(FitnessGoals)
    val fitnessGoalDAO = FitnessGoalDAO()
    fitnessGoalDAO.save(fitnessGoals.get(0))
    fitnessGoalDAO.save(fitnessGoals.get(1))
    fitnessGoalDAO.save(fitnessGoals.get(2))
    return fitnessGoalDAO
}

fun populateImageTable(): ImageDAO {
    SchemaUtils.create(Images)
    val imageDAO = ImageDAO()
    imageDAO.save(images.get(0))
    imageDAO.save(images.get(1))
    imageDAO.save(images.get(2))
    return imageDAO
}