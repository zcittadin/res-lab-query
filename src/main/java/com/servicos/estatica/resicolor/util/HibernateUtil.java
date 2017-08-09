package com.servicos.estatica.resicolor.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.servicos.estatica.resicolor.model.Processo;
import com.servicos.estatica.resicolor.model.Produto;
import com.servicos.estatica.resicolor.model.VwProcesso;

public class HibernateUtil {

	private final static SessionFactory sessionFactory;
	private final static ServiceRegistry serviceRegistry;
	private static final Configuration configuration = new Configuration();

	static {
		getConfiguration();
		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	public static Session openSession() {
		return sessionFactory.openSession();
	}

	public static void closeSessionFactory() throws Exception {
		sessionFactory.close();
	}

	private static void getConfiguration() {
		configuration.addPackage("com.servicos.estatica.resicolor.model");
		configuration.addAnnotatedClass(Produto.class);
		configuration.addAnnotatedClass(Processo.class);
		configuration.addAnnotatedClass(VwProcesso.class);
		configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
		
		//TESTES
//		configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost/resicolor");
//		configuration.setProperty("hibernate.connection.username", "root");
//		configuration.setProperty("hibernate.connection.password", "root");
		
		//OFICIAL
		configuration.setProperty("hibernate.connection.url", "jdbc:mysql://192.168.1.124/resicolor");
		configuration.setProperty("hibernate.connection.username", "vanessa");
		configuration.setProperty("hibernate.connection.password", "vanessa");

		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
//		 configuration.setProperty("hibernate.show_sql", "true");
		configuration.setProperty("hibernate.format_sql", "true");
	}

}
