package model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class Multienio {

	private String nome;
	private List<Ano> anos;
	private List<Score> scores;
	private boolean ajusted;
	
	
	public Multienio() {
		this.nome = "";
		this.anos = new ArrayList<Ano>();
		this.scores = new ArrayList<Score>();
		this.ajusted = false;
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
	public void addANo(Ano ano){
		anos.add(ano);	
				
		anos.sort(new Comparator<Ano>() {

			@Override
			public int compare(Ano o1, Ano o2) {
				// TODO Auto-generated method stub
				return o1.getValor() < o2.getValor() ? -1 : 1 ;
			}
		});
		updateName();
		
	}
	public List<Score> getScores() {
		return scores;
	}
	public void setScores(List<Score> scores) {
		this.scores = scores;
	}
	
	public void updateName(){
		this.nome = "";
		for(int i = 0; i< anos.size() ; i++)
			nome+= i > 0 ? "_"+anos.get(i).getValor() :anos.get(i).getValor(); 
		
	}
	
	//Remove prefeituras que nao se repetem a todos anos
	public void ajustaPrefeituras(){
		//REMOVER PREFEITURAS QUE NAO SE REPETEM TODOS ANOS
				TreeSet<Prefeitura> removeList = new TreeSet<>();
				for(Ano a : anos){
					 for(Prefeitura p : a.getPrefeituras()){				
						 for(Ano a2 : anos){
							 if(!a2.equals(a)){
								 if(a2.getPrefeituras().indexOf(p)< 0 ){
									 removeList.add(p);
									 break;
								 }
								 
							 }
						 }	
						 
					 }
					
				}
				for(Prefeitura p : removeList){
					for(Ano a : anos){
						a.getPrefeituras().remove(p);				
					}
					
				}
				this.ajusted =true;
		
	}
	//IMPRIME ESTE MULTIENIO
	public void print(boolean minCategory, boolean header, DecimalFormat df, boolean absoluteValues, boolean printScores){
		if(!ajusted)
			ajustaPrefeituras();
		
		System.out.println("#Total instancias: "+anos.get(0).getPrefeituras().size());
		String output ="";
		if(header){
			output ="#";
			output +="Codigo";
			
			for(Ano a : anos){
				output +=";População "+a.getValor();
				for(Despesa d : a.getPrefeituras().get(0).getDespesas())
					if(!d.getCodigo().equals("00"))
						if((minCategory && !d.getCodigo().contains(".")) || (!minCategory && d.getCodigo().contains("."))||(d.getCodigo().equals("29") ) )
							output +=";"+d.getNome()+"("+a.getValor()+")";				
				
			}
			
			System.out.println(output);
			
		}	
		
		for(Prefeitura p : anos.get(0).getPrefeituras()){
			output ="";
			output +=p.getCodigo();
			
			for(Ano a : anos){
				int indexPrefeitura = a.getPrefeituras().indexOf(p);
				if(indexPrefeitura >= 0){
					Prefeitura instancia = a.getPrefeituras().get(indexPrefeitura);
					output += absoluteValues ? ";"+instancia.getPopulacao() :";"+instancia.getPopulacaoNormalizada() ;
					for(Despesa d : instancia.getDespesas())
						if(!d.getCodigo().equals("00"))
							if((minCategory && !d.getCodigo().contains(".")) || (!minCategory && d.getCodigo().contains("."))||(d.getCodigo().equals("29") ) )
								output += (absoluteValues ?  ";"+df.format( d.getValor()) : ";" + df.format( d.getValorSuavizadoNormalizado()));				
				}	else{
					System.out.println("#INDEX_NOT_FOUND prefeitura codigo:"+p.getCodigo()+" para ano: "+a.getValor());
					
				}
			}
			System.out.println(output);
		}
		
		
	}
	
	
}
