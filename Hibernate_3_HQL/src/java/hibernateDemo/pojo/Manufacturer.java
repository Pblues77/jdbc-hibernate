package hibernateDemo.pojo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/*
	 USE saledb;
	 CREATE TABLE manufacture(
	 	id int(11) NOT NULL AUTO_INCREMENT,
	 	name varchar(50) NOT NULL,
	 	country varchar(50),
	 	PRIMARY KEY(id)
	 );
	 CREATE TABLE pro_man(
	 	id int(11) NOT NULL AUTO_INCREMENT,
	 	product_id int(11),
	 	manufacturer_id int(11),
	 	PRIMARY KEY(id)
	 );
	 INSERT INTO manufacture (name, country)
	 VALUES ('APPLE', 'USA');
	 INSERT INTO manufacture (name, country)
	 VALUES ('SAMSUNG', 'Korea');
 */
@Entity
@Table(name = "manufacture")
public class Manufacturer implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String country;
	@ManyToMany(mappedBy = "manufacturers")
	private Set<Product> products;
	
	/*
	 * GETTERS, SETTERS
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

}
