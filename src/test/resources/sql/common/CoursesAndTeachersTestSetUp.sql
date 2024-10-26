insert into school_users(id, name, surname, email, password, birth_date, password_confirmation_token)
values ('ebb6885d-4c02-498e-978f-53c8df4d78e8', 'Admin', 'Admin', 'admin@gmail.com',
        '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '2000-01-01', null),
       ('d445aba7-56f0-4033-b8df-099f5de08eec', 'Gary', 'Oldman', 'oldman@gmail.com',
        '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '1958-03-21', null),
       ('63425de7-ad85-4abb-a44b-bba846b07759', 'Jean', 'Reno', 'leon@gmail.com',
        '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '1948-07-30', null),
       ('09b89dd4-4cd0-409a-9525-c192754ca772', 'Kevin', 'Spacey', 'prot@gmail.com',
        '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '1959-07-26', null);

insert into teachers(id, school_user_id, tutor_class_name)
values ('097eaa63-9995-45cb-be07-9f34401c5808', 'd445aba7-56f0-4033-b8df-099f5de08eec', null),
       ('60070625-5eff-4fa1-811f-f6aea73a82e6', '63425de7-ad85-4abb-a44b-bba846b07759', null),
       ('7b236ff9-e134-420e-bce5-b79da1bb419d', '09b89dd4-4cd0-409a-9525-c192754ca772', 'A1');

insert into school_users_roles(school_user_id, role)
values ('ebb6885d-4c02-498e-978f-53c8df4d78e8', 'ROLE_ADMIN'),
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
