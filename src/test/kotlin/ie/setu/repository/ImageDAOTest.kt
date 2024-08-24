package ie.setu.repository

import ie.setu.domain.Image
import ie.setu.domain.db.Images
import ie.setu.helpers.images
import ie.setu.domain.repository.ImageDAO
import ie.setu.helpers.populateImageTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private val image1 = images.get(0)
private val image2 = images.get(1)
private val image3 = images.get(2)

class ImageDAOTest {
    companion object{
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }

    @Nested
    inner class CreateImages {

        @Test
        fun `multiple images added to table can be retrieved successfully`() {
            transaction {
                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageDAO = populateImageTable()
                //Act & Assert
                assertEquals(3, imageDAO.getAll().size)
                assertEquals(image1, imageDAO.findByImageId(image1.id))
                assertEquals(image2, imageDAO.findByImageId(image2.id))
                assertEquals(image3, imageDAO.findByImageId(image3.id))
            }
        }
    }

    @Nested
    inner class ReadImages {

        @Test
        fun `getting all images from a populated table returns all rows`() {
            transaction {
                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageDAO = populateImageTable()
                //Act & Assert
                assertEquals(3, imageDAO.getAll().size)
            }
        }

        @Test
        fun `get image by user id that has no images, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageDAO = populateImageTable()
                //Act & Assert
                assertEquals(0, imageDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get image by user id that exists, results in a correct image(s) returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageDAO = populateImageTable()
                //Act & Assert
                assertEquals(image1, imageDAO.findByUserId(1).get(0))
                assertEquals(image2, imageDAO.findByUserId(1).get(1))
                assertEquals(image3, imageDAO.findByUserId(2).get(0))
            }
        }

        @Test
        fun `get all images over empty table returns none`() {
            transaction {

                //Arrange - create and setup imageDAO object
                SchemaUtils.create(Images)
                val imageDAO = ImageDAO()

                //Act & Assert
                assertEquals(0, imageDAO.getAll().size)
            }
        }

        @Test
        fun `get image by image id that has no records, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageDAO = populateImageTable()
                //Act & Assert
                assertEquals(null, imageDAO.findByImageId(4))
            }
        }

        @Test
        fun `get image by image id that exists, results in a correct image returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageDAO = populateImageTable()
                //Act & Assert
                assertEquals(image1, imageDAO.findByImageId(1))
                assertEquals(image3, imageDAO.findByImageId(3))
            }
        }
    }

    @Nested
    inner class UpdateImages {

        @Test
        fun `updating existing image in table results in successful update`() {
            transaction {

                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageDAO = populateImageTable()

                //Act & Assert
                val image3updated = Image(id = 3, title = "Running", description = "Running Image",
                    image_file_path = "Downloads/fffff.jpg", userId = 2)
                imageDAO.updateByImageId(image3updated.id, image3updated)
                assertEquals(image3updated, imageDAO.findByImageId(3))
            }
        }

        @Test
        fun `updating non-existant image in table results in no updates`() {
            transaction {

                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageDAO = populateImageTable()

                //Act & Assert
                val image4updated = Image(id = 4, title = "Running", description = "Running description",
                    image_file_path = "Downloads/fffff.jpg", userId = 2)
                imageDAO.updateByImageId(4, image4updated)
                assertEquals(null, imageDAO.findByImageId(4))
                assertEquals(3, imageDAO.getAll().size)
            }
        }
    }

    @Nested
    inner class DeleteImages {

        @Test
        fun `deleting a non-existant image (by id) in table results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageDAO = populateImageTable()

                //Act & Assert
                assertEquals(3, imageDAO.getAll().size)
                imageDAO.deleteByImageId(4)
                assertEquals(3, imageDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing image (by id) in table results in record being deleted`() {
            transaction {

                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageDAO = populateImageTable()

                //Act & Assert
                assertEquals(3, imageDAO.getAll().size)
                imageDAO.deleteByImageId(image3.id)
                assertEquals(2, imageDAO.getAll().size)
            }
        }


        @Test
        fun `deleting images when none exist for user id results in no deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageDAO = populateImageTable()

                //Act & Assert
                assertEquals(3, imageDAO.getAll().size)
                imageDAO.deleteByUserId(3)
                assertEquals(3, imageDAO.getAll().size)
            }
        }

        @Test
        fun `deleting images when 1 or more exist for user id results in deletion`() {
            transaction {

                //Arrange - create and populate tables with three users and three images
                val userDAO = populateUserTable()
                val imageDAO = populateImageTable()

                //Act & Assert
                assertEquals(3, imageDAO.getAll().size)
                imageDAO.deleteByUserId(1)
                assertEquals(1, imageDAO.getAll().size)
            }
        }
    }
}