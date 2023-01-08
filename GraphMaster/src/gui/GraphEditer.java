package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import graph.DirectedLink;
import graph.GraphLinks;
import graph.IndirectedLink;

/**
 * A gráf szerkesztésére alkalmas felület, ahol megjelenítődnek a csomópontok és élek.
 * Ez az objektum tárolja a csomópontokat ls éleket.
 * A mentéshez ennek az osztálynak az attribútumait szerializáljuk.
 * 
 * @author Kurcsi Norbert
 *
 */
public class GraphEditer extends JPanel implements Serializable{

	private static final long serialVersionUID = 1662376004487334461L;
	
	transient private GraphWindow window;
	
	transient private Point mousePos;
	
	private Queue<GraphNode> selectedNodes;
	private NodeList<GraphNode> nodes;
	private List<GraphEdge> edges;
	
	private GraphLinks links = new DirectedLink(this);
	private boolean directed;
	
	/**
	 * Beállítja az ablakot, ahol a szerkesztő megjelenik
	 * @param window az ablak, ahol a szerkesztő megjelenik
	 */
	public void setWindow(GraphWindow window) {
		this.window = window;
	}
	
	/**
	 * Megvizsgálja, hogy adott fálj már létezik e a mentések könyvtárában vagy sem
	 * @param file a vizsgálandó fájl
	 * @return igaz ha a fájl már létezik
	 */
	public boolean fileExists(File file) {
		File wd = file.getParentFile();
		File[] files = wd.listFiles();
		for(File f : files) {
			if(f.getName().equals(file.getName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * A mentés dialógus kezelését lebonyolító metódus
	 * @return a dialógusba begépelt szöveg
	 */
	private File saveDialog() {
		String s = (String)JOptionPane.showInputDialog(
                this,
                "Adja meg a fájl nevét, ahova el szeretné menteni a gráfot:",
                "Customized Dialog",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "gráfnév");
		if(s == null) {
			return null;
		}
		File newFile = new File(System.getProperty("user.dir")+"/saves/" + s + ".txt");
		if(!fileExists(newFile)) {
			return newFile;
		}
		return null;
	}
	
	
	/**
	 * Gráf mentését végrehajtó függvény
	 */
	public void saveGraph() {
		File newFile = saveDialog();
		if(newFile != null) {
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(newFile));
				oos.writeObject(this);
				oos.close();
				
				JOptionPane.showMessageDialog(this,
					    "A gráf sikeresen el lett mentve!");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			JOptionPane.showMessageDialog(this,
				    "A megadott fájlnév már létezik vagy nem adott meg fájl nevet!",
				    "Hiba",
				    JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/**
	 * Beállítja egy csomópontokkal megadott él színét
	 * @param color az él új színe
	 * @param starting kezdőcsomópont
	 * @param finalNode végcsomópont
	 */
	public void setEdgeColor(Color color, GraphNode starting, GraphNode finalNode) {
		for(GraphEdge edge : edges) {
			if((edge.getStartingNode() == starting && edge.getFinalNode() == finalNode)
					|| (edge.getStartingNode() == finalNode && edge.getFinalNode() == starting)) {
				edge.setColor(color);
				repaint();
				return;
			}
		}
	}
	
	/**
	 * Az élek súlyát visszaállítja az alapértelmezettre
	 */
	public void resetEdgeColors() {
		for(GraphEdge edge : edges) {
			edge.setColor(Color.black);
		}
		repaint();
	}
	
	/**
	 * A paraméterben megkapott újabban betöltött szerkesztő adatait átmásolja jelenlegi szerkesztőbe,
	 * ezzel betöltve a gráfot
	 * @param newEditer betöltött szerkesztő
	 */
	public void loadGraph(GraphEditer newEditer) {
		this.selectedNodes = newEditer.selectedNodes;
		this.nodes = newEditer.nodes;
		this.edges = newEditer.edges;
		this.links = newEditer.links;
		this.directed = newEditer.directed;
		
		for(GraphNode node : nodes) {
			node.setEditer(this);
			node.addListeners();
			this.add(node);
		}
		for(GraphEdge edge : edges) {
			edge.setEditer(this);
			edge.addListeners();
			this.add(edge);
		}
		
		this.repaint();
		
	}
	
	/**
	 * Visszadja az élek tárolóját
	 * @return élek
	 */
	public List<GraphEdge> getEdges(){
		return edges;
	}
	
	/**
	 * Visszadja a csomópontok tárolóját
	 * @return csomópontok
	 */
	public NodeList<GraphNode> getNodes(){
		return nodes;
	}
	
	/**
	 * Visszaadja a gráf összeköttetéseit tartalmazó objektumot
	 * @return összeköttetések
	 */
	public GraphLinks getLinks(){
		return links;
	}
	
	/**
	 * Vissszaadja a csomópontok számát
	 * @return csomópontok száma
	 */
	public int getNodesNumber() {
		return nodes.size();
	}
	
	/**
	 * Törli az aktuális gráf adatait
	 */
	public void deleteGraph() {
		selectedNodes.clear();
		nodes.clear();
		nodes.resetNumbers();
		edges.clear();
		this.removeAll();
	}
	
	/**
	 * Beállítja a szerkesztő típusát, a betöltött gráftól függően
	 * @param directed igaz, ha irányított
	 */
	public void setEditerType(boolean directed) {
		this.directed = directed;
		if(directed) {
			links = new DirectedLink(this);
		} else {
			links = new IndirectedLink(this);
		}
	}
	
	/**
	 * Létrehoz egy szerkesztőt,a paraméterben megadott ablakban
	 * @param window ablak, ahol létrehozzuk a szerkesztőt
	 */
	public GraphEditer(GraphWindow window) {
		this.window = window;
		this.setLayout(null);
		
		selectedNodes = new ArrayBlockingQueue<GraphNode>(2);
		nodes = new NodeList<GraphNode>();
		edges = new ArrayList<GraphEdge>();
		mousePos = new Point();
		
		this.addKeyListener(new MyKeyListener());
		this.addMouseListener(new MyMouseListener());
		this.addMouseMotionListener(new MyMouseMotionListener());
	}
	
	/**
	 * Visszaadja a megadott csomópont indexjét
	 * @param node csomópont
	 * @return index
	 */
	public int getNodeIndex(GraphNode node) {
		for(int i = 0; i < nodes.size() ; ++i) {
			if(nodes.get(i) == node) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Törli a kiválasztott csomópontot és a hozzátartozó éleket
	 */
	public void removeNode() {
		GraphNode node = selectedNodes.poll();
		removeNodeEdges(node);
		links.deleteNode(getNodeIndex(node));
		nodes.remove(node);
		this.remove(node);
		window.setDeleteButtonActive(false);
		this.repaint();
	}
	
	/**
	 * Kezeli a csomópont hozzáadását a kiválasztott csomópontok tárolójába
	 * @param node csomópont, amelyet kiválasztottunk
	 */
	public void addSelectedNode(GraphNode node) {
		if(selectedNodes.size() == 2) {
			selectedNodes.peek().setNotSelected();
			selectedNodes.peek().repaint();
			selectedNodes.poll();
			
		}
		selectedNodes.add(node);
		handleActiveButtons();
	}
	
	/**
	 * Töröl egy csomópontot a kiválasztott csomópontok közül
	 * @param node törölni kívánt csomópont
	 */
	public void removeSelectedNode(GraphNode node) {
		selectedNodes.remove(node);
		handleActiveButtons();
	}
	
	/**
	 * Kezeli azokat a gombokat, amelyek aktivitása a kiválasztott csomópontoktól függ
	 */
	private void handleActiveButtons() {
		if(selectedNodes.size() == 1) {
			window.setDeleteButtonActive(true);
			window.setAlgorithmButtonActive(true);
		} else {
			window.setDeleteButtonActive(false);
			window.setAlgorithmButtonActive(false);
		}
	}
	
	/**
	 * Visszaad egy kiválasztott csomópontot
	 * @return kiválasztott csomópont
	 */
	public GraphNode getSelectedNode() {
		return selectedNodes.peek();
	}
	
	/**
	 * Hozzáad egy csomópontot a grághoz és a szerkesztőhöz
	 * @param node a hozzáadni kívánt csomópont
	 */
	public void addNode(GraphNode node) {
		this.nodes.add(node);
		links.addNode(nodes.size());
		links.printMatrix(); 
		this.add(node);
		node.repaint();
	}
	
	/**
	 * Törli egy adott csomópont éleit
	 * @param node csomópont, amelynek töröljük az éleit
	 */
	private void removeNodeEdges(GraphNode node) {
		for(int i = edges.size()-1; i >= 0 ; --i) {
			if(edges.get(i).getStartingNode() == node || edges.get(i).getFinalNode() == node) {
				System.out.println("lefut");
				this.remove(edges.get(i));
				edges.remove(i);
			}
		}
	}
	
	/**
	 * megváltoztatja az élek koordinátáit
	 */
	public void changeEdgesLocation() {
		for(GraphEdge edge : edges) {
			edge.calcCenterPoint();
			edge.setBounds();
		}
	}
	
	/**
	 * Megváltoztatja egy él súlyát
	 * @param starter az él kezdőpontja
	 * @param end az él végpontja
	 * @param weight az él új súlya
	 */
	public void changeEdgeWeight(GraphNode starter, GraphNode end, int weight) {
		links.changeEdgeWeight(getNodeIndex(starter), getNodeIndex(end), weight);
	}
	
	/**
	 * Hozzáad egy élet a gráfhoz, amit a kiválasztott csomópontok határoznak meg
	 */
	private void addEdge() {
		GraphNode startingNode = selectedNodes.poll();
		GraphNode finalNode = selectedNodes.poll();
		
		startingNode.setNotSelected();
		finalNode.setNotSelected();
		
		GraphEdge edge = new GraphEdge(startingNode, finalNode, this);
		if(!links.edgeExists(edge)) {
			edges.add(edge);
			links.addEdge(getNodeIndex(startingNode),getNodeIndex(finalNode));
			GraphEditer.this.add(edge);
			GraphEditer.this.repaint();
		}
		
	}
	
	/**
	 * Hozzáad egy élet a gráfhoz, amit a paraméterben megadott csomópontok határoznak meg
	 * @param start kezdő pont
	 * @param finish végpont
	 */
	public void addEdge(GraphNode start, GraphNode finish) {
		GraphEdge edge = new GraphEdge(start, finish, this);
		edges.add(edge);
		links.addEdge(getNodeIndex(start),getNodeIndex(finish));
	}
	
	private class MyKeyListener extends KeyAdapter{
         @Override
         public void keyPressed(KeyEvent e) {
        	 if(e.getKeyCode() == KeyEvent.VK_SPACE) {
        		 int number = nodes.getNextNumber();
            	 addNode(new GraphNode(GraphEditer.this,number,mousePos.x,mousePos.y));
        	 } else if(e.getKeyCode() ==  KeyEvent.VK_ENTER && selectedNodes.size() == 2) {
        		 addEdge();
        	 }
        	 
         }
	}
	
	private class MyMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			requestFocus();
		}
	}

	private class MyMouseMotionListener extends MouseMotionAdapter{
		@Override
		public void mouseMoved(MouseEvent e) {
			mousePos.move(e.getX(),e.getY());
		}
	}
	
	private void drawArrowLine(Graphics2D g, int x1, int y1, int x2, int y2, int d, int h) {
	    int dx = x2 - x1, dy = y2 - y1;
	    double D = Math.sqrt(dx*dx + dy*dy);
	    double xm = D - d, xn = xm, ym = h, yn = -h, x;
	    double sin = dy / D, cos = dx / D;

	    x = xm*cos - ym*sin + x1;
	    ym = xm*sin + ym*cos + y1;
	    xm = x;

	    x = xn*cos - yn*sin + x1;
	    yn = xn*sin + yn*cos + y1;
	    xn = x;

	    int[] xpoints = {x2, (int) xm, (int) xn};
	    int[] ypoints = {y2, (int) ym, (int) yn};

	    g.drawLine(x1, y1, x2, y2);
	    g.fillPolygon(xpoints, ypoints, 3);
	}
	
	private Point calcPointOnCircle(Point a,Point b) {
		double delta_x = a.x - b.x;
		double delta_y = b.y - a.y;
		double theta_radians = Math.atan2(delta_y, delta_x) + Math.PI;
		//System.out.println(Math.toDegrees(theta_radians + Math.PI));
		
		int x = (int) (Math.cos(theta_radians) * 25.0);
		int y = (int) (Math.sin(theta_radians) * 25.0);
		
		return new Point(b.x-x,b.y+y);
	}
	
	/**
	 * Visszaállítja a csomúpontok színét az alapértelmezettre
	 */
	public void resetNodeColors() {
		for(GraphNode node : nodes) {
			node.setColor(Color.blue);
		}
	}
	
	@Override
	public void paint(Graphics g) {
		repaint();
		super.paint(g);
		for(GraphEdge edge: edges) {
			Point a = edge.getStartingNode().getCoordinates();
			Point b = edge.getFinalNode().getCoordinates();
			
			Point arrow = calcPointOnCircle(a,b);
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(3));
			g2.setColor(edge.getColor());
			if(directed) {
				drawArrowLine(g2,a.x,a.y,arrow.x,arrow.y,40,10);
			}else {
				g2.drawLine(a.x, a.y, arrow.x, arrow.y);
			}
		}
		
		for(GraphNode node : nodes) {
			node.myPaintComponent(g);
		}

	}
}
