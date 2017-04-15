USE db_iote2e_batch;

DROP TABLE IF EXISTS pills_dispensed;
CREATE TABLE pills_dispensed (
	pills_dispensed_uuid CHAR(36) NOT NULL PRIMARY KEY,
	login_name VARCHAR(128) NOT NULL,
	actuator_name VARCHAR(128) NOT NULL,
	dispense_state char(12) NOT NULL,
	num_to_dispense INT DEFAULT NULL,
	num_dispensed INT DEFAULT NULL,
	delta INT DEFAULT NULL,
	state_pending_ts datetime(3) DEFAULT NULL,
	state_dispensing_ts datetime(3) DEFAULT NULL,
	state_complete_ts datetime(3) DEFAULT NULL,
	insert_ts timestamp DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS pills_dispensed_image;
CREATE TABLE pills_dispensed_image (
	pills_dispensed_uuid CHAR(36) NOT NULL PRIMARY KEY,
	image_png BLOB NOT NULL,
	insert_ts timestamp DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS bp_series;
CREATE TABLE bp_series (
	bp_series_uuid CHAR(36) NOT NULL PRIMARY KEY,
	login_name VARCHAR(128) NOT NULL,
	systolic int NOT NULL,
	diastolic int NOT NULL,
	bp_series_ts timestamp DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS subject;
CREATE TABLE subject (
	subject_uuid CHAR(36) NOT NULL PRIMARY KEY,
	login_name VARCHAR(128) NOT NULL,
	age int NOT NULL,
	gender char(1) NOT NULL,
	insert_ts timestamp DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into subject (subject_uuid,login_name,age,gender) VALUE( UUID(), 'pzybrick1', 56, 'M');
insert into subject (subject_uuid,login_name,age,gender) VALUE( UUID(), 'jdoe2', 35, 'F');

insert into bp_series (bp_series_uuid,login_name,systolic,diastolic,bp_series_ts) VALUES( UUID(), 'pzybrick1', 118, 80, '2017-04-01T00:00:00.000');
insert into bp_series (bp_series_uuid,login_name,systolic,diastolic,bp_series_ts) VALUES( UUID(), 'pzybrick1', 122, 84, '2017-04-02T00:00:00.000');
insert into bp_series (bp_series_uuid,login_name,systolic,diastolic,bp_series_ts) VALUES( UUID(), 'jdoe2', 110, 70, '2017-04-01T00:00:00.000');
insert into bp_series (bp_series_uuid,login_name,systolic,diastolic,bp_series_ts) VALUES( UUID(), 'jdoe2', 114, 74, '2017-04-02T00:00:00.000');


select * from bp_series;
select login_name, avg(diastolic) from bp_series group by login_name;

insert into pills_dispensed (pills_dispensed_uuid,login_name,actuator_name,dispense_state,num_to_dispense)
	select uuid() as pills_dispensed_uuid, subj.login_name, 'pilldisp1' as actuator_name, 'PENDING' as dispense_state, 
		case when avg(bp.diastolic) < 80 THEN 1 
			when avg(bp.diastolic) < 90 THEN 2 
			else 3 
		end as num_to_dispense
	from subject as subj, bp_series as bp 
	where bp.login_name=subj.login_name 
	group by subj.login_name;

insert into pills_dispensed (pills_dispensed_uuid,login_name,actuator_name,dispense_state,num_to_dispense)
	select uuid() as pills_dispensed_uuid, subj.login_name, 'pilldisp1' as actuator_name, 'PENDING' as dispense_state, 
		case when avg(bp.diastolic) < 80 THEN 1 
			when avg(bp.diastolic) < 90 AND subj.age > 50 THEN 3 
			when avg(bp.diastolic) < 90 THEN 2 
			else 3 
		end as num_to_dispense
	from subject as subj, bp_series as bp 
	where bp.login_name=subj.login_name 
	group by subj.login_name;

