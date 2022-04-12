create table if not exists subject
(
    id       serial not null primary key,
    password varchar(255),
    username varchar(255),

    unique (username)
);

create table if not exists user_group
(
    id   serial not null primary key,
    name varchar(255),

    unique (name)
);

create table if not exists subject_groups
(
    subject_id integer not null,
    groups_id  integer not null,
    constraint subject_user_group_pkey primary key (subject_id, groups_id),
    constraint fk_subject foreign key (subject_id) references subject (id),
    constraint fk_user_group foreign key (groups_id) references user_group (id)
);
