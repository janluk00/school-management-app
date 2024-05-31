begin;

drop table if exists new_subjects cascade;

-- new auxiliary table --
create table new_subjects
(
    name varchar(30) not null,
    constraint new_subjects_pkey primary key (name)
);

-- addition of fixed school subject names --
insert into new_subjects (name) values
    ('MATHEMATICS'), ('BIOLOGY'), ('CHEMISTRY'), ('PHYSICS'), ('GEOGRAPHY'), ('ENGLISH');

-- new auxiliary column --
alter table teachers_subjects add column subject_name varchar(30);

-- transfer of data from subject_id to subject_name based on manually added school subjects --
update teachers_subjects ts
set subject_name = s.name
from subjects s
where ts.subject_id = s.id
    and s.name in ('MATHEMATICS', 'BIOLOGY', 'CHEMISTRY', 'PHYSICS', 'GEOGRAPHY', 'ENGLISH');

-- update of grades table --
alter table grades add column subject_name varchar(30);

update grades g
set subject_name = s.name
from subjects s
where g.subject_id = s.id
    and s.name in ('MATHEMATICS', 'BIOLOGY', 'CHEMISTRY', 'PHYSICS', 'GEOGRAPHY', 'ENGLISH');

-- update of teacher_taught_subjects table --
alter table teacher_taught_subjects add column subject_name varchar(30);

update teacher_taught_subjects tts
set subject_name = s.name
from subjects s
where tts.subject_id = s.id
    and s.name in ('MATHEMATICS', 'BIOLOGY', 'CHEMISTRY', 'PHYSICS', 'GEOGRAPHY', 'ENGLISH');

-- removal of old foreign and primary keys --
alter table teachers_subjects
    drop constraint fk_teachers_subjects_subject,
    drop constraint teachers_subjects_pkey;

alter table grades
    drop constraint fk_grades_subject;

alter table teacher_taught_subjects
    drop constraint fk_teacher_taught_subjects_subject;

-- removal of subject_id columns --
alter table teachers_subjects drop column subject_id;
alter table grades drop column subject_id;
alter table teacher_taught_subjects drop column subject_id;

-- addition of new foreign and primary keys --
alter table teachers_subjects
    add constraint teachers_subjects_pkey
        primary key (teacher_id, subject_name);

alter table teachers_subjects
    add constraint fk_teachers_subjects_subject
        foreign key (subject_name)
            references new_subjects (name);

alter table grades
    add constraint fk_grades_subject
        foreign key (subject_name)
            references new_subjects (name);

alter table teacher_taught_subjects
    add constraint fk_teacher_taught_subjects_subject
        foreign key (subject_name)
            references new_subjects (name);

drop table subjects;

alter table new_subjects rename to school_subjects;

alter table school_subjects
    rename constraint new_subjects_pkey to school_subjects_pkey;

commit;