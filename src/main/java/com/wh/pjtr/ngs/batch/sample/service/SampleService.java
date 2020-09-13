package com.wh.pjtr.ngs.batch.sample.service;

import com.wh.pjtr.ngs.batch.sample.mapper.SampleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SampleService {
	private final SampleMapper sampleMapper;

	public void insertSample() {
		sampleMapper.insertSample();
	}

}
