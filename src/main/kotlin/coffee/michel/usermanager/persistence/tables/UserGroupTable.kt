package coffee.michel.usermanager.persistence.tables

import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.persistence.UserGroupPersistence
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAllBatched
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object UserGroupTable : IntIdTable("user_group"), UserGroupPersistence {
    val name = varchar("name", 255)

    override fun findAll(): Sequence<UserGroup> =
        selectAllBatched().asSequence()
            .flatten()
            .map { UserGroup(it[id].value, it[name]) }

    override fun findById(id: Int): UserGroup? =
        select(UserGroupTable.id eq id)
            .map { UserGroup(it[UserGroupTable.id].value, it[name]) }
            .firstOrNull()

    override fun create(subject: UserGroup): UserGroup = transaction {
        insert { it[name] = subject.name }
            .let { UserGroup(it[UserGroupTable.id].value, it[name]) }
    }

    override fun update(subject: UserGroup) {
        transaction {
            update(where = { UserGroupTable.id eq subject.id!! }, body = {
                it[name] = subject.name
            })
        }
    }

    override fun delete(id: Int) {
        transaction {
            SubjectToUserGroupTable.deleteWhere { SubjectToUserGroupTable.userGroupId eq id }
            deleteWhere { UserGroupTable.id eq id }
        }
    }
}
