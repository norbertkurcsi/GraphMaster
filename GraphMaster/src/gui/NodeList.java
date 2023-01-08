package gui;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Csomópontok tárolására alkalmas tároló, amely tárolja, hogy következő
 * hozzáadott csomópontnak mi kell legyen a sorszáma
 * 
 * @author Kurcsi Norbert
 *
 * @param <E> GraphNode
 */
public class NodeList<E> extends ArrayList<E> implements Serializable{

	private static final long serialVersionUID = 6034283664087739011L;
	
	private int nextNumber = 1;
	
	/**
	 * Visszadja a következő csomópont sorszámát
	 * @return következő sorszám
	 */
	public int getNextNumber() {
		return nextNumber++;
	}
	
	/**
	 * Visszaállítja aszámlálót 1-re
	 */
	public void resetNumbers() {
		nextNumber = 1;
	}
	
}
