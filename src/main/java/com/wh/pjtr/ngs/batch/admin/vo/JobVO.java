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
public class JobVO extends BaseVO {
	private static final long serialVersionUID = -6378498270521902209L;
	private String jobName;
     private String jobExecutionId;
     private String count;
     private String startTime;
     private String endTime;
     private String elapseTime;
     private String status;
     private String exitCode;
     private String exitMessage;
     private String createTime;
     private String lastUpdated;
     private String version;
     private String jobInstanceId;
}
