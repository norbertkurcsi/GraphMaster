package graph;

import gui.GraphEditer;

/**
 * 
 * A GraphLinks abstract osztály leszármazottja.
 * Irányított élek kezelését valósíja meg.
 * Azokat a fügvényeket valosłtja meg, amelyeknél a különbözően kell kezelni az irányított éleket,
 * az általános élektől.
 * 
 * @author Kurcsi Norbert
 * 
 */
public class DirectedLink extends GraphLinks {

	private static final long serialVersionUID = 8790060705898030780L;

	/**
	 * Konstruktor, amely irányított éleket kezelő DirectedLink osztályt hoz létre.
	 * @param editer az a GraphEditer objektum, ahol az éleket kezeljük
	 */
	public DirectedLink(GraphEditer editer) {
		super(editer);
	}
	
	/**
	 * Irányított él hozzáadása a gráf mátrixjához
	 * Az él súlya mindig 1, mert ez az alapbeállítás új él hozzáadása esetén.
	 * 
	 * @param from az él kezdőpontja
	 * @param to az él végpontja
	 */
	@Override
	public void addEdge(int from, int to) {
		matrix[from][to] = 1;
	}
	
	/**
	 * Irányított él súlyának megváltoztatása a gráf mátrixjában.
	 * 
	 * @param starter az él kezdőpontja
	 * @param end az él végpontja
	 * @param weight az él új súlya
	 */
	@Override
	public void changeEdgeWeight(int starter, int end, int weight) {
		matrix[starter][end] = weight;
	}	
}