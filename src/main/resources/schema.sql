drop schema if exists event_management;

-- write on delete and on update for fks, add delete functionality
CREATE DATABASE IF NOT EXISTS event_management;
-- DROP DATABASE event_management;
use event_management;

CREATE TABLE IF NOT EXISTS user_credential (
    email_id VARCHAR(75) PRIMARY KEY,
    user_password VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS address (
address_id INT AUTO_INCREMENT PRIMARY KEY,
address_line1 VARCHAR(75) NOT NULL,
address_line2 VARCHAR(75) NOT NULL,
city VARCHAR(50) NOT NULL,
state VARCHAR(50) NOT NULL,
zipcode VARCHAR(20) NOT NULL,
country VARCHAR(50) NOT NULL,
CONSTRAINT address_unique UNIQUE (address_line1, address_line2, city, state, zipcode, country)
);

CREATE TABLE IF NOT EXISTS user_detail (
	email_id VARCHAR(75) PRIMARY KEY,
	first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    user_img VARCHAR(50) NULL,
    user_address_id INT NULL,
    is_admin BOOL DEFAULT false,
    is_organizer BOOL DEFAULT false,
    CONSTRAINT user_detail_credential_fk FOREIGN KEY (email_id) REFERENCES user_credential(email_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT user_detail_fk2 FOREIGN KEY (user_address_id) REFERENCES address(address_id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS credit_card (
	credit_card_num VARCHAR(19) PRIMARY KEY,
	expiry_date DATE NOT NULL,
	name VARCHAR(75) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_credit_card (
	credit_card_num VARCHAR(19),
	email_id VARCHAR(50) NOT NULL,
    CONSTRAINT user_credit_card_pk PRIMARY KEY (email_id, credit_card_num),
    CONSTRAINT USER_credit_card_Fk FOREIGN KEY (email_id) REFERENCES user_detail(email_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT USER_credit_card_Fk2 FOREIGN KEY (credit_card_num) REFERENCES credit_card(credit_card_num) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_organization(
	org_id INT AUTO_INCREMENT PRIMARY KEY,
	org_name VARCHAR(75) NOT NULL,
	org_desc VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS event_organization_team(
	org_id INT,
    organizer_email_id VARCHAR(75),
    CONSTRAINT event_organization_team_pk PRIMARY KEY (org_id, organizer_email_id),
    CONSTRAINT event_organization_team_fk1 FOREIGN KEY (org_id) REFERENCES event_organization(org_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT event_organization_team_fk2 FOREIGN KEY (organizer_email_id) REFERENCES user_detail(email_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_record (
	event_id INT AUTO_INCREMENT PRIMARY KEY,
	event_name VARCHAR(75) NOT NULL,
	event_description VARCHAR(200),
	start_time DATETIME NOT NULL,
	end_time DATETIME NOT NULL,
	last_registration_date DATETIME NOT NULL,
	total_capacity INT,
	entry_fees FLOAT,
    org_id INT,
    admin_email_id VARCHAR(75) DEFAULT NULL,
	CONSTRAINT event_admin_fk1 FOREIGN KEY (admin_email_id) REFERENCES user_detail(email_id) ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT event_organization_fk2 FOREIGN KEY (org_id) REFERENCES event_organization(org_id) ON UPDATE CASCADE ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS user_transaction (
	trans_id INT AUTO_INCREMENT PRIMARY KEY,
    trans_datetime DATETIME NOT NULL,
    trans_status ENUM ("COMPLETE", "INPROGRESS","FAILED") NOT NULL,
    credit_card_num VARCHAR(19),
    email_id VARCHAR(75),
	event_id INT NOT NULL,
    CONSTRAINT trans_credit_card_fk FOREIGN KEY (credit_card_num) REFERENCES credit_card(credit_card_num) ON UPDATE CASCADE ON DELETE SET NULL,
	CONSTRAINT trans_user_fk FOREIGN KEY (email_id) REFERENCES user_detail(email_id) ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT trans_event_fk FOREIGN KEY (event_id) REFERENCES event_record(event_id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS event_ticket (
	ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_user_name VARCHAR(75) NOT NULL,
    id_proof ENUM ("PASSPORT", "STATE_ID", "DRIVING_LICENSE") DEFAULT NULL,
    ticket_status ENUM ("PENDING", "CONFIRMED") NOT NULL,
    trans_id INT NOT NULL,
    CONSTRAINT trans_event_ticket_fk FOREIGN KEY (trans_id) REFERENCES user_transaction(trans_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_image (
	event_img_id INT AUTO_INCREMENT PRIMARY KEY,
	event_img_loc VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS event_image_fk (
	event_id INT,
	event_img_id INT,
    CONSTRAINT event_image_pk PRIMARY KEY (event_id, event_img_id),
	CONSTRAINT event_image_fk1 FOREIGN KEY (event_id) REFERENCES event_record(event_id) ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT event_image_fk2 FOREIGN KEY (event_img_id) REFERENCES event_image(event_img_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_approval (
	event_id INT PRIMARY KEY,
    date_of_approval DATETIME NOT NULL,
    approval_status ENUM ("APPROVED", "IN-PROGRESS", "REJECTED") NOT NULL,
    approval_desc VARCHAR(200),
    CONSTRAINT admin_event_fk1 FOREIGN KEY (event_id) REFERENCES event_record(event_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_comment (
	comment_id INT AUTO_INCREMENT PRIMARY KEY,
	comment_text VARCHAR(250) NOT NULL,
	comment_datetime DATETIME NOT NULL,
	email_id VARCHAR(50) NOT NULL,
	event_id INT NOT NULL,
	CONSTRAINT event_comment_fk1 FOREIGN KEY (event_id) REFERENCES event_record(event_id) ON UPDATE CASCADE ON DELETE RESTRICT,
	CONSTRAINT event_comment_fk2 FOREIGN KEY (email_id) REFERENCES user_detail(email_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_survey (
	event_id INT PRIMARY KEY,
	survey_url VARCHAR(100) NOT NULL,
    org_id INT NOT NULL,
    CONSTRAINT event_survey_fk FOREIGN KEY (event_id) REFERENCES event_record(event_id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT organizer_survey_fk FOREIGN KEY (org_id) REFERENCES event_organization(org_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS survey_response (
	event_id INT,
	user_email_id VARCHAR(75), 
    survey_response_loc VARCHAR(100) NOT NULL,
    CONSTRAINT survey_response_pk PRIMARY KEY (event_id, user_email_id),
    CONSTRAINT survey_response_fk1 FOREIGN KEY (event_id) REFERENCES event_survey(event_id) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT survey_response_fk2 FOREIGN KEY (user_email_id) REFERENCES user_detail(email_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS advertisement (
	ad_id INT AUTO_INCREMENT PRIMARY KEY,
	ad_title VARCHAR(60) NOT NULL,
	ad_img_loc VARCHAR(100) NOT NULL,
	ad_begin_time DATETIME NOT NULL,
	ad_end_time DATETIME NOT NULL,
	admin_email_id VARCHAR(75),
    ad_org_id INT NOT NULL,
	CONSTRAINT admin_ad_fk FOREIGN KEY (admin_email_id) REFERENCES user_detail(email_id) ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT organization_ad_fk FOREIGN KEY (ad_org_id) REFERENCES event_organization(org_id) ON UPDATE CASCADE ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS advertisement_approval (
	ad_id INT PRIMARY KEY,
    date_of_approval DATETIME,
    approval_status ENUM ("APPROVED", "IN-PROGRESS", "REJECTED"),
    approval_desc VARCHAR(200),
    CONSTRAINT admin_advertisement_fk FOREIGN KEY (ad_id) REFERENCES advertisement(ad_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS event_venue (
	address_id INT,
    event_id INT,
    CONSTRAINT event_venue_pk PRIMARY KEY (address_id, event_id),
    CONSTRAINT event_venue_fk1 FOREIGN KEY (address_id) REFERENCES address(address_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT event_venue_fk2 FOREIGN KEY (event_id) REFERENCES event_record(event_id) ON UPDATE CASCADE ON DELETE RESTRICT
);
-- Frequently used Functions
-- 1)
DELIMITER $$
CREATE FUNCTION email_credential_exists(in_email_id VARCHAR(75))
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE email_count INT;
    SELECT COUNT(*) INTO email_count FROM user_credential WHERE email_id = in_email_id;
    RETURN email_count > 0;
END $$

-- 2)
DELIMITER $$
CREATE FUNCTION user_exists(in_email_id VARCHAR(75))
RETURNS boolean
DETERMINISTIC
BEGIN
    DECLARE email_count INT;
    SELECT COUNT(*) INTO email_count FROM user_detail WHERE email_id = in_email_id;
    RETURN email_count > 0;
END $$
DELIMITER ;

-- 3)
DELIMITER $$
CREATE FUNCTION org_exists(in_org_id VARCHAR(75))
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE org_count INT;
    SELECT COUNT(*) INTO org_count FROM event_organization WHERE org_id = in_org_id;
    RETURN (org_count = 1);
END $$
DELIMITER ;

-- 3)
DELIMITER $$
CREATE FUNCTION event_exists(in_event_id INT)
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE event_count INT;
    SELECT COUNT(*) INTO event_count FROM event_record WHERE event_id = in_event_id;
    RETURN (event_count = 1);
END $$
DELIMITER ;

-- 4)
DELIMITER $$
CREATE FUNCTION check_admin(in_email_id VARCHAR(75))
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE is_user_admin BOOLEAN;
    SELECT is_admin INTO is_user_admin FROM user_detail WHERE email_id = in_email_id;
    RETURN is_user_admin;
END $$
DELIMITER ;

-- 4)
DELIMITER $$
CREATE FUNCTION check_organizer(in_email_id VARCHAR(75))
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE is_user_organizer BOOLEAN;
    SELECT is_organizer INTO is_user_organizer FROM user_detail WHERE email_id = in_email_id;
    RETURN is_user_organizer;
END $$
DELIMITER ;

-- 5)
DELIMITER $$
CREATE FUNCTION get_ticket_left_count(in_event_id INT)
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE tickets_left_count BOOLEAN;
    SELECT er.total_capacity - COUNT(*) INTO tickets_left_count FROM (SELECT * FROM event_record WHERE event_id = in_event_id) as er
    inner join user_transaction as ut on er.event_id = ut.event_id
    inner join event_ticket as et on ut.trans_id = et.trans_id
    GROUP BY er.event_id;
    RETURN tickets_left_count;
END $$
DELIMITER ;

-- 3)
DELIMITER $$
CREATE FUNCTION advertisement_exists(in_ad_id INT)
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE ad_count INT;
    SELECT COUNT(*) INTO ad_count FROM advertisement WHERE ad_id = in_ad_id;
    RETURN (ad_count = 1);
END $$
DELIMITER ;
 
-- 1) Register a user
DELIMITER $$
CREATE PROCEDURE insert_user_credential(IN in_email_id VARCHAR(75), IN new_pwd VARCHAR(100))
BEGIN
    IF NOT email_exists(in_email_id) THEN
        INSERT INTO user_credential (email_id, user_password) VALUES (in_email_id, new_pwd);
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User already exists';
    END IF;
END $$ 
DELIMITER ;

-- 2) update user_credential
DELIMITER $$
CREATE PROCEDURE update_user_credential(IN in_email_id VARCHAR(75), IN new_pwd VARCHAR(100))
BEGIN
    IF NOT email_exists(in_email_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User not present';
    ELSE
        UPDATE user_credential SET password = new_pwd
        WHERE email_id = in_email_id;
    END IF;
	SELECT * FROM user_credential WHERE email_id = in_email_id;
END $$ 
DELIMITER ;

-- 2) delete user_credential
DELIMITER $$
CREATE PROCEDURE delete_user_credential(IN in_email_id VARCHAR(75))
BEGIN
    IF NOT user_exists(in_email_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User not present';
    ELSE
		DELETE FROM user_credential WHERE email_id = in_email_id;
    END IF;
	SELECT * FROM user_credential WHERE email_id = in_email_id;
END $$ 
DELIMITER ;

-- 3) Get user credentials
DELIMITER $$
CREATE PROCEDURE get_user_credential(IN user_email_id VARCHAR(75))
BEGIN
    SELECT * FROM user_credential WHERE email_id = user_email_id;
END $$
DELIMITER ;

-- 4) Insert User
DELIMITER $$
CREATE PROCEDURE insert_user(IN in_email_id VARCHAR(75), IN user_pwd VARCHAR(100), IN in_first_name VARCHAR(50),
IN in_last_name VARCHAR(50), IN in_date_of_birth DATE, IN in_user_img VARCHAR(50), IN in_user_address_id INT, IN in_is_admin BOOL, IN in_is_organizer BOOL)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
		IF NOT user_exists(in_email_id) THEN
			INSERT INTO user_credential (email_id, user_password) VALUES (in_email_id, user_pwd);
			INSERT INTO user_detail (email_id, first_name, last_name, date_of_birth, user_img, user_address_id, is_admin, is_organizer) 
			VALUES (in_email_id, in_first_name, in_last_name,
			in_date_of_birth, in_user_img, in_user_address_id, in_is_admin, in_is_organizer);
		ELSE
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User already exists';
		END IF;
    COMMIT;
END $$ 
DELIMITER ;

-- 5) Get user details
DELIMITER $$
CREATE PROCEDURE get_user_detail(IN user_email_id VARCHAR(75))
BEGIN
    SELECT * FROM user_detail WHERE email_id = user_email_id;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE insert_user_address(IN in_email_id VARCHAR(75), IN in_address_line1 VARCHAR(75), IN in_address_line2 VARCHAR(75),
IN in_city VARCHAR(50), IN in_state VARCHAR(50), IN in_zipcode VARCHAR(20), IN in_country VARCHAR(50))
BEGIN
	DECLARE count_address INT;
    DECLARE exist_address_id INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
    IF user_exists(in_email_id) THEN
    SELECT COUNT(*) INTO count_address FROM address WHERE address_line1 = in_address_line1 AND address_line2 = in_address_line2
            AND city = in_city AND state = in_state AND zipcode = zipcode AND country = country;
            IF count_address = 0 THEN
            INSERT INTO address (address_line1, address_line2, city, state, zipcode, country)
            VALUES (in_address_line1, in_address_line2, in_city, in_state, in_zipcode, in_country);
            UPDATE user_detail
            SET user_address_id = LAST_INSERT_ID()
            WHERE email_id = in_email_id;
            ELSE
            SELECT address_id INTO exist_address_id FROM address WHERE address_line1 = in_address_line1 AND address_line2 = in_address_line2;
            UPDATE user_detail
            SET user_address_id = exist_address_id
            WHERE email_id = in_email_id;
            END IF;
            ELSE
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User does not exist';
            END IF;
            COMMIT;
END $$
DELIMITER ;

-- 7) Get Address
DELIMITER $$
CREATE PROCEDURE get_user_address(IN in_email_id VARCHAR(75))
BEGIN
	SELECT a.address_line1, a.address_line2, a.city, a.state, a.zipcode, a.country 
    FROM (SELECT * FROM user_detail where email_id = in_email_id) AS in_user INNER JOIN address as a
    WHERE in_user.user_address_id = a.address_id;
END $$
DELIMITER ;

-- 8) INSERT organization (can add trigger for this)
DELIMITER $$
CREATE PROCEDURE insert_event_org(IN in_email_id VARCHAR(75), IN in_org_name VARCHAR(75), IN in_org_desc VARCHAR(200))
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
    IF check_organizer(in_email_id) THEN
        INSERT INTO event_organization (org_name, org_desc) VALUES (in_org_name, in_org_desc);
        INSERT INTO event_organization_team (org_id, organizer_email_id) VALUES (LAST_INSERT_ID(), in_email_id);
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only organizer can add an organization';
    END IF;
    COMMIT;
END $$ 
DELIMITER ;

-- 8.1) Get organization
DELIMITER $$
CREATE PROCEDURE get_event_org(IN in_org_id INT)
BEGIN
	SELECT * FROM event_organization where org_id = in_org_id;
END $$ 
DELIMITER ;

-- 8.1) Get organization
DELIMITER $$
CREATE PROCEDURE delete_event_org(IN in_org_id INT)
BEGIN
	DELETE FROM event_organization where org_id = in_org_id;
END $$ 
DELIMITER ;

-- 9) Add users to an organization (trigger can be added)
DELIMITER $$
CREATE TRIGGER before_org_user_insert
BEFORE INSERT ON event_organization_team
FOR EACH ROW
BEGIN
    IF NOT org_exists(NEW.org_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Organizations does not exist';
    END IF;
    
    IF NOT check_organizer(NEW.organizer_email_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Organizer does not exist';
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE insert_event_org_team(IN in_org_id INT, IN in_email_id VARCHAR(75))
BEGIN
	DECLARE count_exists_org_team INT;
    SELECT COUNT(*) INTO count_exists_org_team FROM event_organization_team WHERE org_id = in_org_id AND organizer_email_id = in_email_id;
    IF count_exists_org_team > 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Organizer already exists in the Organization';
	ELSE
		INSERT INTO event_organization_team (org_id, organizer_email_id) VALUES (in_org_id, in_email_id);
	END IF;
END $$
DELIMITER ;

-- 9.1)
DELIMITER $$
CREATE PROCEDURE get_event_org_team(IN in_org_id INT)
BEGIN
	SELECT * FROM event_organization_team;
END $$
DELIMITER 

-- 9.2)
DELIMITER $$
CREATE PROCEDURE get_event_orgs_by_organizer(IN in_user_email VARCHAR(75))
BEGIN
	SELECT eo.* FROM event_organization_team as eot INNER JOIN event_organization as eo ON organizer_email_id = in_user_email;
END $$
DELIMITER 

-- 9.2)
DELIMITER $$
CREATE PROCEDURE delete_organizer_from_org(IN in_user_email VARCHAR(75), IN in_org_id INT)
BEGIN
	DELETE FROM event_organization_team WHERE organizer_email_id = in_user_email AND org_id = in_org_id;
END $$
DELIMITER 

-- 9) INSERT events
DELIMITER $$
CREATE TRIGGER before_event_insert
BEFORE INSERT ON event_record
FOR EACH ROW
BEGIN
    IF NOT org_exists(NEW.org_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Organizations does not exist';
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE insert_event_record( IN in_event_name VARCHAR(75), IN in_event_description VARCHAR(200),
IN in_start_time DATETIME, IN in_end_time DATETIME, IN in_last_registration_date DATETIME, IN in_total_capacity INT, 
IN in_entry_fees FLOAT, IN in_org_id INT)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
		INSERT INTO event_record (event_name, event_description, start_time, end_time, last_registration_date, total_capacity, entry_fees, org_id) 
		VALUES (in_event_name, in_event_description, in_start_time, in_end_time, in_last_registration_date, in_total_capacity, in_entry_fees, in_org_id);
        INSERT INTO event_approval (event_id, date_of_approval, approval_status) VALUES (LAST_INSERT_ID(), CURRENT_TIMESTAMP(), "IN-PROGRESS");
    COMMIT;
END $$
DELIMITER ;

-- 10) Update events -
DELIMITER $$
CREATE TRIGGER before_event_update
BEFORE UPDATE ON event_record
FOR EACH ROW
BEGIN
    IF NOT org_exists(NEW.org_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Organizations does not exist';
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE update_event_record(IN in_event_id INT, IN in_event_name VARCHAR(75), IN in_event_description VARCHAR(200),
IN in_start_time DATETIME, IN in_end_time DATETIME, IN in_last_registration_date DATETIME, IN in_total_capacity INT, 
IN in_entry_fees FLOAT, IN in_org_id INT)
BEGIN
	UPDATE event_record SET event_name = in_event_name,
    event_description = in_event_description,
    start_time = in_start_time,
    end_time = in_end_time,
    last_registration_date = in_last_registration_date,
    total_capacity = in_total_capacity,
    entry_fees = in_entry_fees,
    org_id = in_org_id
	WHERE event_id = in_event_id;
END $$
DELIMITER ;

-- 11) Get events
DELIMITER $$
CREATE PROCEDURE get_event_record(IN in_event_id INT)
BEGIN
	SELECT * FROM (SELECT * FROM event_record where event_id = in_event_id) as in_event_record
    LEFT JOIN event_approval as ae on in_event_record.event_id = ae.event_id;
END $$
DELIMITER ;

-- 11) Get events
DELIMITER $$
CREATE PROCEDURE get_events_by_org_id(IN in_event_status ENUM("APPROVED", "IN-PROGRESS", "REJECTED"), IN in_org_id INT)
BEGIN
	SELECT in_event_record.* FROM (SELECT * FROM event_record where org_id = in_org_id) as in_event_record
    LEFT JOIN event_approval as ae on in_event_record.event_id = ae.event_id WHERE ae.approval_status = in_event_status;
END $$
DELIMITER ;

-- 12) Get all events
DELIMITER $$
CREATE PROCEDURE get_events_by_status(IN in_approval_status ENUM ("APPROVED", "IN-PROGRESS", "REJECTED"))
BEGIN
	SELECT * FROM event_record as er LEFT JOIN event_approval as ea on er.event_id = ea.event_id
    WHERE ea.approval_status = in_approval_status
    ORDER BY last_registration_date DESC;
END $$
DELIMITER ;

-- 12) Get all approved events
DELIMITER $$
CREATE PROCEDURE get_all_approved_events()
BEGIN
	SELECT * FROM event_record as er LEFT JOIN event_approval as ae on er.event_id = ae.event_id
    WHERE ae.approval_status = "APPROVED"
    ORDER BY last_registration_date DESC;
END $$
DELIMITER ;

-- 13) Add admin approval for an event
DELIMITER $$
CREATE TRIGGER before_event_approval_insert
BEFORE INSERT ON event_approval
FOR EACH ROW
BEGIN
    IF NOT event_exists(NEW.event_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event does not exist';
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE insert_event_approval(IN in_event_id INT, IN in_date_of_approval DATETIME,
IN in_approval_status ENUM ("APPROVED", "IN-PROGRESS", "REJECTED"), IN in_approval_desc VARCHAR(200), IN in_admin_email_id VARCHAR(75))
BEGIN

	IF NOT check_admin(in_admin_email_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only admin can update the advertisement';
    END IF;
    INSERT INTO event_approval (event_id, date_of_approval, approval_status, approval_desc) 
    VALUES (in_event_id, in_date_of_approval, in_approval_status, in_approval_desc);
    
    UPDATE event_record SET admin_email_id = in_admin_email_id
    WHERE event_id = in_event_id;
END $$
DELIMITER ;

-- 14) Update admin approval
DELIMITER $$
CREATE TRIGGER before_event_approval_update
BEFORE UPDATE ON event_approval
FOR EACH ROW
BEGIN
	DECLARE event_approval_count INT;
    IF NOT event_exists(NEW.event_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event does not exist';
    END IF;
    SELECT COUNT(*) INTO event_approval_count FROM event_approval WHERE event_id = NEW.event_id;
    IF event_approval_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event has not been approved';
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE update_event_approval( IN in_event_id INT, IN in_date_of_approval DATETIME,
IN in_approval_status ENUM ("APPROVED", "IN-PROGRESS", "REJECTED"), IN in_approval_desc VARCHAR(200), IN in_admin_email_id VARCHAR(75))
BEGIN
	IF NOT check_admin(in_admin_email_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only admin can update the advertisement';
    END IF;
    UPDATE event_approval SET
    date_of_approval = in_date_of_approval,
    approval_status = in_approval_status,
    approval_desc = in_approval_desc
	WHERE event_id = in_event_id;
    
    UPDATE event_record SET admin_email_id = in_admin_email_id
    WHERE event_id = in_event_id;
END $$
DELIMITER ;

-- 15) Insert Credit Card
DELIMITER $$
CREATE PROCEDURE insert_credit_card(IN in_credit_card_num VARCHAR(19), IN in_expiry_date DATE, IN in_name VARCHAR(75),
IN in_email_id VARCHAR(50))
BEGIN
	DECLARE count_credit_card INT;
    DECLARE count_user_credit_card INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
		IF NOT user_exists(in_email_id) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User does not exist';
		END IF;
        SELECT COUNT(*) INTO count_credit_card FROM credit_card as cc where cc.credit_card_num = in_credit_card_num;
		SELECT COUNT(*) INTO count_user_credit_card FROM user_credit_card as ucc where ucc.credit_card_num = in_credit_card_num;
		if count_user_credit_card > 0 THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Credit Card already exists';
		END IF;
        IF count_credit_card = 0 THEN
			INSERT INTO credit_card (credit_card_num, expiry_date, name)
				VALUES (in_credit_card_num, in_expiry_date, in_name);
		END IF;
		INSERT INTO user_credit_card (credit_card_num, email_id)
			VALUES (in_credit_card_num, in_email_id);
	COMMIT;
END $$
DELIMITER ;
-- 15) Update Credit Card
DELIMITER $$
CREATE PROCEDURE update_credit_card(IN in_credit_card_num VARCHAR(19), IN in_expiry_date DATE, IN in_name VARCHAR(75),
IN in_email_id VARCHAR(50))
BEGIN
	DECLARE count_credit_card INT;
    IF NOT user_exists(in_email_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User does not exist';
	END IF;
    SELECT COUNT(*) INTO count_credit_card FROM credit_card as cc where cc.credit_card_num = in_credit_card_num;
    if count_credit_card = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Credit Card does not exists';
	END IF;
    UPDATE credit_card SET
    expiry_date = in_expiry_date,
    name = in_name
    WHERE credit_card_num = in_credit_card_num;
END $$
DELIMITER ;

-- 15.1)
DELIMITER $$
CREATE PROCEDURE get_cc_num(IN in_credit_card_num VARCHAR(19))
BEGIN
	SELECT * FROM credit_card WHERE credit_card_num = in_credit_card_num;
END $$
DELIMITER ;

-- 15.2)
DELIMITER $$
CREATE PROCEDURE get_cc_name(IN in_name VARCHAR(75))
BEGIN
	SELECT * FROM credit_card WHERE name = in_name;
END $$
DELIMITER ;

-- 15.3)
DELIMITER $$
CREATE PROCEDURE get_cc_email_id(IN in_email_id VARCHAR(75))
BEGIN
	SELECT cc.* FROM user_credit_card as ucc INNER JOIN credit_card as cc ON ucc.credit_card_num = cc.credit_card_num WHERE email_id = in_email_id;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE delete_user_cc(IN in_email_id VARCHAR(75), IN in_credit_card_num VARCHAR(19))
BEGIN
	DELETE FROM user_credit_card WHERE email_id = in_email_id AND credit_card_num = in_credit_card_num;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE delete_cc(IN in_credit_card_num VARCHAR(19))
BEGIN
	DELETE FROM credit_card WHERE credit_card_num = in_credit_card_num;
END $$
DELIMITER ;
-- Working from here on....
-- 16) Initiate Transaction
DELIMITER $$
CREATE PROCEDURE initiate_user_transaction(IN in_trans_datetime DATETIME, IN in_email_id VARCHAR(75), IN in_event_id INT, IN num_tickets_request INT)
BEGIN
	DECLARE count_cc INT;
    DECLARE trans_id_added INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
		IF NOT event_exists(in_event_id) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event does not exist';
		END IF;
		IF NOT user_exists(in_email_id) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User does not exist';
		ELSE
			IF get_ticket_left_count(in_event_id) < num_tickets_request THEN
				SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event tickets are not available';
			END IF;
			INSERT INTO user_transaction (trans_datetime, trans_status, email_id, event_id)
			VALUES (in_trans_datetime, "INPROGRESS", in_email_id, in_event_id);	
            SET trans_id_added = LAST_INSERT_ID();
			CALL insert_event_ticket("PENDING", trans_id_added, num_tickets_request);
			SELECT * FROM user_transaction WHERE trans_id = trans_id_added;
		END IF;
    COMMIT;
END $$
DELIMITER ;

-- 16) Successful Transaction
DELIMITER $$
CREATE PROCEDURE successful_user_transaction(IN in_trans_datetime DATETIME, IN in_trans_id INT, IN in_credit_card_num VARCHAR(19))
BEGIN
	DECLARE count_cc INT;
    DECLARE count_trans INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
    SELECT COUNT(*) INTO count_trans FROM user_transaction WHERE trans_id = in_trans_id;
    IF in_credit_card_num IS NOT NULL THEN
		SELECT COUNT(*) INTO count_cc FROM user_credit_card WHERE credit_card_num = in_credit_card_num AND email_id = (SELECT email_id FROM user_transaction WHERE trans_id = in_trans_id);
		IF count_cc = 0 THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Credit Card does not exist';
		END IF;
    END IF;
    IF count_trans = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transaction does not exist';
	END IF;
	UPDATE user_transaction SET trans_datetime = in_trans_datetime,
	trans_status = "COMPLETE",
	credit_card_num = in_credit_card_num
	WHERE trans_id = in_trans_id;
	CALL confirm_event_ticket(in_trans_id);
	SELECT * FROM user_transaction WHERE trans_id = in_trans_id;
    COMMIT;
END $$
DELIMITER ;

-- 16) Successful Transaction
DELIMITER $$
CREATE PROCEDURE failed_user_transaction(IN in_trans_datetime DATETIME, IN in_trans_id INT)
BEGIN
    DECLARE count_trans INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
    SELECT COUNT(*) INTO count_trans FROM user_transaction WHERE trans_id = in_trans_id;
    IF count_trans = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transaction does not exist';
	END IF;
	UPDATE user_transaction SET trans_datetime = in_trans_datetime,
	trans_status = "FAILED"
	WHERE trans_id = in_trans_id;
	CALL deny_event_ticket(in_trans_id);
	SELECT * FROM user_transaction WHERE trans_id = in_trans_id;
    COMMIT;
END $$
DELIMITER ;

-- 17) Update Transaction
DELIMITER $$
CREATE PROCEDURE update_user_transaction (IN in_trans_id INT, IN in_trans_datetime DATETIME,
IN in_trans_status ENUM('COMPLETE', 'INPROGRESS', 'FAILED'), IN in_credit_card_num VARCHAR(19), 
IN in_email_id VARCHAR(75),IN in_event_id INT)
BEGIN
	DECLARE trans_count INT;
	IF NOT event_exists(in_event_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event does not exist';
	END IF;
    SELECT COUNT(*) INTO trans_count from user_transaction WHERE trans_id = in_trans_id;
    IF trans_count = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transaction does not exist';
	ELSE
		UPDATE user_transaction SET trans_datetime = in_trans_datetime,
		trans_status = in_trans_status,
		credit_card_num = in_credit_card_num,
		email_id = in_email_id,
        event_id = in_event_id
		WHERE trans_id = in_trans_id;
	END IF;
END $$
DELIMITER ;



-- 17) Get Event for which user have registered
DELIMITER $$
CREATE PROCEDURE get_user_events

-- 18) Insert comment
DELIMITER $$
CREATE PROCEDURE insert_event_comment(IN in_comment_text VARCHAR(250), IN in_comment_datetime DATETIME, IN in_email_id VARCHAR(50),
IN in_event_id INT)
BEGIN
	IF NOT user_exists(in_email_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User does not exist';
    END IF;
    IF NOT event_exists(in_event_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event does not exist';
    END IF;
    INSERT INTO event_comment (comment_text, comment_datetime, email_id, event_id)
    VALUES (in_comment_text, in_comment_datetime, in_email_id, in_event_id);
END $$
DELIMITER ;

-- 19) Update comment
DELIMITER $$
CREATE PROCEDURE update_event_comment(IN in_comment_id INT,IN in_comment_text VARCHAR(250), IN in_comment_datetime DATETIME, IN in_email_id VARCHAR(50),
IN in_event_id INT)
BEGIN
	DECLARE count_comment INT;
	IF NOT user_exists(in_email_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User does not exist';
    END IF;
    IF NOT event_exists(in_event_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event does not exist';
    END IF;
    SELECT COUNT(*) INTO count_comment FROM event_comment WHERE comment_id = in_comment_id;
    IF count_comment > 0 THEN
		UPDATE event_comment SET comment_text = in_comment_text,
		comment_datetime = in_comment_datetime,
		email_id = in_email_id,
		event_id = in_event_id
		WHERE comment_id = in_comment_id;
	ELSE
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Comment does not exist';
	END IF;
END $$
DELIMITER ;

-- 20) Get comment
DELIMITER $$
CREATE PROCEDURE get_event_comment(IN in_email_id VARCHAR(50), IN in_event_id INT)
BEGIN
	SELECT * FROM event_comment WHERE email_id = in_email_id AND event_id = in_event_id;
END $$
DELIMITER ;

-- 20.1) Get comment
DELIMITER $$
CREATE PROCEDURE delete_event_comment(IN in_comment_id INT)
BEGIN
	DELETE FROM event_comment WHERE comment_id = in_comment_id;
END $$
DELIMITER ;

-- 18) Insert Event Ticket
DELIMITER $$
CREATE PROCEDURE insert_event_ticket(IN in_ticket_status ENUM("CONFIRMED", "PENDING"), IN in_trans_id INT, IN num_tickets_request INT)
BEGIN
	DECLARE count INT DEFAULT 1;
	DECLARE count_transaction INT;
	DECLARE new_ticket_id INT;
	DECLARE in_event_id INT;
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
		SELECT ut.event_id INTO in_event_id FROM event_ticket as et INNER JOIN user_transaction AS ut
		ON et.trans_id = ut.trans_id GROUP BY ut.event_id;
		CREATE TEMPORARY TABLE IF NOT EXISTS temp_ticket_ids (ticket_id INT);
		SELECT COUNT(*) INTO count_transaction FROM user_transaction WHERE trans_id = in_trans_id;
		IF count_transaction = 0 THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transaction does not exist';
		END IF;
		IF num_tickets_request < 0 THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Number of ticket requested should be positive';
		ELSEIF num_tickets_request > get_ticket_left_count(in_event_id) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Numer of tickets requested is more than available seat for the event';
		END IF;
		WHILE count <= num_tickets_request DO
			INSERT INTO event_ticket (ticket_user_name, ticket_status, trans_id)
			VALUES (CONCAT("Ticket", count), in_ticket_status, in_trans_id);
			SET count = count + 1;
			SET new_ticket_id = LAST_INSERT_ID();
			INSERT INTO temp_ticket_ids (ticket_id) VALUES (new_ticket_id);
		END WHILE;
-- 		SELECT et.* FROM event_ticket et INNER JOIN temp_ticket_ids tt ON et.ticket_id = tt.ticket_id;
	COMMIT;
END $$
DELIMITER ;
-- 18.1) Called from Transaction on sucess
DELIMITER $$
CREATE PROCEDURE confirm_event_ticket(IN in_trans_id INT)
BEGIN
    DECLARE trans_count INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
	SELECT COUNT(*) INTO trans_count from user_transaction WHERE trans_id = in_trans_id;
    IF trans_count = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transaction does not exist';
	END IF;
    UPDATE event_ticket SET
	ticket_status = "CONFIRMED"
    WHERE trans_id = in_trans_id;
    COMMIT;
END $$
DELIMITER ;
-- 18.1) Called from Transaction on sucess
DELIMITER $$
CREATE PROCEDURE deny_event_ticket(IN in_trans_id INT)
BEGIN
    DECLARE trans_count INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
	SELECT COUNT(*) INTO trans_count from user_transaction WHERE trans_id = in_trans_id;
    IF trans_count = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transaction does not exist';
	END IF;
    DELETE FROM event_ticket
    WHERE trans_id = in_trans_id;
    COMMIT;
END $$
DELIMITER ;
-- 19) Update Event Ticket
DELIMITER $$
CREATE PROCEDURE update_event_ticket( IN in_ticket_id INT, IN in_ticket_user_name VARCHAR(75),
IN in_id_proof ENUM('PASSPORT', 'STATE_ID', 'DRIVING_LICENSE'), IN in_trans_id INT)
BEGIN
	DECLARE count_transaction INT;
    DECLARE count_ticket INT;
	SELECT COUNT(*) INTO count_transaction FROM user_transaction WHERE trans_id = in_trans_id;
    IF count_transaction = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Transaction does not exist';
	END IF;
    SELECT COUNT(*) INTO count_ticket FROM event_ticket WHERE ticket_id = in_ticket_id;
    IF count_ticket = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ticket does not exist';
	END IF;
    UPDATE event_ticket SET ticket_user_name = in_ticket_user_name,
	id_proof = in_id_proof,
	trans_id = in_trans_id
    WHERE ticket_id = in_ticket_id;
