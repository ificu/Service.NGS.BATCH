package com.wh.pjtr.ngs.batch.sample.vo;

import com.wh.pjtr.ngs.batch.common.model.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SampleVO extends BaseVO {

	@ApiModelProperty(value = "샘플ID")
	private int sampleId;

	@ApiModelProperty(value = "샘플이름")
	private String sampleName;
}
