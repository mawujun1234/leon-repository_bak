package test.mawujun.generate;

import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import test.mawujun.repository.App;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JPATest {
	@Autowired
	EntityManagerFactory entityManagerFactory;

	@Test
	public void init() throws Exception {
		// JPASetup jPASetup= new JPASetup("aaa", "aaa");

		// EntityManagerFactory entityManagerFactory = JPA.getEntityManagerFactory();

		
		Metamodel mm = entityManagerFactory.getMetamodel();

		Set<ManagedType<?>> managedTypes = mm.getManagedTypes();
		// Assert.assertEquals(managedTypes., 1);
		ManagedType itemType = managedTypes.iterator().next();
		assertEquals(itemType.getPersistenceType(), Type.PersistenceType.ENTITY);
		SingularAttribute nameAttribute = itemType.getSingularAttribute("name");
		assertEquals(nameAttribute.getJavaType(), String.class);
		assertEquals(nameAttribute.getPersistentAttributeType(), Attribute.PersistentAttributeType.BASIC);
		assertFalse(nameAttribute.isOptional()); // NOT NULL
		nameAttribute.
		
		

		SingularAttribute auctionEndAttribute = itemType.getSingularAttribute("auctionEnd");
		assertEquals(auctionEndAttribute.getJavaType(), Date.class);
		assertFalse(auctionEndAttribute.isCollection());
		assertFalse(auctionEndAttribute.isAssociation());

	}

}
