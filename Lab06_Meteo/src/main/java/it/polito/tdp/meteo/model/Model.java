package it.polito.tdp.meteo.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	
	private MeteoDAO dao;
	private List<Rilevamento> lista;
	private List<Rilevamento> quarantacinqueGiorni;
	private List<Rilevamento> soluzione;
	
	private int bestCost = 0;
	

	public Model() {
		this.dao = new MeteoDAO();
		this.lista = dao.getAllRilevamenti();
		//this.quarantacinqueGiorni = dao.getAllRilevamentiLocalitaMese(mese, NUMERO_GIORNI_TOTALI);
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese, String citta) {
		//calcolo l'umidità media del mese passato in input
		//la media = somma dell'umidità/n gg
		double media = 0.0;
		double somma = 0;
		double giorni = 0;
		
		for(Rilevamento r: lista) {
			int rmese = Integer.parseInt(r.getData().toString().substring(5, 7));
			if( (rmese == mese) && (r.getLocalita().equals(citta)) ) {
				somma += r.getUmidita();
				giorni++;
			}
		}
		
		media = (somma/giorni);
		NumberFormat nf = new DecimalFormat("0.00");
		
		return nf.format(media);
	}
	
	// of course you can change the String output with what you think works best
	public String trovaSequenza(int mese) {
	//public List<Rilevamento>trovaSequenza(int mese) {
		//richiamo i metodi che mi permettono di avere la ricerca
		//condizione iniziale
		String risultato = "";
		//inizializza 45gg che richiama il metodo
		quarantacinqueGiorni = new ArrayList<Rilevamento>(dao.getAllRilevamentiLocalitaMese(mese, NUMERO_GIORNI_TOTALI));
		
		List <Rilevamento> parziale = new ArrayList<Rilevamento>();
		int livello = 1;
		soluzione = new ArrayList<Rilevamento>();
				
		cerca(parziale, livello);
		
		for(Rilevamento r: soluzione) {
			risultato += r.toStringBello()+"\n";
		}
				
		return risultato;
		//return soluzione;
	}
	
	public List<String> getElencoMesi() {
		List<String> mesi = new ArrayList<String>();
		
    	for(Integer i=1; i<13;i++) {
    		mesi.add(""+i);
    	}
		
		return mesi;
	}
	
	public void cerca(List<Rilevamento> parziale, int livello) {
		//condizioni
		if( maxGiorni(parziale) == true) {
			return;
		}
		if( minGiorni(parziale) ) {
			return;
		}
		//nella soluzione finale io voglio il costo migliore
		//caso terminale: ho trascorso 15gg
		if( livello == NUMERO_GIORNI_TOTALI+1) {
			this.soluzione = new ArrayList<Rilevamento>(parziale);
			//aggiorno il costo migliore
			int costo = calcoloCosto(parziale);
			
			if(soluzione == null || costo<calcoloCosto(soluzione)) {
				soluzione  = new ArrayList<>(parziale);
			}
			return;
		}

		//caso intermedio: vado solo se i giorni sono ok 
		for(Rilevamento r: cittaDelGiorno(livello)) {
			//aggiungo la città alla soluzione parziale
			parziale.add(r);
			//passo al giorno successivo
			cerca(parziale, livello+1);
			//backtracking
			parziale.remove(parziale.size()-1);
		}
		
	}
	
	//un giorno
	public List<Rilevamento> cittaDelGiorno(int giorno){
		
		List<Rilevamento> cittagiorno = new ArrayList<Rilevamento>();
	
		for(Rilevamento r: quarantacinqueGiorni) {
			if( giorno == Integer.parseInt((r.getData().toString().substring(8))) ) {
				cittagiorno.add(r);
			}
		}
		
		return cittagiorno;
	}
	
	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, int finoa) {
		return dao.getAllRilevamentiLocalitaMese(mese, finoa);
	}
	
	public List<String> localita(){
		List<String> citta = new ArrayList<>();
		 for(Rilevamento r: lista) {
			 if(!citta.contains(r.getLocalita())) {
				 citta.add(r.getLocalita()); 
			 }
		 }
		 return citta;
	}
	
	public boolean maxGiorni(List<Rilevamento>parziale) {
		boolean troppi = false;
		
		for(String s: localita()) {
			int conta = 0;
			for(Rilevamento r : parziale) {
				if(r.getLocalita().equals(s)) {
					conta++;
				}
			}
			if(conta> NUMERO_GIORNI_CITTA_MAX) {
				troppi = true;
				return troppi;
			}
		}
		
		return troppi;
	}
	
	public boolean minGiorni(List<Rilevamento>parziale) {
		boolean pochi = false;
		//prima citta
		String cittaAttuale = "";
		int succ = 0;
		
		for(Rilevamento r: parziale) {
			//primo rilevamento?
			if(succ == 0) {
				cittaAttuale =parziale.get(0).getLocalita();
			}

			String var = r.getLocalita();
			
			if(var.equals(cittaAttuale)) {
				succ++;
			}
			else {
				if(succ< NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN && succ != 0) {
					pochi = true;
					return pochi;
				}
				else {
					cittaAttuale= r.getLocalita();
					succ = 1;
				}
			}
			
		}
		
		return pochi;
	}

	public int calcoloCosto(List<Rilevamento> parziale) {
		//dato un parziale calcolo il costo di quel parziale
		int costo =0;
		
		//somma tutte l'umidita
		for(Rilevamento r: parziale) {
			costo += r.getUmidita();
		}
		//conta quante volte cambio citta
		for(int i=1; i<parziale.size(); i++) {
			if( !parziale.get(i).getLocalita().equals(parziale.get(i-1).getLocalita())) {
				costo+= COST;
			}
		}
		
		return costo;
	}
}
