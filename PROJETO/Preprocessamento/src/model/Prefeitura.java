package model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Prefeitura implements Comparable<Prefeitura>  {
	private int codigo;
	private String nome;
	private String uf;
	private int populacao;	
	private int populacaoSuavizada;
	private int populacaoNormalizada;	
	private List<Despesa> despesas;
	private List<Score> scores;	
	
	
	public Prefeitura(int codigo, String nome, String uf, int populacao) {		
		this.codigo = codigo;
		this.nome = nome;
		this.uf = uf;
		this.populacao = populacao;		
		despesas = new ArrayList<Despesa>();
		scores = new ArrayList<Score>();	
		this.populacaoNormalizada = 0;
		this.populacaoSuavizada = 0;
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
	
	
	public int getPopulacaoSuavizada() {
		return populacaoSuavizada;
	}
	public void setPopulacaoSuavizada(int populacaoSuavizada) {
		this.populacaoSuavizada = populacaoSuavizada;
	}
	public int getPopulacaoNormalizada() {
		return populacaoNormalizada;
	}
	public void setPopulacaoNormalizada(int populacaoNormalizada) {
		this.populacaoNormalizada = populacaoNormalizada;
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
