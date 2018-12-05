package com.mawujun.repository.mybatis.expression;

import java.io.Serializable;

import javax.persistence.criteria.Expression;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;
import org.hibernate.query.criteria.internal.ParameterRegistry;
import org.hibernate.query.criteria.internal.Renderable;
import org.hibernate.query.criteria.internal.ParameterContainer.Helper;
import org.hibernate.query.criteria.internal.compile.RenderingContext;
import org.hibernate.query.criteria.internal.expression.LiteralExpression;
import org.hibernate.query.criteria.internal.expression.function.BasicFunctionExpression;

/**
* @author mawujun 16064988
* @createDate ：2018年12月5日 下午1:41:30
*/
public class ConvertFunction extends BasicFunctionExpression<String> implements Serializable{
	public static final String NAME = "convert";

	private final Expression<String> value;
	private  int len=50;
	private final Expression<Integer> style;

	public ConvertFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> value,
			int len,
			Expression<Integer> style) {
		super( criteriaBuilder, String.class, NAME );
		this.value = value;
		this.len = len;
		this.style = style;
	}
//
//	@SuppressWarnings({ "RedundantCast" })
//	public ConvertFunction(
//			CriteriaBuilderImpl criteriaBuilder,
//			Expression<String> value, 
//			Expression<Integer> start) {
//		this( criteriaBuilder, value, start, (Expression<Integer>)null );
//	}
//
//	public ConvertFunction(
//			CriteriaBuilderImpl criteriaBuilder,
//			Expression<String> value,
//			int start) {
//		this(
//				criteriaBuilder,
//				value,
//				new LiteralExpression<Integer>( criteriaBuilder, start )
//		);
//	}

	/**
	 * 
	 * @param criteriaBuilder
	 * @param value 字段表达式
	 * @param len 要转换的varchar的长度
	 * @param style sql server的style值
	 */
	public ConvertFunction(
			CriteriaBuilderImpl criteriaBuilder,
			Expression<String> value,
			int len,
			int style) {
		this(
				criteriaBuilder,
				value,
				len,
				new LiteralExpression<Integer>( criteriaBuilder, style )
		);
	}

	public Expression<String> getValue() {
		return value;
	}

	@Override
	public void registerParameters(ParameterRegistry registry) {
		//Helper.possibleParameter( getLen(), registry );
		//Helper.possibleParameter( getStyle(), registry );
		//Helper.possibleParameter( getValue(), registry );
	}

	public String render(RenderingContext renderingContext) {
//		StringBuilder buffer = new StringBuilder();
//		buffer.append( "convert(" )
//				.append( "varchar("+len+")" )
//				.append( ',' )
//				.append( ( (Renderable) getValue() ).render( renderingContext ) )
//				.append( ',' )
//				//.append( ( (Renderable) getStyle() ).render( renderingContext ) );
//				.append("23");
//
//		buffer.append( ')' );
//		return buffer.toString();
		
		StringBuilder buffer = new StringBuilder();
		//buffer.append( "substring(" );
		//buffer.append( "LOWER(" );
		
		buffer.append( "convert(" )
				.append( "varchar("+len+")" )
				.append( ',' )
				.append( ( (Renderable) getValue() ).render( renderingContext ) )
				.append( ',' )
				//.append( ( (Renderable) getStyle() ).render( renderingContext ) );
				.append("23");
		buffer.append( ')' );
		//buffer.append( ")" );

		//buffer.append(",").append("1").append(",").append("9").append(")");
		
		return buffer.toString();
	}

	public int getLen() {
		return len;
	}

	public Expression<Integer> getStyle() {
		return style;
	}
}
