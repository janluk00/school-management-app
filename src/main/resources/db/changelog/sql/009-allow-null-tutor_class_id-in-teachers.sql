alter table teachers alter column school_class_id drop not null;
alter table teachers rename column school_class_id to tutor_class_id;