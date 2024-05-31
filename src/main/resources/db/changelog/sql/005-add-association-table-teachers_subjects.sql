drop table if exists teachers_subjects cascade;

create table teachers_subjects
(
    teacher_id  uuid    not null,
    subject_id  uuid    not null,
    constraint teachers_subjects_pkey
        primary key (teacher_id, subject_id)
);

alter table teachers_subjects
    add constraint fk_teachers_subjects_teacher
        foreign key (teacher_id)
            references teachers (id);

alter table teachers_subjects
    add constraint fk_teachers_subjects_subject
        foreign key (subject_id)
            references subjects (id);