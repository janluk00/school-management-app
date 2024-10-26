insert into school_users(id, name, surname, email, password, birth_date, password_confirmation_token)

values ('ebb6885d-4c02-498e-978f-53c8df4d78e8', 'Admin', 'Admin', 'admin@gmail.com',
        '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '2000-01-01', null),
-- Students
       ('bbfe1b4e-4597-401d-b05a-f66bd1aca9ad', 'John', 'Doe', 'johndoe@gmail.com',
        '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '2000-05-20', null),
       ('bea87528-af06-4c52-9085-3938fe8cd4c5', 'Michael', 'Jackson', 'moonwalk@gmail.com',
         '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '1958-08-29', null),
       ('82a00587-f6d3-47c1-95b1-bb8745b93005', 'Andy', 'Dufresne', 'shawshank@gmail.com',
         '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '1958-10-16', null),
       ('cec8a5ca-a1fa-4b86-8684-999c92251e2b', 'Ellis', 'Jackson', 'red@gmail.com',
         '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '1937-06-01', null),
-- Teachers
        ('d445aba7-56f0-4033-b8df-099f5de08eec', 'Gary', 'Oldman', 'oldman@gmail.com',
         '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '1958-03-21', null),
        ('63425de7-ad85-4abb-a44b-bba846b07759', 'Jean', 'Reno', 'leon@gmail.com',
         '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '1948-07-30', null),
        ('09b89dd4-4cd0-409a-9525-c192754ca772', 'Kevin', 'Spacey', 'prot@gmail.com',
         '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '1959-07-26', null);

insert into students(id, school_user_id, school_class_name)
values ('a5f5e4f8-405e-41d8-adb4-0398f71345d8', 'bbfe1b4e-4597-401d-b05a-f66bd1aca9ad', 'A1'),
       ('03829453-2eae-4555-9bce-8daf6ff13182', 'bea87528-af06-4c52-9085-3938fe8cd4c5', 'A2'),
       ('86110bd0-d7d6-4b5d-856c-dcdbc9fe94ba', '82a00587-f6d3-47c1-95b1-bb8745b93005', 'C3'),
       ('deb99863-2e4f-4556-9b6c-c024788c96f5', 'cec8a5ca-a1fa-4b86-8684-999c92251e2b', 'C3');

insert into teachers(id, school_user_id, tutor_class_name)
values ('097eaa63-9995-45cb-be07-9f34401c5808', 'd445aba7-56f0-4033-b8df-099f5de08eec', null),
       ('60070625-5eff-4fa1-811f-f6aea73a82e6', '63425de7-ad85-4abb-a44b-bba846b07759', null),
       ('7b236ff9-e134-420e-bce5-b79da1bb419d', '09b89dd4-4cd0-409a-9525-c192754ca772', 'A1');

insert into school_users_roles(school_user_id, role)
values ('ebb6885d-4c02-498e-978f-53c8df4d78e8', 'ROLE_ADMIN'),
       ('bbfe1b4e-4597-401d-b05a-f66bd1aca9ad','ROLE_STUDENT'),
       ('bea87528-af06-4c52-9085-3938fe8cd4c5','ROLE_STUDENT'),
       ('82a00587-f6d3-47c1-95b1-bb8745b93005', 'ROLE_STUDENT'),
       ('cec8a5ca-a1fa-4b86-8684-999c92251e2b', 'ROLE_STUDENT'),
       ('d445aba7-56f0-4033-b8df-099f5de08eec', 'ROLE_TEACHER'),
       ('63425de7-ad85-4abb-a44b-bba846b07759', 'ROLE_TEACHER'),
       ('09b89dd4-4cd0-409a-9525-c192754ca772', 'ROLE_TEACHER');

insert into teachers_subjects(teacher_id, subject_name)
values ('097eaa63-9995-45cb-be07-9f34401c5808', 'MATHEMATICS'),
       ('097eaa63-9995-45cb-be07-9f34401c5808', 'PHYSICS'),
       ('7b236ff9-e134-420e-bce5-b79da1bb419d', 'ENGLISH'),
       ('7b236ff9-e134-420e-bce5-b79da1bb419d', 'CHEMISTRY');

insert into courses(id, teacher_id, subject_name)
values ('9d74d24f-1287-44a3-a3e6-88a56acc4249', '097eaa63-9995-45cb-be07-9f34401c5808', 'MATHEMATICS'),
       ('dd11ed32-0158-4c03-84a8-44b70182aca7', '097eaa63-9995-45cb-be07-9f34401c5808', 'PHYSICS'),
       ('3f1a6ad6-1dc5-4de6-bd41-440d6994a29c', '7b236ff9-e134-420e-bce5-b79da1bb419d', 'ENGLISH');

insert into courses_school_classes(course_id, school_class_name)
values ('9d74d24f-1287-44a3-a3e6-88a56acc4249', 'C3'),
       ('dd11ed32-0158-4c03-84a8-44b70182aca7', 'C3'),
       ('3f1a6ad6-1dc5-4de6-bd41-440d6994a29c', 'A1');

insert into grades(id, issued_date, student_id, teacher_id, grade, subject_name)
values ('ec8ea86b-8185-4507-b98b-e86ce4f9aeee', '2024-07-08 19:07:10.713674',
        'deb99863-2e4f-4556-9b6c-c024788c96f5',  '097eaa63-9995-45cb-be07-9f34401c5808', 4.5, 'MATHEMATICS'),
       ('2e229356-daab-4842-9bcf-a8e280615fa2', '2024-07-08 21:07:10.713674',
        'deb99863-2e4f-4556-9b6c-c024788c96f5',  '097eaa63-9995-45cb-be07-9f34401c5808', 2.75, 'MATHEMATICS'),
       ('73755686-1855-4316-9097-c566f542b800', '2024-07-08 21:07:10.713674',
        '86110bd0-d7d6-4b5d-856c-dcdbc9fe94ba',  '097eaa63-9995-45cb-be07-9f34401c5808', 2.25, 'MATHEMATICS'),
       ('c576d5d6-1e99-4c58-a927-6bb1fc2d0732', '2024-07-08 21:07:10.713674',
        '86110bd0-d7d6-4b5d-856c-dcdbc9fe94ba',  '097eaa63-9995-45cb-be07-9f34401c5808', 1.50, 'MATHEMATICS'),
       ('be118f27-80f2-487e-8670-94cde5b786ec', '2024-07-08 21:07:10.713674',
        'deb99863-2e4f-4556-9b6c-c024788c96f5',  '097eaa63-9995-45cb-be07-9f34401c5808', 3.75, 'PHYSICS'),
       ('70cd92ee-4734-47a0-bf1e-29a83588d49a', '2024-07-08 21:07:10.713674',
        '86110bd0-d7d6-4b5d-856c-dcdbc9fe94ba',  '097eaa63-9995-45cb-be07-9f34401c5808', 2.25, 'PHYSICS'),
       ('628663a0-3fe3-4a59-855f-74d6fc884a4a', '2024-07-08 21:07:10.713674',
        '86110bd0-d7d6-4b5d-856c-dcdbc9fe94ba',  '097eaa63-9995-45cb-be07-9f34401c5808', 2.25, 'PHYSICS');
