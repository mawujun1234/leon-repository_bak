package com.mawujun.document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
	
//	/**
//	 * 获取文件的存放目录
//	 * @param element
//	 */
//	public void configure(final Element element) {
//		
//		this.name = XmlUtil.getAttribute(element, "name");
//		String value = XmlUtil.getChildElementText(element, "repository-root-directory", "");
//		if (StringUtil.isNotBlank(value)) {
//			this.root = Preference.instance().substitute(value);
//		}
//		this.root = StringUtil.trimNotNull(this.root);
//	}
	
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
			return Document.fromLocalFile(doc);
		} else {
			return null;
		}
	}

	@Override
	public boolean save(final Document document) throws IOException {
		checkRoot();
		if (document != null) {
			File file = new File(new File(this.root), document.getName());
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
