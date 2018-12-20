package test.mawujun.generator;

import java.util.List;

import org.junit.Test;

import com.mawujun.generator.code.DbTableMetadataService;
import com.mawujun.generator.code.EntityTable;
import com.mawujun.generator.code.PropertyColumn;

public class DbTableMetadataServiceTest {
	@Test
	public void test() {
		DbTableMetadataService service=new DbTableMetadataService();
		 List<EntityTable> list=service.getTablesInfo();
		 for(EntityTable et:list) {
			 System.out.println(et.getEntityTableName());
			 List<PropertyColumn> columns=et.getPropertyColumns();
			 for(PropertyColumn col:columns) {
				 System.out.println(col);
			 }
		 }
		
	}

}
