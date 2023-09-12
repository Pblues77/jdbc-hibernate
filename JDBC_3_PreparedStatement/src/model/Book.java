package model;

public class Book {
	private String id;
	private String name;
	private double price;
	private int publishingYear;

	public Book() {
		super();
	}

	public Book(String id, String name, double price, int publishingYear) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.publishingYear = publishingYear;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getPublishingYear() {
		return publishingYear;
	}

	public void setPublishingYear(int publishingYear) {
		this.publishingYear = publishingYear;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", price=" + price + ", publishingYear=" + publishingYear + "]";
	}
	
	
}
