package graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import gui.GraphEdge;
import gui.GraphEditer;
import gui.GraphNode;
import gui.NodeList;

/**
 * 
 * Abstract osztály a gráf összeköttetéseinek tárolására, amit egy illeszkedési mátrixban valósít meg.
 * Az illeszkedési mátrixot használja a különböző algoritmusok futtatásához, mint például szélességi,
 * mélységi és dijkstra algoritmus.
 * 
 * @author Kurcsi Norbert
 *
 */
public abstract class GraphLinks implements Serializable{

	private static final long serialVersionUID = -6481579515567248499L;
	// illeszkedési mátrix
	protected int[][] matrix;
	
	// az editer ahol a gráf szerkeszthető
	protected GraphEditer editer;
	
	
	/**
	 * Visszaadja a gráf illeszkedési mátrixját egy kétdimenziós egész értékeket tároló tömbben
	 * 
	 * @return a gráf illeszkedési mátrixja
	 */
	public int[][] getMatrix(){
		return matrix;
	}
	
	/**
	 * Konstruktor az összeköttetéseket tároló és kezelő osztály létrehozásához
	 * 
	 * @param editer az editer, ahol az adott gráfot lehet szerkeszteni
	 */
	public GraphLinks(GraphEditer editer) {
		this.editer = editer;
		matrix = new int[0][0];
	}
	
	/**
	 * Egy új csomópont hozzáadását az illeszkedési mátrixhoz, megvalósító függvény.
	 * 
	 * @param nodeNumber a csomópontok száma az új illeszkedési mátrixban
	 */
	public void addNode(int nodeNumber) {
		// új illeszkedési mátrix létrehozása
		int[][] newMatrix = new int[nodeNumber][nodeNumber];
		
		// a régi illeszkedési mátrix átmásolása az új mátrixba
		for(int i = 0; i < nodeNumber-1; ++i) {
			for(int j = 0; j < nodeNumber-1 ; ++j) {
				newMatrix[i][j] = matrix[i][j];
			}
		}
		
		// az új sor és oszlop feltöltése nullákkal, mivel az új csomópontnak. még nem lehetnek élei
		for(int i = 0; i < nodeNumber; ++i) {
			newMatrix[nodeNumber-1][i] = 0;
			newMatrix[i][nodeNumber-1] = 0;
		}
		
		// átállítjuk a referenciát az új mátrixra
		matrix = newMatrix;
	}
	
	/**
	 * Egy csomópont törlése az illeszkedési mátrixból. Mikor egy csomópont törlődik, akkor az összes hozzátartozó
	 * él is törlődik vele együtt.
	 * 
	 * @param node a törölni kívánt csomópont indexje
	 */
	public void deleteNode(int node) {
		// az új mátrix létrehozása
		int newSize = matrix.length - 1;
		int[][] newMatrix = new int[newSize][newSize];
		
		int actualRow = 0; int actualColumn = 0;
		for(int i = 0; i < matrix.length ; ++i) {
			if(i != node) { 	// ha a sor nem a törölni kívánt mátrixhoz tartozik, akkor átmásoljuk
				for(int j = 0; j < matrix.length; ++j) {
					if(j != node) {
						newMatrix[actualRow][actualColumn++] = matrix[i][j];
					}
				}
				actualRow++;
				actualColumn = 0;
			}
		}
		
		// referencia átadása
		matrix = newMatrix;
	}
	
