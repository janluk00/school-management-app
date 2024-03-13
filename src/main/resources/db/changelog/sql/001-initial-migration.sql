create extension if not exists "uuid-ossp";

drop type if exists class_type cascade;
drop type if exists grade_type cascade;
drop type if exists subject_type cascade;

create type class_type as enum ('A1', 'A2', 'A3', 'B1', 'B2', 'B3', 'C1', 'C2', 'C3');
create type grade_type as enum
(
    'A', 'A_MINUS',
    'B_PLUS', 'B', 'B_MINUS',
    'C_PLUS', 'C', 'C_MINUS',
    'D_PLUS', 'D', 'D_MINUS',
    'E_PLUS', 'E', 'E_MINUS',
    'F_PLUS', 'F'
);
create type subject_type as enum ('MATHEMATICS', 'BIOLOGY', 'CHEMISTRY', 'PHYSICS', 'GEOGRAPHY', 'ENGLISH');

drop table if exists school_users cascade;

create table school_users
(
    id          uuid            not null,
    name        varchar(50)     not null,
    surname     varchar(50)     not null,
    email       varchar(100)    not null,
    password    varchar(200)    not null,
    birth_date  date,
    token       varchar(25)     not null,
    constraint school_users_pkey primary key (id)
);

drop table if exists roles cascade;

create table roles
(
    role    varchar(13)   not null,
    constraint roles_pkey primary key (role)
);

drop table if exists school_users_roles cascade;

create table school_users_roles
(
    school_user_id  uuid            not null,
    role            varchar(13)     not null,
    constraint school_user_roles_pkey primary key (school_user_id, role)
);

alter table school_users_roles
    add constraint fk_school_user_roles_school_user
        foreign key (school_user_id)
            references school_users (id);

alter table school_users_roles
    add constraint fk_school_user_roles_role
        foreign key (role)
            references roles (role);

drop table if exists school_classes cascade;

create table school_classes
(
    id      uuid        not null,
    name    class_type  not null,
    constraint school_classes_pkey primary key (id)
);

drop table if exists subjects cascade;

create table subjects
(
    id      uuid            not null,
    name    subject_type    not null,
    constraint subjects_pkey primary key (id)
);

drop table if exists students cascade;

create table students
(
    id              uuid    not null,
    school_user_id  uuid    not null,
    school_class_id uuid    not null,
    constraint students_pkey primary key (id)
);

alter table students
    add constraint fk_students_school_user
        foreign key (school_user_id)
            references school_users (id);

alter table students
    add constraint fk_students_school_class
        foreign key (school_class_id)
            references school_classes (id);

drop table if exists teachers cascade;

create table teachers
(
    id              uuid    not null,
    school_user_id  uuid    not null,
    school_class_id uuid    not null,
    constraint teachers_pkey primary key (id)
);

alter table teachers
    add constraint fk_teachers_school_user
        foreign key (school_user_id)
            references school_users (id);

alter table teachers
    add constraint fk_teachers_school_class
        foreign key (school_class_id)
            references school_classes (id);

drop table if exists grades cascade;

create table grades
(
    id          uuid        not null,
    grade       grade_type  not null,
    issued_date timestamp   not null,
    student_id  uuid   not null,
    teacher_id  uuid   not null,
    subject_id  uuid   not null,
    constraint grades_pkey primary key (id)
);

alter table grades
    add constraint fk_grades_student
        foreign key (student_id)
            references students (id);

alter table grades
    add constraint fk_grades_teacher
        foreign key (teacher_id)
            references teachers (id);

alter table grades
    add constraint fk_grades_subject
        foreign key (subject_id)
            references subjects (id);

drop table if exists teacher_taught_subjects cascade;

create table teacher_taught_subjects
(
    id          uuid    not null,
    teacher_id  uuid    not null,
    subject_id  uuid    not null,
    constraint teacher_taught_subjects_pkey primary key (id)
);

alter table teacher_taught_subjects
    add constraint teacher_taught_subjects_teacher
        foreign key (teacher_id)
            references teachers (id);

alter table teacher_taught_subjects
    add constraint teacher_taught_subjects_subject
        foreign key (subject_id)
            references subjects (id);

drop table if exists teacher_taught_subjects_school_classes cascade;

create table teacher_taught_subjects_school_classes
(
    teacher_taught_subject_id   uuid    not null,
    school_class_id             uuid    not null,
    constraint teacher_taught_subjects_school_classes_pkey
        primary key (teacher_taught_subject_id, school_class_id)
);

alter table teacher_taught_subjects_school_classes
    add constraint teacher_taught_subjects_school_classes_teacher_taught_subject
        foreign key (teacher_taught_subject_id)
            references teacher_taught_subjects (id);

alter table teacher_taught_subjects_school_classes
    add constraint teacher_taught_subjects_school_classes_school_class
        foreign key (school_class_id)
            references school_classes (id);
