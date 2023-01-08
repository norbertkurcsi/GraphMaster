package gui;

import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * Gráf csomópontját megvalósíó osztály. Tárolja a csomópont koordinátáit,
 * kezeli a csomópontokkal kapcsolatos eseményeket, tárolja a csomópont színét,
 * illetve, hogy ki van-e választva az adott csomópont vagy sem.
 * 
 * @author Kurcsi Norbert
 *
 */
public class GraphNode extends JButton implements Serializable{

	private static final long serialVersionUID = -701320348185744611L;

	// a szerkesztő ahol a csomópont kirajzolódik
	private GraphEditer editer;
	
	// tárolja, hogy a kurzor a csp fölött van-e
	private boolean mouseOver = false;
	
	// tárolja, hogy a csp ki van-e választva
	private boolean nodeSelected = false;
	
	// a csp koordinátái
	private Point coordinates;
	
	// a csp kirajzolásakor megjelenő szám
	private int number;
	
	// a csp sszíne
	private Color color;
	
	
	/**
	 * Beállítja a szerkesztőt, ahol a csomópont kirajzolódik
	 * @param e szerkesztő, ahol a csp kirajzolódik
	 */
	public void setEditer(GraphEditer e) {
		editer = e;
	}
	
	
	/**
	 * Vissszadja a csp kirajzolásakor megjelenő számot
	 * @return megjelenő szám
	 */
	public int getNumber() {
		return number;
	}
	
	/**
	 * Beállítja a csp színét
	 * @param c a csp új színe
	 */
	public void setColor(Color c) {
		color = c;
		nodeSelected = false;
		editer.removeSelectedNode(GraphNode.this);
		repaint();
	}
	
	/**
	 * Hozzáadja a csp-hoz a MouseListenereket
	 */
	public void addListeners() {
		MouseAdapter mouseListener = new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("bement");
				if(nodeSelected) {
					editer.removeSelectedNode(GraphNode.this);
				} else {
					editer.addSelectedNode(GraphNode.this);
				}
				nodeSelected = !nodeSelected;
				editer.requestFocus();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				coordinates.translate(e.getX()-25, e.getY()-25);
				setBounds(coordinates);
				editer.changeEdgesLocation();
				repaint();
			}

		};

		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
	}

	
	/**
	 * Konstrukor, amely egy szerkesztő, a csp-on kirajzolódó szám, és a csp koordinátáit megadva létrehoz egy csomópontot
	 * @param e szerkesztő
	 * @param number a csomóponton megjelenő szám
	 * @param x a csomópont horizontális koordinátája
	 * @param y a csomópont vertikélis koordinátája
	 */
	public GraphNode(GraphEditer e,int number,int x, int y){
		super(Integer.toString(number));
		this.number = number;
		editer = e;
		setOpaque(false);
		setFocusPainted(false);
		setBorderPainted(false);
		this.setFocusable(true);
		
		Font newTextFieldFont=new Font(this.getFont().getName(),this.getFont().getStyle(),25);
		this.setFont(newTextFieldFont);
		
		color = Color.blue;
		
		coordinates = new Point(x,y);
		setBounds(coordinates);

		addListeners();
	}
	
	/**
	 * Visszadja a csp koordinátáit
	 * @return a csp koordinátái
	 */
	public Point getCoordinates() {
		return coordinates;
	}
	
	/**
	 * Visszadja, hogy a csp ki van-e választva vagy sem
	 * @return igaz, ha a csp ki van választva
	 */
	public boolean isSelected() {
		return nodeSelected;
	}
	
	/**
	 * Beállítja a csp koordinátáit
	 * @param coord a csp új koordinátái
	 */
	public void setCoordinates(Point coord) {
		coordinates = coord;
	}
	
	/**
	 * Beállítja a csomópontot nem kiválassztott állapotba
	 */
	public void setNotSelected() {
		nodeSelected = false;
	}
	
	
	/**
	 * Beállítja a csomópontot a megadott koordinátára
	 * @param coord a csomópont új helye
	 */
	public void setBounds(Point coord) {
		super.setBounds(coord.x-25,coord.y-25,50,50);
	}

	
	/**
	 * Visszadja a csomópont sugarának hosszát
	 * @return csomópont sugara
	 */
	private int getDiameter(){
		int diameter = Math.min(getWidth(), getHeight());
		return diameter;
	}

	/**
	 * Visszadja a csomópontnak bállított preferált nagyságot
	 * @return preferált méret
	 */
	@Override
	public Dimension getPreferredSize(){
		FontMetrics metrics = getGraphics().getFontMetrics(getFont());
		int minDiameter = 10 + Math.max(metrics.stringWidth(getText()), metrics.getHeight());
		return new Dimension(minDiameter, minDiameter);
	}

	/**
	 * Visszadja, hogy egy adott koordináta, benne van-e a csomópontban
	 * @param x a koordináta horizontális értéke
	 * @param y a koordináta vertikális értéke
	 * @return igaz, ha a pont benne van a csomópontban
	 */
	@Override
	public boolean contains(int x, int y){
		int radius = getDiameter()/2;
		return Point2D.distance(x, y, getWidth()/2, getHeight()/2) < radius;
	}

	
	/**
	 * A csomópont kirajzolása a grafikus felületre
	 * @param grafikus felület
	 */
	@Override
	public void paintComponent(Graphics g){

		int diameter = getDiameter();
		int radius = diameter/2;

		if(nodeSelected){
			g.setColor(Color.LIGHT_GRAY);
		}
		else{
			g.setColor(color);
		}
		g.fillOval(getWidth()/2 - radius, getHeight()/2 - radius, diameter, diameter);

		if(mouseOver){
			g.setColor(color);
		}
		else{
			g.setColor(Color.BLACK);
		}
		g.drawOval(getWidth()/2 - radius, getHeight()/2 - radius, diameter, diameter);

		g.setColor(Color.BLACK);
		g.setFont(getFont());
		FontMetrics metrics = g.getFontMetrics(getFont());
		int stringWidth = metrics.stringWidth(getText());
		int stringHeight = metrics.getHeight();
		g.drawString(getText(), getWidth()/2 - stringWidth/2, getHeight()/2 + stringHeight/4);
	}
	
	/**
	 * A csomópont kirajzolása a grafikus felületre
	 * @param grafikus felület
	 */
	public void myPaintComponent(Graphics g) {
		int x = coordinates.x;
		int y = coordinates.y;
		int diameter = getDiameter();
		int radius = diameter/2;

		if(nodeSelected){
			g.setColor(Color.LIGHT_GRAY);
		}
		else{
			g.setColor(color);
		}
		g.fillOval(x - radius, y - radius, diameter, diameter);

		if(mouseOver){
			g.setColor(color);
		}
		else{
			g.setColor(Color.BLACK);
		}
		g.drawOval(x - radius, y - radius, diameter, diameter);

		g.setColor(Color.BLACK);
		g.setFont(getFont());
		FontMetrics metrics = g.getFontMetrics(getFont());
		int stringWidth = metrics.stringWidth(getText());
		int stringHeight = metrics.getHeight();
		g.drawString(getText(), x - stringWidth/2, y + stringHeight/4);
	}
	
}