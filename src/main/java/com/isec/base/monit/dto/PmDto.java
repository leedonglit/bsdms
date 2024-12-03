package com.isec.base.monit.dto;

import lombok.Data;

@Data
@com.core.security.database.jdbc.annotation.DTO(tableName= "idgar_pm_tab", pkey= "pm_id")
public class PmDto implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@javax.persistence.Column(name="pm_id")
	private String pm_id;//
	
	@javax.persistence.Column(name="EleType")
	private String EleType;//
	
	@javax.persistence.Column(name="No")
	private String No;//

	@javax.persistence.Column(name="IsotopeType")
	private String IsotopeType;//

	@javax.persistence.Column(name="PMType")
	private String PMType;//

	@javax.persistence.Column(name="Content")
	private String Content;//

	@javax.persistence.Column(name="Ic")
	private String Ic;//

	@javax.persistence.Column(name="icsd")
	private String icsd;//

	@javax.persistence.Column(name="n")
	private String n;//

	@javax.persistence.Column(name="mc")
	private String mc;//

	@javax.persistence.Column(name="mcsd")
	private String mcsd;//

	@javax.persistence.Column(name="SamplingDate")
	private String SamplingDate;//

	@javax.persistence.Column(name="Year")
	private String Year;//

	@javax.persistence.Column(name="Season")
	private String Season;//

	@javax.persistence.Column(name="SamplingSite")
	private String SamplingSite;//

	@javax.persistence.Column(name="Nation")
	private String Nation;//

	@javax.persistence.Column(name="Region")
	private String Region;//

	@javax.persistence.Column(name="Latitude")
	private String Latitude;//

	@javax.persistence.Column(name="Longitude")
	private String Longitude;//

	@javax.persistence.Column(name="Laboratory")
	private String Laboratory;//

	@javax.persistence.Column(name="Reference")
	private String Reference;//

}
