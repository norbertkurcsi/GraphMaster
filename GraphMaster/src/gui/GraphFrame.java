package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Ablak osztáy, amelyben a program fut, itt jelennek meg a különböző ablakok, illetve 
 * itt jelenik meg a keret tetején elhelyezkedő menü bar
 * 
 * @author Kurcsi Norbert
 *
 */
public class GraphFrame extends JFrame implements ActionListener{

	private static final long serialVersionUID = 2164517807453908126L;
	
	private StartingWindow startingWindow;
	private GraphTypeWindow graphTypeWindow;
	private GraphWindow graphWindow;
	private JMenuBar mb;
	private JMenuItem save,load;
	private JFileChooser fc;
	
	/**
	 * Inicializálja a keret tetjén elhelyezkedő menü bart
	 */
	private void initMenuBar() {
		mb = new JMenuBar();
		
		JMenu file = new JMenu("Fájl");
		
		save = new JMenuItem("Mentés");
		load = new JMenuItem("Betöltés");
		save.addActionListener(this);
		load.addActionListener(this);
		
		file.add(save);
		file.add(load);
		
		mb.add(file);
		
		mb.setVisible(false);
		
		this.setJMenuBar(mb);
	}
	
	/**
	 * Ellenórzi, hogy a paraméterben megadott fájl, a program saves mappájában található
	 * @param current a fájl amit ellenőriz
	 * @return igaz, ha a file a sabes mappában van
	 */
	private boolean checkSelectedFile(File current) {
		final File graph = new File(System.getProperty("user.dir"));
		final File saves = new File(graph + "/saves");
		
		
		if(!current.getParentFile().getName().equals(saves.getName())) {
			return false;
		}
		if(!current.getParentFile().getParentFile().getName().equals(graph.getName())) {
			return false;
		}
		return true;
	}
	
	/**
	 * kezeli a fájl kiválasztó ablakot
	 * @return igaz, ha megfelelő fájl lett kiválasztva
	 */
	private boolean handleFileChooser() {
		
		int returnVal = fc.showOpenDialog(fc);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			
			if(checkSelectedFile(file)) {
				graphWindow.loadGraph(file);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Inicializálja a keretet
	 */
	private void initialization() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("GraphMaster");
		setSize(1080,720);
		
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.dir")+"/saves"));
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("Text file","*.txt", "txt"));
		
		this.setMinimumSize(new Dimension(1080,720));
	}

	/**
	 * A keret konstruktora, amely létrehozza a keretben megjeleníthető ablakokat
	 */
	public GraphFrame() {
		initialization();
		initMenuBar();
		startingWindow = new StartingWindow(this);
		graphTypeWindow = new GraphTypeWindow(this);
		graphWindow = new GraphWindow(this);
		
		this.add(startingWindow);
	}
	
	/**
	 * Kicseréli a megjelenítendő ablakot
	 * @param from az ablak ami jelenleg látható
	 * @param target az ablak amit meg kell jeleníteni
	 */
	public void changeWindow(JPanel from, JPanel target) {
		if(target == null) {
			return;
		}
		from.setVisible(false);
		
		target.setVisible(true);
		this.add(target);
	}
	
	/**
	 * Visszatéríti a GraphWindow ablak refernciáját
	 * @param directed igaz, ha a GraphWindow-ban irányított gráf van
	 * @return az ablak referenciája
	 */
	public JPanel getGraphWindow(boolean directed) {
		graphWindow.setEditerType(directed);
		mb.setVisible(true);
		return graphWindow;
	}
	
	/**
	 * Visszatéríti a GraphWindow ablak refernciáját, akkor hívandó, ha a GraphWindow-ba új gráf kerül
	 * @return az ablak referenciája
	 */
	public JPanel getGraphWindowWithLoad() {
		mb.setVisible(true);
		if(handleFileChooser()) {
			mb.setVisible(true);
			return graphWindow;
		}
		JOptionPane.showMessageDialog(this,
			    "A fájlt nem sikrült betölteni!",
			    "Hiba",
			    JOptionPane.ERROR_MESSAGE);
		mb.setVisible(false);
		return null;
	}
	
	/**
	 * Gráf törlését kezeli
	 */
	public void handleDelteGraph() {
		int returnVal = fc.showOpenDialog(fc);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			
			if(checkSelectedFile(file)) {
				if(file.delete()) {
					JOptionPane.showMessageDialog(this,
						    "A gráf sikeresen törölve!");
					return;
				}
			}
			JOptionPane.showMessageDialog(this,
				    "A fájlt nem sikrült törölni!",
				    "Hiba",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Visszatéríti a StartingWindow ablak refernciáját
	 * @return az ablak referenciája
	 */
	public JPanel getStartingWindow() {
		mb.setVisible(false);
		return startingWindow;
	}
	
	/**
	 * Visszatéríti a GraphTypeWindow ablak refernciáját
	 * @return az ablak referenciája
	 */
	public JPanel getGraphTypeWindow() {
		return graphTypeWindow;
	}
	
	
	/**
	 * Main metódus
	 * @param args parancssori paraméterek
	 */
	public static void main(String[] args) {
		GraphFrame frame = new GraphFrame();
		frame.setVisible(true);
	}

	/**
	 * Gombok megnyomásának kezelése
	 * @param e event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == save) {
			graphWindow.saveGraph();
		} else if(e.getSource() == load) {
			handleFileChooser();
		}
	}
}