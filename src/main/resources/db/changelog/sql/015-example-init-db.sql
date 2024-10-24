insert into school_users(id, name, surname, email, password, birth_date, password_confirmation_token)
values ('37cb3950-1f36-4dd9-9d73-5b9ffe47aa19', 'Thanos', 'Thanos', 'thanos@gmail.com',
        '{bcrypt}$2a$10$sf7v/Xt0qh78KPCBeRunjOEGdZIPOl9WIh1B6OSCJi.Zb5.aczMGG', '2000-01-01', null);

insert into school_users_roles(school_user_id, role)
values ('37cb3950-1f36-4dd9-9d73-5b9ffe47aa19', 'ROLE_ADMIN');
