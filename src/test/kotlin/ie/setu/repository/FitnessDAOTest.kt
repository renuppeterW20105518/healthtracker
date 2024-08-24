package ie.setu.repository

import ie.setu.domain.FitnessGoal
import ie.setu.domain.db.FitnessGoals
import ie.setu.domain.repository.FitnessGoalDAO
import ie.setu.helpers.fitnessGoals
import ie.setu.helpers.populateFitnessGoalTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val fitnessGoal1 = fitnessGoals.get(0)
private val fitnessGoal2 = fitnessGoals.get(1)
private val fitnessGoal3 = fitnessGoals.get(2)
class FitnessDAOTest {

    companion object{
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateFitnessGoals {

        @Test
        fun `multiple fitness goals added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessGoalDAO = populateFitnessGoalTable()
                //Act & Assert
                assertEquals(3, fitnessGoalDAO.getAll().size)
                assertEquals(fitnessGoal1, fitnessGoalDAO.findByFitnessId(fitnessGoal1.id))
                assertEquals(fitnessGoal2, fitnessGoalDAO.findByFitnessId(fitnessGoal2.id))
                assertEquals(fitnessGoal3, fitnessGoalDAO.findByFitnessId(fitnessGoal3.id))
            }
        }
    }

    @Nested
    inner class ReadFitnessGoals {

        @Test
        fun `getting all fitness goals from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessGoalDAO = populateFitnessGoalTable()
                //Act & Assert
                assertEquals(3, fitnessGoalDAO.getAll().size)
            }
        }

        @Test
        fun `get fitness goal by user id that has no fitness goals, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessGoalDAO = populateFitnessGoalTable()
                //Act & Assert
                assertEquals(0, fitnessGoalDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get fitness goal by user id that exists, results in a correct fitness goals(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessGoalDAO = populateFitnessGoalTable()
                //Act & Assert
                assertEquals(fitnessGoal1, fitnessGoalDAO.findByUserId(1).get(0))
                assertEquals(fitnessGoal2, fitnessGoalDAO.findByUserId(1).get(1))
                assertEquals(fitnessGoal3, fitnessGoalDAO.findByUserId(2).get(0))
            }
        }

        @Test
        fun `get all fitness goals over empty table returns none`() {
            transaction {

                //Arrange - create and setup fitnessGoalDAO object
                SchemaUtils.create(FitnessGoals)
                val fitnessGoalDAO = FitnessGoalDAO()

                //Act & Assert
                assertEquals(0, fitnessGoalDAO.getAll().size)
            }
        }

        @Test
        fun `get fitness goal by fitness goals id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessGoalDAO = populateFitnessGoalTable()
                //Act & Assert
                assertEquals(null, fitnessGoalDAO.findByFitnessId(4))
            }
        }

        @Test
        fun `get fitness goal by fitness goal id that exists, results in a correct fitness goal returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessGoalDAO = populateFitnessGoalTable()
                //Act & Assert
                assertEquals(fitnessGoal1, fitnessGoalDAO.findByFitnessId(1))
                assertEquals(fitnessGoal3, fitnessGoalDAO.findByFitnessId(3))
            }
        }
    }

    @Nested
    inner class UpdateFitnessGoals {

        @Test
        fun `updating existing fitness goal in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessGoalDAO = populateFitnessGoalTable()

                //Act & Assert
                val fitnessGoal3updated = FitnessGoal(id = 3, goal = "Weight loss", duration = 42.0,
                    target = "60", status = "incomplete", started = DateTime.now(), ended = DateTime.now(), userId = 2)
                fitnessGoalDAO.updateByFitnessId(fitnessGoal3updated.id, fitnessGoal3updated)
                assertEquals(fitnessGoal3updated, fitnessGoalDAO.findByFitnessId(3))
            }
        }

        @Test
        fun `updating non-existant fitness goal in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessGoalDAO = populateFitnessGoalTable()

                //Act & Assert
                val fitnessGoal4updated = FitnessGoal(id = 4, goal = "Cardio", duration = 42.0, target = "target",
                    status = "incomplete", started = DateTime.now(), ended = DateTime.now(), userId = 2)
                fitnessGoalDAO.updateByFitnessId(4, fitnessGoal4updated)
                assertEquals(null, fitnessGoalDAO.findByFitnessId(4))
                assertEquals(3, fitnessGoalDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteFitnessGoals {

        @Test
        fun `deleting a non-existant fitness goal (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessGoalDAO = populateFitnessGoalTable()

                //Act & Assert
                assertEquals(3, fitnessGoalDAO.getAll().size)
                fitnessGoalDAO.deleteByFitnessId(4)
                assertEquals(3, fitnessGoalDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing fitness goal (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessGoalDAO = populateFitnessGoalTable()

                //Act & Assert
                assertEquals(3, fitnessGoalDAO.getAll().size)
                fitnessGoalDAO.deleteByFitnessId(fitnessGoal3.id)
                assertEquals(2, fitnessGoalDAO.getAll().size)
            }
        }


        @Test
        fun `deleting fitness goals when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessGoalDAO = populateFitnessGoalTable()

                //Act & Assert
                assertEquals(3, fitnessGoalDAO.getAll().size)
                fitnessGoalDAO.deleteByUserId(3)
                assertEquals(3, fitnessGoalDAO.getAll().size)
            }
        }

        @Test
        fun `deleting fitness goals when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three fitness goals
                val userDAO = populateUserTable()
                val fitnessGoalDAO = populateFitnessGoalTable()

                //Act & Assert
                assertEquals(3, fitnessGoalDAO.getAll().size)
                fitnessGoalDAO.deleteByUserId(1)
                assertEquals(1, fitnessGoalDAO.getAll().size)
            }
        }
    }
}