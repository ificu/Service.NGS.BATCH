package com.wh.pjtr.ngs.batch.common.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Service;

import com.wh.pjtr.ngs.batch.util.StringUtils;

import lombok.RequiredArgsConstructor;

//import com.skcc.oms.batch.biz.oms.mapper.NsfCodeMapper;
//import com.skcc.oms.batch.biz.oms.vo.NsfCodeVo;
//import com.skcc.oms.batch.common.mapper.CmBatchLogMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 스케줄링과 잡 실행 공통서비스</br>
 * <b>현재 사용여부에 대한 로직은 주석처리됨</b>
 * @author 07734
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class JobService  {
	private final JobLauncher jobLauncher;
	private final DefaultListableBeanFactory defaultListableBeanFactory;

	/**
	 * 스케줄링 실행
	 */
	public void runSchedulingJob(String jobName) {
		runJob(jobName);
	}

	/**
	 * 잡 실행
	 */
	public void runJob(String jobName) {
		// Get Job
		Job job = (Job) defaultListableBeanFactory.getBean(StringUtils.convert2CamelCase(jobName));

		JobParameters param = new JobParametersBuilder().addString("JobID", job.getName() + "-" + System.currentTimeMillis()).toJobParameters();
		JobExecution execution;
		try {
			execution = jobLauncher.run(job, param);
			log.info("{} JOB EXECUTION : {}", job.getName(), execution.toString());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
			log.error("{} JOB EXECUTION : ERROR | {}", job.getName(), e.getStackTrace());
			throw new RuntimeException(e);
		}
	}
}