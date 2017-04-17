package model;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

public class Despesa implements Comparable<Despesa> {
	private String codigo;
	private String nome;
	private Double valor;	
	
	
	public Despesa(String codigo, String nome, Double valor) {		
		this.codigo = codigo;
		this.nome = nome;
		this.valor = valor;
	}

	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Double getValor() {
		return valor;
	}
	
	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Override
	public int compareTo(Despesa arg0) {
		
		return this.codigo.compareTo(arg0.getCodigo());
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.getCodigo()+"-"+this.getNome()+" : "+this.getValor();
	}
	
	@Override
	public boolean equals(Object o) {
		
		if(!(o instanceof Despesa)) return false;
		
	    Despesa other = (Despesa) o;
	    
	    return (this.codigo.trim().compareTo( other.codigo.trim()) == 0 );
	}
	
	
	
}
