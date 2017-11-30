import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class Sampling {
	private JDialog dialog;
	private JPanel panel;
	private JLabel label;
	private ArrayList<JRadioButton> radioButtons;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton button;
	
	public Sampling(){
		this.setDialog(new JDialog());
		this.setPanel(new JPanel());
		this.setLabel(new JLabel());
		this.setRadioButtons(new ArrayList<JRadioButton>());
		this.setButton(new JButton());
	}

	void init() {
		this.getPanel().setLayout(null);
		
		this.getDialog().setTitle("Sampling");
		this.getDialog().setSize(220, 200);
		
		this.getLabel().setText("Eliga el tamaño del mustreo:");
		this.getLabel().setBounds(80, 40, 50, 20);
	
		for(int i=2; i<5; i++){
			JRadioButton radioButton = new JRadioButton(i + "+" + i);
			getRadioButtons().add(radioButton);
			getButtonGroup().add(radioButton);
			
		}
	
		this.getPanel().add(this.getLabel());
		for(int i=0; i<getRadioButtons().size();i++)
			this.getPanel().add(this.getRadioButtons().get(i));
		
		this.getButton().setText("Sample");
		this.getPanel().add(this.getButton());
		
		this.getDialog().add(this.getPanel());	
	}
	
	void mostrar(JDialog dialog) {
		this.getDialog().setLocation(dialog.getWidth() + (int)dialog.getLocation().getX() + 100, (int)dialog.getLocation().getY() + (int)dialog.getLocation().getY() / 4);
		this.getDialog().setVisible(true);	
	}
	
	public void buildImage(Imagen imagenActual){
		String text = "";
		for(int i=0; i<getRadioButtons().size();i++)
			if(getRadioButtons().get(i).isSelected())
				text = getRadioButtons().get(i).getText();
		
		if(!text.isEmpty()){
			getDialog().removeAll();
			BufferedImage imagen = imagenActual.getImagen();
			
			@SuppressWarnings("serial")
			JPanel panel = new JPanel() {
	    			@Override
	    			public void paintComponent(Graphics g) {
	    				super.paintComponent(g); 
	    				g.drawImage(imagen, 0, 0, null);
	    			}
			};
			
			this.getDialog().add(panel);
			this.getDialog().setIconImage(imagen);
			this.getDialog().setTitle(imagenActual.getContenedor().getTitle() + " muestreada a " + text);
			this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
			this.getDialog().setSize(imagen.getWidth(), imagen.getHeight() + 45);
			this.getDialog().setLocationByPlatform(true);
			this.getDialog().setVisible(true);
			this.getDialog().setResizable(false);
		}
	}
	
	/**
	 * @return the dialog
	 */
	public JDialog getDialog() {
		return dialog;
	}

	/**
	 * @param dialog the dialog to set
	 */
	public void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	/**
	 * @return the textField
	 */
	public JLabel getLabel() {
		return label;
	}

	/**
	 * @param textField the textField to set
	 */
	public void setLabel(JLabel label) {
		this.label = label;
	}

	/**
	 * @return the radioButtons
	 */
	public ArrayList<JRadioButton> getRadioButtons() {
		return radioButtons;
	}

	/**
	 * @param radioButtons the radioButtons to set
	 */
	public void setRadioButtons(ArrayList<JRadioButton> radioButtons) {
		this.radioButtons = radioButtons;
	}

	/**
	 * @return the button
	 */
	public JButton getButton() {
		return button;
	}

	/**
	 * @param button the button to set
	 */
	public void setButton(JButton button) {
		this.button = button;
	}

	/**
	 * @return the buttonGroup
	 */
	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	/**
	 * @return the panel
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * @param panel the panel to set
	 */
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
}
