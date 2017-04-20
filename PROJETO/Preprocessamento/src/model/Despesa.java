package model;

import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

public class Despesa implements Comparable<Despesa> {
	private String codigo;
	private String nome;
	private double valor;	
	private double valorSuavizado;
	private double valorNormalizado;
	
	
	public Despesa(String codigo, String nome, double valor) {		
		this.codigo = codigo;
		this.nome = nome;
		this.valor = valor;		
		this.valorSuavizado = 0;
		this.valorNormalizado = 0;
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
	
	public double getValor() {
		return valor;
	}
	
	public void setValor(double float1) {
		this.valor = float1;
	}
	

	public double getValorSuavizado() {
		return valorSuavizado;
	}

	public void setValorSuavizado(double valorSuavizado) {
		this.valorSuavizado = valorSuavizado;
	}

	public double getValorNormalizado() {
		return valorNormalizado;
	}

	public void setValorNormalizado(double valorNormalizado) {
		this.valorNormalizado = valorNormalizado;
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
