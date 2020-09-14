/*
 * Copyright (c) 2019 SK Holdings Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK Holdings.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SK Holdings.
 */
package com.wh.pjtr.ngs.batch.admin.mapper;

import com.wh.pjtr.ngs.batch.admin.vo.JobVO;
import com.wh.pjtr.ngs.batch.admin.vo.StepVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface AdminMapper {
	JobVO lastExecuteJobResult(String jobName);
	List<JobVO> executeJobResultList(String jobName, RowBounds rowBounds);
	List<StepVO> executeStepResultList(String jobExecutionId, RowBounds rowBounds);
}
