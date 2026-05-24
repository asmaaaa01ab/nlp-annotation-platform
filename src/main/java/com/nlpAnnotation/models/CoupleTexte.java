package com.nlpAnnotation.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "COUPLE_TEXTE")
public class CoupleTexte {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String texte1;
	private String texte2;
	@ManyToOne
	@JoinColumn(name = "dataset_id")
	private Dataset dataset;
	public CoupleTexte() {}
	public CoupleTexte(String texte1, String texte2, Dataset dataset) {
		this.texte1 = texte1;
		this.texte2 = texte2;
		this.dataset = dataset;
	}
	public Integer getId() {
		return this.id;
	}
	public String getTexte1() {
		return this.texte1;
	}
	public String getTexte2() {
		return this.texte2;
	}
	public Dataset getDataset() {
		return this.dataset;
	}
	public void setTexte1(String texte) {
		this.texte1 = texte;
	}
	public void setTexte2(String texte) {
		this.texte2 = texte;
	}
	public void setDataset(Dataset data) {
		this.dataset = data;
	}
}
