package model;

public class Despesa {
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
	
	
}