END $$
DELIMITER ;

-- 20) Get transaction using email 
DELIMITER $$
CREATE PROCEDURE get_user_transaction(IN in_email_id varchar(100))
BEGIN
	SELECT * FROM user_transaction WHERE email_id = in_email_id;
END $$
DELIMITER ;

-- 20) Get Ticket using email 
DELIMITER $$
CREATE PROCEDURE get_user_ticket(IN in_email_id varchar(100))
BEGIN
	SELECT * FROM event_ticket inner join user_transaction using (trans_id) WHERE email_id = in_email_id;
END $$
DELIMITER ;

-- 20.1) Get event tickets using event_id
DELIMITER $$
CREATE PROCEDURE get_all_event_ticket(IN in_event_id INT)
BEGIN
	SELECT et.*, ut.event_id FROM (SELECT * FROM user_transaction WHERE event_id = in_event_id) as ut INNER JOIN event_ticket as et ON ut.trans_id = et.trans_id;
END $$
DELIMITER ;

-- 21) Add event image (Should we handle who updated the image)
DELIMITER $$
CREATE PROCEDURE insert_event_image(IN in_event_img_loc VARCHAR(150), IN in_event_id INT)
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
    END;
    START TRANSACTION;
		IF NOT event_exists(in_event_id) THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event does not exist';
		END IF;
		INSERT INTO event_image (event_img_loc) VALUES (in_event_img_loc);
        INSERT INTO event_image_fk (event_id, event_img_id) VALUES (in_event_id, LAST_INSERT_ID());
    COMMIT;
