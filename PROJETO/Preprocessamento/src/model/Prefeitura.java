package model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Prefeitura implements Comparable<Prefeitura>  {
	private int codigo;
	private String nome;
	private String uf;
	private int populacao;	
	private List<Despesa> despesas;
	private List<Score> scores;	
	
	
	public Prefeitura(int codigo, String nome, String uf, int populacao) {		
		this.codigo = codigo;
		this.nome = nome;
		this.uf = uf;
		this.populacao = populacao;		
		despesas = new ArrayList<Despesa>();
		scores = new ArrayList<Score>();
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public int getPopulacao() {
		return populacao;
	}
	public void setPopulacao(int populacao) {
		this.populacao = populacao;
	}
	public List<Despesa> getDespesas() {
		return despesas;
	}
	public void setDespesas(List<Despesa> despesas) {
		this.despesas = despesas;
	}
	public void setDespesas(TreeSet<Despesa> treeSetDespesas) {
		despesas = new ArrayList<Despesa>(treeSetDespesas);
	}
	public List<Score> getScores() {
		return scores;
	}
	public void setScores(List<Score> scores) {
		this.scores = scores;
	}
	@Override
	public int compareTo(Prefeitura o) {
		if(this.codigo == o.getCodigo())
			return 0;
		else		
			return this.codigo > o.getCodigo() ? 1: -1;
	}
	
	@Override
	public String toString() {
		
		return this.nome+";"+this.uf+";"+this.codigo+";"+this.populacao+";"+this.despesas;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if(!(o instanceof Prefeitura)) return false;
		
		Prefeitura other = (Prefeitura) o;
	    
	    return (this.codigo == other.codigo);
	}
	
}
