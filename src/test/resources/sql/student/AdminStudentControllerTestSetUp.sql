insert into school_users(id, name, surname, email, password, birth_date, password_confirmation_token)
values ('ebb6885d-4c02-498e-978f-53c8df4d78e8', 'Admin', 'Admin', 'admin@gmail.com',
        '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '2000-01-01', null),
       ('bbfe1b4e-4597-401d-b05a-f66bd1aca9ad', 'John', 'Doe', 'johndoe@gmail.com',
        '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '2000-05-20', null),
       ('bea87528-af06-4c52-9085-3938fe8cd4c5', 'Michael', 'Jackson', 'moonwalk@gmail.com',
         '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '1958-08-29', null),
       ('82a00587-f6d3-47c1-95b1-bb8745b93005', 'Andy', 'Dufresne', 'shawshank@gmail.com',
         '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '1958-10-16', null),
       ('cec8a5ca-a1fa-4b86-8684-999c92251e2b', 'Ellis', 'Jackson', 'red@gmail.com',
         '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '1937-06-01', null);

insert into students(id, school_user_id, school_class_name)
values ('a5f5e4f8-405e-41d8-adb4-0398f71345d8', 'bbfe1b4e-4597-401d-b05a-f66bd1aca9ad', 'A1'),
       ('03829453-2eae-4555-9bce-8daf6ff13182', 'bea87528-af06-4c52-9085-3938fe8cd4c5', 'A2'),
       ('86110bd0-d7d6-4b5d-856c-dcdbc9fe94ba', '82a00587-f6d3-47c1-95b1-bb8745b93005', 'C3'),
       ('deb99863-2e4f-4556-9b6c-c024788c96f5', 'cec8a5ca-a1fa-4b86-8684-999c92251e2b', 'C3');

insert into school_users_roles(school_user_id, role)
values ('ebb6885d-4c02-498e-978f-53c8df4d78e8', 'ROLE_ADMIN'),
       ('bbfe1b4e-4597-401d-b05a-f66bd1aca9ad','ROLE_STUDENT'),
       ('bea87528-af06-4c52-9085-3938fe8cd4c5','ROLE_STUDENT'),
       ('82a00587-f6d3-47c1-95b1-bb8745b93005', 'ROLE_STUDENT'),
       ('cec8a5ca-a1fa-4b86-8684-999c92251e2b', 'ROLE_STUDENT');
