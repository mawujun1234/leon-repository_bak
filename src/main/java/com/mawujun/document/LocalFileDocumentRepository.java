package com.mawujun.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import com.mawujun.exception.BizException;
import com.mawujun.util.StringUtil;

/**
 * 将文档保存在本地目录中
 * @author mawujun 16064988@qq.com
 *
 */
public class LocalFileDocumentRepository implements DocumentRepository {

	/**
	 * 本地仓库名称
	 */
	private String name;
	
	/**
	 * 文档存放的跟目录
	 */
	@Value("${leon.doc.rootdir}")
	private String root;
	
	
	public void checkRoot() {
		if(!StringUtil.hasText(root)) {
			throw new BizException("需要指定文件存储根目录路:leon.doc.rootdir");
		}
	}

	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean exists(final String fullName) throws IOException {
		checkRoot();
		File doc = new File(new File(this.root), fullName);
		return doc.isFile() && doc.exists();
	}


	@Override
	public boolean delete(final String fullName) throws IOException {
		checkRoot();
		File doc = new File(new File(this.root), fullName);
		if (doc.isFile() && doc.exists()) {
			doc.delete();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Document load(final String fullName) throws IOException {
		checkRoot();
		File doc = new File(new File(this.root), fullName);
		if (doc.isFile() && doc.exists()) {
			return fromLocalFile(doc);
		} else {
			return null;
		}
	}
	
	/**
	 * 加载文件的内容到文档中去，文档的名称是文件的名称
	 * 
	 * @param file 需要加载的文件
	 * @return 加载的新文档
	 * @throws IOException 如果读取文件时出错
	 */
	public Document fromLocalFile(final File file) throws IOException {
		return fromLocalFile(file.getAbsolutePath(), file);
	}
	
	/**
	 * @param fullName 文档的名称
	 * @param file 需要加载的文件
	 * @return 加载的新文件
	 * @throws IOException 如果读取文件时出错
	 */
	public Document fromLocalFile(final String fullName, final File file) throws IOException {
		
		Document document = new Document(fullName);
		try (InputStream in = new FileInputStream(file)) {
			for (int b = in.read(); b != -1; b = in.read()) {
				document.write((byte) b);
			}
			document.setLastModified(new Date(file.lastModified()));
		} catch (IOException e) {
			throw e;
		}
		return document;
	}

	@Override
	public boolean save(final Document document) throws IOException {
		checkRoot();
		if (document != null) {
			File file = new File(new File(this.root), document.getFullName());
			try {
				if ((file.getParentFile() != null) && (!file.getParentFile().exists())) {
					file.getParentFile().mkdirs();
				}
				if (file.exists()) {
					file.delete();
				}
			} catch (Exception e) {
				throw new IOException(e);
			}
			
			InputStream in = document.getInputStream();
			try (OutputStream out = new FileOutputStream(file, true)) {
				for (int i = in.read(); i != -1; i = in.read()) {
					out.write(i);
				}
			}
			return true;
		} else {
			return false;
		}
	}

}
