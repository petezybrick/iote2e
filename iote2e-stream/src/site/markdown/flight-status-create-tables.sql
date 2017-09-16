DROP TABLE IF EXISTS flight_status;
CREATE TABLE flight_status (
	flight_status_uuid CHAR(36) NOT NULL PRIMARY KEY,
	airframe_Uuid VARCHAR(255) NOT NULL,
	flight_number CHAR(5) NOT NULL,
	from_airport CHAR(3) NOT NULL,
	to_airport CHAR(3) NOT NULL,
	lat DECIMAL(10,8) NOT NULL, 
	lng DECIMAL(11,8) NOT NULL,
	alt DECIMAL(8,2) NOT NULL,
	airspeed DECIMAL(6,2) NOT NULL,
	heading DECIMAL(5,2) NOT NULL,
	flight_status_ts datetime(3) NOT NULL,
	insert_ts timestamp(3) DEFAULT CURRENT_TIMESTAMP(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS engine_status;
CREATE TABLE engine_status (
	engine_status_uuid CHAR(36) NOT NULL PRIMARY KEY,
	flight_status_uuid CHAR(36) NOT NULL,
	engine_uuid CHAR(36) NOT NULL,
	engine_number INTEGER NOT NULL,	
	oil_temp_c DECIMAL(5,2) NOT NULL,
	oil_pressure DECIMAL(5,2) NOT NULL,
	exhaust_gas_temp_c DECIMAL(6,2) NOT NULL,
	n1_pct DECIMAL(5,2) NOT NULL,
	n2_pct DECIMAL(5,2) NOT NULL,	
	engine_status_ts datetime(3) NOT NULL,
	insert_ts timestamp(3) DEFAULT CURRENT_TIMESTAMP(3),
	CONSTRAINT fk_flight_status FOREIGN KEY (flight_status_uuid)
  		REFERENCES flight_status(flight_status_uuid)
  		ON DELETE CASCADE
  		ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


