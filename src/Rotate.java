import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Rotate {
	private final int JDIALOG_BORDE = 22;
	private JDialog ventana;
	private JDialog dialog;
	private JButton boton;
	private ArrayList<JRadioButton> radioButtons;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField rotacion;
	private JCheckBox rotarPintar;
	
	public Rotate() {
		this.setVentana(new JDialog());
		this.setDialog(new JDialog());
		this.setBoton(new JButton("Rotate"));
		this.setRadioButtons(new ArrayList<JRadioButton>());
		this.setRotacion(new JTextField());
		this.setRotarPintar(new JCheckBox("Rotate y Paint"));
		this.setRadioButtons(new ArrayList<JRadioButton>());
	}

	public void init() {
		this.getVentana().setTitle("Rotate Transform");
		
	}
	
	void mostrar(JDialog dialog) {
		this.getVentana().setLocation(dialog.getWidth() + (int)dialog.getLocation().getX() + 100, (int)dialog.getLocation().getY() + (int)dialog.getLocation().getY() / 4);
		this.getRadioButtons().get(0).setSelected(true);
		this.getRotacion().setText("");
		this.getRotarPintar().setSelected(false);
		this.getVentana().setVisible(true);	
	}
	
	public void flip(Imagen imagenActual, int tipo) {
		if(tipo == 2)
			flipZ(imagenActual);
		else
			flipVH(imagenActual, tipo);
	}
	
	public void flipVH(Imagen imagenActual, int tipo) {
		this.setDialog(new JDialog());
		BufferedImage imagen = imagenActual.getImagen();
		
		String cadena = "";
		int width = imagen.getWidth();
		int height = imagen.getHeight();
		BufferedImage newImg = new BufferedImage(width, height, imagen.getType());

		//Flip horizontal
		if(tipo == 0) {
			cadena = "Horizontally";
			for(int i = 0; i < width; i++) 
				for(int j = 0; j < height; j++) {
					int nuevoColor = imagenActual.getValorPixel(width-1-i, j);
					nuevoColor = (nuevoColor << 16) | (nuevoColor << 8) | nuevoColor;
					
					newImg.setRGB(i, j,nuevoColor); 
				}
		}
		else if(tipo == 1) {
			cadena = "Vertically";
			for(int i = 0; i < width; i++) 
				for(int j = 0; j < height; j++) {
					int nuevoColor = imagenActual.getValorPixel(i, height-1-j);
					nuevoColor = (nuevoColor << 16) | (nuevoColor << 8) | nuevoColor;
					
					newImg.setRGB(i, j,nuevoColor); 
				}
		}
		
		@SuppressWarnings("serial")
		JPanel panel = new JPanel() {
    			@Override
    			public void paintComponent(Graphics g) {
    				super.paintComponent(g);
    				g.drawImage(newImg, 0, 0, null);
    				imagenActual.getRecortar().pintarRectangulo(g);
    			}
		};
		
		this.getDialog().add(panel);
		this.getDialog().setIconImage(newImg);
		this.getDialog().setTitle(imagenActual.getContenedor().getTitle() + " with flip " + cadena);
		this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
		this.getDialog().setSize(newImg.getWidth(), newImg.getHeight() + JDIALOG_BORDE);
		this.getDialog().setLocationByPlatform(true);
		this.getDialog().setVisible(true);
		this.getDialog().setResizable(false);
	}
	
	public void flipZ(Imagen imagenActual) {
		this.setDialog(new JDialog());
		BufferedImage imagen = imagenActual.getImagen();

		int width = imagen.getWidth();
		int height = imagen.getHeight();
		BufferedImage newImg = new BufferedImage(height, width, imagen.getType());
		
		for(int i = 0; i < height; i++) 
			for(int j = 0; j < width; j++) {
				int nuevoColor = imagenActual.getValorPixel(j, i);
				nuevoColor = (nuevoColor << 16) | (nuevoColor << 8) | nuevoColor;
				
				newImg.setRGB(i, j,nuevoColor); 
			}
		
		@SuppressWarnings("serial")
		JPanel panel = new JPanel() {
    			@Override
    			public void paintComponent(Graphics g) {
    				super.paintComponent(g);
    				g.drawImage(newImg, 0, 0, null);
    				imagenActual.getRecortar().pintarRectangulo(g);
    			}
		};
		
		this.getDialog().add(panel);
		this.getDialog().setIconImage(newImg);
		this.getDialog().setTitle(imagenActual.getContenedor().getTitle() + " with flip Z");
		this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
		this.getDialog().setSize(newImg.getWidth(), newImg.getHeight() + JDIALOG_BORDE);
		this.getDialog().setLocationByPlatform(true);
		this.getDialog().setVisible(true);
		this.getDialog().setResizable(false);
	}
	
	public JDialog getVentana() {
		return ventana;
	}

	public void setVentana(JDialog ventana) {
		this.ventana = ventana;
	}

	public JDialog getDialog() {
		return dialog;
	}

	public void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	public JButton getBoton() {
		return boton;
	}

	public void setBoton(JButton boton) {
		this.boton = boton;
	}

	public ArrayList<JRadioButton> getRadioButtons() {
		return radioButtons;
	}

	public void setRadioButtons(ArrayList<JRadioButton> radioButtons) {
		this.radioButtons = radioButtons;
	}

	public JTextField getRotacion() {
		return rotacion;
	}

	public void setRotacion(JTextField rotacion) {
		this.rotacion = rotacion;
	}

	public JCheckBox getRotarPintar() {
		return rotarPintar;
	}

	public void setRotarPintar(JCheckBox rotarPintar) {
		this.rotarPintar = rotarPintar;
	}

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}
}
