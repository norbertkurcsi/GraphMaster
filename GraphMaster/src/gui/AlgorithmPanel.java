package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import graph.Step;

/**
 * Az algoritmusok futtatásáért, algoritmus lépések kezeléséért,
 * illetve az algoritmus kiválasztásáért felelős panel.
 * 
 * @author Kurcsi Norbert
 *
 */
public class AlgorithmPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 6829051184068982707L;
	
	// az algoritmus kiválasztását biztosító JComboBox
	private JComboBox<String> comboBox;
	// az algoritmus futtatásának indítását kezelő gomb
	private JButton button;
	// algoritmus lépéseit kezelő gomb
	private JButton step;
	
	// ablak referencia, arra az ablakra amely tartalmazza a panelt
	private GraphWindow window;
	
	// bejárás essetén, a lépéseket tartalmazó List
	private List<Step> search;
	
	// Dijkstra esetén az úttömböt tartalmazó tömb
	private int[] route;
	
	// lépésszámláló
	private int stepCounter = 0;
	
	// referencia a kezdőpontra, ahonnan az algoritmust indítjuk
	private GraphNode starting;
	
	// az algoritmus panel állapotai
	private enum State{
		BFS,DFS,DIJKSTRA,NONE
	}
	
	// alg állapotát tároló változó
	private State state = State.NONE;
	
	/**
	 * Konstruktor, amely az ablak megadásával, létrehozza a panelt
	 * @param window
	 */
	public AlgorithmPanel(GraphWindow window){
		this.window = window;
		
		setBackground(Color.gray);
		
		String[] options = new String[3];
		options[0] = "Dijkstra";
		options[1] = "Mélységi bejárás";
		options[2] = "Szélességi bejárás";
		
		comboBox = new JComboBox<String>(options);
		
		button = new JButton("Futtatás");
		button.addActionListener(this);
		button.setEnabled(false);
		
		step = new JButton("Lépés");
		step.addActionListener(this);
		step.setEnabled(false);
		
		this.add(comboBox);
		this.add(button);
		this.add(step);
	}
	
	/**
	 * A megadott paraméter függvényében átállítja az indítás gombot aktív/inaktív állapotba
	 * @param active igaz, ha aktívvá akarjuk állítani az indít gombot
	 */
	public void setAlgorithButtonActive(boolean active) {
		if(active) {
			button.setEnabled(true);
		} else {
			button.setEnabled(false);
		}
	}
	
	/**
	 * A JComboBox-ban kiválasztott algoritmus függvényében, lefuttatja az algoritmust,
	 * és beállítja a panel állapotát a megfelelő értékre
	 */
	private void handleComboBox() {
		starting =  window.getSelectedNode();
		starting.setColor(Color.red);
		button.setEnabled(false);
		switch((String) comboBox.getSelectedItem()) {
		case "Dijkstra":
			state = State.DIJKSTRA;
			step.setEnabled(true);
			route =  window.getLinks().dijkstra(starting);
			break;
		case "Mélységi bejárás":
			state = State.DFS;
			step.setEnabled(true);
			search =  window.getLinks().dfs(starting);
			break;
		case "Szélességi bejárás":
			state = State.BFS;
			step.setEnabled(true);
			search =  window.getLinks().bfs(starting);
			break;
		}
	}
	
	/**
	 * Abban az esetben ha a futtatott algoritmus egy bejárás.
	 * Lépteti az algoritmust, illetve ha az algoritmus befejeződött, akkor visszaállítja
	 * a gráfot standard állapotba, illetve a panelt semleges állapotba.
	 */
	private void handleSearch() {
		if(stepCounter == search.size()) {
			state = State.NONE;
			step.setEnabled(false);
			button.setEnabled(true);
			stepCounter = 0;
			String message = "";
			for(Step step : search) {
				message = new String(message + step.getFrom().getNumber() + " -> " + step.getTarget().getNumber() + "\n");
			}
			JOptionPane.showMessageDialog(this,message);
			window.resetNodeColors();
			window.resetEdgeColors();
		} else {
			search.get(stepCounter).getTarget().setColor(Color.red);
			window.setEdgeColor(Color.red,search.get(stepCounter).getFrom() ,search.get(stepCounter).getTarget());
			stepCounter++;
		}
	}
	
	/**
	 * Abban az esetben ha a futtatott algoritmus Dijkstra.
	 * Lépteti az algoritmust, illetve ha az algoritmus befejeződött, akkor visszaállítja
	 * a gráfot standard állapotba, illetve a panelt semleges állapotba.
	 */
	private void handleDijkstra() {
		if(stepCounter == route.length) {
			state = State.NONE;
			step.setEnabled(false);
			button.setEnabled(true);
			stepCounter = 0;
			JOptionPane.showMessageDialog(this,"Algoritmus befejeződött!");
			window.resetNodeColors();
			window.resetEdgeColors();
		} else {
			List<Step> steps = window.getLinks().convertDijkstraRoute(route, stepCounter++);
			window.resetNodeColors();
			window.resetEdgeColors();
			starting.setColor(Color.red);
			for(int i = steps.size()-1; i >= 0; --i) {
				steps.get(i).getFrom().setColor(Color.red);
				window.setEdgeColor(Color.green,steps.get(i).getFrom() ,steps.get(i).getTarget());
			}
			if(steps.size() != 0) {
				steps.get(0).getFrom().setColor(Color.green);
			}
		}
	}
	
	/**
	 * Eldönti, hogy a kiválasztott algoritmus egy bejárás, vagy Dijkstra algoritmus
	 */
	private void handleStep() {
		if(state == State.DIJKSTRA) {
			handleDijkstra();
		} else {
			handleSearch();
		}
	}
	
	/**
	 * A gombok megnyomása esetén, azok akcióit kezeli
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == button) {
			handleComboBox();
		} else if(e.getSource() == step) {
			handleStep();
		}
	}
}
