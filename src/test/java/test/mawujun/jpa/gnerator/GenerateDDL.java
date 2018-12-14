package test.mawujun.jpa.gnerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mawujun.generator.ddl.GenerateDDLService;

import test.mawujun.jpa.utils.M;



public class GenerateDDL {
	private final static Logger logger=LoggerFactory.getLogger(GenerateDDL.class);

	public static void main(String[] args) {
		
		GenerateDDLService.generateDLL("test.mawujun.model,com.mawujun","/db/script");

//		//File file=new File("E:\\my-workspace\\leon-repository\\src\\test\\java\\test\\mawujun\\jpa\\gnerator\\hibernate.cfg.xml");
//		File file=generateCfg();
//		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure(file).build();
//	    Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata();
//	    
//	    SchemaExport export = new SchemaExport();
//	    export.setOutputFile("E:\\my-workspace\\leon-repository\\src\\test\\java\\test\\mawujun\\jpa\\gnerator\\aaaa.sql");
//	    export.setDelimiter(";");
//	    export.setFormat(true);
//	    
//	    export.drop(EnumSet.of(TargetType.SCRIPT), metadata);
//	    export.createOnly(EnumSet.of(TargetType.SCRIPT),metadata);
//	    
//	   // export.execute(EnumSet.of(TargetType.SCRIPT), Action.BOTH, metadata, serviceRegistry);
	}
	
}
