package com.wh.pjtr.ngs.batch.common.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private ApplicationContext context;

    public ApplicationContext getApplicationContext() {
        return context;
    }

	@Override
	public void setApplicationContext(ApplicationContext ctx) {
        context = ctx;
	}
}