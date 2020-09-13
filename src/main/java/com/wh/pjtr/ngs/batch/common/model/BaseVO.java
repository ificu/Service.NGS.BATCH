package com.wh.pjtr.ngs.batch.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class BaseVO implements Serializable {

	private static final long serialVersionUID = 8098737578371242510L;

	@ApiModelProperty(value = "등록자ID")
	private String rgstId;

	@ApiModelProperty(value = "등록일시")
	private Timestamp rgstDtm;

	@ApiModelProperty(value = "최종수정자ID")
	private String chgrId;

	@ApiModelProperty(value = "최종수정일시")
	private Timestamp chgDtm;

	@ApiModelProperty(value = "전체건수")
	@JsonIgnore
	private int totalCount;
}