END $$
DELIMITER ;
-- 22) Update event images
DELIMITER $$
CREATE PROCEDURE update_event_image(IN in_img_id INT, IN in_event_img_loc VARCHAR(150))
BEGIN
	DECLARE count_event_img INT;
    SELECT COUNT(*) INTO count_event_img FROM event_image WHERE event_img_id = in_img_id;
	IF count_event_img = 0 THEN
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event Image does not exist';
	END IF;
	UPDATE event_image SET event_img_loc = in_event_img_loc
    WHERE event_img_id = in_img_id;
END $$
DELIMITER ;
-- 23) Get all event images, update event image
DELIMITER $$
CREATE PROCEDURE get_event_images(IN in_event_id INT)
BEGIN
	SELECT ei.event_img_id, ei.event_img_loc FROM (SELECT * FROM event_image_fk WHERE event_id = in_event_id) as ei_fk
    LEFT JOIN event_image as ei ON ei_fk.event_img_id = ei.event_img_id;
END $$
DELIMITER ;
-- 23.1) DELETE whole image
DELIMITER $$
CREATE PROCEDURE delete_event_images(IN in_event_img_id INT)
BEGIN
	DELETE FROM event_image WHERE event_img_id = in_event_img_id;
END $$
DELIMITER ;
-- DELETE JUST FROM CURRENT EVENT
DELIMITER $$
CREATE PROCEDURE delete_event_images_fk(IN in_event_id INT, IN in_event_img_id INT)
BEGIN
	DELETE FROM event_image_fk WHERE event_id = in_event_id AND event_img_id = in_event_img_id;
