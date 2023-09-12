package hibernateDemo;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import hibernateDemo.pojo.Category;
import hibernateDemo.pojo.Manufacturer;
import hibernateDemo.pojo.Product;

public class HibernateTester {
	public static void main(String[] args) {
//		categoryTest();
//		productTestManyToOne();
//		categoryTestManyToOne();
//		pro_manManyToMany();
		/*
		 * HQL: Query và Criteria
		 */
//		hql_criteria_1();
//		hql_criteria_2();
		hql_query();
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

	private static void hql_criteria_1() {
		Session session = HibernateUtils.getFactory().openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		/*
		 * 
		 */
		CriteriaQuery<Product> query = builder.createQuery(Product.class);

		// Co nhieu table -> tao nhieu Root
		Root root = query.from(Product.class);
		query = query.select(root);// select * from
		// where
		Predicate p1 = builder.like(root.get("name").as(String.class), "%Nokia%");
		Predicate p2 = builder.greaterThan(root.get("price").as(BigDecimal.class), 10000000);
		query.where(builder.or(p1));

		Query q = session.createQuery(query);
		List<Product> products = q.getResultList();
		products.forEach(p -> System.out.println(p.toString() + "\n"));
		/*
		 * 
		 */
		CriteriaQuery<Object[]> queryObjArr = builder.createQuery(Object[].class);
		Root rootObjArr = queryObjArr.from(Product.class);
		queryObjArr = queryObjArr.multiselect(builder.count(rootObjArr.get("id").as(Integer.class)),
				builder.max(rootObjArr.get("price").as(BigDecimal.class)));

		Query q2 = session.createQuery(queryObjArr);
		Object[] re = (Object[]) q2.getSingleResult();
		System.out.println("Count = " + re[0]);
		System.out.println("Max = " + re[1]);
		session.close();
	}

	/*
	 * select c.name, Count(p.id), Max(p.price) from category c inter join product p
	 * on c.id = p.category_id group by c.name order by c.name asc
	 */
	private static void hql_criteria_2() {
		Session session = HibernateUtils.getFactory().openSession();

		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Object[]> query = builder.createQuery(Object[].class);

		Root<Product> pRoot = query.from(Product.class);
		Root<Category> cRoot = query.from(Category.class);
		query.where(builder.equal(pRoot.get("category"), cRoot.get("id")));

		query = query.multiselect(cRoot.get("name").as(String.class), builder.count(pRoot.get("id").as(Integer.class)),
				builder.max(pRoot.get("price").as(BigDecimal.class)));

		query = query.groupBy(cRoot.get("name").as(String.class));
		query = query.orderBy(builder.asc(cRoot.get("name").as(String.class)));

		Query q = session.createQuery(query);
		List<Object[]> re = q.getResultList();
		System.out.println("\n ---------------------Show test 5 result ------------------>");
		re.forEach(k -> {
			System.out.printf("%s - count: %d - Max: %.2f\n", k[0], k[1], k[2]);
		});

		session.close();
	}

	private static void hql_query() {
		Session session = HibernateUtils.getFactory().openSession();
		// select 1
		Query q = session.createQuery("FROM Product");
		List<Product> products = q.getResultList();
		System.out.println();
		products.forEach(p -> System.out.println(p));
		System.out.println();
		// select 2
		q = session.createQuery("SELECT p.id, p.name, p.price FROM Product p");// Product class
		List<Object[]> products_2 = q.getResultList();
		products_2.forEach(p2 -> System.out.println(p2[0] + "\t" + p2[1] + "\t" + p2[2]));
		// where and order by
		System.out.println();
		q = session.createQuery("SELECT p.id, p.name, p.price FROM Product p " + "WHERE p.id=:id");// Product class
		q.setParameter("id", 1);
		List<Object[]> products_3 = q.getResultList();
		products_3.forEach(p -> System.out.println(p[0] + "\t" + p[1] + "\t" + p[2]));
		// join, group by, min, max, count,...
		System.out.println("\n-------------------------------");
//		"SELECT c.name, COUNT(p.id), MAX(p.price), MIN(p.price) FROM Product p INNER JOIN p.category c GROUP BY c.name";
		q = session.createQuery(
				"SELECT c.name, Count(p.id), Max(p.price), Min(p.price) FROM Product p INNER JOIN Category c ON p.category.id=c.id "
						+ "GROUP BY c.name");
		List<Object[]> re = q.getResultList();
		re.forEach(obj -> {
			System.out.printf("%s: Count: %d; Max: %2f; Min: %2f\n", obj[0], obj[1], obj[2], obj[3]);

		});
		// insert, update, delete
		System.out.println("\n-------------------------------");
		q = session.createQuery("UPDATE Product p SET p.name=:name_1 WHERE p.id=:id_1");
		q.setParameter("name_1", "30/7/2023 Code on Ubuntu 20.04 :))");
		q.setParameter("id_1", 1);
		session.getTransaction().begin();
		q.executeUpdate();
		session.getTransaction().commit();
		session.close();
	}
}
