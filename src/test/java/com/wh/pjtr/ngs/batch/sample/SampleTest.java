package com.wh.pjtr.ngs.batch.sample;

import com.wh.pjtr.ngs.batch.common.TestJobConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TestJobConfiguration.class })
public class SampleTest {
	@Autowired @Qualifier("SAMPLE_JOB_TEST") private JobLauncherTestUtils sampleJobTest;

	@Test
	public void setSampleJobTest() throws Exception {
		TestJobConfiguration.commonAssertions(sampleJobTest.launchJob());
	}

	@Test
	public void sampleStep() {
		TestJobConfiguration.commonAssertions(sampleJobTest.launchStep("SAMPLE_STEP"));
	}
}