END $$
DELIMITER ;

-- 24) Insert Advertisements
DELIMITER $$
CREATE PROCEDURE insert_advertisement(IN in_ad_title VARCHAR(60), IN in_ad_img_loc VARCHAR(100),IN in_ad_begin_time DATETIME,
IN in_ad_end_time DATETIME,IN in_ad_org_id INT)
BEGIN
    IF NOT org_exists(in_ad_org_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Organization does not exist';
    END IF;
    INSERT INTO advertisement (ad_title, ad_img_loc, ad_begin_time, ad_end_time, ad_org_id)
    VALUES (in_ad_title, in_ad_img_loc, in_ad_begin_time, in_ad_end_time, in_ad_org_id);
    call insert_ad_approval(last_insert_id(), current_timestamp(), "IN-PROGRESS", "", null);
END$$
DELIMITER ;

-- 25) Update Adrevtisement
DELIMITER $$

CREATE PROCEDURE update_advertisement(IN in_ad_id INT, IN in_ad_title VARCHAR(60), IN in_ad_img_loc VARCHAR(100),IN in_ad_begin_time DATETIME,
    IN in_ad_end_time DATETIME, IN in_ad_org_id INT)
BEGIN
	DECLARE count_ad INT;
    SELECT COUNT(*) INTO count_ad FROM advertisement WHERE ad_id = in_ad_id;
    IF NOT org_exists(in_ad_org_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Organization does not exist';
    END IF;
    IF count_ad = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Advertisement does not exist';
    END IF;
    UPDATE advertisement
    SET ad_title = in_ad_title,
	ad_img_loc = in_ad_img_loc,
	ad_begin_time = in_ad_begin_time,
	ad_end_time = in_ad_end_time,
	ad_org_id = in_ad_org_id
    WHERE ad_id = in_ad_id;
    select * from advertisement where ad_id = in_ad_id;
END$$
DELIMITER ;

-- 26) Add admin approval for an event
DELIMITER $$
CREATE TRIGGER before_ad_approval_insert
BEFORE INSERT ON advertisement_approval
FOR EACH ROW
BEGIN
    IF NOT advertisement_exists(NEW.ad_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Advertisement does not exist';
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE insert_ad_approval( IN in_ad_id INT, IN in_date_of_approval DATETIME,
IN in_approval_status ENUM ("APPROVED", "IN-PROGRESS", "REJECTED"), IN in_approval_desc VARCHAR(200), IN in_admin_email_id VARCHAR(75))
BEGIN

	IF NOT check_admin(in_admin_email_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only admin can provide approval to advertisement';
    END IF;
    INSERT INTO advertisement_approval (ad_id, date_of_approval, approval_status, approval_desc) 
    VALUES (in_ad_id, in_date_of_approval, in_approval_status, in_approval_desc);
    
    UPDATE advertisement SET admin_email_id = in_admin_email_id
    WHERE ad_id = in_ad_id;
END $$
DELIMITER ;

-- 27) Update admin approval
DELIMITER $$
CREATE TRIGGER before_ad_approval_update
BEFORE UPDATE ON advertisement_approval
FOR EACH ROW
BEGIN
	DECLARE ad_approval_count INT;
    IF NOT advertisement_exists(NEW.ad_id) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Advertisement does not exist';
    END IF;
    SELECT COUNT(*) INTO ad_approval_count FROM advertisement_approval WHERE ad_id = NEW.ad_id;
    IF ad_approval_count = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ad has not been reviewed';
    END IF;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE update_ad_approval( IN in_ad_id INT, IN in_date_of_approval DATETIME,
IN in_approval_status ENUM ("APPROVED", "IN-PROGRESS", "REJECTED"), IN in_approval_desc VARCHAR(200), IN in_admin_email_id VARCHAR(75))
BEGIN
	IF NOT check_admin(in_admin_email_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only admin can update the advertisement approval';
    END IF;
    UPDATE advertisement_approval SET
    date_of_approval = in_date_of_approval,
    approval_status = in_approval_status,
    approval_desc = in_approval_desc
	WHERE ad_id = in_ad_id;
    
    UPDATE advertisement SET admin_email_id = in_admin_email_id
    WHERE ad_id = in_ad_id;
END $$
DELIMITER ;

-- 28) Get all ads
DELIMITER $$
CREATE PROCEDURE get_ads_by_status(IN in_approval_status ENUM ("APPROVED", "IN-PROGRESS", "REJECTED"))
BEGIN
	SELECT * FROM advertisement as ad LEFT JOIN advertisement_approval as aa on ad.ad_id = aa.ad_id
    WHERE aa.approval_status = in_approval_status
    ORDER BY ad.ad_begin_time DESC;
END $$
DELIMITER ;

-- 28) Get all ads
DELIMITER $$
CREATE PROCEDURE get_ads_by_status_org_id(IN in_approval_status ENUM ("APPROVED", "IN-PROGRESS", "REJECTED"), IN in_ad_org_id INT)
BEGIN
	SELECT ad.* FROM (SELECT * FROM advertisement WHERE ad_org_id = in_ad_org_id) as ad LEFT JOIN advertisement_approval as aa on ad.ad_id = aa.ad_id
    WHERE aa.approval_status = in_approval_status
    ORDER BY ad.ad_begin_time DESC;
