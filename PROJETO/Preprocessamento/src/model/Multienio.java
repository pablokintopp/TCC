package model;

import java.util.ArrayList;
import java.util.List;

public class Multienio {

	private String nome;
	private List<Ano> anos;
	private List<Score> scores;
	
	
	
	public Multienio() {
		this.nome = "";
		this.anos = new ArrayList<Ano>();
		this.scores = new ArrayList<Score>();
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public List<Ano> getAnos() {
		return anos;
	}
	public void setAnos(List<Ano> anos) {
		this.anos = anos;
	}
	public List<Score> getScores() {
		return scores;
	}
	public void setScores(List<Score> scores) {
		this.scores = scores;
	}
	
	
	
	
}
