package hibernateDemo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.hibernate.Session;

import hibernateDemo.pojo.Category;
import hibernateDemo.pojo.Manufacturer;
import hibernateDemo.pojo.Product;

public class HibernateTester {
	public static void main(String[] args) {
		categoryTest();
		productTestManyToOne();
		categoryTestManyToOne();
		pro_manManyToMany();
	}

	private static void categoryTest() {
		Session session = HibernateUtils.getFactory().openSession();
		// If category empty -> AUTO_INCREMENT =1;
		Query q0 = session.createQuery("FROM Category");// HQL
		List<Category> cates_0 = q0.getResultList();
		if (cates_0.isEmpty()) {
			System.out.println("Table empty --> reset AUTO_INCREMENT=1");
			session.getTransaction().begin();
			session.createSQLQuery("ALTER TABLE category AUTO_INCREMENT = 1").executeUpdate();
			session.getTransaction().commit();
		}
		/*
		 * insert
		 */

		session.getTransaction().begin();
		Category c1 = new Category();
		c1.setName("Phu Kien");
		c1.setDescription("");
		session.save(c1);
		Category c2 = new Category();
		c2.setName("Laptop");
		c2.setDescription("Dell, Mac");
		session.save(c2);
		Category c3 = new Category();
		c3.setName("Dien Thoai");
		c3.setDescription("Oppo, Iphone, Nokia,...");
		session.save(c3);

		session.getTransaction().commit();
		/*
		 * get -> update
		 */
		Category c4 = session.get(Category.class, 1);
		if (c4 != null) {
			c4.setDescription("Du Lieu Da Cap Nhat");
			// ví dụ cập nhật từ 2 client chung 1 id =1(tức là tranh chấp dữ liệu thường
			// là update, delete) thì sao? -> getTransaction()
			session.getTransaction().begin();
			session.save(c4);// lưu trên Ram thôi
			session.getTransaction().commit();
		}

		/*
		 * delete
		 */
//		Category c5 = session.get(Category.class, 1);
//		if (c5 != null) {
//			session.getTransaction().begin();
//			session.delete(c5);
//			session.getTransaction().commit();
//		}
		/*
		 * What's HQL(Hibernate Query Language)? -> Tương tự SQL nhưng HQL là một ngôn
		 * ngữ truy vấn hướng đối tượng, không phụ thuộc databases nào.
		 */
		Query q = session.createQuery("FROM Category WHERE id=1");// HQL
		List<Category> cates = q.getResultList();
		cates.forEach(c -> System.out.println(c.getId() + " - " + c.getName() + " - " + c.getDescription()));
		session.close();
	}

	private static void productTestManyToOne() {
		Session session = HibernateUtils.getFactory().openSession();

		Product p0 = new Product();
		p0.setName("Ipad Pro 2022");
		p0.setPrice(new BigDecimal(22000000));

		Category c = session.get(Category.class, 2);
		p0.setCategory(c);

		session.save(p0);
		session.close();
	}

	private static void categoryTestManyToOne() {
		Session session = HibernateUtils.getFactory().openSession();

		Category c = session.get(Category.class, 2);
		System.out.println("--------------------------");
		c.getProducts().forEach(p -> {
			System.out.println(p.getId() + " - " + p.getName());
		});
		System.out.println("--------------------------");
		session.close();
	}
	
	private static void pro_manManyToMany() {
		Session session = HibernateUtils.getFactory().openSession();
		
		Product p = new Product();
		p.setName("Nokia 2024");
		p.setPrice(new BigDecimal(14000000));
		
		Category c = session.get(Category.class, 1);
		p.setCategory(c);
		
		Set<Manufacturer> mans = new HashSet<Manufacturer>();
		mans.add(session.get(Manufacturer.class, 1));
		mans.add(session.get(Manufacturer.class, 2));
		p.setManufacturers(mans);
		
		session.getTransaction().begin();
		session.save(p);
		session.getTransaction().commit();
		
		session.close();
	}
}
