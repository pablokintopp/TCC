package model;

import java.util.ArrayList;
import java.util.List;

public class Ano {
	private int valor;
	private int totalPrefeituras;
	private List<Prefeitura> prefeituras;	
	
	
	
	public Ano(int valor) {		
		this.valor = valor;
		this.prefeituras = new ArrayList<Prefeitura>();
		
	}
	public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	public int getTotalPrefeituras() {
		return totalPrefeituras;
	}
	public void setTotalPrefeituras(int totalPrefeituras) {
		this.totalPrefeituras = totalPrefeituras;
	}
	public List<Prefeitura> getPrefeituras() {
		return prefeituras;
	}
	public void setPrefeituras(List<Prefeitura> prefeituras) {
		this.prefeituras = prefeituras;
		this.totalPrefeituras = this.prefeituras.size();
	}
	
	@Override
	public boolean equals(Object o) {
		
		if(!(o instanceof Ano)) return false;
		
		Ano other = (Ano) o;
	    
	    return (this.valor == other.valor);
	}

	
}