END $$
DELIMITER ;

-- 29) Get all approved ads
DELIMITER $$
CREATE PROCEDURE get_all_approved_ads()
BEGIN
	SELECT * FROM advertisement as ad LEFT JOIN advertisement_approval as aa on ad.ad_id = aa.ad_id
    WHERE aa.approval_status = "APPROVED" AND ad.ad_begin_time <= CURRENT_TIMESTAMP() AND ad.ad_end_time > CURRENT_TIMESTAMP()
    ORDER BY RAND();
END $$
DELIMITER ;
-- 29.1) Delete ads
DELIMITER $$
CREATE PROCEDURE delete_ads(IN in_ad_id INT)
BEGIN
	DELETE FROM advertisement WHERE ad_id = in_ad_id;
END $$
DELIMITER ;

-- 30) Add Insert procedure for event sruvey
DELIMITER $$
CREATE PROCEDURE insert_event_survey(IN in_event_id INT, IN in_survey_url VARCHAR(100), IN in_org_id INT)
BEGIN
	IF NOT event_exists(in_event_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event does not exist';
    END IF;
    IF NOT org_exists(in_org_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only valid organization can add the survey';
    END IF;
    INSERT INTO event_survey (event_id, survey_url, org_id)
    VALUES (in_event_id, in_survey_url, in_org_id);
END$$
DELIMITER ;

-- 31) Update procedure for event survey
DELIMITER $$
CREATE PROCEDURE update_event_survey(IN in_event_id INT, IN in_survey_url VARCHAR(100), IN in_org_id INT)
BEGIN
	IF NOT event_exists(in_event_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event does not exist';
    END IF;
    IF NOT org_exists(in_org_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Only valid organization can add the survey';
    END IF;
    UPDATE event_survey SET survey_url = in_survey_url,
	org_id = in_org_id
    WHERE event_id = in_event_id;
END$$
DELIMITER ;

-- 33) Get event survey for a event
DELIMITER $$
CREATE PROCEDURE get_event_survey(IN in_event_id INT)
BEGIN
    SELECT * FROM event_survey WHERE event_id = in_event_id;
END$$
DELIMITER ;

-- 33) Get event survey for a event
DELIMITER $$
CREATE PROCEDURE get_event_survey_by_org_id(IN in_org_id INT)
BEGIN
    SELECT * FROM event_survey WHERE org_id = in_org_id;
END$$
DELIMITER ;

-- 32) Insert procedure for event response 
DELIMITER $$
CREATE PROCEDURE insert_survey_response(IN in_event_id INT, IN in_user_email_id VARCHAR(75),IN in_survey_response_loc VARCHAR(100))
BEGIN
	IF NOT event_exists(in_event_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event does not exist';
    END IF;
    INSERT INTO survey_response (event_id, user_email_id, survey_response_loc)
    VALUES (in_event_id, in_user_email_id, in_survey_response_loc);
END$$
DELIMITER ;
-- 33) Update procedure for event response 
DELIMITER $$
CREATE PROCEDURE update_survey_response(IN in_event_id INT, IN in_user_email_id VARCHAR(75), IN in_survey_response_loc VARCHAR(100))
BEGIN
	IF NOT event_exists(in_event_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event does not exist';
    END IF;
    UPDATE survey_response
    SET survey_response_loc = in_survey_response_loc
    WHERE event_id = in_event_id AND user_email_id = in_user_email_id;
END$$
DELIMITER ;

-- 33) Get event survey response for a event
DELIMITER $$
CREATE PROCEDURE get_event_survey_response(IN in_event_id INT)
BEGIN
    SELECT * FROM survey_response WHERE event_id = in_event_id;
