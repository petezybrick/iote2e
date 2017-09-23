package com.pzybrick.iote2e.stream.validic;

import java.time.OffsetDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Diabete extends ValidicBody {
	public enum relationShipToMealValues {
		before, after, random, fasting
	};

	@JsonProperty("schema_name")
	private String schemaName;
	@JsonProperty("_id")
	private String id;
	@JsonProperty("blood_glucose")
	private Double bloodGlucose;
	@JsonProperty("c_peptide")
	private Double cPeptide;
	@JsonProperty("fasting_plasma_glucose_test")
	private Double fastingPlasmaGlucoseTest;
	@JsonProperty("hba1c")
	private Double hba1c;
	@JsonProperty("insulin")
	private Double insulin;
	@JsonProperty("last_updated")
	private OffsetDateTime lastUpdated;
	@JsonProperty("oral_glucose_tolerance_test")
	private Double oralGlucoseToleranceTest;
	@JsonProperty("random_plasma_glucose_test")
	private Double randomPlasmaGlucoseTest;
	@JsonProperty("relationship_to_meal")
	private String relationshipToMeal;
	@JsonProperty("source")
	private String source;
	@JsonProperty("source_name")
	private String sourceName;
	@JsonProperty("timestamp")
	private OffsetDateTime timestamp;
	@JsonProperty("triglyceride")
	private Double triglyceride;
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("utc_offset")
	private String utcOffset;
	@JsonProperty("validated")
	private Boolean validated;

	public Diabete() {
		this.schemaName = "Diabete";
	}

	public String getId() {
		return id;
	}

	public Double getBloodGlucose() {
		return roundDouble(bloodGlucose);
	}

	public Double getcPeptide() {
		return roundDouble(cPeptide);
	}

	public Double getFastingPlasmaGlucoseTest() {
		return roundDouble(fastingPlasmaGlucoseTest);
	}

	public Double getHba1c() {
		return roundDouble(hba1c);
	}

	public Double getInsulin() {
		return roundDouble(insulin);
	}

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	public OffsetDateTime getLastUpdated() {
		return lastUpdated;
	}

	public Double getOralGlucoseToleranceTest() {
		return roundDouble(oralGlucoseToleranceTest);
	}

	public Double getRandomPlasmaGlucoseTest() {
		return roundDouble(randomPlasmaGlucoseTest);
	}

	public String getRelationshipToMeal() {
		return relationshipToMeal;
	}

	public String getSource() {
		return source;
	}

	public String getSourceName() {
		return sourceName;
	}

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	public Double getTriglyceride() {
		return roundDouble(triglyceride);
	}

	public String getUserId() {
		return userId;
	}

	public String getUtcOffset() {
		return utcOffset;
	}

	public Boolean getValidated() {
		return validated;
	}

	public Diabete setId(String id) {
		this.id = id;
		return this;
	}

	public Diabete setBloodGlucose(Double bloodGlucose) {
		this.bloodGlucose = bloodGlucose;
		return this;
	}

	public Diabete setcPeptide(Double cPeptide) {
		this.cPeptide = cPeptide;
		return this;
	}

	public Diabete setFastingPlasmaGlucoseTest(Double fastingPlasmaGlucoseTest) {
		this.fastingPlasmaGlucoseTest = fastingPlasmaGlucoseTest;
		return this;
	}

	public Diabete setHba1c(Double hba1c) {
		this.hba1c = hba1c;
		return this;
	}

	public Diabete setInsulin(Double insulin) {
		this.insulin = insulin;
		return this;
	}

	public Diabete setLastUpdated(OffsetDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
		return this;
	}

	public Diabete setOralGlucoseToleranceTest(Double oralGlucoseToleranceTest) {
		this.oralGlucoseToleranceTest = oralGlucoseToleranceTest;
		return this;
	}

	public Diabete setRandomPlasmaGlucoseTest(Double randomPlasmaGlucoseTest) {
		this.randomPlasmaGlucoseTest = randomPlasmaGlucoseTest;
		return this;
	}

	public Diabete setRelationshipToMeal(String relationshipToMeal) {
		this.relationshipToMeal = relationshipToMeal;
		return this;
	}

	public Diabete setSource(String source) {
		this.source = source;
		return this;
	}

	public Diabete setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	public Diabete setTimestamp(OffsetDateTime timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public Diabete setTriglyceride(Double triglyceride) {
		this.triglyceride = triglyceride;
		return this;
	}

	public Diabete setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public Diabete setUtcOffset(String utcOffset) {
		this.utcOffset = utcOffset;
		return this;
	}

	public Diabete setValidated(Boolean validated) {
		this.validated = validated;
		return this;
	}

	@Override
	public String toString() {
		return "Diabete [schemaName=" + schemaName + ", id=" + id + ", bloodGlucose=" + bloodGlucose + ", cPeptide="
				+ cPeptide + ", fastingPlasmaGlucoseTest=" + fastingPlasmaGlucoseTest + ", hba1c=" + hba1c
				+ ", insulin=" + insulin + ", lastUpdated=" + lastUpdated + ", oralGlucoseToleranceTest="
				+ oralGlucoseToleranceTest + ", randomPlasmaGlucoseTest=" + randomPlasmaGlucoseTest
				+ ", relationshipToMeal=" + relationshipToMeal + ", source=" + source + ", sourceName=" + sourceName
				+ ", timestamp=" + timestamp + ", triglyceride=" + triglyceride + ", userId=" + userId + ", utcOffset="
				+ utcOffset + ", validated=" + validated + "]";
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

}