import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/*
Input pe care am testat:

booked calendar1: [['9:00','10:30'],['12:00','13:00'],['16:00','18:00']]
calendar1 range limits: ['9:00','20:00']

booked calendar2: [['10:00','11:30'],['12:30','14:30'],['14:30','15:00'],['16:00','17:00']]
calendar2 range limits: ['10:00','18:30']

Meeting Time Minutes: 30

*/

public class Calendar {
	//Preluam cele 2 calendare, alaturi de limitele de lucru sub forma de ArrayList de Interval si formam ArrayList-ul de intervale care fac match (respecta cerinta problemei)
	public static ArrayList<Interval> rezolvare(ArrayList<Interval> c1, ArrayList<Interval> c2, int meeting) {
		ArrayList<Interval> answer = new ArrayList<Interval>(); //Sirul in care vom adauga Intervalele care fac match
		
		for(Interval i1: c1) { //Parcurgem calendarul 1
			//convertim in minute limita minima si limita maxima a intervalului i1, corespunzator cu un interval din calendarul 1 
			int convRangeMin1 = convMin(i1.getRangeMin()); 
			int convRangeMax1 = convMin(i1.getRangeMax());
			
			for(Interval i2: c2) { //Parcurgem calendarul 2
				//luam 2 variabile, min si max, care ar putea fi limitele Intervalului pe care vrem sa-l aflam
				int min;
				int max;
				
				//acelasi lucru ca si pentru calendar 1
				int convRangeMin2 = convMin(i2.getRangeMin());
				int convRangeMax2 = convMin(i2.getRangeMax());
				
				//Pentru a putea vedea daca exista un interval care sa respecte conditia,
				//Trebuie sa verificam daca intervalele nu sunt decalate unele de altele
				//exemplu: intervalul [13:00-16:00] si intervalul [11:30-12:30] => ele nu au niciun minut in comun
				//Prin urmare trebuie sa verificam daca limita inferioara de la i1 e mai minca decat limita superioara de la i2
				// SI
				//limita superioara de la i1 e mai mare decat limita inferioara de la i2
				if(convRangeMin1 < convRangeMax2 && convRangeMax1 > convRangeMin2) {
					//calculam minimul potentialului Interval ca fiind maximul dintre limitele inferioare ale celor 2 intervale
					if(convRangeMin1 > convRangeMin2) { 
						min = convRangeMin1;
					}else {
						min = convRangeMin2;
					}
					
					//calculam maximul potentialului Interval ca fiind minimul dintre limitele superioare ale celor 2 intervale
					if(convRangeMax1 < convRangeMax2) {
						max = convRangeMax1;
					}else {
						max = convRangeMax2;
					}
					
					//vedem daca noul intervalul este mai mare sau egal cu timpul unei sedinte (meeting)
					if(max - min >= meeting) { //daca da, convertim din minute in hh:mm fiecare limita si formam noul Interval
						int hhMax = max/60;
						int mmMax = max - hhMax*60;
						HashMap<Integer, Integer> rangeMax = new HashMap<Integer, Integer>();
						rangeMax.put(hhMax, mmMax);
						
						int hhMin = min/60;
						int mmMin = min - hhMin*60;
						HashMap<Integer, Integer> rangeMin = new HashMap<Integer, Integer>();
						rangeMin.put(hhMin, mmMin);
						
						Interval interv = new Interval(rangeMin, rangeMax, false);
						answer.add(interv); //adaugam in lista de Intervale care fac match
						
					}
				}
				
			}
		}
		
		return answer;
	}
	
	public static int convMin(HashMap<Integer, Integer> h) { //metoda de convertire a formatului ora-minut in minute
		int conv = 0;
		for(int i: h.keySet()) { //ora e key-ul din HashMap, minutele reprezinta value-ul din HashMap
			conv = i * 60 + h.get(i);
		}
		return conv;
	}
	
	public static ArrayList<Interval> intervaleValide(ArrayList<String> list){ //formam intervalele valide
		ArrayList<Interval> intervals = new ArrayList<Interval>();
		
		//Din sirul de ore, cele de pe poz pare reprezinta limita inf a intervalului, cele de pe poz impare, reprezinta limita superioara
		for(int i = 0; i < list.size() - 1; i++) { 
			HashMap<Integer, Integer> rangeMin = splitRange(list.get(i));
			HashMap<Integer, Integer> rangeMax = splitRange(list.get(i + 1));
			Interval interval = new Interval(rangeMin, rangeMax, false); 
			intervals.add(interval);
			i += 1;
		}
		
		return intervals;
	}
	
