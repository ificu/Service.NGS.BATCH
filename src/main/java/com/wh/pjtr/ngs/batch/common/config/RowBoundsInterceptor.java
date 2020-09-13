/*
The contents of this file are subject to the Mozilla Public License Version 1.1 (the "License"); you may not use this file
except in compliance with the License. You may obtain a copy of the License at http://www.mozilla.org/MPL/.
Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND,
either express or implied. See the License for the specific language governing rights and limitations under the License.
The Original Code is cardparser.c, cardparser.h.

The Initial Developer of the Original Code is Lindsay Mathieson.
Portions created by SK Holdings are Copyright (c) 2016 SK Holdings Co., Ltd. All rights reserved.
Contributor(s):  Gil-Dong Hong (gdh@sk.com) <- 수정한 내용 작성
출처: https://antop.tistory.com/182
*/
package com.wh.pjtr.ngs.batch.common.config;

import com.wh.pjtr.ngs.batch.common.model.PageInfo;
import com.wh.pjtr.ngs.batch.common.model.SortingInfo;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.util.List;
import java.util.Properties;

@Component
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class RowBoundsInterceptor implements Interceptor {
	private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
	private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
	private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
		MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
		String originalSql = (String) metaStatementHandler.getValue("delegate.boundSql.sql");
		RowBounds rb = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");

		// RowBounds가 없으면 그냥 실행
		if (rb == null || rb == RowBounds.DEFAULT) {
			return invocation.proceed();
		}

		int offset = 0;
		int limit  = 0;
		List<SortingInfo> sortingInfo = null;

		if(rb instanceof PageInfo) {
			offset = ((PageInfo<?>)rb).getPageOffset();
			limit  = ((PageInfo<?>)rb).getPageSize();
			sortingInfo = ((PageInfo<?>)rb).getSortingInfo();
		} else {
			offset = rb.getOffset();
			limit  = rb.getLimit();
		}

		// RowBounds가 있다!
		// SortingInfo가 있으면 ORDER BY 구문을 만든다.
		// 원래 쿼리에 limit 문을 붙여준다.
		StringBuilder sb = new StringBuilder();
		if(! CollectionUtils.isEmpty(sortingInfo)) {
			sb.append("SELECT T.* FROM ( ");
			sb.append(originalSql);
			sb.append(" ) T ");
			sb.append(" ORDER BY");
			char separator = ' ';
			for(SortingInfo sort : sortingInfo) {
				sb.append(separator).append(sort.getSortDescription());
				separator = ',';
			}
		} else {
			sb.append(originalSql);
		}

		sb.append(" LIMIT ").append(offset).append(", ").append(limit);
		// RowBounds 정보 제거
		metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
		metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
		// 변경된 쿼리로 바꿔치기
		metaStatementHandler.setValue("delegate.boundSql.sql", sb.toString());

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		 // Do nothing
	}

}