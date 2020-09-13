package com.wh.pjtr.ngs.batch.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * Job 실행 전/후로 Job에 관련된 로그를 남기는 Listener</br>
 * 로컬/개발/테스트용으로 운영에서는 설정을 삭제하거나 log Level를 높여서 로그가 남기지 않도록 함</br>
 * <b>현재 console log만 찍도록 설정함</b>
 * @author 07734
 */
@Slf4j
@Component
public class JobLoggingListener extends JobExecutionListenerSupport implements JobExecutionListener , Ordered {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		JobInstance jobInstance = jobExecution.getJobInstance();
		log.info("********************** Job Started **********************");
		log.info("              Name : {}", jobInstance.getJobName());
		log.info("       Instance Id : {}", jobInstance.getInstanceId());
		log.info("           Version : {}", jobInstance.getVersion());
		log.info(" ConfigurationName : {}", jobExecution.getJobConfigurationName());
		log.info("        CreateTime : {}", jobExecution.getCreateTime());
		log.info("         StartTime : {}", jobExecution.getStartTime());
		log.info("       LastUpdated : {}", jobExecution.getLastUpdated());
		log.info("            Status : {}", jobExecution.getStatus());
		log.info("");
		log.info(" Parameters : {}", jobExecution.getJobParameters());
		log.info("**********************************************************");
	}

	@Override
	public void afterJob(JobExecution jobExecution){
		setExecutionLogToExitMessage(jobExecution);

		JobInstance jobInstance = jobExecution.getJobInstance();
		log.info("********************* Job Ended *********************");
		log.info("	              Name : {}", jobInstance.getJobName());
		log.info("	           EndTime : {}", jobExecution.getEndTime());
		log.info("	       Elapse time : {} ms", jobExecution.getEndTime().getTime()- jobExecution.getStartTime().getTime());
		log.info("	       LastUpdated : {}", jobExecution.getLastUpdated());
		log.info("	            Status : {}", jobExecution.getStatus());
		log.info("	          ExitCode : {}", jobExecution.getExitStatus().getExitCode());
		log.info("	   ExitDescription : {}", jobExecution.getExitStatus().getExitDescription());
		log.info("	  ExecutionContext : {}", jobExecution.getExecutionContext());
		log.info("");
		printStepExecutions(jobExecution.getStepExecutions());
		log.info("*********************************************************");
	}

	/**
	 * Execution에 대한 Log를 EXIT_MESSAGE에 전달
	 */
	private void setExecutionLogToExitMessage(JobExecution jobExecution){
		StringBuilder sb = new StringBuilder();
		Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();
		for(StepExecution stepExecution : stepExecutions){
			String exitDescription = stepExecution.getExitStatus().getExitDescription();
			if(StringUtils.isEmpty(exitDescription)){
				sb.append(stepExecution.getStepName() + ":" + exitDescription);
			}
		}

		// Exit_Message가 있을때는 Logging 건너뜀
		if(jobExecution.getExitStatus().getExitDescription().isEmpty()){
			// Job Log
			JobInstance jobInstance = jobExecution.getJobInstance();
			sb.append("\n" + "*************************** Job ***************************" + "\n");
			sb.append("Name          : " + jobInstance.getJobName() + "\n");
			sb.append("EndTime       : " + jobExecution.getEndTime() + "\n");
			sb.append("Elapse time   : " + (jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime()) + "ms\n");
			sb.append("Status        : " + jobExecution.getStatus() + "\n");
			sb.append("ExitCode      : " + jobExecution.getExitStatus().getExitCode() + "\n");
			sb.append("JobParameters : " + jobExecution.getJobParameters() + "\n");

			// Step Log
			Iterator<StepExecution> it = jobExecution.getStepExecutions().iterator();
			sb.append("-------------------------- Steps --------------------------" + "\n");
			while(it.hasNext()){
				StepExecution stepExecution = it.next();
				sb.append("Step Name          : " + stepExecution.getStepName() + "\n");
				sb.append("Id                 : " + stepExecution.getId() + "\n");
				sb.append("Read Count         : " + stepExecution.getReadCount() + "\n");
				sb.append("Step Name          : " + stepExecution.getStepName() + "\n");
				sb.append("Read Count         : " + stepExecution.getReadCount() + "\n");
				sb.append("Write Count        : " + stepExecution.getWriteCount() + "\n");
				sb.append("Commit Count       : " + stepExecution.getCommitCount() + "\n");
				sb.append("Rollback Count     : " + stepExecution.getRollbackCount() + "\n");
				sb.append("Skip Count         : " + stepExecution.getSkipCount() + "\n");
				sb.append("ReadSkip Count     : " + stepExecution.getReadSkipCount() + "\n");
				sb.append("Process Skip Count : " + stepExecution.getProcessSkipCount() + "\n");
				sb.append("Write Skip Count   : " + stepExecution.getWriteSkipCount() + "\n");
				sb.append("Filter Count       : " + stepExecution.getFilterCount() + "\n");
				sb.append("Start Time         : " + stepExecution.getStartTime() + "\n");
				sb.append("End Time           : " + stepExecution.getEndTime() + "\n");
				sb.append("Elapse time        : " + (stepExecution.getEndTime().getTime() - stepExecution.getStartTime().getTime()) + " ms\n");
				sb.append("Status             : " + stepExecution.getStatus() + "\n");
				sb.append("ExitCode           : " + stepExecution.getExitStatus().getExitCode() + "\n");
				for (Throwable throwable : stepExecution.getFailureExceptions()) {
					sb.append("FailureException : \n" + throwable.getLocalizedMessage() + "\n");
				}
				sb.append("-----------------------------------------------------------" + "\n");
			}
			sb.append("************************************************************");
			String exitCode = jobExecution.getExitStatus().getExitCode();
			jobExecution.setExitStatus(new ExitStatus(exitCode, sb.toString()));
		}

	}

	/**
	 * step에 대한 상세 로그
	 */
	private void printStepExecutions(Collection<StepExecution> stepExecutions){
			log.info("------------------ Job Step ------------------");
		for (StepExecution stepExecution : stepExecutions){
			log.info("           Step Name : {}", stepExecution.getStepName());
			log.info("                  Id : {}", stepExecution.getId());
			log.info("          Read Count : {}", stepExecution.getReadCount());
			log.info("         Write Count : {}", stepExecution.getWriteCount());
			log.info("        Commit Count : {}", stepExecution.getCommitCount());
			log.info("      Rollback Count : {}", stepExecution.getRollbackCount());
			log.info("          Skip Count : {}", stepExecution.getSkipCount());
			log.info("     Read Skip Count : {}", stepExecution.getReadSkipCount());
			log.info("  Process Skip Count : {}", stepExecution.getProcessSkipCount());
			log.info("    Write Skip Count : {}", stepExecution.getWriteSkipCount());
			log.info("        Filter Count : {}", stepExecution.getFilterCount());
			log.info("          Start Time : {}", stepExecution.getStartTime());
			log.info("            End Time : {}", stepExecution.getEndTime());
			log.info("         Elapse time : {}", stepExecution.getEndTime().getTime()-stepExecution.getStartTime().getTime());
			log.info("              Status : {}", stepExecution.getStatus());
			log.info("           Exit Code : {}", stepExecution.getExitStatus().getExitCode());
			log.info("    ExecutionContext : {}", stepExecution.getExecutionContext());
			Throwable lastThrowable = null;
			for (Throwable throwable : stepExecution.getFailureExceptions()) {
				log.info("    failureException : {}", throwable.getLocalizedMessage());
				lastThrowable = throwable;
			}
			if(lastThrowable != null ){
				log.info("  Last failureException : ", lastThrowable);
			}
		}
			log.info("----------------------------------------------");
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}