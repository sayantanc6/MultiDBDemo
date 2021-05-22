package dummy.staging.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT")
public class Product {
	    
	@Id
	@Column(name = "ID")
    private int id;

	@Column(name = "NAME")
    private String name;

	@Column(name = "PRICE")
    private double price;

    public Product() {
        super();
    }

    private Product(int id, String name, double price) {
        super();
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product from(int id, String name, double price) {
        return new Product(id, name, price);
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(final double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Product [name=")
            .append(name)
            .append(", id=")
            .append(id)
            .append("]");
        return builder.toString();
    }
}
