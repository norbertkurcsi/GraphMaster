package graph;

import gui.GraphNode;

/**
 * 
 * Egy algoritmus egy darab lépésének a tárolására és kezelésére alkalmas osztály.
 * 
 * @author Kurcsi Norbert
 *
 */
public class Step {
	// Kezdő és végső csomópont, amelyek a lépést alkotják
	private GraphNode from,target;
	
	/**
	 * Visszaadja a lépés kezdőpontját alkotó GraphNode csomópontot. 
	 * 
	 * @return kezdőpont
	 */
	public GraphNode getFrom() {
		return from;
	}
	
	/**
	 * Visszaadja a lépés célpontját alkotó GraphNode csomópontot. 
	 * 
	 * @return cél csomópont
	 */
	public GraphNode getTarget() {
		return target;
	}
	
	/**
	 * Beállítja a lépés kezdő és cél csomópontját a paraméterekben megadott értékekre.
	 * @param f kezdőpont
	 * @param t végpont
	 */
	public void setStep(GraphNode f, GraphNode t) {
		from = f;
		target = t;
	}
}
