package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * Az algoritmusok futtatásáért felelős panel.
 * 
 * @author Kurcsi Norbert
 *
 */
public class RepresentationPanel extends JPanel implements ActionListener{
	
	private static final long serialVersionUID = 4365358990262452425L;
	private JComboBox<String> comboBox;
	private JButton button;
	private GraphWindow window;
	private String repTitle;
	private TableFrame frame;

	/**
	 * Beállítj aktívra az algoritmus elindításához szükséges gombot
	 */
	public void setButtonActive() {
		button.setEnabled(true);
	}
	
	/**
	 * Konstruktor amely, azt az ablakot megkapva param;terben, ahol az algoritmus futtatása megjelenik,
	 * létrehozza a JComboBox-ot illetve a gombokat, amelyek kellenek az algoritmusok futtatásához
	 * @param w ablak ahol megjelenik
	 */
	public RepresentationPanel(GraphWindow w){
		setBackground(Color.gray);
		window=w;
		
		String[] options = new String[3];
		options[0] = "Pont-pont mátrix";
		options[1] = "Éllista";
		options[2] = "Pont-él mátrix";
		
		comboBox = new JComboBox<String>(options);
		button = new JButton("Megjelenít");
		button.setActionCommand("button");
		button.addActionListener(this);
		
		this.add(comboBox);
		this.add(button);
	}
	
	/**
	 * Kezeli a JComboBox/ban kiválasztott opciók esetén végrehajtandó utasításokat
	 */
	private void handleComboBoxOptions() {
		switch((String) comboBox.getSelectedItem()) {
		case "Pont-pont mátrix":
			repTitle = "Pont-Pont Mátrix";
			frame.initForOption(repTitle,TableFrame.Options.POINTPOINT, window.getLinks().getMatrix());
			break;
		case "Éllista":
			repTitle = "Éllista";
			frame.initForOption(repTitle,TableFrame.Options.EDGELIST, window.getLinks().converToEdgeList());
			break;
		case "Pont-él mátrix":
			repTitle = "Pont-Él Mátrix";
			frame.initForOption(repTitle,TableFrame.Options.POINTEDGE, window.getLinks().converToPointEdgeMatrix());
			break;
		}
	}

	
	/**
	 * Ha egy gomb megnyomásra kerül a panelből. akkor meghívja a megfelelő metódusokat
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("button")) {
			frame = new TableFrame(repTitle,window.getNodes(),this);
			handleComboBoxOptions();
			frame.setVisible(true);
			button.setEnabled(false);
		}
		
	}
}