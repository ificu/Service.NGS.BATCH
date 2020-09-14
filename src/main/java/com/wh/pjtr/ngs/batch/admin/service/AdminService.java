/*
 * Copyright (c) 2019 SK Holdings Co., Ltd. All rights reserved.
 *
 * This software is the confidential and proprietary information of SK Holdings.
 * You shall not disclose such confidential information and shall use it only in
 * accordance with the terms of the license agreement you entered into with SK Holdings.
 */
package com.wh.pjtr.ngs.batch.admin.service;

import com.wh.pjtr.ngs.batch.admin.mapper.AdminMapper;
import com.wh.pjtr.ngs.batch.admin.vo.JobVO;
import com.wh.pjtr.ngs.batch.admin.vo.JobWrapVO;
import com.wh.pjtr.ngs.batch.admin.vo.StepVO;
import com.wh.pjtr.ngs.batch.admin.vo.StepWrapVO;
import com.wh.pjtr.ngs.batch.common.model.PageInfo;
import com.wh.pjtr.ngs.batch.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class AdminService {
	private final AdminMapper adminMapper;

	public PageInfo<JobVO> lastExecuteJobResult(String[] jobNameList) {
		List<JobVO> resList = new ArrayList<>();
		for (String jobName: jobNameList) {
			JobVO jobVO = adminMapper.lastExecuteJobResult(StringUtils.convert2UpperCase(jobName));
			if (Objects.isNull(jobVO)) {
				Map<String, String> resMap1 = new HashMap<>();
				resMap1.put("JOB_NAME", StringUtils.convert2UpperCase(jobName));
				JobVO tempVO = new JobVO();
				tempVO.setJobName(StringUtils.convert2UpperCase(jobName));
				resList.add(tempVO);
			}
			resList.add(jobVO);
		}
		return new PageInfo<>(resList);
	}

	public JobWrapVO executeJobResultList(JobWrapVO jobWrapVO) {
		JobVO jobVO = jobWrapVO.getSearchCondition();
		PageInfo<JobVO> pageInfo = jobWrapVO.getPageInfo();

		List<JobVO> jobResultList = adminMapper.executeJobResultList(jobVO.getJobName(), pageInfo);
		pageInfo.setContent(jobResultList);
		return jobWrapVO;
	}

	public StepWrapVO executeStepResultList(StepWrapVO stepWrapVO) {
		StepVO stepVO = stepWrapVO.getSearchCondition();
		PageInfo<StepVO> pageInfo = stepWrapVO.getPageInfo();

		List<StepVO> stepResultList = adminMapper.executeStepResultList(stepVO.getJobExecutionId(), pageInfo);
		pageInfo.setContent(stepResultList);
		return stepWrapVO;
	}
}