END$$
DELIMITER ;

-- 33) Get event survey response by org id for a event
DELIMITER $$
CREATE PROCEDURE get_event_survey_response_by_org_id(IN in_org_id INT)
BEGIN
    SELECT sr.* FROM survey_response as sr INNER JOIN event_record AS er ON sr.event_id = er.event_id WHERE er.org_id = in_org_id;
END$$
DELIMITER ;


-- 34) Insert venue for event
DELIMITER $$
CREATE PROCEDURE insert_event_venue(IN in_address_id INT, IN in_event_id INT)
BEGIN
	DECLARE address_count INT;
    DECLARE event_venue_count INT;
	IF NOT event_exists(in_event_id) THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Event does not exist';
    END IF;
    SELECT COUNT(*) INTO event_venue_count FROM event_venue WHERE address_id = in_address_id AND event_id = in_event_id;
    SELECT COUNT(*) INTO address_count FROM address WHERE address_id = in_address_id;
    IF address_count = 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Address does not exist';
    END IF;
    IF event_venue_count > 0 THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Venue already present';
    END IF;
    INSERT INTO event_venue (address_id, event_id) VALUES (in_address_id, in_event_id);
END$$
DELIMITER ;

-- 35) Get event venue
DELIMITER $$
CREATE PROCEDURE get_event_venue(IN in_event_id INT)
BEGIN
	SELECT ad.* FROM (SELECT * FROM event_record WHERE event_id = in_event_id) AS er
    INNER JOIN event_venue as ev ON er.event_id = ev.event_id
    INNER JOIN address as ad ON ev.address_id = ad.address_id;
