package com.pzybrick.iote2e.stream.persist;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BloodPressureVo {
	private static final Logger logger = LogManager.getLogger(BloodPressureVo.class);
	private String bloodPressureUuid;
	private String hdrSourceName;
	private Timestamp hdrSourceCreationDateTime;
	private String hdrUserId;
	private String hdrModality;
	private String hdrSchemaNamespace;
	private String hdrSchemaVersion;
	private Timestamp effectiveTimeFrame;
	private String descriptiveStatistic;
	private String userNotes;
	private String positionDuringMeasurement;
	private String systolicBloodPressureUnit;
	private int systolicBloodPressureValue;
	private String diastolicBloodPressureUnit;
	private int diastolicBloodPressureValue;
	private Timestamp insertTs;


	public BloodPressureVo() {
	}


	public BloodPressureVo(ResultSet rs) throws SQLException {
		this.bloodPressureUuid = rs.getString("blood_pressure_uuid");
		this.hdrSourceName = rs.getString("hdr_source_name");
		this.hdrSourceCreationDateTime = rs.getTimestamp("hdr_source_creation_date_time");
		this.hdrUserId = rs.getString("hdr_user_id");
		this.hdrModality = rs.getString("hdr_modality");
		this.hdrSchemaNamespace = rs.getString("hdr_schema_namespace");
		this.hdrSchemaVersion = rs.getString("hdr_schema_version");
		this.effectiveTimeFrame = rs.getTimestamp("effective_time_frame");
		this.descriptiveStatistic = rs.getString("descriptive_statistic");
		this.userNotes = rs.getString("user_notes");
		this.positionDuringMeasurement = rs.getString("position_during_measurement");
		this.systolicBloodPressureUnit = rs.getString("systolic_blood_pressure_unit");
		this.systolicBloodPressureValue = rs.getInt("systolic_blood_pressure_value");
		this.diastolicBloodPressureUnit = rs.getString("diastolic_blood_pressure_unit");
		this.diastolicBloodPressureValue = rs.getInt("diastolic_blood_pressure_value");
		this.insertTs = rs.getTimestamp("insert_ts");
	}


	public String getBloodPressureUuid() {
		return this.bloodPressureUuid;
	}
	public String getHdrSourceName() {
		return this.hdrSourceName;
	}
	public Timestamp getHdrSourceCreationDateTime() {
		return this.hdrSourceCreationDateTime;
	}
	public String getHdrUserId() {
		return this.hdrUserId;
	}
	public String getHdrModality() {
		return this.hdrModality;
	}
	public String getHdrSchemaNamespace() {
		return this.hdrSchemaNamespace;
	}
	public String getHdrSchemaVersion() {
		return this.hdrSchemaVersion;
	}
	public Timestamp getEffectiveTimeFrame() {
		return this.effectiveTimeFrame;
	}
	public String getDescriptiveStatistic() {
		return this.descriptiveStatistic;
	}
	public String getUserNotes() {
		return this.userNotes;
	}
	public String getPositionDuringMeasurement() {
		return this.positionDuringMeasurement;
	}
	public String getSystolicBloodPressureUnit() {
		return this.systolicBloodPressureUnit;
	}
	public int getSystolicBloodPressureValue() {
		return this.systolicBloodPressureValue;
	}
	public String getDiastolicBloodPressureUnit() {
		return this.diastolicBloodPressureUnit;
	}
	public int getDiastolicBloodPressureValue() {
		return this.diastolicBloodPressureValue;
	}
	public Timestamp getInsertTs() {
		return this.insertTs;
	}


	public BloodPressureVo setBloodPressureUuid( String bloodPressureUuid ) {
		this.bloodPressureUuid = bloodPressureUuid;
		return this;
	}
	public BloodPressureVo setHdrSourceName( String hdrSourceName ) {
		this.hdrSourceName = hdrSourceName;
		return this;
	}
	public BloodPressureVo setHdrSourceCreationDateTime( Timestamp hdrSourceCreationDateTime ) {
		this.hdrSourceCreationDateTime = hdrSourceCreationDateTime;
		return this;
	}
	public BloodPressureVo setHdrUserId( String hdrUserId ) {
		this.hdrUserId = hdrUserId;
		return this;
	}
	public BloodPressureVo setHdrModality( String hdrModality ) {
		this.hdrModality = hdrModality;
		return this;
	}
	public BloodPressureVo setHdrSchemaNamespace( String hdrSchemaNamespace ) {
		this.hdrSchemaNamespace = hdrSchemaNamespace;
		return this;
	}
	public BloodPressureVo setHdrSchemaVersion( String hdrSchemaVersion ) {
		this.hdrSchemaVersion = hdrSchemaVersion;
		return this;
	}
	public BloodPressureVo setEffectiveTimeFrame( Timestamp effectiveTimeFrame ) {
		this.effectiveTimeFrame = effectiveTimeFrame;
		return this;
	}
	public BloodPressureVo setDescriptiveStatistic( String descriptiveStatistic ) {
		this.descriptiveStatistic = descriptiveStatistic;
		return this;
	}
	public BloodPressureVo setUserNotes( String userNotes ) {
		this.userNotes = userNotes;
		return this;
	}
	public BloodPressureVo setPositionDuringMeasurement( String positionDuringMeasurement ) {
		this.positionDuringMeasurement = positionDuringMeasurement;
		return this;
	}
	public BloodPressureVo setSystolicBloodPressureUnit( String systolicBloodPressureUnit ) {
		this.systolicBloodPressureUnit = systolicBloodPressureUnit;
		return this;
	}
	public BloodPressureVo setSystolicBloodPressureValue( int systolicBloodPressureValue ) {
		this.systolicBloodPressureValue = systolicBloodPressureValue;
		return this;
	}
	public BloodPressureVo setDiastolicBloodPressureUnit( String diastolicBloodPressureUnit ) {
		this.diastolicBloodPressureUnit = diastolicBloodPressureUnit;
		return this;
	}
	public BloodPressureVo setDiastolicBloodPressureValue( int diastolicBloodPressureValue ) {
		this.diastolicBloodPressureValue = diastolicBloodPressureValue;
		return this;
	}
	public BloodPressureVo setInsertTs( Timestamp insertTs ) {
		this.insertTs = insertTs;
		return this;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bloodPressureUuid == null) ? 0 : bloodPressureUuid.hashCode());
		result = prime * result + ((descriptiveStatistic == null) ? 0 : descriptiveStatistic.hashCode());
		result = prime * result + ((diastolicBloodPressureUnit == null) ? 0 : diastolicBloodPressureUnit.hashCode());
		result = prime * result + diastolicBloodPressureValue;
		result = prime * result + ((effectiveTimeFrame == null) ? 0 : effectiveTimeFrame.hashCode());
		result = prime * result + ((hdrModality == null) ? 0 : hdrModality.hashCode());
		result = prime * result + ((hdrSchemaNamespace == null) ? 0 : hdrSchemaNamespace.hashCode());
		result = prime * result + ((hdrSchemaVersion == null) ? 0 : hdrSchemaVersion.hashCode());
		result = prime * result + ((hdrSourceCreationDateTime == null) ? 0 : hdrSourceCreationDateTime.hashCode());
		result = prime * result + ((hdrSourceName == null) ? 0 : hdrSourceName.hashCode());
		result = prime * result + ((hdrUserId == null) ? 0 : hdrUserId.hashCode());
		result = prime * result + ((positionDuringMeasurement == null) ? 0 : positionDuringMeasurement.hashCode());
		result = prime * result + ((systolicBloodPressureUnit == null) ? 0 : systolicBloodPressureUnit.hashCode());
		result = prime * result + systolicBloodPressureValue;
		result = prime * result + ((userNotes == null) ? 0 : userNotes.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BloodPressureVo other = (BloodPressureVo) obj;
		if (bloodPressureUuid == null) {
			if (other.bloodPressureUuid != null)
				return false;
		} else if (!bloodPressureUuid.equals(other.bloodPressureUuid))
			return false;
		if (descriptiveStatistic == null) {
			if (other.descriptiveStatistic != null)
				return false;
		} else if (!descriptiveStatistic.equals(other.descriptiveStatistic))
			return false;
		if (diastolicBloodPressureUnit == null) {
			if (other.diastolicBloodPressureUnit != null)
				return false;
		} else if (!diastolicBloodPressureUnit.equals(other.diastolicBloodPressureUnit))
			return false;
		if (diastolicBloodPressureValue != other.diastolicBloodPressureValue)
			return false;
		if (effectiveTimeFrame == null) {
			if (other.effectiveTimeFrame != null)
				return false;
		} else if (!effectiveTimeFrame.equals(other.effectiveTimeFrame))
			return false;
		if (hdrModality == null) {
			if (other.hdrModality != null)
				return false;
		} else if (!hdrModality.equals(other.hdrModality))
			return false;
		if (hdrSchemaNamespace == null) {
			if (other.hdrSchemaNamespace != null)
				return false;
		} else if (!hdrSchemaNamespace.equals(other.hdrSchemaNamespace))
			return false;
		if (hdrSchemaVersion == null) {
			if (other.hdrSchemaVersion != null)
				return false;
		} else if (!hdrSchemaVersion.equals(other.hdrSchemaVersion))
			return false;
		if (hdrSourceCreationDateTime == null) {
			if (other.hdrSourceCreationDateTime != null)
				return false;
		} else if (!hdrSourceCreationDateTime.equals(other.hdrSourceCreationDateTime))
			return false;
		if (hdrSourceName == null) {
			if (other.hdrSourceName != null)
				return false;
		} else if (!hdrSourceName.equals(other.hdrSourceName))
			return false;
		if (hdrUserId == null) {
			if (other.hdrUserId != null)
				return false;
		} else if (!hdrUserId.equals(other.hdrUserId))
			return false;
		if (positionDuringMeasurement == null) {
			if (other.positionDuringMeasurement != null)
				return false;
		} else if (!positionDuringMeasurement.equals(other.positionDuringMeasurement))
			return false;
		if (systolicBloodPressureUnit == null) {
			if (other.systolicBloodPressureUnit != null)
				return false;
		} else if (!systolicBloodPressureUnit.equals(other.systolicBloodPressureUnit))
			return false;
		if (systolicBloodPressureValue != other.systolicBloodPressureValue)
			return false;
		if (userNotes == null) {
			if (other.userNotes != null)
				return false;
		} else if (!userNotes.equals(other.userNotes))
			return false;
		return true;
	}
}

// BloodPressureVo bloodPressureVo = new BloodPressureVo()
//	 .setBloodPressureUuid("xxx")
//	 .setHdrSourceName("xxx")
//	 .setHdrSourceCreationDateTime("xxx")
//	 .setHdrUserId("xxx")
//	 .setHdrModality("xxx")
//	 .setHdrSchemaNamespace("xxx")
//	 .setHdrSchemaVersion("xxx")
//	 .setEffectiveTimeFrame("xxx")
//	 .setDescriptiveStatistic("xxx")
//	 .setUserNotes("xxx")
//	 .setPositionDuringMeasurement("xxx")
//	 .setSystolicBloodPressureUnit("xxx")
//	 .setSystolicBloodPressureValue("xxx")
//	 .setDiastolicBloodPressureUnit("xxx")
//	 .setDiastolicBloodPressureValue("xxx")
//	 .setInsertTs("xxx")
//	 ;
