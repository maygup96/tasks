package com.example.tasks.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "data")
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private String tableName;		// int or Integer
    
    @Column(nullable = false)
    private String status;
    
    public Long getId() {
		return id;
	}

	public int getCount() {
		return count;
	}

	public String getTabName() {
		return tableName;
	}

	public String getStatus() {
		return status;
	}

	public StatusData getParent_status() {
		return parent_status;
	}


	public void setCount(int count) {
		this.count = count;
	}

	public void setTabName(String name) {
		this.tableName = name;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setParent_status(StatusData parent_status) {
		this.parent_status = parent_status;
	}

	@ManyToOne
    @JoinColumn(name="parent_status_id", nullable = false)
    private StatusData parent_status;

    // Getters and Setters
}
