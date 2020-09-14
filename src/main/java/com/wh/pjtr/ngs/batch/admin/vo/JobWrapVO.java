package com.wh.pjtr.ngs.batch.admin.vo;

import com.wh.pjtr.ngs.batch.common.model.PageInfo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class JobWrapVO {
	private JobVO batchLastExcuteJobResultVO;
	private JobVO searchCondition;
	private PageInfo<JobVO> pageInfo;
}
