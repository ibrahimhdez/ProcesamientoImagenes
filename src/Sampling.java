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
	private final int BORDE_JDIALOG = 22;
	private JDialog ventana;
	private JDialog dialog;
	private JPanel panel;
	private JLabel label;
	private ArrayList<JRadioButton> radioButtons;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JButton button;
	
	public Sampling(){
		this.setVentana(new JDialog());
		this.setDialog(new JDialog());
		this.setPanel(new JPanel());
		this.setLabel(new JLabel());
		this.setRadioButtons(new ArrayList<JRadioButton>());
		this.setButton(new JButton());
	}

	void init() {
		this.getVentana().setTitle("Sampling");
		this.getVentana().setSize(220, 110);
		
		this.getLabel().setText("Choose the sampling size:");
	
		for(int i = 2; i < 5; i++){
			JRadioButton radioButton = new JRadioButton(i + "x" + i);
			
			getRadioButtons().add(radioButton);
			getButtonGroup().add(radioButton);
		}
	
		this.getPanel().add(this.getLabel());
		for(int i = 0; i < getRadioButtons().size(); i++)
			this.getPanel().add(this.getRadioButtons().get(i));
		
		this.getButton().setText("Sample");
		this.getPanel().add(this.getButton());
		
		this.getVentana().add(this.getPanel());	
	}
	
	void mostrar(JDialog dialog) {
		this.getVentana().setLocation(dialog.getWidth() + (int)dialog.getLocation().getX() + 100, (int)dialog.getLocation().getY() + (int)dialog.getLocation().getY() / 4);
		this.getVentana().setVisible(true);	
	}
	
	public void buildImage(Imagen imagenActual){
		this.setDialog(new JDialog());
		String text = "";
		for(int i=0; i<getRadioButtons().size();i++)
			if(getRadioButtons().get(i).isSelected())
				text = getRadioButtons().get(i).getText();
		
		if(!text.isEmpty()){
			int numPixel = new Integer(text.substring(0, 1));
			BufferedImage imagen = imagenActual.getImagen();
			ArrayList<Integer> pixeles = new ArrayList<>();
			
			for(int i = 0; i < imagen.getWidth(); i++)
				for(int j = 0; j < imagen.getHeight(); j++) {
					int valorPixel = imagenActual.getValorPixel(i, j);
					
					pixeles.add(valorPixel);
					
					if((pixeles.size() == numPixel) || (j == (imagen.getHeight() - 1))){
						int nuevoValor = convertirPixeles(pixeles);
						int nuevoColor = (nuevoValor << 16) | (nuevoValor << 8) | nuevoValor;

						for(int x = j - pixeles.size() + 1; x < j + 1; x++) 
							imagen.setRGB(i, x, nuevoColor);
						
						pixeles.clear();
					}
				}
			
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
			this.getDialog().setSize(imagen.getWidth(), imagen.getHeight() + BORDE_JDIALOG);
			this.getDialog().setVisible(true);
			this.getDialog().setResizable(false); 
		}
	}
	
	private int convertirPixeles(ArrayList<Integer> pixeles) {
		int sum = 0;
		
		for(int i = 0; i < pixeles.size(); i++) 
			sum += pixeles.get(i);
			
		return sum / pixeles.size();
	}
	
	public JDialog getVentana() {
		return ventana;
	}

	public void setVentana(JDialog ventana) {
		this.ventana = ventana;
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
