package it.polito.tdp.meteo.model;

//import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model m = new Model();
		
		System.out.println(m.getUmiditaMedia(03, "Torino"));
		System.out.println(m.getUmiditaMedia(03, "Milano"));
		System.out.println(m.getUmiditaMedia(03, "Genova"));
		
		for(Rilevamento r: m.getAllRilevamentiLocalitaMese(03, 5)) {
			System.out.println(r.toStringBello());
		}
		//System.out.println("LE CITTA: "+m.localita(3));
		//System.out.println("STAMPO IO: "+m.trovaSequenza(03));
		
		System.out.println("Ci sono pochi giorni consecutivi? "+m.minGiorni(m.getAllRilevamentiLocalitaMese(03, 15)));
		
		System.out.println("Ci sono troppi giorni? "+m.maxGiorni(m.getAllRilevamentiLocalitaMese(03, 15)));
		
		System.out.println("Calcola costo: "+m.calcoloCosto(m.getAllRilevamentiLocalitaMese(03, 5)));
		
		System.out.println("\nStampo il V giorno: ");
		for(Rilevamento r: m.cittaDelGiorno(5)) {
			System.out.println(r.toStringBello());
		}
		
		System.out.println("\nPossibile soluzione: \n"+m.trovaSequenza(5));
		
	}

}
