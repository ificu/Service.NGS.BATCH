package com.wh.pjtr.ngs.batch.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.junit.Assert.*;

/**
 * 배치 테스트 환경구성
 * @author 07734
 */
@Slf4j
@EnableBatchProcessing
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.wh.pjtr.ngs.batch")
public class TestJobConfiguration {
    @Autowired private AbstractAutowireCapableBeanFactory beanFactory;
    @Autowired private List<Job> jobs;

    @PostConstruct
    public void registerServices() {
        jobs.forEach(j -> {
            JobLauncherTestUtils u = create(j);
            final String name = j.getName() + "_TEST";
            beanFactory.registerSingleton(name, u);
            beanFactory.autowireBean(u);
            log.info("Registered JobLauncherTest: {}", name);
        });
    }

    private JobLauncherTestUtils create(final Job j) {
        return new MyJobLauncherTestUtils(j);
    }

    private static class MyJobLauncherTestUtils extends JobLauncherTestUtils {
        MyJobLauncherTestUtils(Job j) {
            this.setJob(j);
        }

        @Override // to remove @Autowire from base class
        public void setJob(Job job) {
            super.setJob(job);
        }
    }

    public static void commonAssertions(JobExecution jobExecution) {
        assertNotNull(jobExecution);

        BatchStatus batchStatus = jobExecution.getStatus();
        assertEquals(BatchStatus.COMPLETED, batchStatus);
        assertFalse(batchStatus.isUnsuccessful());

        ExitStatus exitStatus = jobExecution.getExitStatus();
        assertEquals("COMPLETED", exitStatus.getExitCode());
        List<Throwable> failureExceptions = jobExecution.getFailureExceptions();
        assertNotNull(failureExceptions);
        assertTrue(failureExceptions.isEmpty());
    }
}
