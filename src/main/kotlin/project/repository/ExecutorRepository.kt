package project.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import project.model.Executor

@Repository
interface ExecutorRepository : JpaRepository<Executor, Long>
