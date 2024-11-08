drop database if exists surveydb; 
create schema surveydb;
use surveydb;
show tables;

-- for consistency use this user for the project
drop user if exists javadev@localhost;
create user javadev@localhost identified with mysql_native_password by "abcd";
grant select, insert, create, drop, update, delete, references, index, alter on surveydb.* to javadev@localhost; 

create table roles(
	role_id int auto_increment primary key,
    role_name char(50) unique not null,
    can_respond bool not null default TRUE,
    can_author_form bool not null default FALSE,
    can_author_group bool not null default FALSE,
    can_manage_forms bool not null default FALSE,
    can_manage_groups bool not null default FALSE,
    can_manage_roles bool not null default FALSE,
    can_manage_users bool not null default FALSE
);
desc roles;
select * from roles;

create table users(
	user_id int auto_increment primary key,
    first_name char(30),
    middle_name varchar(100),
    last_name char(30),
    email char(50) unique not null,
    phone_code int,
    phone_number int,
    dob date,
    gender char(1) default 'O',
    passkey varchar(500) not null,
    user_role int default 2,
    foreign key (user_role) references roles(role_id) on delete restrict on update cascade
);
desc users;

create table sessionLogs(
	log_time DATETIME,
    user_id int,
	login bool not null,
    device text(500),
    location text(500),
    primary key (log_time,user_id),
    foreign key (user_id) references users(user_id) on delete cascade on update cascade 
);
desc sessionLogs;

create table userGroups(
	group_id int auto_increment primary key,
    group_name varchar(100),
    created_at datetime,
    group_owner int,
    foreign key (group_owner) references users(user_id) on delete set null on update cascade
);
desc userGroups;

create table member_of(
	group_id int,
    foreign key (group_id) references userGroups(group_id) on delete cascade on update cascade,
    user_id int,
    foreign key (user_id) references users(user_id) on delete cascade on update cascade,
    primary key (group_id,user_id)
);

create table forms(
	form_id int auto_increment primary key,
    form_name varchar(100),
    author int,
    foreign key (author) references users(user_id) on delete set null on update cascade,
    created_at datetime,
    form_desc varchar(400),
    start_time datetime not null,
    end_time datetime
);

-- form_id,user_id,notif_type make prim key
create table notifications(
    notif_id int auto_increment primary key,
	form_id int,
    foreign key (form_id) references forms(form_id) on delete cascade on update cascade,
    user_id int,
    foreign key(user_id) references users(user_id) on delete cascade on update cascade,
    notif_time datetime,
    seen bool default FALSE,
    message varchar(100),
    notif_type ENUM('C', 'A', 'E', 'G', 'R') NOT NULL DEFAULT 'C'
);

create table responsesMetadata(
	form_id int,
    foreign key (form_id) references forms(form_id) on delete cascade on update cascade,
    user_id int,
    foreign key (user_id) references users(user_id) on delete cascade on update cascade,
    response_time datetime,
    device text(400),
    location text(400),
    primary key (form_id,user_id)
);

create table dataTypes(
	type_id int auto_increment primary key,
    type_name char(30) not null,
    type_desc varchar(100),
    max_val int,
    min_val int,
    mapped_to enum('I', 'F', 'S') not null default 'S',
    uses_options bool default false not null
);

-- conditional attribute required???
create table questions(
	question_id int not null,
    form_id int,
    foreign key (form_id) references forms(form_id) on delete cascade on update cascade,
	data_type int,
    foreign key (data_type) references dataTypes(type_id) on delete restrict on update cascade,
    required bool default false not null,
    prompt varchar(500),
    min_val long,
    max_val long,
    conditional bool default false,
    primary key (question_id,form_id)
);

create table questionResponse(
	user_id int,
    foreign key (user_id) references users(user_id) on delete cascade on update cascade,
    question_id int,
    form_id int,
    foreign key (question_id,form_id) references questions(question_id,form_id) on delete cascade on update cascade,
    response_int bigint,
    response_string varchar(500),
    response_float decimal(10,10),
    PRIMARY KEY (form_id,question_id,user_id)
);

create table question_options(
	form_id int,
	question_id int,
    option_id int,
    foreign key (question_id,form_id) references questions(question_id,form_id) on delete cascade on update cascade,
    -- option_value char(50) not null,
    option_value_int bigint,
    option_value_string varchar(500),
    option_value_float decimal(10,10),
--    special_option bool default false,
    primary key (form_id,question_id,option_id)
);
desc question_options;

