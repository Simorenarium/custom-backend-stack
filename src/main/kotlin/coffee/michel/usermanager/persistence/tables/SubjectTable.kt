package coffee.michel.usermanager.persistence.tables

import coffee.michel.usermanager.domain.Subject
import coffee.michel.usermanager.domain.UserGroup
import coffee.michel.usermanager.persistence.SubjectPersistence
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAllBatched
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

object SubjectTable : IntIdTable("subject"), SubjectPersistence {
    private val username = varchar("username", 255)
    private val password = varchar("password", 255)

    override fun findAll() = SubjectTable.selectAllBatched()
        .asSequence()
        .flatten()
        .mapTo { subject -> fetchUserGroupsBySubjectId(subject[id].value) }
        .map { (subject, userGroups) -> mapToSubject(subject, userGroups) }

    override fun findById(id: Int) = SubjectTable.select { SubjectTable.id eq id }
        .asSequence()
        .mapTo { subject -> fetchUserGroupsBySubjectId(subject[SubjectTable.id].value) }
        .map { (subject, userGroups) -> mapToSubject(subject, userGroups) }
        .firstOrNull()

    override fun create(subject: Subject) = transaction {
        subject.groups.forEach { userGroup ->
            createUserGroupMapping(subject, userGroup)
        }
        return@transaction insert {
            it[username] = subject.username
            it[password] = subject.password
        }[SubjectTable.id]
            .let { subject.copy(id = it.value) }
    }

    override fun update(subject: Subject) {
        transaction {
            update(where = { SubjectTable.id eq subject.id!! }, body = {
                it[username] = subject.username
                it[password] = subject.password
            })
            SubjectToUserGroupTable.deleteWhere { SubjectToUserGroupTable.subjectId eq subject.id!! }
            subject.groups.forEach { userGroup ->
                createUserGroupMapping(subject, userGroup)
            }
        }
    }

    override fun delete(id: Int) {
        transaction {
            SubjectTable.deleteWhere { SubjectTable.id eq id }
            SubjectToUserGroupTable.deleteWhere { SubjectToUserGroupTable.subjectId eq id }
        }
    }

    private fun createUserGroupMapping(
        subject: Subject,
        userGroup: UserGroup
    ) {
        SubjectToUserGroupTable.insert {
            it[subjectId] = subject.id!!
            it[userGroupId] = userGroup.id!!
        }
    }

    private fun fetchUserGroupsBySubjectId(subjectId: Int) =
        (UserGroupTable innerJoin SubjectToUserGroupTable)
            .slice(UserGroupTable.id, UserGroupTable.name)
            .select { SubjectToUserGroupTable.subjectId eq subjectId }
            .asSequence()

    private fun mapToSubject(
        subject: ResultRow,
        userGroups: Sequence<ResultRow>
    ) = Subject(
        id = subject[id].value,
        username = subject[username],
        password = subject[password],
        groups = userGroups.map { mapToUserGroup(it) }.toSet()
    )

    private fun mapToUserGroup(it: ResultRow) =
        UserGroup(it[UserGroupTable.id].value, it[UserGroupTable.name])
}

fun <T, K> Sequence<T>.mapTo(transform: (T) -> K): Sequence<Pair<T, K>> = map { it to transform(it) }
