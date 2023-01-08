package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import graph.GraphLinks;


/**
 * Ablak amely tartalmazza a gráf szerkesztőt, illetve a gráfszerkesztőhöz tartozó menüpanelt.
 *
 * @author Kurcsi Norbert
 *
 */
public class GraphWindow extends JPanel implements ActionListener {

	private static final long serialVersionUID = -3419375731129627508L;
	
	private JButton backButton;
	private RepresentationPanel representation;
	private AlgorithmPanel algorithm;
	private DeleteButton deleteButton;
	private boolean directed;
	
	private GraphFrame frame;
	private GraphEditer graphEditer;
	
	/**
	 * Visszadja, hogy az ablakban lévő gráf irányított-e vagy sem
	 * @return igaz, ha irányított
	 */
	public boolean getDirected() {
		return directed;
	}
	
	/**
	 * Visszadja keretet, ahol az ablak megjelenik
	 * @return keret
	 */
	public GraphFrame getFrame() {
		return frame;
	}
	
	/**
	 * Enum osztáy a gombok kezeléséhez
	 * 
	 * @author Kurcsi Norbert
	 *
	 */
	private enum Actions {
		BACK,
		REPRESENTATION,
		RUN,
	}
	
	/**
	 * Visszadja az ablakban szerkesztett gráf összeköttetéseit tartalmazó objektumot
	 * @return összeköttetések
	 */
	public GraphLinks getLinks() {
		return graphEditer.getLinks();
	}
	
	/**
	 * Visszadja az ablakban szerkesztett gráf csomópontjainak a listáját
	 * @return gráf csomópontjai
	 */
	public NodeList<GraphNode> getNodes(){
		return graphEditer.getNodes();
	}
	
	/**
	 * Beállítja az összes csomópont színét az alapszínre
	 */
	public void resetNodeColors() {
		graphEditer.resetNodeColors();
	}
	
	/**
	 * Betölt egy gráfot fájlból
	 * @param file a fájl ahonnan betöltjük a gráfot
	 */
	public void loadGraph(File file) {
		graphEditer.deleteGraph();
		try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            GraphEditer newEditer = (GraphEditer)ois.readObject();
            graphEditer.loadGraph(newEditer);
            ois.close();
        } catch(Exception ex) {
        	JOptionPane.showMessageDialog(this,
				    "A fájlt nem sikerült betölteni!",
				    "Hiba",
				    JOptionPane.ERROR_MESSAGE);
//            ex.printStackTrace();
        }
	}
	
	/**
	 * Elmenti a gráfot
	 */
	public void saveGraph() {
		graphEditer.saveGraph();
	}
	
	/**
	 * Visszadja a kiválasztott csomópontot
	 * @return kiválasztott csomópont
	 */
	public GraphNode getSelectedNode() {
		return graphEditer.getSelectedNode();
	}
	
	
	/**
	 * Beállítja két csomópont által meghatározott él színét
	 * @param color az új szín
	 * @param starting kezdőpont
	 * @param finalNode végpont
	 */
	public void setEdgeColor(Color color, GraphNode starting, GraphNode finalNode) {
		graphEditer.setEdgeColor(color, starting, finalNode);
	}
	
	
	/**
	 * Vissszaállítja a gráf éleinek színét az alapértelmezettre
	 */
	public void resetEdgeColors() {
		graphEditer.resetEdgeColors();
	}
	
	/**
	 * Létrehozza a címpanelt
	 */
	private void createTitlePanel() {
		JLabel title = new JLabel("Graph Master");
		title.setFont(new Font("MV Boli", Font.BOLD, 40));
		title.setForeground(Color.white);
		title.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setBackground(new Color(0,0,0,0));
		titlePanel.add(title);
		titlePanel.setPreferredSize(new Dimension(0,70));
		titlePanel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.white));

		this.add(titlePanel,BorderLayout.NORTH);
	}
	
	/**
	 * Beállítja a töröl gomb állapotát a paraméterben megadott érték szerint
	 * @param active ha igaz, akkor a gomg aktívra állítása
	 */
	public void setDeleteButtonActive(boolean active) {
		if(active) {
			deleteButton.setEnabled(true);
		} else {
			deleteButton.setEnabled(false);
		}
	}
	
	/**
	 * Beállítja az algoritmus futtatását kezdeményező gomb állapotát a paraméterben megadott érték szerint
	 * @param active ha igaz, akkor a gomg aktívra állítása
	 */
	public void setAlgorithmButtonActive(boolean active) {
		algorithm.setAlgorithButtonActive(active);
	}
	
	/**
	 * Inicializája a menü-panelt és hozzáadja a gombokat
	 */
	private void initializeMenuPanel() {
		JPanel menu = new JPanel();
		menu.setBackground(Color.gray);
		
		menu.add(backButton);
		menu.add(representation);
		menu.add(algorithm);
		menu.add(deleteButton);

		
		this.add(menu,BorderLayout.SOUTH);
	}
	
	/**
	 * Inicializálja a gombokat és beállítja őket
	 */
	private void initializeButtons() {
		//Buttons
		backButton = new JButton("Vissza");
		representation = new RepresentationPanel(this);
		algorithm = new AlgorithmPanel(this);
		deleteButton = new DeleteButton(graphEditer);
		
		
		backButton.setActionCommand(Actions.BACK.name());
		
		backButton.addActionListener(this);

	}
	
	/**
	 * Beállítja a szerkesztő típusát, attól függően, hogy irányított vagy irányítatlan gráfról van-e szó
	 * @param directed igaz, ja irányított
	 */
	public void setEditerType(boolean directed) {
		this.directed = directed;
		graphEditer.setEditerType(directed);
	}
	
	/**
	 * Konstruktor az ablak létrehoásához, amely beállítja ennek elemeit
	 * @param frame keret, amiben az ablak megjelenik
	 */
	public GraphWindow(GraphFrame frame) {
		this.frame = frame;
		this.setLayout(new BorderLayout());
		this.setBackground(Color.gray);
		this.setBorder(BorderFactory.createLineBorder(Color.white,5));
		
		graphEditer = new GraphEditer(this);
		
		createTitlePanel();
		this.initializeButtons();
		this.initializeMenuPanel();
		
		this.add(graphEditer,BorderLayout.CENTER);
	}

	/**
	 * Kezeli a töröl gomb megnyomását
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == Actions.BACK.name()) {
			frame.changeWindow(this,frame.getStartingWindow());
			graphEditer.deleteGraph();
		}
	}

}