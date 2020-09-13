package com.wh.pjtr.ngs.batch.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties({"offset", "limit", "pageOffset"})
public class PageInfo<T extends BaseVO> extends RowBounds implements Serializable {

	private static final long serialVersionUID = -48522432669970757L;

	@ApiModelProperty(value = "페이지번호 (0 ~ n)")
	private int pageNumber;

	@ApiModelProperty(value = "페이지 당 건수")
	private int pageSize;

	@ApiModelProperty(value = "전체건수")
	private int totalCount;

	@ApiModelProperty(value = "조회결과 목록")
	private List<? extends BaseVO> content = new ArrayList<>();

	@ApiModelProperty(value = "정렬조건")
	private List<SortingInfo> sortingInfo;

	/**
	 * 페이지 처리 없이 조회결과를 반환할 때 생성자
	 */
	public PageInfo(List<? extends BaseVO> content) {
		this.content = content;
		this.pageSize = Integer.MAX_VALUE;
		this.totalCount = content == null ? 0: content.size();
	}

	/**
	 * RowBounds Offset
	 */
	public int getPageOffset() {
		return pageNumber * pageSize;
	}

	/**
	 * 총 페이지수 반환
	 */
	public int getTotalPages() {
		return totalCount == 0 ? 1 : (int)Math.ceil((double) totalCount / (double) pageSize);
	}

	/**
	 * 페이지 조회결과 세팅
	 */
	public void setContent(List<? extends BaseVO> content) {
		this.content = content;

		if(CollectionUtils.isEmpty(this.content)) {
			this.totalCount = 0;
		} else {
			this.totalCount = this.content.get(0).getTotalCount();
		}
	}

}
