package ie.setu.domain.repository


import ie.setu.domain.FitnessGoal
import ie.setu.domain.db.FitnessGoals
import ie.setu.utils.mapToFitnessGoal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class FitnessGoalDAO{
    fun getAll(): ArrayList<FitnessGoal> {
        val fitnessGoalsList: ArrayList<FitnessGoal> = arrayListOf()
        transaction {
            FitnessGoals.selectAll().map {
                fitnessGoalsList.add(mapToFitnessGoal(it)) }
        }
        return fitnessGoalsList
    }

    fun findByFitnessId(id: Int): FitnessGoal?{
        return transaction {
            FitnessGoals
                .select() { FitnessGoals.id eq id}
                .map{ mapToFitnessGoal(it) }
                .firstOrNull()
        }
    }

    fun findByUserId(userId: Int): List<FitnessGoal>{
        return transaction {
            FitnessGoals
                .select {FitnessGoals.userId eq userId}
                .map {mapToFitnessGoal(it)}
        }
    }

    fun save(fitnessGoal: FitnessGoal): Int{
        return transaction {
            FitnessGoals.insert {
                it[goal] = fitnessGoal.goal
                it[duration] = fitnessGoal.duration
                it[target] = fitnessGoal.target
                it[status] = fitnessGoal.status
                it[started] = fitnessGoal.started
                it[ended] = fitnessGoal.ended
                it[userId] = fitnessGoal.userId
            }
        }get FitnessGoals.id
    }

    fun updateByFitnessId(fitnessId: Int, fitnessToUpdate: FitnessGoal): Int{
        return transaction {
            FitnessGoals.update ({
                FitnessGoals.id eq fitnessId}) {
                it[goal] = fitnessToUpdate.goal
                it[duration] = fitnessToUpdate.duration
                it[target] = fitnessToUpdate.target
                it[status] = fitnessToUpdate.status
                it[started] = fitnessToUpdate.started
                it[ended] = fitnessToUpdate.ended
                it[userId] = fitnessToUpdate.userId
            }
        }
    }

    fun deleteByFitnessId (fitnessId: Int): Int{
        return transaction{
            FitnessGoals.deleteWhere { FitnessGoals.id eq fitnessId }
        }
    }

    fun deleteByUserId (userId: Int): Int{
        return transaction{
            FitnessGoals.deleteWhere { FitnessGoals.userId eq userId }
        }
    }
}
