package com.pzybrick.iote2e.stream.validic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Biometric extends ValidicBody {

	@JsonProperty("schema_name")
	private String schemaName;
	@JsonProperty("_id")
	private String id;
	@JsonProperty("blood_calcium")
	private Double bloodCalcium;
	@JsonProperty("blood_chromium")
	private Double bloodChromium;
	@JsonProperty("blood_folic_acid")
	private Double bloodFolicAcid;
	@JsonProperty("blood_magnesium")
	private Double bloodMagnesium;
	@JsonProperty("blood_potassium")
	private Double bloodPotassium;
	@JsonProperty("blood_sodium")
	private Double bloodSodium;
	@JsonProperty("blood_vitamin_b12")
	private Double bloodVitaminB12;
	@JsonProperty("blood_zinc")
	private Double bloodZinc;
	@JsonProperty("creatine_kinase")
	private Double creatineKinase;
	@JsonProperty("crp")
	private Double crp;
	@JsonProperty("diastolic")
	private Double diastolic;
	@JsonProperty("ferritin")
	private Double ferritin;
	@JsonProperty("hdl")
	private Double hdl;
	@JsonProperty("hscrp")
	private Double hscrp;
	@JsonProperty("il6")
	private Double il6;
	@JsonProperty("last_updated")
	private OffsetDateTime lastUpdated;
	@JsonProperty("ldl")
	private Double ldl;
	@JsonProperty("resting_heartrate")
	private Double restingHeartrate;
	@JsonProperty("source")
	private String source;
	@JsonProperty("source_name")
	private String sourceName;
	@JsonProperty("spo2")
	private Double spo2;
	@JsonProperty("systolic")
	private Double systolic;
	@JsonProperty("temperature")
	private Double temperature;
	@JsonProperty("testosterone")
	private Double testosterone;
	@JsonProperty("timestamp")
	private OffsetDateTime timestamp;
	@JsonProperty("total_cholesterol")
	private Double totalCholesterol;
	@JsonProperty("tsh")
	private Double tsh;
	@JsonProperty("uric_acid")
	private Double uricAcid;
	@JsonProperty("user_id")
	private String userId;
	@JsonProperty("utc_offset")
	private String utcOffset;
	@JsonProperty("validated")
	private Boolean validated;
	@JsonProperty("vitamin_d")
	private Double vitaminD;
	@JsonProperty("white_cell_count")
	private Double whiteCellCount;
	
	public Biometric() {
		this.schemaName = "Biometric";
	}

	public String getId() {
		return id;
	}

	public Double getBloodCalcium() {
		return roundDouble(bloodCalcium);
	}

	public Double getBloodChromium() {
		return roundDouble(bloodChromium);
	}

	public Double getBloodFolicAcid() {
		return roundDouble(bloodFolicAcid);
	}

	public Double getBloodMagnesium() {
		return roundDouble(bloodMagnesium);
	}

	public Double getBloodPotassium() {
		return roundDouble(bloodPotassium);
	}

	public Double getBloodSodium() {
		return roundDouble(bloodSodium);
	}

	public Double getBloodVitaminB12() {
		return roundDouble(bloodVitaminB12);
	}

	public Double getBloodZinc() {
		return roundDouble(bloodZinc);
	}

	public Double getCreatineKinase() {
		return roundDouble(creatineKinase);
	}

	public Double getCrp() {
		return roundDouble(crp);
	}

	public Double getDiastolic() {
		return roundDouble(diastolic);
	}

	public Double getFerritin() {
		return roundDouble(ferritin);
	}

	public Double getHdl() {
		return roundDouble(hdl);
	}

	public Double getHscrp() {
		return roundDouble(hscrp);
	}

	public Double getIl6() {
		return roundDouble(il6);
	}

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
	public OffsetDateTime getLastUpdated() {
		return lastUpdated;
	}

	public Double getLdl() {
		return roundDouble( ldl );
	}

	public Double getRestingHeartrate() {
		return roundDouble( restingHeartrate );
	}

	public String getSource() {
		return source;
	}

	public String getSourceName() {
		return sourceName;
	}

	public Double getSpo2() {
		return roundDouble( spo2 );
	}

	public Double getSystolic() {
		return roundDouble( systolic );
	}

	public Double getTemperature() {
		return roundDouble(temperature);
	}

	public Double getTestosterone() {
		return roundDouble( testosterone );
	}

	public OffsetDateTime getTimestamp() {
		return timestamp;
	}

	public Double getTotalCholesterol() {
		return roundDouble( totalCholesterol );
	}

	public Double getTsh() {
		return roundDouble( tsh );
	}

	public Double getUricAcid() {
		return roundDouble( uricAcid );
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

	public Double getVitaminD() {
		return roundDouble( vitaminD );
	}

	public Double getWhiteCellCount() {
		return roundDouble(whiteCellCount );
	}

	public Biometric setId(String id) {
		this.id = id;
		return this;
	}

	public Biometric setBloodCalcium(Double bloodCalcium) {
		this.bloodCalcium = bloodCalcium;
		return this;
	}

	public Biometric setBloodChromium(Double bloodChromium) {
		this.bloodChromium = bloodChromium;
		return this;
	}

	public Biometric setBloodFolicAcid(Double bloodFolicAcid) {
		this.bloodFolicAcid = bloodFolicAcid;
		return this;
	}

	public Biometric setBloodMagnesium(Double bloodMagnesium) {
		this.bloodMagnesium = bloodMagnesium;
		return this;
	}

	public Biometric setBloodPotassium(Double bloodPotassium) {
		this.bloodPotassium = bloodPotassium;
		return this;
	}

	public Biometric setBloodSodium(Double bloodSodium) {
		this.bloodSodium = bloodSodium;
		return this;
	}

	public Biometric setBloodVitaminB12(Double bloodVitaminB12) {
		this.bloodVitaminB12 = bloodVitaminB12;
		return this;
	}

	public Biometric setBloodZinc(Double bloodZinc) {
		this.bloodZinc = bloodZinc;
		return this;
	}

	public Biometric setCreatineKinase(Double creatineKinase) {
		this.creatineKinase = creatineKinase;
		return this;
	}

	public Biometric setCrp(Double crp) {
		this.crp = crp;
		return this;
	}

	public Biometric setDiastolic(Double diastolic) {
		this.diastolic = diastolic;
		return this;
	}

	public Biometric setFerritin(Double ferritin) {
		this.ferritin = ferritin;
		return this;
	}

	public Biometric setHdl(Double hdl) {
		this.hdl = hdl;
		return this;
	}

	public Biometric setHscrp(Double hscrp) {
		this.hscrp = hscrp;
		return this;
	}

	public Biometric setIl6(Double il6) {
		this.il6 = il6;
		return this;
	}

	public Biometric setLastUpdated(OffsetDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
		return this;
	}

	public Biometric setLdl(Double ldl) {
		this.ldl = ldl;
		return this;
	}

	public Biometric setRestingHeartrate(Double restingHeartrate) {
		this.restingHeartrate = restingHeartrate;
		return this;
	}

	public Biometric setSource(String source) {
		this.source = source;
		return this;
	}

	public Biometric setSourceName(String sourceName) {
		this.sourceName = sourceName;
		return this;
	}

	public Biometric setSpo2(Double spo2) {
		this.spo2 = spo2;
		return this;
	}

	public Biometric setSystolic(Double systolic) {
		this.systolic = systolic;
		return this;
	}

	public Biometric setTemperature(Double temperature) {
		this.temperature = temperature;
		return this;
	}

	public Biometric setTestosterone(Double testosterone) {
		this.testosterone = testosterone;
		return this;
	}

	public Biometric setTimestamp(OffsetDateTime timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public Biometric setTotalCholesterol(Double totalCholesterol) {
		this.totalCholesterol = totalCholesterol;
		return this;
	}

	public Biometric setTsh(Double tsh) {
		this.tsh = tsh;
		return this;
	}

	public Biometric setUricAcid(Double uricAcid) {
		this.uricAcid = uricAcid;
		return this;
	}

	public Biometric setUserId(String userId) {
		this.userId = userId;
		return this;
	}

	public Biometric setUtcOffset(String utcOffset) {
		this.utcOffset = utcOffset;
		return this;
	}

	public Biometric setValidated(Boolean validated) {
		this.validated = validated;
		return this;
	}

	public Biometric setVitaminD(Double vitaminD) {
		this.vitaminD = vitaminD;
		return this;
	}

	public Biometric setWhiteCellCount(Double whiteCellCount) {
		this.whiteCellCount = whiteCellCount;
		return this;
	}

	@Override
	public String toString() {
		return "Biometric [schemaName=" + schemaName + ", id=" + id + ", bloodCalcium=" + bloodCalcium
				+ ", bloodChromium=" + bloodChromium + ", bloodFolicAcid=" + bloodFolicAcid + ", bloodMagnesium="
				+ bloodMagnesium + ", bloodPotassium=" + bloodPotassium + ", bloodSodium=" + bloodSodium
				+ ", bloodVitaminB12=" + bloodVitaminB12 + ", bloodZinc=" + bloodZinc + ", creatineKinase="
				+ creatineKinase + ", crp=" + crp + ", diastolic=" + diastolic + ", ferritin=" + ferritin + ", hdl="
				+ hdl + ", hscrp=" + hscrp + ", il6=" + il6 + ", lastUpdated=" + lastUpdated + ", ldl=" + ldl
				+ ", restingHeartrate=" + restingHeartrate + ", source=" + source + ", sourceName=" + sourceName
				+ ", spo2=" + spo2 + ", systolic=" + systolic + ", temperature=" + temperature + ", testosterone="
				+ testosterone + ", timestamp=" + timestamp + ", totalCholesterol=" + totalCholesterol + ", tsh=" + tsh
				+ ", uricAcid=" + uricAcid + ", userId=" + userId + ", utcOffset=" + utcOffset + ", validated="
				+ validated + ", vitaminD=" + vitaminD + ", whiteCellCount=" + whiteCellCount + "]";
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

}
