package com.mawujun.document;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * 
 * @author mawujun 16064988@qq.com
 *
 */
@Configuration
public class DocumentRepositoryConfig {
	
	@Value("${leon.doc.class}")
	private String leon_doc_class;
	
	@Bean
	public DocumentRepository documentRepository() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class clazz=null;
		if(leon_doc_class==null) {
			clazz=LocalFileDocumentRepository.class;
		} else {
			clazz=Class.forName(leon_doc_class);
		}
		
		return (DocumentRepository)clazz.newInstance();
		
	}

}
