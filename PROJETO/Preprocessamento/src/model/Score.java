package model;

public class Score {
	private String algoritmo;
	private String detalhe;
	private double valor;
	private double limiarAnomalia;
	private boolean anomalia;	
	
	public Score(String algoritmo, double valor, double limiarAnomalia) {		
		this.algoritmo = algoritmo;
		this.valor = valor;
		this.detalhe="";
		this.limiarAnomalia = limiarAnomalia;
		if(valor > limiarAnomalia)
			anomalia = true;
		else
			anomalia = false;
	}
	public Score(String algoritmo, double valor){
		this.algoritmo = algoritmo;
		this.valor = valor;
		this.detalhe="";
		
	}
	public Score(String algoritmo, double valor, String detalhe){
		this.algoritmo = algoritmo;
		this.valor = valor;
		this.detalhe = detalhe;
	}
	public String getAlgoritmo() {
		return algoritmo;
	}
	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public double getLimiarAnomalia() {
		return limiarAnomalia;
	}
	public void setLimiarAnomalia(double limiarAnomalia) {
		this.limiarAnomalia = limiarAnomalia;
	}
	public boolean isAnomalia() {
		return anomalia;
	}
	public void setAnomalia(boolean anomalia) {
		this.anomalia = anomalia;
	}
	@Override
	public String toString() {
		
		return this.getAlgoritmo()+"_"+this.getDetalhe()+";"+this.getValor();
	}
	public String getDetalhe() {
		return detalhe;
	}
	public void setDetalhe(String detalhe) {
		this.detalhe = detalhe;
	}
	
	
}
