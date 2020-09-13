package com.wh.pjtr.ngs.batch.common.listener;

import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.beans.factory.config.BeanPostProcessor;

import lombok.extern.slf4j.Slf4j;

/**
 * Job 객체 생성시 등록된 Job Execution Listener들을 자동으로 등록
 * @author 07734
 */
@Slf4j
public class JobListenerBeanPostProcessor implements BeanPostProcessor {
    private JobExecutionListener[] jobExecutionListeners;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof AbstractJob) {
            AbstractJob job = (AbstractJob) bean;
            job.setJobExecutionListeners(jobExecutionListeners);
        }
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    public void setJobExecutionListeners(JobExecutionListener[] jobExecutionListeners) {
        for (JobExecutionListener jobExecutionListener : jobExecutionListeners) {
            log.info("jobExecutionListener : {}", jobExecutionListener);
        }
    }
}