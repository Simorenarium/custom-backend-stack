package coffee.michel.usermanager.persistence

import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.persistence.tables.UserGroupTable

interface UserGroupPersistence {
    companion object {
        fun instance() = UserGroupTable
    }

    fun findAll(): Sequence<UserGroup>
    fun findById(id: Int): UserGroup?
    fun create(subject: UserGroup): UserGroup
    fun update(subject: UserGroup)
    fun delete(id: Int)
}