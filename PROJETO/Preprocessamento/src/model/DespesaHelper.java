package model;

public class DespesaHelper extends Despesa{
	private double media;
	private double desvioPadrao;
	public DespesaHelper(String codigo, String nome, double valor) {
		
		super(codigo, nome, valor);
		this.media = 0;
		this.desvioPadrao =0;
		
	}
	public double getMedia() {
		return media;
	}
	public void setMedia(double media) {
		this.media = media;
	}
	public double getDesvioPadrao() {
		return desvioPadrao;
	}
	public void setDesvioPadrao(double desvioPadrao) {
		this.desvioPadrao = desvioPadrao;
	}
	
	
	

}
