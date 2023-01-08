package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A kezdőablak kirajzolását és eseményeinek kezelését megvalósító osztály
 * 
 * @author Kurcsi Norbert
 *
 */
public class StartingWindow extends JPanel implements ActionListener{

	private static final long serialVersionUID = 5843004590413065023L;
	
	private JButton createGraphButton;
	private JButton loadGraphButton;
	private JButton deleteButton;
	private GraphFrame frame;
	
	private enum Actions{
		CREATE,
		LOAD,
		DELETE
	}
	
	/**
	 * Létrehozza a címpanelt, majd hozzáadja az ablakhoz
	 */
	private void createTitlePanel() {
		JLabel title = new JLabel("Graph Master");
		title.setFont(new Font("MV Boli", Font.BOLD, 100));
		title.setForeground(Color.white);
		title.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.setBackground(new Color(0,0,0,0));
		titlePanel.add(title);
		titlePanel.setPreferredSize(new Dimension(0,200));

		this.add(titlePanel,BorderLayout.NORTH);
	}
	
	/**
	 * Létrehozza a menu panelt és hozzáadja az ablakhoz 
	 */
	private void createMenuPanel() {
		createGraphButton = new JButton("Gráf létrehozása");
		loadGraphButton = new JButton("Gráf betöltése");
		deleteButton = new JButton("Gráf törlése");
		createGraphButton.setPreferredSize(new Dimension(200,80));
		loadGraphButton.setPreferredSize(new Dimension(200,80));
		deleteButton.setPreferredSize(new Dimension(200,80));
		createGraphButton.setActionCommand(Actions.CREATE.name());
		loadGraphButton.setActionCommand(Actions.LOAD.name());
		deleteButton.setActionCommand(Actions.DELETE.name());

		
		JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,100,10));
		menuPanel.setBackground(new Color(0,0,0,0));
		menuPanel.setPreferredSize(new Dimension(0,200));
		
		menuPanel.add(createGraphButton);
		menuPanel.add(loadGraphButton);
		menuPanel.add(deleteButton);
		
		this.add(menuPanel,BorderLayout.SOUTH);

	}
	
	/**
	 * A kezdőoldal konstruktora, amely egy ablak keretet megadva, létrehozza a kezdő ablakot,
	 * illetve meghívja a komponenseinek az inicializáló függvlnyeit
	 * @param frame keret, ahol kirajzolódik az ablak
	 */
	public StartingWindow(GraphFrame frame) {
		this.frame = frame;
		this.setLayout(new BorderLayout());
		
		this.createTitlePanel();
		this.createMenuPanel();
		
//		ActionListener listener = new ActionListener(this);
		
		createGraphButton.addActionListener(this);
		
		loadGraphButton.addActionListener(this);
		deleteButton.addActionListener(this);
	}

	/**
	 * Kirajzolja az ablak hátterét
	 * @param g grafikus felület, ahova a háttér kirajzolódik
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image hatter = null;
		if (hatter == null) {
			hatter = (new ImageIcon("img/background.jpg")).getImage();
		}
		// Kirajzoljuk a háttérképet.
		g.drawImage(hatter, 0, 0, frame.getWidth(), frame.getHeight(), null);
	}

	/**
	 * A kezdőoldal által tartalmazott gombok megnyomássa esetén kezeli ezeknek meghívandó metódusait.
	 * @oaram e event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == Actions.CREATE.name()) {
			frame.changeWindow(this,frame.getGraphTypeWindow());
		} else if(e.getActionCommand() == Actions.LOAD.name()) {
			frame.changeWindow(this,frame.getGraphWindowWithLoad());
		} else if(e.getActionCommand() == Actions.DELETE.name()) {
			frame.handleDelteGraph();
		}
	}
}
