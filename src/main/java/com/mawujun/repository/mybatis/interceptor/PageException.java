package com.mawujun.repository.mybatis.interceptor;

/**
 * 分页异常
 * 
 * @author mawujun
 *
 */
public class PageException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2965769605953837318L;

	public PageException() {
		super();
	}

	public PageException(String message) {
		super(message);
	}

	public PageException(String message, Throwable cause) {
		super(message, cause);
	}

	public PageException(Throwable cause) {
		super(cause);
	}

}
