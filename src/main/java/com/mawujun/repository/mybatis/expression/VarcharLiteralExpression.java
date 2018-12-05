package com.mawujun.repository.mybatis.expression;

import java.io.Serializable;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.Renderable;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.ExpressionImpl;
import org.hibernate.query.criteria.internal.expression.function.CastFunction;

/**
 * 用于sql server
* @author mawujun 16064988
* @createDate ：2018年12月5日 上午11:24:01
*/
public class VarcharLiteralExpression extends ExpressionImpl<String> implements Serializable {
	private int len;
	public VarcharLiteralExpression(CriteriaBuilderImpl criteriaBuilder,int len) {
		super( criteriaBuilder, String.class );
		this.len=len;
	}

	public void registerParameters(ParameterRegistry registry) {
		// nothing to do
	}

	public String render(RenderingContext renderingContext) {
		return "varchar("+len+")";
		
//		StringBuilder buffer = new StringBuilder();
//		buffer.append( "substring(" );
//		
//		buffer.append( "convert(" )
//				.append( "varchar("+len+")" )
//				.append( ',' )
//				//.append( ( (Renderable) getValue() ).render( renderingContext ) )
//				.append( "aaa" )
//				.append( ',' )
//				//.append( ( (Renderable) getStyle() ).render( renderingContext ) );
//				.append("23");
//		buffer.append( ')' );
//		
//		buffer.append(",").append("1").append(",").append("7").append(")");
//		
//		return buffer.toString();
	}

	public String renderProjection(RenderingContext renderingContext) {
		return CastFunction.CAST_NAME + "( 	varchar  as " + renderingContext.getCastType( getJavaType() ) + ')';
	}
}
