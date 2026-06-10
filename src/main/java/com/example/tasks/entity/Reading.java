package com.example.tasks.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reading") // Creates 'users' table in RDS
public class Reading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String article_type;
    
    @Column(nullable = false)
    private Integer pages;

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArticle_type() {
		return article_type;
	}

	public void setArticle_type(String article_type) {
		this.article_type = article_type;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}

    
}
