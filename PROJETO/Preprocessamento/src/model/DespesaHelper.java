package model;

public class DespesaHelper extends Despesa{
	private double media;
	private double desvioPadrao;
	private double mediaRelativo;
	private double desvioPadraoRelativo;
	
	public DespesaHelper(String codigo, String nome, double valor) {
		
		super(codigo, nome, valor);
		this.media = 0;
		this.desvioPadrao =0;
		this.mediaRelativo = 0;
		this.desvioPadraoRelativo = 0;
		
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
	public double getMediaRelativo() {
		return mediaRelativo;
	}
	public void setMediaRelativo(double mediaRelativo) {
		this.mediaRelativo = mediaRelativo;
	}
	public double getDesvioPadraoRelativo() {
		return desvioPadraoRelativo;
	}
	public void setDesvioPadraoRelativo(double desvioPadraoRelativo) {
		this.desvioPadraoRelativo = desvioPadraoRelativo;
	}
	
	
	
	

}
