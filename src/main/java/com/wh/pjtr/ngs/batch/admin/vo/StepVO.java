package com.wh.pjtr.ngs.batch.admin.vo;

import com.wh.pjtr.ngs.batch.common.model.BaseVO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StepVO extends BaseVO {
	private static final long serialVersionUID = -6984924122042066318L;
	private String stepExecutionId;
	 private String version;
	 private String stepName;
     private String jobExecutionId;
     private String startTime;
     private String endTime;
     private String elapseTime;
     private String status;
     private String commitCount;
     private String readCount;
     private String filterCount;
     private String writeCount;
     private String readSkipCount;
     private String writeSkipCount;
     private String processSkipCount;
     private String rollbackCount;
     private String exitCode;
     private String exitMessage;
     private String createTime;
     private String lastUpdated;
}
