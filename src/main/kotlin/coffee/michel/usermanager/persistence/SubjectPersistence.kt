package coffee.michel.usermanager.persistence

import coffee.michel.usermanager.domain.Subject
import coffee.michel.usermanager.persistence.tables.SubjectTable

interface SubjectPersistence {
    companion object {
        fun instance() = SubjectTable
    }

    fun findAll(): Sequence<Subject>
    fun findById(id: Int): Subject?
    fun create(subject: Subject): Subject
    fun update(subject: Subject)
    fun delete(id: Int)
}