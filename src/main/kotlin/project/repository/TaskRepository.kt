package project.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.model.Task

@Repository
interface TaskRepository : JpaRepository<Task, Long>{
}