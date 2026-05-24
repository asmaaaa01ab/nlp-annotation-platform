package com.nlpAnnotation.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TACHE")
public class Tache {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(name = "date_limite")
	private LocalDate dateLimite;
	private int avancement = 0;
	@ManyToOne
	@JoinColumn(name = "dataset_id")
	private Dataset dataset;
	@ManyToOne
	@JoinColumn(name = "annotateur_id")
	private Utilisateur annotateur;
	public Tache() {}
	public Tache(LocalDate date, Dataset data, Utilisateur annotateur) {
		this.dateLimite = date;
		this.dataset = data;
		this.annotateur = annotateur;
	}
	public Integer getId() {
		return this.id;
	}
	public int getAvancement() {
		return this.avancement;
	}
	public LocalDate getDateLimite() {
		return this.dateLimite;
	}
	public Dataset getDataset() {
		return this.dataset;
	}
	public Utilisateur getAnnotateur() {
		return this.annotateur;
	}
	public void setAvancement(int avancement) {
		this.avancement = avancement;
	}
	public void setDateLimite(LocalDate date) {
		this.dateLimite = date;
	}
	public void setDataset(Dataset data) {
		this.dataset = data;
	}
	public void setAnnotateur(Utilisateur annotateur) {
		this.annotateur = annotateur;
	}
}
