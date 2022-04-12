package coffee.michel.usermanager.persistence.tables

import org.jetbrains.exposed.sql.Table

object SubjectToUserGroupTable : Table("subject_groups") {
    val subjectId = (integer("subject_id").references(SubjectTable.id))
    val userGroupId = (integer("user_group_id").references(UserGroupTable.id))
}