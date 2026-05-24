package com.nlpAnnotation.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATASET")
public class Dataset {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "nom_dataset")
	private String nomDataset;
	private String description;
	public Dataset() {}
	public Dataset(String nomDataset, String description) {
		this.nomDataset = nomDataset;
		this.description = description;
	}
	public Integer getId() {
		return this.id;
	}
	public String getNomDataset() {
		return this.nomDataset;
	}
	public String getDescription() {
		return this.description;
	}
	public void setNomDataset(String nomDataset) {
		this.nomDataset = nomDataset;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
