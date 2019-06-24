package com.mawujun.document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.mawujun.exception.BizException;
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
	 * 文档名称,包含group的名稱，即包含子路径的名称，例如aa/bbb/ccc.jpg
	 */
	private String fullName;
	
	/**
	 * 文档名称，不包含目录
	 */
	private String fileName;
	
	/**
	 * 文档的原始名称
	 */
	private String originalFullName;
	
	/**
	 * 文档的原始名称，不包含目录
	 */
	private String originalFileName;

	/**
	 * 是否使用原始的文件名保存文件
	 */
	private boolean userOrginalFileName=false;
	/**
	 * 最后一次修改时间
	 */
	private Date lastModified;
	
	public final static String spart_char="__" ;
	/**
	 * 文档内容的字节缓冲流
	 */
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
	/**
	 * fullName=ccc.jpg表是把jpg文件存放在指定的根目录下
	 * fullName=aa/bbb/ccc.jpg:表示将文件存放在根目录下，并且生成子目录
	 * @param fullName 文档的全名,
	 * @param userOrginalFileName 使用文件原来的名字，应用场景是load/download已经存在的文件的时候，还有就是保存的时候，按照原来的名字进行保存
	 */
	public Document(String fullName,Boolean userOrginalFileName) {
		if(!StringUtils.hasText(fullName)) {
			throw new BizException("文件名不能为空!");
		}
		fullName=FileUtil.normalize(fullName);
		this.userOrginalFileName=userOrginalFileName;
		this.originalFullName= FileUtil.addStartSlash(FileUtil.removeEndSlash(fullName));;
		this.originalFileName=FileUtil.getFileName(fullName);
		
		if(userOrginalFileName) {
			this.fileName=this.originalFileName;
			this.fullName =originalFullName;
		} else {
			String aa[]=this.originalFileName.split("\\.");
			this.fileName=aa[0]+spart_char+uniqueName()+"."+aa[1];
			this.fullName = FileUtil.getFileParentPath(this.originalFullName)+File.separator+this.fileName;
		}

	}
	public Document(final String fullName) {
		this(fullName,false);
	}
	/**
	 * @param fullName 文档的全名，可以是文件名，也可以带路径
	 * @param userOrginalFileName 使用文件原来的名字，应用场景是load/download已经存在的文件的时候，还有就是保存的时候，按照原来的名字进行保存
	 */
	public Document(String groupName,final String filename,Boolean userOrginalFileName) {
		
		if(!StringUtils.hasText(filename)) {
			throw new BizException("文件名不能为空!");
		}
		//groupName=FileUtil.normalize(groupName);
		//fullName=FileUtil.normalize(fullName);
		this.userOrginalFileName=userOrginalFileName;
		if(groupName==null) {
			groupName="";
		}
		this.originalFileName=FileUtil.getFileName(filename);
		this.originalFullName = FileUtil.addStartSlash(FileUtil.removeEndSlash(FileUtil.combine(groupName, filename)));
		
		if(userOrginalFileName) {
			this.fileName=this.originalFileName;
			this.fullName =originalFullName;
		} else {
			String aa[]=this.originalFileName.split("\\.");
			this.fileName=aa[0]+spart_char+uniqueName()+"."+aa[1];
			this.fullName = FileUtil.getFileParentPath(this.originalFullName)+"/"+this.fileName;
		}		
	}
	public Document(String groupName,final String fullName) {
		this(groupName,fullName,false);
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
	 * @return 文档的大小，按字节算
	 */
	public int size() {
		return this.buffer.size();
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
	 * 江文件的内容输出到外部的OutputStream，例如response.getOutputStream();
	 * @param os
	 * @return
	 * @throws IOException 
	 */
	public void writeToOutputStream(OutputStream out) throws IOException {
		 // 读取要下载的文件，保存到文件输入流
        InputStream in = this.getInputStream();
        // 创建输出流
        //OutputStream out = response.getOutputStream();
        // 创建缓冲区
        byte[] buffer = new byte[1024];
        int len = 0;
        // 循环将输入流中的内容读取到缓冲区当中
        while ((len = in.read(buffer)) > 0) {
            // 输出缓冲区的内容到浏览器，实现文件下载
            out.write(buffer, 0, len);
        }
        // 关闭文件输入流
        in.close();
        // 关闭输出流
        out.close();
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
	public String getFileName() {
		return fileName;
	}
	public String getFullName() {
		return fullName;
	}
	public boolean isUserOrginalFileName() {
		return userOrginalFileName;
	}
	public String getOriginalFullName() {
		return originalFullName;
	}
	public String getOriginalFileName() {
		return originalFileName;
	}
	public static String getSpartChar() {
		return spart_char;
	}
	public ByteArrayOutputStream getBuffer() {
		return buffer;
	}

}
