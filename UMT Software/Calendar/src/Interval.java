import java.util.HashMap;

public class Interval {
	//primul Integer (key) reprezinta ora, al doilea Integer (Value) reprezinta minutele
	//fiecare HashMap contine doar un singur element
	private HashMap<Integer, Integer> rangeMin; //limita inferioara a intervalului
	private HashMap<Integer, Integer> rangeMax; //limita superioara a intervalului
	private boolean esteOcupat; // nu este utilizat -- provine din cadrul altei idei
	
	public Interval(HashMap<Integer, Integer> rangeMin, HashMap<Integer, Integer> rangeMax, boolean esteOcupat) {
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
		this.esteOcupat = esteOcupat;
	}
	
	public HashMap<Integer, Integer> getRangeMin(){
		return this.rangeMin;
	}
	
	public void setRangeMin(HashMap<Integer, Integer> rangeMin){
		this.rangeMin = rangeMin;
	}
	
	public HashMap<Integer, Integer> getRangeMax(){
		return this.rangeMax;
	}
	
	public void setRangeMax(HashMap<Integer, Integer> rangeMax){
		this.rangeMax = rangeMax;
	}
	
	@Override
	public String toString() { //metoda de toString creata pentru afisarea unui rezultat
		//formatul intervalului ['hh:mm','hh:mm']
		String rezultat = "['";
		
		for(Integer i1 : rangeMin.keySet()) {
			//preluam key-ul care e hh (i1) si value-ul corespunzator care e mm (returnat de get(i1))
			rezultat += i1 + ":" + rangeMin.get(i1) + "','"; 
		}
		
		for(Integer i1 : rangeMax.keySet()) {
			rezultat += i1 + ":" + rangeMax.get(i1) + "']";
		}
		
		return rezultat;
	}
	
}
