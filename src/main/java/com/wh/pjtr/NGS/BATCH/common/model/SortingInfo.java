package com.wh.pjtr.ngs.batch.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wh.pjtr.ngs.batch.util.StringUtils;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SortingInfo implements Serializable {
	private static final long serialVersionUID = 8651391751049064028L;

	private String sortingKey;
	private String sortingDirection;

	@JsonIgnore
	public String getSortDescription() {
		return StringUtils.camelCaseToUnderscoreName(sortingKey).concat("desc".equalsIgnoreCase(sortingDirection) ? " DESC":" ASC");
	}
}
