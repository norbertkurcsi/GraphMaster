package graph;

import gui.GraphEditer;

/**
 * 
 * A GraphLinks abstract osztály leszármazottja.
 * Irányítatlan élek kezelését valósíja meg.
 * Azokat a fügvényeket valosłtja meg, amelyeknél a különbözően kell kezelni az irnyítatlan éleket,
 * az általános élektől.
 * 
 * @author Kurcsi Norbert
 * 
 */
public class IndirectedLink extends GraphLinks{

	private static final long serialVersionUID = 1L;

	/**
	 * Konstruktor, amely irányítatlan éleket kezelő IndirectedLink osztályt hoz létre.
	 * @param editer az a GraphEditer objektum, ahol az éleket kezeljük
	 */
	public IndirectedLink(GraphEditer editer) {
		super(editer);
	}
	
	/**
	 * Irányítatlan él hozzáadása a gráf mátrixjához
	 * Az él súlya mindig 1, mert ez az alapbeállítás új él hozzáadása esetén.
	 * 
	 * @param from az él kezdőpontja
	 * @param to az él végpontja
	 */
	@Override
	public void addEdge(int from, int to) {
		matrix[from][to] = 1;
		matrix[to][from] = 1;
	}
	
	/**
	 * Irányítatlan él súlyának megváltoztatása a gráf mátrixjában.
	 * 
	 * @param starter az él kezdőpontja
	 * @param end az él végpontja
	 * @param weight az él új súlya
	 */
	@Override
	public void changeEdgeWeight(int starter, int end, int weight) {
		matrix[starter][end] = weight;
		matrix[end][starter] = weight;
	}


}
