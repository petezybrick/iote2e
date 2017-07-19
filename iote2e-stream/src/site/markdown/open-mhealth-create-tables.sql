DROP TABLE IF EXISTS blood_glucose;
CREATE TABLE blood_glucose (
	blood_glucose_uuid CHAR(36) NOT NULL PRIMARY KEY,
	hdr_source_name VARCHAR(255) NOT NULL,
	hdr_source_creation_date_time datetime(3) NOT NULL,
	hdr_user_id VARCHAR(255) NOT NULL,
	hdr_modality VARCHAR(255) NOT NULL,
	hdr_schema_namespace VARCHAR(255) NOT NULL,
	hdr_schema_version VARCHAR(255) NOT NULL,
	effective_time_frame datetime(3) NOT NULL,
	descriptive_statistic VARCHAR(255),
	user_notes VARCHAR(255),
	blood_specimen_type VARCHAR(255),
	temporal_relationship_to_meal VARCHAR(255),
	temporal_relationship_to_sleep VARCHAR(255),
	blood_glucose_unit VARCHAR(255) NOT NULL,
	blood_glucose_value INTEGER NOT NULL,
	insert_ts timestamp(3)  DEFAULT CURRENT_TIMESTAMP(3),
	KEY ix_blood_glucose_user_ts(hdr_user_id,effective_time_frame)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS blood_pressure;
CREATE TABLE blood_pressure (
	blood_pressure_uuid CHAR(36) NOT NULL PRIMARY KEY,
	hdr_source_name VARCHAR(255) NOT NULL,
	hdr_source_creation_date_time datetime(3) NOT NULL,
	hdr_user_id VARCHAR(255) NOT NULL,
	hdr_modality VARCHAR(255) NOT NULL,
	hdr_schema_namespace VARCHAR(255) NOT NULL,
	hdr_schema_version VARCHAR(255) NOT NULL,
	effective_time_frame datetime(3) NOT NULL,
	descriptive_statistic VARCHAR(255),
	user_notes VARCHAR(255),
	position_during_measurement VARCHAR(255),
	systolic_blood_pressure_unit VARCHAR(255) NOT NULL,
	systolic_blood_pressure_value INTEGER NOT NULL,
	diastolic_blood_pressure_unit VARCHAR(255) NOT NULL,
	diastolic_blood_pressure_value INTEGER NOT NULL,
	insert_ts timestamp(3)  DEFAULT CURRENT_TIMESTAMP(3),
	KEY ix_blood_pressure_user_ts(hdr_user_id,effective_time_frame)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS body_temperature;
CREATE TABLE body_temperature (
	body_temperature_uuid CHAR(36) NOT NULL PRIMARY KEY,
	hdr_source_name VARCHAR(255) NOT NULL,
	hdr_source_creation_date_time datetime(3) NOT NULL,
	hdr_user_id VARCHAR(255) NOT NULL,
	hdr_modality VARCHAR(255) NOT NULL,
	hdr_schema_namespace VARCHAR(255) NOT NULL,
	hdr_schema_version VARCHAR(255) NOT NULL,
	effective_time_frame datetime(3) NOT NULL,
	descriptive_statistic VARCHAR(255),
	user_notes VARCHAR(255),
	measurement_location VARCHAR(255),
	body_temperature_unit VARCHAR(255) NOT NULL,
	body_temperature_value DECIMAL(4,1) NOT NULL,
	insert_ts timestamp(3)  DEFAULT CURRENT_TIMESTAMP(3),
	KEY ix_body_temperature_user_ts(hdr_user_id,effective_time_frame)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS heart_rate;
CREATE TABLE heart_rate (
	heart_rate_uuid CHAR(36) NOT NULL PRIMARY KEY,
	hdr_source_name VARCHAR(255) NOT NULL,
	hdr_source_creation_date_time datetime(3) NOT NULL,
	hdr_user_id VARCHAR(255) NOT NULL,
	hdr_modality VARCHAR(255) NOT NULL,
	hdr_schema_namespace VARCHAR(255) NOT NULL,
	hdr_schema_version VARCHAR(255) NOT NULL,
	effective_time_frame datetime(3) NOT NULL,
	user_notes VARCHAR(255),
	temporal_relationship_to_physical_activity VARCHAR(255),
	heart_rate_unit VARCHAR(255) NOT NULL,
	heart_rate_value INTEGER NOT NULL,
	insert_ts timestamp(3)  DEFAULT CURRENT_TIMESTAMP(3),
	KEY ix_heart_rate_user_ts(hdr_user_id,effective_time_frame)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS hk_workout;
CREATE TABLE hk_workout (
	hk_workout_uuid CHAR(36) NOT NULL PRIMARY KEY,
	hdr_source_name VARCHAR(255) NOT NULL,
	hdr_source_creation_date_time datetime(3) NOT NULL,
	hdr_user_id VARCHAR(255) NOT NULL,
	hdr_modality VARCHAR(255) NOT NULL,
	hdr_schema_namespace VARCHAR(255) NOT NULL,
	hdr_schema_version VARCHAR(255) NOT NULL,
	effective_time_frame datetime(3) NOT NULL,
	user_notes VARCHAR(255),
	activity_name VARCHAR(255),
	distance_unit VARCHAR(255) NOT NULL,
	distance_value INTEGER NOT NULL,
	kcal_burned_unit VARCHAR(255) NOT NULL,
	kcal_burned_value INTEGER NOT NULL,
	insert_ts timestamp(3)  DEFAULT CURRENT_TIMESTAMP(3),
	KEY ix_hk_workout_user_ts(hdr_user_id,effective_time_frame)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS respiratory_rate;
CREATE TABLE respiratory_rate (
	respiratory_rate_uuid CHAR(36) NOT NULL PRIMARY KEY,
	hdr_source_name VARCHAR(255) NOT NULL,
	hdr_source_creation_date_time datetime(3) NOT NULL,
	hdr_user_id VARCHAR(255) NOT NULL,
	hdr_modality VARCHAR(255) NOT NULL,
	hdr_schema_namespace VARCHAR(255) NOT NULL,
	hdr_schema_version VARCHAR(255) NOT NULL,
	effective_time_frame datetime(3) NOT NULL,
	user_notes VARCHAR(255),
	descriptive_statistic VARCHAR(255),
	temporal_relationship_to_physical_activity VARCHAR(255),
	respiratory_rate_unit VARCHAR(255) NOT NULL,
	respiratory_rate_value DECIMAL(4,1) NOT NULL,
	insert_ts timestamp(3)  DEFAULT CURRENT_TIMESTAMP(3),
	KEY ix_respiratory_rate_user_ts(hdr_user_id,effective_time_frame)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
