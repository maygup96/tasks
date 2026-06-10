package com.example.tasks.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "gardening")
public class Gardening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String plant_type;

    @Column(nullable = false)
    private String activity_type;
    
    @ManyToOne
    @JoinColumn(name="references_id", nullable = false)  // understand references
    private Reading references;

	public Long getId() {
		return id;
	}

	public String getPlant_type() {
		return plant_type;
	}

	public void setPlant_type(String plant_type) {
		this.plant_type = plant_type;
	}

	public String getActivity_type() {
		return activity_type;
	}

	public void setActivity_type(String activity_type) {
		this.activity_type = activity_type;
	}

	public Reading getReferences() {
		return references;
	}

	public void setReferences(Reading references) {
		this.references = references;
	}

    // Getters and Setters
}
