alter table teacher_taught_subjects rename to teachers_in_course;

alter table teachers_in_course
RENAME constraint teacher_taught_subjects_pkey to teachers_in_course_pkey;

alter table teachers_in_course
RENAME constraint fk_teacher_taught_subjects_subject to fk_teachers_in_course_subject;

alter table teachers_in_course
RENAME constraint fk_teacher_taught_subjects_teacher to fk_teachers_in_course_teacher;

alter table teacher_taught_subjects_school_classes rename to teachers_in_course_school_classes;

alter table teachers_in_course_school_classes
RENAME constraint teacher_taught_subjects_school_classes_pkey to teachers_in_course_school_classes_pkey;

alter table teachers_in_course_school_classes
RENAME constraint fk_teacher_taught_subjects_school_class
                    to fk_teachers_in_course_school_classes_school_class;

alter table teachers_in_course_school_classes
RENAME constraint fk_teacher_taught_subjects_school_classes_teacher_taught_subject
                    to fk_teachers_in_course_school_classes_taught_subject;