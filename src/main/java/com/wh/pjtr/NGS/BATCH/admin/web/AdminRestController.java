package com.wh.pjtr.ngs.batch.admin.web;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wh.pjtr.ngs.batch.admin.service.AdminService;
import com.wh.pjtr.ngs.batch.admin.vo.JobVO;
import com.wh.pjtr.ngs.batch.admin.vo.JobWrapVO;
import com.wh.pjtr.ngs.batch.admin.vo.StepWrapVO;
import com.wh.pjtr.ngs.batch.common.model.PageInfo;
import com.wh.pjtr.ngs.batch.common.config.ApplicationContextProvider;
import com.wh.pjtr.ngs.batch.util.StringUtils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AdminRestController {
	@NonNull private final JobLauncher jobLauncher;
	@NonNull private final DefaultListableBeanFactory defaultListableBeanFactory;
	@NonNull private final AdminService adminService;
	@NonNull private final ApplicationContextProvider applicationContextProvider;

	@GetMapping("/runJob/{jobName}")
	public String runJob(@PathVariable("jobName") String jobName) {
		Job job = (Job) defaultListableBeanFactory.getBean(StringUtils.convert2CamelCase(jobName));
		JobParameters param = new JobParametersBuilder()
									.addString("JobID", job.getName()  + "-" + System.currentTimeMillis())
									.toJobParameters();
		String jobResult = null;
		try {
			JobExecution execution = jobLauncher.run(job, param);
			jobResult = execution.toString();
			log.info("{} JOB EXECUTION: {}", job.getName(), jobResult);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
			log.error("{} JOB EXECUTION ERROR: {}", job.getName(), e.getMessage());
		}
		return jobResult;
	}

	@GetMapping("/lastExecuteJobResult")
	public ResponseEntity<PageInfo<JobVO>> lastExecuteJobResult() {
		return ResponseEntity.ok(adminService.lastExecuteJobResult(applicationContextProvider.getApplicationContext().getBeanNamesForType(Job.class)));
	}

	@PostMapping("/executeJobResultList")
	public ResponseEntity<JobWrapVO> executeJobResultList(@RequestBody JobWrapVO jobWrapVO) {
		return ResponseEntity.ok(adminService.executeJobResultList(jobWrapVO));
	}

	@PostMapping("/executeStepResultList")
	public ResponseEntity<StepWrapVO> executeStepResultList(@RequestBody StepWrapVO stepWrapVO) {
		return ResponseEntity.ok(adminService.executeStepResultList(stepWrapVO));
	}
}
