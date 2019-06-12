package com.mawujun.document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.mawujun.io.FileUtil;
import com.mawujun.lang.UUID;
import com.mawujun.util.ArrayUtil;
import com.mawujun.util.StringUtil;
import com.mawujun.util.URLUtil;

/**
 * 用来封装文档（如图片等），文档没有定义存储地址，可以存放在任何地方，如本地主机，远端DFS,Mongdb等。
 * @author mawujun 16064988@qq.com
 *
 */
public class Document {

	/**
	 * 文档名称
	 */
	private String fullName;
	
	/**
	 * 最后一次修改时间
	 */
	private Date lastModified;
	
	/**
	 * 文档内容的字节缓冲流
	 */
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
	/**
	 * @param fullName 文档的全名
	 */
	public Document(final String fullName) {
		this.fullName = FileUtil.stripStartSlash(FileUtil.stripEndSlash(FileUtil.normalize(fullName)));
	}
	
//	/**
//	 * @param rootName 文档的跟名
//	 * @param segmentNames 按分段方式表示的文档名称
//	 */
//	public Document(final String rootName, final String... segmentNames) {
//		
//		String result = URLUtil.normalize(rootName);
//		if (ArrayUtil.isNotEmpty(segmentNames)) {
//			for (String name : segmentNames) {
//				if (!StringUtil.isBlank(name)) {
//					result = FileUtil.combine(result, URLUtil.normalize(name));
//				}
//			}
//		}
//		this.fullName = FileUtil.stripStartSlash(FileUtil.stripEndSlash(result));
//	}
	
	/**
	 * 生成唯一的文件名称，通过UUID随机生成文件名称。
	 * 
	 * @return 唯一的文档名称
	 */
	public static String uniqueName() {
		return UUID.randomUUID().toString();
	}
	
	/**
	 * @return 文档的名称
	 */
	public String getName() {
		return this.fullName;
	}
	
	/**
	 * @return 文档的大小，按字节算
	 */
	public int size() {
		return this.buffer.size();
	}
	
	/**
	 * 加载文件的内容到文档中去，文档的名称是文件的名称
	 * 
	 * @param file 需要加载的文件
	 * @return 加载的新文档
	 * @throws IOException 如果读取文件时出错
	 */
	public static Document fromLocalFile(final File file) throws IOException {
		return fromLocalFile(file.getAbsolutePath(), file);
	}
	
	/**
	 * @param fullName 文档的名称
	 * @param file 需要加载的文件
	 * @return 加载的新文件
	 * @throws IOException 如果读取文件时出错
	 */
	public static Document fromLocalFile(final String fullName, final File file) throws IOException {
		
		Document document = new Document(fullName);
		try (InputStream in = new FileInputStream(file)) {
			for (int b = in.read(); b != -1; b = in.read()) {
				document.buffer.write((byte) b);
			}
			document.lastModified = new Date(file.lastModified());
		} catch (IOException e) {
			throw e;
		}
		return document;
	}

	/**
	 * @return 文档的最后修改日期
	 */
	public Date getLastModified() {
		return this.lastModified;
	}
	
	/**
	 * @param lastModified 文档的最后修改日期
	 */
	public void setLastModified(final Date lastModified) {
		this.lastModified = lastModified;
	}

	/**
	 * @return 文档字节数据缓冲区
	 */
	public byte[] getData() {
		return this.buffer.toByteArray();
	}

	/**
	 * @param data 文档字节数据缓冲区
	 * @throws IOException 如果写缓存时出错
	 */
	public void setData(final byte[] data) throws IOException {
		
		this.buffer = new ByteArrayOutputStream();
		this.buffer.write(data);
	}

	/**
	 * @return 数据缓冲区封装为输入流
	 */
	public InputStream getInputStream() {
		return new ByteArrayInputStream(this.buffer.toByteArray());
	}
	
	/**
	 * @return 将数据缓冲区封装为输出流
	 */
	public OutputStream getOutputStream() {
		return this.buffer;
	}
	/**
	 * @param b 写入的字节
	 */
	public void write(final byte b) {
		this.buffer.write(b);
	}
	
	/**
	 * @param bytes 写入的字节数组
	 * @throws IOException 如果写缓存时出错
	 */
	public void write(final byte... bytes) throws IOException {
		this.buffer.write(bytes);
	}
	
	/**
	 * @param bytes 写入字节的数字
	 * @param offset 数字开始位置
	 * @param length 写入字节的长度
	 */
	public void write(final byte[] bytes, final int offset, final int length) {
		this.buffer.write(bytes, offset, length);
	}
}
