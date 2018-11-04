package test.mawujun.generate;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.annotations.common.util.StringHelper;

public class JPASetup {
	protected final String persistenceUnitName;
    protected final Map<String, String> properties = new HashMap<>();
    protected final EntityManagerFactory entityManagerFactory;

    public JPASetup(
                    String persistenceUnitName,
                    String... hbmResources) throws Exception {

        this.persistenceUnitName = persistenceUnitName;
        
        properties.put("hibernate.connection.driver_class", "org.h2.Driver");
        properties.put("hibernate.connection.url", "jdbc:h2:mem:test;DB_CLOSE_ON_EXIT=FALSE");
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.connection.username", "sa");
//        properties.put("hibernate.connection.password", "");
//        properties.put("", "");
//        properties.put("", "");
        

        // No automatic scanning by Hibernate, all persistence units list explicit classes/packages
        properties.put(
            "hibernate.archive.autodetection",
            "none"
        );

        // Really the only way how we can get hbm.xml files into an explicit persistence
        // unit (where Hibernate scanning is disabled)
        properties.put(
            "hibernate.hbmxml.files",
            StringHelper.join(",", hbmResources != null ? hbmResources : new String[0])
        );

        // We don't want to repeat these settings for all units in persistence.xml, so
        // they are set here programmatically
        properties.put(
            "hibernate.format_sql",
            "true"
        );
        properties.put(
            "hibernate.use_sql_comments",
            "true"
        );

//        // Select database SQL dialect
//        properties.put(
//            "hibernate.dialect",
//            databaseProduct.hibernateDialect
//        );
        
  

        entityManagerFactory =
            Persistence.createEntityManagerFactory(getPersistenceUnitName(), properties);
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public void createSchema() {
        generateSchema("create");
    }

    public void dropSchema() {
        generateSchema("drop");
    }

    public void generateSchema(String action) {
        // Take exiting EMF properties, override the schema generation setting on a copy
        Map<String, String> createSchemaProperties = new HashMap<>(properties);
        createSchemaProperties.put(
            "javax.persistence.schema-generation.database.action",
            action
        );
        Persistence.generateSchema(getPersistenceUnitName(), createSchemaProperties);
    }
}