create table conditionalOrdering(
	form_id int,
    question_id int,
    foreign key (form_id,question_id) references questions(form_id,question_id) on delete cascade on update cascade,
    condition_id int,
--    compare_value varchar(100),
	compare_value_int bigint,
    compare_value_float decimal(10,10),
    compare_value_string varchar(500),
	question_default int,
    foreign key (form_id,question_default) references questions(form_id,question_id) on delete cascade on update cascade,
    question_alt int,
    foreign key (form_id,question_alt) references questions(form_id,question_id) on delete cascade on update cascade,
    primary key (form_id,question_id,condition_id)
);

create table dataSave(
	form_id int references forms(form_id),
    data_id int not null,
	save_time datetime,
    report_path char(40),
    data_path char(40),
    requested_by int,
    foreign key (requested_by) references users(user_id) on delete set null on update cascade,
    primary key (form_id,data_id)
);

create table analyticFunctions(
	func_id int auto_increment primary key,
    func_name varchar(100) not null,
    func_path char(40) not null
);

create table analysed_by(
	type_id int,
    foreign key (type_id) references dataTypes(type_id) on delete cascade on update cascade,
    func_id int,
    foreign key (func_id) references analyticFunctions(func_id) on delete restrict on update cascade,
    primary key (type_id,func_id)
);

create table can_respond(
    form_id int,
    group_id int,
    primary key(form_id, group_id),
    foreign key (form_id) references forms(form_id) on delete cascade on update cascade,
    foreign key (group_id) references userGroups(group_id) on delete cascade on update cascade
);

show tables;
desc conditionalOrdering;

INSERT INTO roles (role_name, can_respond, can_author_form, can_author_group, can_manage_forms, can_manage_groups, can_manage_roles, can_manage_users) VALUES
('Employee', TRUE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE),
('Admin', TRUE, TRUE, TRUE, TRUE, TRUE, TRUE, TRUE),
('Moderator', TRUE, FALSE, FALSE, TRUE, TRUE, FALSE, FALSE),
('Manager', TRUE, FALSE, FALSE, FALSE, FALSE, TRUE, FALSE),
('Author', TRUE, TRUE, TRUE, FALSE, FALSE, FALSE, FALSE),
('Guest', FALSE, FALSE, FALSE, FALSE, FALSE, FALSE, FALSE);

INSERT INTO dataTypes (type_name, type_desc, max_val, min_val, mapped_to, uses_options) VALUES
('Text', 'A simple text input', 500, 0, 'S', FALSE),
('Number', 'An integer input', 2147483647, -2147483648, 'I', FALSE),
('Boolean', 'A Logical True/False', 1, 0, 'I', FALSE),
('Float', 'Stores floating point numbers', null, null, 'F', FALSE),
('Radio-Int', 'MCQ for integers', null, null, 'I', TRUE),
('Radio-Float', 'MCQ for floating points', null, null, 'F', TRUE),
('Radio-String', 'MCQ for strings', null, null, 'S', TRUE);

INSERT INTO analyticFunctions (func_name, func_path) VALUES
('Average', 'Mean'),
('Sum', 'Total'),
('Count', 'Frequencycounter'),
('Max', 'Max'),
('Min', 'Min'),
('Median','Median'),
('Mode','Mode'),
('Standard Deviaion','StandardDev'),
('Variance','Variance'),
('List of Uniques','UniqueList'),
('Percentage of Null','NullPercent');

INSERT INTO analysed_by (type_id, func_id) VALUES
(1, 3),(1,7),(1,10),(1,11),
(2, 1),(2, 2),(2, 3),(2, 4),(2, 5),(2, 6),(2, 7),(2, 8),(2, 9),(2, 10),(2, 11),
(3, 3),(3,7),(3,10),(3,11),
(4, 1),(4, 2),(4, 3),(4, 4),(4, 5),(4, 6),(4, 7),(4, 8),(4, 9),(4, 10),(4, 11),
(5, 1),(5, 2),(5, 3),(5, 4),(5, 5),(5, 6),(5, 7),(5, 8),(5, 9),(5, 10),(5, 11),
(6, 1),(6, 2),(6, 3),(6, 4),(6, 5),(6, 6),(6, 7),(6, 8),(6, 9),(6, 10),(6, 11),
(7, 3),(7,7),(7,10),(7,11);

DELIMITER //

CREATE TRIGGER check_required_responses
BEFORE INSERT ON responsesMetadata
FOR EACH ROW
BEGIN
    DECLARE missing_responses INT;

    SELECT COUNT(*)
    INTO missing_responses
    FROM questions q
    LEFT JOIN questionResponse qr 
        ON q.form_id = qr.form_id 
        AND q.question_id = qr.question_id 
        AND qr.user_id = NEW.user_id
    WHERE q.form_id = NEW.form_id
        AND q.required = TRUE
        AND (qr.response_int IS NULL AND qr.response_string IS NULL AND qr.response_float IS NULL);

    IF missing_responses > 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Cannot submit response metadata: required questions are missing answers.';
    END IF;
END //

DELIMITER ;

