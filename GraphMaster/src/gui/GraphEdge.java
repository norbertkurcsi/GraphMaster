package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serializable;

import javax.swing.JTextField;

/**
 * Gráf élét megvalósító osztály. Tárolja a csomópontokat, amelyek között fut,
 * az él koordinátáit, illetve kezeli JtextFieldet, amely segítségével meg lehet változtatni
 * az él súlyát.
 * @author Kurcsi Norbert
 *
 */
public class GraphEdge extends JTextField implements Serializable{
	
	private static final long serialVersionUID = 7546836082521620245L;
	
	// kezdő csomópont
	private GraphNode startingNode;
	
	// végső csomópont
	private GraphNode finalNode;
	
	// él súlya
	private int weight = 1;
	
	// él színe
	private Color color;
	
	// a szerkesztő, ahol az él kirajzolódik
	private GraphEditer editer;
	
	// a JTextField koordinátája
	private Point center;
	
	/**
	 * Beállítja az él színét a paraméterben megadott színre
	 * @param c az él új színe
	 */
	public void setColor(Color c) {
		color = c;
	}
	
	/**
	 * Vissszaadja az él színét
	 * @return az él színe
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Visszaadja az él súlyát
	 * @return az él súlya
	 */
	public int getWeight() {
		return weight;
	}
	
	/**
	 * Hozzáadja a Listener-eket az élhez
	 */
	public void addListeners() {
		this.addFocusListener(new MyFocusListener());
		this.addKeyListener(new MyKeyListener());
	}
	
	/**
	 * Beállítja a szerkesztőt, ahol az él kirajzolódik
	 * @param editer szerkesztő, ahol az él kirajzolódik
	 */
	public void setEditer(GraphEditer editer) {
		this.editer = editer;
	}
	
	/**
	 * Konstruktor, amely 2 db csomópontból, illetve a szerkesztő megadásával,
	 * létrehoz egy élet
	 * @param a kezdőpont
	 * @param b végpont
	 * @param editer szerkesztő, ahol az él kirajzolódik
	 */
	public GraphEdge(GraphNode a, GraphNode b,GraphEditer editer) {
		super();
		startingNode = a;
		finalNode = b;
		this.editer = editer;
		calcCenterPoint();
		setText(String.valueOf(1));
		Font newTextFieldFont=new Font(this.getFont().getName(),this.getFont().getStyle(),16);
		this.setFont(newTextFieldFont);
		this.setHorizontalAlignment(JTextField.CENTER);
		
		color = Color.black;
		
		this.setBounds(center.x,center.y,30,30);
	
		addListeners();
	}
	
	/**
	 * Visszaadja az él kezdőpontját
	 * @return kezdőpont
	 */
	public GraphNode getStartingNode() {
		return startingNode;
	}
	
	/**
	 * Visszadja az él végpontját
	 * @return végpont
	 */
	public GraphNode getFinalNode() {
		return finalNode;
	}
	
	/**
	 * Kiszámítja a JTextField koordinátáját
	 */
	public void calcCenterPoint() {
		Point a = startingNode.getCoordinates();
		Point b = finalNode.getCoordinates();
		center = new Point((int)(a.x+b.x)/2,(int)(a.y+b.y)/2);
	}
	
	/**
	 * Beállítja a JTextField pozicíóját a center vátozóban tárolt pontra
	 */
	public void setBounds() {
		this.setBounds(center.x,center.y,30,30);
		repaint();
	}
	
	/**
	 * Az él FocusListener-je, amely elvégzi a szükséges múveleteket, ha a JTextField megkapja,
	 * vagy elveszíti a fókuszt.
	 * 
	 * @author Kurcsi Norbert
	 *
	 */
	private class MyFocusListener implements FocusListener{

		/**
		 * Abban az esetben, ha textfield megkapja a focust, kiválasztja az egész szöveget, ami benne van
		 * 
		 * @param e event
		 */
		@Override
		public void focusGained(FocusEvent e) {
			GraphEdge.this.selectAll();
		}

		/**
		 * Ha a textfield elveszíti a focust, akkor ellenőrzi, hogy a benne lévő szöveg
		 * megfelel-e egy él súlyának a feltételeinek, és ha igen, akkor beállítja az él súlyát
		 * 
		 * @param e event
		 */
		@Override
		public void focusLost(FocusEvent e) {
			String text = GraphEdge.this.getText();
			try {
				int number = Integer.parseInt(text);
				if(number >= 1 && number <= 99) {
					weight = number;
					editer.changeEdgeWeight(startingNode, finalNode, weight);
				} else {
					GraphEdge.this.setText(String.valueOf(weight));
				}
			} catch (NumberFormatException exception) {
				GraphEdge.this.setText(String.valueOf(weight));
			}
		}
	}
	
	/**
	 * KeyListener, amely kezeli azt, hogy ha ENTER billentyűt nyomott a felhasználó,
	 * akkor továbbadja a fokuszt
	 * 
	 * @author Kurcsi Norbert
	 *
	 */
	private class MyKeyListener extends KeyAdapter{
		
		/**
		 * Ha ENTER billentyű leütés történt, akkor továbbadja a fókuszt
		 */
        @Override
        public void keyPressed(KeyEvent e) {
        	if(e.getKeyCode() == KeyEvent.VK_ENTER) {
       		 	GraphEdge.this.transferFocus();
       	 	} 
        }
	}
}