	/**
	 * Az illesszkedési mátrix kiírása a konzolba.
	 */
	public void printMatrix() {
		for(int i = 0; i<matrix.length;++i) {
			for(int j = 0; j < matrix[i].length;++j) {
				System.out.print(matrix[i][j]+ " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * Él hozzáadása. Abstarct metódus, az irányított és iranyítatlan élek meg kell valósítsák.
	 * @param from kezdőpont
	 * @param to végpont
	 */
	public abstract void addEdge(int from, int to);
	
	/**
	 * Él hosszának a megváltoztatása. Abstarct metódus, az irányított és iranyítatlan élek meg kell valósítsák.
	 * @param starter kezdőpont
	 * @param end végpont
	 * @param weight az él új súlya
	 */
	public abstract void changeEdgeWeight(int starter, int end, int weight);
	
	/**
	 * Illeszkedési mátrix átalakítása pont-él mátrixba
	 * @return kétdimenziós, egész értékeket tároló tömb, ami a gráf pont-él mátrixját ábrázolja
	 */
	public int[][] converToPointEdgeMatrix(){
		List<GraphEdge> edges= editer.getEdges();
		
		// a pont-él mátrixba a sorok száma egyenlő a az élek számával,
		// az oszlopok száma egyenlő a csomópontok számával
		int[][] newMatrix = new int[edges.size()][matrix.length];
		
		// az tömb feltöltése nullákkal
		for(int i=0; i < edges.size(); ++i)
	        for(int j=0; j < matrix.length; ++j)
	            newMatrix[i][j] = 0;
		
		// értékek behelyezése a mátrixba
		for(int i = 0; i < edges.size() ; ++i) {
			GraphNode startingNode = edges.get(i).getStartingNode();
			GraphNode finalNode = edges.get(i).getFinalNode();
			newMatrix[i][editer.getNodeIndex(startingNode)] = edges.get(i).getWeight();
			newMatrix[i][editer.getNodeIndex(finalNode)] = edges.get(i).getWeight();
		}
		
		return newMatrix;
	}
	
	/**
	 * Az illeszkedsési mátrixot átalakítja éllistába. Az éllista egy olyan kétdimenziós tömb,
	 * amely az élek kezdő és végpontját tárolja
	 * @return éllista
	 */
	public int[][] converToEdgeList(){
		List<GraphEdge> edges= editer.getEdges();
		
		// az éllistát tároló kétdimenziós tömb létrehozása
		int[][] newMatrix = new int[edges.size()][2];
		
		// csomópontok elhelyezése az éllistába
		for(int i = 0; i < edges.size() ; ++i) {
			GraphNode startingNode = edges.get(i).getStartingNode();
			GraphNode finalNode = edges.get(i).getFinalNode();
			newMatrix[i][0] = editer.getNodeIndex(startingNode);
			newMatrix[i][1] = editer.getNodeIndex(finalNode);
		}
		
		return newMatrix;
	}
	
	/**
	 * Az gráf összes élének a súlyának az összegét számolja ki.
	 * 
	 * @return élek súlyának az összege
	 */
	private int sumOfEdges() {
		int sum = 0;
		for(GraphEdge e : editer.getEdges()) {
			sum += e.getWeight();
		}
		return sum;
	}

	/**
	 * Dijkstra algoritmus futtatása a gráfon, az illeszkedési mátrix által nyújtott ábrázolást felhasználva.
	 * A függvény egy úgynevezett úttömböt hoz létre, amiből kiolvasható a gráf összes csomópontjához
	 * tartozó legrövidebb út, a megadott kezdőpontból.
	 * 
	 * @param starter kezdőpont, ahonnan az algoritmust indítjuk
	 * @return egészeket tároló egydimenziós tömb, amely az algoritmus futása során létrejött úttömböt tárolja
	 */
	public int[] dijkstra(GraphNode starter) {
		//Deklarációk, inicializációk
		boolean[] visited = new boolean[matrix.length];
		int[] routeLength = new int[matrix.length];
		int[] route = new int[matrix.length];
		int maxi = sumOfEdges();
		int mini, actual = editer.getNodeIndex(starter);
		
		for(int i=0;i<matrix.length;++i) {
			visited[i] = false;
			routeLength[i] = 0;
			route[i] = -1;
		}
 		
		//Algoritmus
		visited[editer.getNodeIndex(starter)] = true;
		for(int i = 0;i<matrix.length;++i) {
			if(matrix[editer.getNodeIndex(starter)][i]>0) {
				routeLength[i]=matrix[editer.getNodeIndex(starter)][i];
				route[i]=editer.getNodeIndex(starter);
			} else {
				routeLength[i] = maxi;
			}
		}
		
		routeLength[editer.getNodeIndex(starter)] = 0;
		
		while(true) {
			mini = maxi;
			for(int i = 0 ; i < matrix.length ; ++i) {
				if(routeLength[i] < mini && visited[i] == false) {
					mini = routeLength[i];
					actual = i;
				}
			}
			
			if(mini == maxi) {
				break;
			} else {
				visited[actual] = true;
				for(int i = 0 ; i < matrix.length; ++i) {
					if(matrix[actual][i] > 0) {
						int newLength = mini + matrix[actual][i];
						if(newLength < routeLength[i]) {
							routeLength[i] = newLength;
							route[i] = actual;
						}
					}
				}
			}
		}
		
		return route;
	}
	
	/**
	 * Metódus, amely ellenőrzi, hogy egy megadott él már létezik-e a gráfban vagy sem
	 * 
	 * @param edge az ellenőrizni kívány él
	 * @return igaz, ha az él már létezik
	 */
	public boolean edgeExists(GraphEdge edge) {
		GraphNode a = edge.getStartingNode();
		GraphNode b = edge.getFinalNode();
		
		int aIndex = editer.getNodeIndex(a);
		int bIndex = editer.getNodeIndex(b);
		
		if(matrix[aIndex][bIndex] != 0 || matrix[bIndex][aIndex] != 0) {
			return true;
		} 
		return false;
	}
	
	/**
	 * A Dijkstra algoritmus által létrehozott úttömböt, illetve egy csomópont sorszámát megadva,
	 * kiolvassa az úttömbből, az adott csomóponthoz vezető legrövidebb utat.
	 * 
	 * @param route a Dijkstra alg által létrehozott úttömb
	 * @param starter a csomópont, amelynek a legrövidebb útját szeretnénk kiolvasni
	 * @return egy lépések sorozatát tartalmazó List objektum
	 */
	public List<Step> convertDijkstraRoute(int[] route, int starter) {
		List<Step> steps = new ArrayList<Step>();
		NodeList<GraphNode> nodes = editer.getNodes();
		
		int actual = starter;
		while(route[actual] != -1) {
			Step step = new Step();
			step.setStep(nodes.get(actual), nodes.get(route[actual]));
			steps.add(step);
			actual = route[actual];
		}
		
		return steps;
	}

	/**
	 * Mélységi bejárást megvalósító metódus, amely egy lépések sorozatát tartalmazó List objektumot hoz létre,
	 * amelyből ki lehet olvasni azt, hogy a bejárás milyen sorrendben érte el a csomópontokat.
	 * @param starter kezdőpont, ahonnan a bejárást indítjuk
	 * @return lépések sorozatát tartalmazó List objektum, amelybpl ki lehet olvasni, hogy milyen sorrendben jártuk be a pontokat
	 */
	public List<Step> dfs(GraphNode starter){
		// Deklarációk, inicializációk
		List<Step> steps = new ArrayList<Step>();
		List<Integer> route = new ArrayList<Integer>();
		Stack<Integer> stack = new Stack<Integer>();
		NodeList<GraphNode> nodes = editer.getNodes();
		boolean[] visited = new boolean[matrix.length];
		int first = 0;
		boolean ok = true;
		
		// visited vektor feltöltése hamis értékekkel
		for(int i=0;i<matrix.length;++i) {
			visited[i] = false;
		}
		
		// a stackhez és az úthoz hozzáadjuk a kezdőpontot
		stack.push(editer.getNodeIndex(starter));
		route.add(editer.getNodeIndex(starter));
		// kezdőpont igazra állítása a visited vektorban
		visited[editer.getNodeIndex(starter)] = true;
		
		while(first >= 0) {
			for(int i = 0 ; i < matrix.length && ok; ++i) {		
				if(matrix[stack.get(first)][i] != 0 && visited[i] == false) {
					ok = false;
					route.add(i);
					visited[i] = true;		// a vizsgált csomópont első olyan szomszédjára lépünk, ahol még nem jártunk
					
					// szomszéd hozzáadása a lépésekhez
					Step actual = new Step();
					actual.setStep(nodes.get(stack.get(first)),nodes.get(i));
					steps.add(actual);
				}
			}
			
			// ha nem kaptunk új csomópontot visszalépünk a stacken
			if(ok) {
				first--;
				stack.pop();
			} else { // ha igen, akkor hozzáadjuk a csp a stackhez
				first++;
				stack.push(route.get(route.size()-1));
			}
			ok = true;
		}
		return steps;
	}
	
	/**
	 * Szélességi bejárást megvalósító metódus, amely egy lépések sorozatát tartalmazó List objektumot hoz létre,
	 * amelyből ki lehet olvasni azt, hogy a bejárás milyen sorrendben érte el a csomópontokat.
	 * @param starter kezdőpont, ahonnan a bejárást indítjuk
	 * @return lépések sorozatát tartalmazó List objektum, amelybpl ki lehet olvasni, hogy milyen sorrendben jártuk be a pontokat
	 */
	public List<Step> bfs(GraphNode starter){
		// Deklarációk, inicializációk
		List<Step> steps = new ArrayList<Step>();
		List<Integer> route = new ArrayList<Integer>();
		boolean[] visited = new boolean[matrix.length];
		NodeList<GraphNode> nodes = editer.getNodes();
		int first = 0;
		
		// visited vektor feltöltése hamis értékekkel
		for(int i = 0 ; i < matrix.length ; ++i) {
			visited[i] = false;
		}
		
		// az úthoz hozzáadjuk először a kezdőpontot
		route.add(editer.getNodeIndex(starter));
		// a kezdőpont visited vektorát igazra állítjuk
		visited[route.get(first)] = true;
		
		while(first <= route.size()-1) {
			for(int  i = 0 ; i < matrix.length ; ++i) {		// végigmegyünk az aktuális csomópont összes szomszédján
				if(matrix[route.get(first)][i] != 0 && visited[i] == false) {	// ha kapunk olyant, ahol még nem jártunk
					route.add(i); 	// hozzáadjuk az út tömbhöz
					visited[i] = true;
					
					// hozzáadjuk a lépéseket tartalmazó List-hez
					Step actual = new Step();
					actual.setStep(nodes.get(route.get(first)),nodes.get(i));
					steps.add(actual);
				}
			}
			first++;
		}
		return steps;
	}
}