END$$
DELIMITER ;
-- 35) Update venue
DELIMITER $$

CREATE PROCEDURE update_event_address(IN in_address_id INT, IN in_event_id INT, IN new_address_id INT)
BEGIN
    UPDATE event_venue
    SET address_id = new_address_id
    WHERE address_id = in_address_id AND event_id = in_event_id;
END$$
DELIMITER ;

-- All for testing purpose
INSERT INTO user_credential (email_id, user_password)
VALUES 
    ('bilwal.sagar@gmail.com', '{bcrypt}$2a$10$Ztf6/zxMMCZ644Jf6/1fKeV/y38CENLpbMZpkZNit6DJXj3KOeVR2'),
    ('div_shekhar@gmail.com', '{bcrypt}$2a$10$Ztf6/zxMMCZ644Jf6/1fKeV/y38CENLpbMZpkZNit6DJXj3KOeVR2');
    
INSERT INTO user_detail (email_id, first_name, last_name, date_of_birth, user_img, is_admin, is_organizer)
VALUES 
    ("bilwal.sagar@gmail.com", "Sagar", "Bilwal", "1999-04-30", "sagar.jpg", true, true),
    ("div_shekhar@gmail.com", "Divyendu", "Shekhar", "1997-08-30", "div.jpg", true, false);