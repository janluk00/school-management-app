-- school_classes --
alter table school_classes add column temp_name varchar(3) not null;
update school_classes set temp_name = name::text;
alter table school_classes drop column name;
alter table school_classes RENAME column temp_name to name;
drop type class_type;

-- subjects --
alter table subjects add column temp_name varchar(50) not null;
update subjects set temp_name = name::text;
alter table subjects drop column name;
alter table subjects RENAME column temp_name to name;
drop type subject_type;

-- grades --
alter table grades add column temp_grade numeric(3, 2) not null;
update grades set temp_grade =
    case grade
        when 'A' then 6
        when 'A_MINUS' then 5.75
        when 'B_PLUS' then 5.25
        when 'B' then 5
        when 'B_MINUS' then 4.75
        when 'C_PLUS' then 4.25
        when 'C' then 4
        when 'C_MINUS' then 3.75
        when 'D_PLUS' then 3.25
        when 'D' then 3
        when 'D_MINUS' then 2.75
        when 'E_PLUS' then 2.25
        when 'E' then 2
        when 'E_MINUS' then 1.75
        when 'F_PLUS' then 1.25
        when 'F' then 1
    end;
alter table grades drop column grade;
alter table grades RENAME column temp_grade to grade;
drop type grade_type;