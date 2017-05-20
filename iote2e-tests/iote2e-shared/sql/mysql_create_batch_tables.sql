USE db_iote2e_batch;
DROP TABLE IF EXISTS temperature;
CREATE TABLE temperature (
	request_uuid CHAR(36) NOT NULL PRIMARY KEY,
	login_name VARCHAR(128) NOT NULL,
	source_name VARCHAR(128) NOT NULL,
	request_timestamp datetime(3) NOT NULL,
	degrees_c DECIMAL(6,2) NOT NULL,
	insert_ts timestamp  DEFAULT CURRENT_TIMESTAMP,
	KEY ix_temperature_request_ts(request_timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS humidity;
CREATE TABLE humidity (
	request_uuid CHAR(36) NOT NULL PRIMARY KEY,
	login_name VARCHAR(128) NOT NULL,
	source_name VARCHAR(128) NOT NULL,
	request_timestamp datetime(3) NOT NULL,
	pct_humidity DECIMAL(6,2) NOT NULL,
	insert_ts timestamp  DEFAULT CURRENT_TIMESTAMP,
	KEY ix_humidity_request_ts(request_timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS switch;
CREATE TABLE switch (
	request_uuid CHAR(36) NOT NULL PRIMARY KEY,
	login_name VARCHAR(128) NOT NULL,
	source_name VARCHAR(128) NOT NULL,
	request_timestamp datetime(3) NOT NULL,
	switch_state INTEGER NOT NULL,
	insert_ts timestamp  DEFAULT CURRENT_TIMESTAMP,
	KEY ix_switch_request_ts(request_timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS heartbeat;
CREATE TABLE heartbeat (
	request_uuid CHAR(36) NOT NULL PRIMARY KEY,
	login_name VARCHAR(128) NOT NULL,
	source_name VARCHAR(128) NOT NULL,
	request_timestamp datetime(3) NOT NULL,
	heartbeat_state TINYINT(1) NOT NULL,
	insert_ts timestamp DEFAULT CURRENT_TIMESTAMP,
	KEY ix_heartbeat_request_ts(request_timestamp)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