	public static HashMap<Integer, Integer> splitRange(String s){ //despartim formatul hh:mm in ora si minut si formam range-urile
		HashMap<Integer, Integer> range = new HashMap<Integer, Integer>();
		String[] hhmm = s.split(":"); //facem split dupa :
		
		int hh = Integer.parseInt(hhmm[0]); //convertim string-ul in integer
		int mm;
		
		//in ce priveste minutul (m1m2)
		//daca m1 e 0, se ia doar m2
		//daca m1 e diferit de 0, se iau ambele
		//pentru formarea minutului de la range
		if(hhmm[1].charAt(0) == '0') {
			int mmASCII = hhmm[1].charAt(1); 
			mm = mmASCII - 48;
		}
		else
		{
			mm = Integer.parseInt(hhmm[1]);
		}
		
		range.put(hh, mm);
		return range;
	}
	
	//range limits pentru fiecare calendar se adauga la intervalele calendarului
	public static ArrayList<String> imbinareIntervale(ArrayList<String> c, ArrayList<String> cLimits) { 
		ArrayList<String> intervalImbinat = new ArrayList<String>();
		
		intervalImbinat.add(cLimits.get(0)); //limita inferioara se adauga prima
		intervalImbinat.addAll(c); //intre cele 2 se adauga calendarul propriu-zis
		intervalImbinat.add(cLimits.get(1)); //limita superioara se adauga ultima
		
		return intervalImbinat;
		
		
	}
	
	public static ArrayList<String> prelucrareCalendar(String calendar) { //aici prelucram datele de intrare si formam string-uri de ore
		String[] primulSplit = calendar.split("[,]"); //primul split se face dupa virgula
		
		ArrayList<String> hString = new ArrayList<String>();
		
		for(String s: primulSplit) { //aici eliminam caracterele diferite de digit-uri si :
			if(s.contains("[['")) {
				String stringAux = "";
				for(int i = 3; i < s.length() - 1; i++) {
					stringAux += String.valueOf(s.charAt(i));
				}
				hString.add(stringAux);
			}else {
				if(s.contains("['")) {
					String stringAux = "";
					for(int i = 2; i < s.length() - 1; i++) {
						stringAux += String.valueOf(s.charAt(i));
					}
					hString.add(stringAux);
				}
			}
			
			if(s.contains("']]")) {
				String stringAux = "";
				for(int i = 1; i < s.length() - 3; i++) {
					stringAux += String.valueOf(s.charAt(i));
				}
				hString.add(stringAux);
			}else {
				if(s.contains("']")) {
					String stringAux = "";
					for(int i = 1; i < s.length() - 2; i++) {
						stringAux += String.valueOf(s.charAt(i));
					}
					hString.add(stringAux);
				}
			}
		}
		
		return hString;
		
	}
	
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in); //citim de la tastatura
		
		//introducere si prelucrare date de intrare
		System.out.println("booked calendar1: ");
		String calendar1 = sc.next();
		
		ArrayList<String> calendar1Prelucrat = prelucrareCalendar(calendar1);

		System.out.println("calendar1 range limits: ");
		String calendar1Limits = sc.next();
		
		ArrayList<String> calendar1LimitsPrelucrat = prelucrareCalendar(calendar1Limits);
		ArrayList<String> calendar1Unit = imbinareIntervale(calendar1Prelucrat, calendar1LimitsPrelucrat);
		ArrayList<Interval> c1 = intervaleValide(calendar1Unit);
		
		System.out.println("booked calendar2: ");
		String calendar2 = sc.next();
		
		ArrayList<String> calendar2Prelucrat = prelucrareCalendar(calendar2);
		
		System.out.println("calendar2 range limits: ");
		String calendar2Limits = sc.next();

		ArrayList<String> calendar2LimitsPrelucrat = prelucrareCalendar(calendar2Limits);
		ArrayList<String> calendar2Unit = imbinareIntervale(calendar2Prelucrat, calendar2LimitsPrelucrat);
		ArrayList<Interval> c2 = intervaleValide(calendar2Unit);
		
		System.out.println("Meeting Time Minutes: ");
		int meeting = sc.nextInt();
		
		//formarea rezultatului final
		ArrayList<Interval> rez = rezolvare(c1, c2, meeting);
		
		System.out.println("[");
		for(Interval ii: rez) {
			System.out.print(ii.toString());
			System.out.println(",");
		}
		System.out.print("]");
		
		
	}
}
