alter table school_users_roles
    drop constraint if exists fk_school_user_roles_role;

alter table roles alter column role type varchar(13);

alter table school_users_roles alter column role type varchar(13);

alter table school_users_roles
    add constraint fk_school_user_roles_role
        foreign key (role)
            references roles (role);

drop type if exists role_type cascade;