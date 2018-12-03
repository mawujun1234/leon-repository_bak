package test.mawujun.mybatis.dialect;

import org.junit.Assert;
import org.junit.Test;

import com.mawujun.repository.mybatis.dialect.SqlServer2005Dialect;



/**
* @author mawujun 16064988
* @createDate ：2018年12月3日 下午8:40:22
*/
public class SqlServer2005DialectTest {
	
	SqlServer2005Dialect dialect=new SqlServer2005Dialect();
	@Test
	public void test() {
		String sql="select * from (select * from aaaa where 1=1 and a.bb=c.gg order  by bbb asc,a.ccc desc ) where 2=2 order by   id desc    ";
		Assert.assertEquals(true, dialect.existsOrderBy(sql));
		Assert.assertEquals(true, dialect.existsEndOrderBy(sql));
		Assert.assertEquals("select * from (select * from aaaa where 1=1 and a.bb=c.gg ) where 2=2 ", dialect.removeOrderby(sql));
		Assert.assertEquals("order by   id desc    ", dialect.getEndOrderBy(sql));
	}

}
