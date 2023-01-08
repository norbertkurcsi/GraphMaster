package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * A töröl gomb állapotait, illetve megnyomás esetén a csomópont törlését kezdeményező gomb.
 * 
 * @author Kurcsi Norbert
 *
 */
public class DeleteButton extends JButton implements ActionListener{
	
	private static final long serialVersionUID = -312927781225590471L;
	
	// Editer, ahol a gráfot tudjuk szerkeszteni, és ahonnan a csomópont is törlésre kerül
	private GraphEditer editer;
	
	/**
	 * Konstruktor a fomb létrehozásához, illetve annak inicializálásához
	 * 
	 * @param editer a szerkesztő, ahol a gráfot, amelynek csomópontjának törlését a gomb inicializálja
	 */
	public DeleteButton(GraphEditer editer) {
		this.editer = editer;
		
		this.setText("Törlés");
		this.setActionCommand("delete");
		this.setEnabled(false);
		
		this.addActionListener(this);
	}
	
	/**
	 * A gomb megnyomása esetén, a csomópont törlését kezdeményezi
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("delete")) {
			editer.removeNode();
		}
	}
}