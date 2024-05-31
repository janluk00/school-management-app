begin;

drop table if exists new_school_classes cascade;

-- new auxiliary table --
create table new_school_classes
(
    name varchar(3) not null,
    constraint new_school_classes_pkey primary key (name)
);

-- addition of fixed school class names --
insert into new_school_classes (name) values
    ('A1'), ('A2'), ('A3'), ('B1'), ('B2'), ('B3'), ('C1'), ('C2'), ('C3');

-- new auxiliary column --
alter table teacher_taught_subjects_school_classes add column school_class_name varchar(3);

-- transfer of data from school_class_id to school_class_name based on manually added school classes --
update teacher_taught_subjects_school_classes ttssc
set school_class_name = sc.name
from school_classes sc
where ttssc.school_class_id = sc.id
    and sc.name in ('A1', 'A2', 'A3', 'B1', 'B2', 'B3', 'C1', 'C2', 'C3');

-- update of teachers table --
alter table teachers add column tutor_class_name varchar(3);

update teachers t
set tutor_class_name = sc.name
from school_classes sc
where t.tutor_class_id = sc.id
    and sc.name in ('A1', 'A2', 'A3', 'B1', 'B2', 'B3', 'C1', 'C2', 'C3');

-- update of students table --
alter table students add column school_class_name varchar(3);

update students s
set school_class_name = sc.name
from school_classes sc
where s.school_class_id = sc.id
    and sc.name in ('A1', 'A2', 'A3', 'B1', 'B2', 'B3', 'C1', 'C2', 'C3');

-- removal of old foreign and primary keys --
alter table teacher_taught_subjects_school_classes
    drop constraint fk_teacher_taught_subjects_school_classes_school_class,
    drop constraint teacher_taught_subjects_school_classes_pkey;

alter table teachers
    drop constraint fk_teachers_school_class;

alter table students
    drop constraint fk_students_school_class;

-- removal of school_class_id columns --
alter table teacher_taught_subjects_school_classes drop column school_class_id;
alter table teachers drop column tutor_class_id;
alter table students drop column school_class_id;

-- addition of new foreign and primary keys --
alter table teacher_taught_subjects_school_classes
    add constraint teacher_taught_subjects_school_classes_pkey
        primary key (teacher_taught_subject_id, school_class_name);

alter table teacher_taught_subjects_school_classes
    add constraint fk_teacher_taught_subjects_school_class
        foreign key (school_class_name)
            references new_school_classes (name);

alter table teachers
    add constraint fk_teachers_school_class
        foreign key (tutor_class_name)
            references new_school_classes (name);

alter table students
    add constraint fk_students_school_class
        foreign key (school_class_name)
            references new_school_classes (name);

-- removal of old table --
drop table school_classes;

alter table new_school_classes rename to school_classes;

alter table school_classes
    rename constraint new_school_classes_pkey to school_classes_pkey;

commit;