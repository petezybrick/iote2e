package com.pzybrick.iote2e.stream.persist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openmhealth.schema.domain.omh.BodyTemperature;
import org.openmhealth.schema.domain.omh.DataPointHeader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pzybrick.iote2e.common.config.MasterConfig;


public class BodyTemperatureDao implements OmhDao  {
	private static final Logger logger = LogManager.getLogger(BodyTemperatureDao.class);
	private static String sqlDeleteByPk = "DELETE FROM body_temperature WHERE body_temperature_uuid=?";
	private static String sqlInsert = "INSERT INTO body_temperature (body_temperature_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,descriptive_statistic,user_notes,measurement_location,body_temperature_unit,body_temperature_value) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static String sqlFindByPk = "SELECT body_temperature_uuid,hdr_source_name,hdr_source_creation_date_time,hdr_user_id,hdr_modality,hdr_schema_namespace,hdr_schema_version,effective_time_frame,descriptive_statistic,user_notes,measurement_location,body_temperature_unit,body_temperature_value,insert_ts FROM body_temperature WHERE body_temperature_uuid=?";

	
	public static void insertFromJsonSchema( PreparedStatement pstmt, DataPointHeader dataPointHeader, String rawJson, ObjectMapper objectMapper ) throws Exception {
		try {
			BodyTemperature bodyTemperature = objectMapper.readValue(rawJson, BodyTemperature.class);
			int offset = 1;
			pstmt.setString( offset++, dataPointHeader.getId() );
			pstmt.setString( offset++, dataPointHeader.getAcquisitionProvenance().getSourceName() );
			pstmt.setTimestamp( offset++, 
					new Timestamp( dataPointHeader.getAcquisitionProvenance().getSourceCreationDateTime().getLong(java.time.temporal.ChronoField.NANO_OF_SECOND)) );
			pstmt.setString( offset++, dataPointHeader.getUserId());
			pstmt.setString( offset++, dataPointHeader.getAcquisitionProvenance().getModality().name() );
			pstmt.setString( offset++, dataPointHeader.getSchemaId().getNamespace() );
			pstmt.setString( offset++, dataPointHeader.getSchemaId().getVersion().toString());			
			pstmt.setTimestamp( offset++, 
					new Timestamp(bodyTemperature.getEffectiveTimeFrame().getDateTime().getLong(java.time.temporal.ChronoField.NANO_OF_SECOND)) );
			pstmt.setString( offset++, bodyTemperature.getDescriptiveStatistic().name() );
			pstmt.setString( offset++, bodyTemperature.getUserNotes() );
			
			pstmt.setString( offset++, bodyTemperature.getMeasurementLocation().name() );
			pstmt.setString( offset++, bodyTemperature.getBodyTemperature().getUnit() );
			pstmt.setFloat( offset++, bodyTemperature.getBodyTemperature().getValue().floatValue() );
			pstmt.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	
	public static void insertBatchMode( Connection con, BodyTemperatureVo bodyTemperatureVo ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, bodyTemperatureVo.getBodyTemperatureUuid() );
			pstmt.setString( offset++, bodyTemperatureVo.getHdrSourceName() );
			pstmt.setTimestamp( offset++, bodyTemperatureVo.getHdrSourceCreationDateTime() );
			pstmt.setString( offset++, bodyTemperatureVo.getHdrUserId() );
			pstmt.setString( offset++, bodyTemperatureVo.getHdrModality() );
			pstmt.setString( offset++, bodyTemperatureVo.getHdrSchemaNamespace() );
			pstmt.setString( offset++, bodyTemperatureVo.getHdrSchemaVersion() );
			pstmt.setTimestamp( offset++, bodyTemperatureVo.getEffectiveTimeFrame() );
			pstmt.setString( offset++, bodyTemperatureVo.getDescriptiveStatistic() );
			pstmt.setString( offset++, bodyTemperatureVo.getUserNotes() );
			pstmt.setString( offset++, bodyTemperatureVo.getMeasurementLocation() );
			pstmt.setString( offset++, bodyTemperatureVo.getBodyTemperatureUnit() );
			pstmt.setFloat( offset++, bodyTemperatureVo.getBodyTemperatureValue() );
			pstmt.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (pstmt != null)
				pstmt.close();
			} catch (Exception e) {
				logger.warn(e);
			}
		}
	}

	public static void insert( MasterConfig masterConfig, BodyTemperatureVo bodyTemperatureVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(false);
			pstmt = con.prepareStatement(sqlInsert);
			int offset = 1;
			pstmt.setString( offset++, bodyTemperatureVo.getBodyTemperatureUuid() );
			pstmt.setString( offset++, bodyTemperatureVo.getHdrSourceName() );
			pstmt.setTimestamp( offset++, bodyTemperatureVo.getHdrSourceCreationDateTime() );
			pstmt.setString( offset++, bodyTemperatureVo.getHdrUserId() );
			pstmt.setString( offset++, bodyTemperatureVo.getHdrModality() );
			pstmt.setString( offset++, bodyTemperatureVo.getHdrSchemaNamespace() );
			pstmt.setString( offset++, bodyTemperatureVo.getHdrSchemaVersion() );
			pstmt.setTimestamp( offset++, bodyTemperatureVo.getEffectiveTimeFrame() );
			pstmt.setString( offset++, bodyTemperatureVo.getDescriptiveStatistic() );
			pstmt.setString( offset++, bodyTemperatureVo.getUserNotes() );
			pstmt.setString( offset++, bodyTemperatureVo.getMeasurementLocation() );
			pstmt.setString( offset++, bodyTemperatureVo.getBodyTemperatureUnit() );
			pstmt.setFloat( offset++, bodyTemperatureVo.getBodyTemperatureValue() );
			pstmt.execute();
			con.commit();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if( con != null ) {
				try {
					con.rollback();
				} catch(Exception erb ) {
					logger.warn(e.getMessage(), e);
				}
			}
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				logger.warn(e);
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception exCon) {
				logger.warn(exCon.getMessage());
			}
		}
	}

	public static void deleteByPk( MasterConfig masterConfig, BodyTemperatureVo bodyTemperatureVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, bodyTemperatureVo.getBodyTemperatureUuid() );
			pstmt.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if( con != null ) {
				try {
					con.rollback();
				} catch(Exception erb ) {
					logger.warn(e.getMessage(), e);
				}
			}
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				logger.warn(e);
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception exCon) {
				logger.warn(exCon.getMessage());
			}
		}
	}

	public static void deleteBatchMode( Connection con, BodyTemperatureVo bodyTemperatureVo ) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sqlDeleteByPk);
			int offset = 1;
			pstmt.setString( offset++, bodyTemperatureVo.getBodyTemperatureUuid() );
			pstmt.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				logger.warn(e);
			}
		}
	}

	public static BodyTemperatureVo findByPk( MasterConfig masterConfig, BodyTemperatureVo bodyTemperatureVo ) throws Exception {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = PooledDataSource.getInstance(masterConfig).getConnection();
			con.setAutoCommit(true);
			pstmt = con.prepareStatement(sqlFindByPk);
			int offset = 1;
			pstmt.setString( offset++, bodyTemperatureVo.getBodyTemperatureUuid() );
			ResultSet rs = pstmt.executeQuery();
			if( rs.next() ) return new BodyTemperatureVo(rs);
			else return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
			} catch (Exception e) {
				logger.warn(e);
			}
		}
	}
}
