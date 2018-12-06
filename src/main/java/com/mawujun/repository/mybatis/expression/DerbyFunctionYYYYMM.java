package com.mawujun.repository.mybatis.expression;

import java.io.Serializable;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.Renderable;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.ExpressionImpl;
import org.hibernate.query.criteria.internal.expression.function.CastFunction;

/**
* @author mawujun 16064988
* @createDate ：2018年12月5日 下午1:41:30
*/
public class DerbyFunctionYYYYMM  extends ExpressionImpl<String> implements Serializable{
	private Expression<String> path;
	public DerbyFunctionYYYYMM(CriteriaBuilderImpl criteriaBuilder,Path path) {
		super( criteriaBuilder, String.class );
		this.path=path;
	}

	public void registerParameters(ParameterRegistry registry) {
		// nothing to do
	}

	public String render(RenderingContext renderingContext) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("varchar(");
		buffer.append( ( (Renderable) getPath() ).render( renderingContext ) );
		buffer.append(")");
//		buffer.append("char(");
//		buffer.append( "YEAR(" )
//				.append( ( (Renderable) getPath() ).render( renderingContext ) )
//				.append( ")||'-'||" )
//				.append( "MONTH(" )
//				.append( ( (Renderable) getPath() ).render( renderingContext ) )
//				.append(")");
//		buffer.append(")");

//		
		return buffer.toString();
	}

	public String renderProjection(RenderingContext renderingContext) {
		return CastFunction.CAST_NAME + "( 	varchar  as " + renderingContext.getCastType( getJavaType() ) + ')';
	}

	public Expression<String> getPath() {
		return path;
	}

	public void setPath(Expression<String> path) {
		this.path = path;
	}
}
