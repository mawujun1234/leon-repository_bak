package com.mawujun.document;

import java.io.IOException;

/**
   *    用于文档存储的接口定义
 * @author mawujun 16064988@qq.com
 *
 */
public interface DocumentRepository {
	/**
	 * 缺省的文档仓库
	 */
	String DEFAULT = "default";

	/**
	 * @return 仓库名称
	 */
	String getName();
	
	/**
	 * 用来检测文档是否存在
	 * 
	 * @param fullName 文档名称，一般和文件系统的路径一样
	 * @return 文档是否存在
	 * @throws IOException 如果测试文档存在失败
	 */
	boolean exists(String fullName) throws IOException;

	/**
	 * 删除文档，如果文档存在并成功删除，返回true。如果文档不存在，返回false
	 * 
	 * @param fullName 文档名称
	 * @return 是否文件存在并删除
	 * @throws IOException 如果删除时发生错误
	 */
	boolean delete(String fullName) throws IOException;
	
	/**
	 * 通过文档名称加载一个存在的稳定
	 * 
	 * @param fullName 文档名称
	 * @return 已加载的文档，如果文档不存在，将返回null
	 * @throws IOException 如果加载文档时失败
	 */
	Document load(String fullName) throws IOException;
	
	/**
	 * 保存一个文档并返回true，如果文档内容为空，将返回false
	 * 
	 * @param document 文档
	 * @return 如果需要保存的文档为空
	 * @throws IOException 如果保存文档时失败
	 */
	boolean save(Document document) throws IOException;
}
