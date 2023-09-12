package hibernateDemo.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/*
 * B2: 
		 USE saledb;
		 CREATE TABLE product(
		 	id int(11) NOT NULL AUTO_INCREMENT,
		 	name varchar(50) NOT NULL,
		 	description varchar(255),
		 	price decimal(10,0),
		 	image varchar(100),
		 	created_date datetime,
		 	active bit(1),
		 	category_id int(11),
		 	PRIMARY KEY(id),
		 	FOREIGN KEY (category_id) REFERENCES category(id)
		 );
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String description;
	private BigDecimal price;
	private String image;
	@Column(name = "created_date")
	@Temporal(TemporalType.DATE)
	private Date createdDate;
	private boolean active;
	@ManyToOne(fetch = FetchType.LAZY) // fetch sẽ get dữ liệu theo sql nào? -> quan trọng khi lượng userlớn //còn có
										// cascade?
	@JoinColumn(name = "category_id")
	private Category category;
	@ManyToMany
	@JoinTable(
			name = "pro_man",
			joinColumns = {@JoinColumn(name = "product_id")},
			inverseJoinColumns = {@JoinColumn(name = "manufacturer_id")}
	)
	private Set<Manufacturer> manufacturers;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Set<Manufacturer> getManufacturers() {
		return manufacturers;
	}

	public void setManufacturers(Set<Manufacturer> manufacturers) {
		this.manufacturers = manufacturers;
	}

}
