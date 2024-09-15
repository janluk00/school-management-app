alter table teachers_in_course RENAME to courses;
alter table teachers_in_course_school_classes rename to courses_school_classes;

alter table courses drop constraint fk_teachers_in_course_subject;
alter table courses drop constraint fk_teachers_in_course_teacher;
alter table courses_school_classes drop constraint fk_teachers_in_course_school_classes_school_class;
alter table courses_school_classes drop constraint fk_teachers_in_course_school_classes_taught_subject;

alter table courses_school_classes rename column teacher_in_course_id to course_id;

alter table courses
    rename constraint teachers_in_course_pkey to courses_pkey;

alter table courses_school_classes
    rename constraint teachers_in_course_school_classes_pkey to courses_school_classes_pkey;

alter table courses
    add constraint fk_courses_subjects
        foreign key (subject_name)
            references school_subjects(name);

alter table courses
    add constraint fk_courses_teachers
        foreign key (teacher_id)
            references teachers(id);

alter table courses_school_classes
    add constraint fk_courses_school_classes_school_classes
        foreign key (school_class_name)
            references school_classes(name);

alter table courses_school_classes
    add constraint fk_courses_teachers
        foreign key (course_id)
            references courses(id);
