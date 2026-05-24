package com.nlpAnnotation.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CLASSE_POSSIBLE")
public class ClassePossible {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "texte_classe")
	private String texteClasse;
	@ManyToOne
	@JoinColumn(name = "dataset_id")
	private Dataset dataset;
	public ClassePossible() {}
	public ClassePossible(String texteClasse, Dataset dataset) {
		this.texteClasse = texteClasse;
		this.dataset = dataset;
	}
	public Integer getId() {
		return this.id;
	}
	public String getTexteClasse() {
		return this.texteClasse;
	}
	public Dataset getDataset() {
		return this.dataset;
	}
	public void setTexteClasse(String texte) {
		this.texteClasse = texte;
	}
	public void setDataset(Dataset data) {
		this.dataset = data;
	}
}
