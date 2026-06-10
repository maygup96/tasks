package com.example.tasks.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "laundary") // Creates 'users' table in RDS
public class Laundary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String garment_type;

    @Column(nullable = false)
    private String color;
    
    @Column(nullable = false)
    private String fabric;

	public Long getId() {
		return id;
	}

	public String getGarment_type() {
		return garment_type;
	}

	public void setGarment_type(String garment_type) {
		this.garment_type = garment_type;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFabric() {
		return fabric;
	}

	public void setFabric(String fabric) {
		this.fabric = fabric;
	}

    
}
