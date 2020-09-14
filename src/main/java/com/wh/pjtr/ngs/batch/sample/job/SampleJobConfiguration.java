package com.wh.pjtr.ngs.batch.sample.job;

import com.wh.pjtr.ngs.batch.common.job.JobService;
import com.wh.pjtr.ngs.batch.common.listener.JobLoggingListener;
import com.wh.pjtr.ngs.batch.sample.service.SampleService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Configuration
public class SampleJobConfiguration {
    private final JobService jobService;
	private final JobLoggingListener listener;
	private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SqlSessionFactory sqlSessionFactory;
    private final SampleService sampleService;
    @Value("${spring.batch.chunk}") private int chunk;

    /**
     * 샘플 배치</br>
     * 배치주기: 매 1분</br>
     */
    @Scheduled(cron="1 * * * * *")
    public void sampleJobScheduler() {
        jobService.runSchedulingJob("SAMPLE_JOB");
    }

    @Bean
    public Job sampleJob() {
        return jobBuilderFactory.get("SAMPLE_JOB")
				                .incrementer(new RunIdIncrementer())
				                .listener(listener)
				                .start(sampleStep())
				                .build();
    }

    @Bean
    public Step sampleStep() {
		return stepBuilderFactory.get("SAMPLE_STEP")
                .tasklet((contribution, chunkContext) -> {
                    sampleService.insertSample();
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
