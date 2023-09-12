package hibernateDemo;

import java.util.Properties;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import hibernateDemo.pojo.Category;
import hibernateDemo.pojo.Product;

//B1: 
public class HibernateUtils {
	private static final SessionFactory FACTORY;

	// Nhằm tạo SessionFACTORY 1 lần duy nhất khi chạy
	static {
		org.hibernate.cfg.Configuration conf = new org.hibernate.cfg.Configuration();

		conf.configure("hibernate.cfg.xml");
		// Cấu hình
//		Properties props = new Properties();
//		props.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
//		props.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
//		props.put(Environment.URL, "jdbc:mysql://localhost/saledb");
//		props.put(Environment.USER, "root");
//		props.put(Environment.PASS, "chelseafc");
//		props.put(Environment.SHOW_SQL, "true");
//		conf.setProperties(props);
		//
		conf.addAnnotatedClass(Category.class);
		conf.addAnnotatedClass(Product.class);

		ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();

		FACTORY = conf.buildSessionFactory(registry);
	}

	public static SessionFactory getFactory() {
		return FACTORY;
	}
}
