import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Scale {
	private final int JDIALOG_BORDE = 22;
	private JDialog ventana;
	private JDialog dialog;
	private JButton boton;
	private ArrayList<JRadioButton> radioButtons;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField ancho;
	private JTextField alto;
	
	public Scale() {
		this.setVentana(new JDialog());
		this.setDialog(new JDialog());
		this.setBoton(new JButton("Sacle now!"));
		this.setRadioButtons(new ArrayList<JRadioButton>());
		this.setAncho(new JTextField());
		this.setAlto(new JTextField());
	}
	
	public void init() {
		
		this.getVentana().setTitle("Scale Transform");
		
		JPanel mainPanel = new JPanel();
		JPanel panel = new JPanel();
		JPanel panelRB = new JPanel();
		
		mainPanel.setLayout(new GridLayout(2, 1, 5, 5));
		panel.setLayout(new GridLayout(2, 3, 5, 5));
		panelRB.setLayout(new BoxLayout(panelRB, BoxLayout.Y_AXIS));
		
		panel.add(new JLabel("New width related to original:"));
		panel.add(getAncho());
		panel.add(new JLabel("%"));
		
		panel.add(new JLabel("New height related to original:"));
		panel.add(getAlto());
		panel.add(new JLabel("%"));

		getRadioButtons().add(new JRadioButton("VMP"));
		getRadioButtons().add(new JRadioButton("Bilineal"));
		
		getButtonGroup().add(getRadioButtons().get(0));
		getButtonGroup().add(getRadioButtons().get(1));
		
		panelRB.add(new JLabel("Select interpolation method:"));
		panelRB.add(getRadioButtons().get(0));
		panelRB.add(getRadioButtons().get(1));
		
		JPanel auxPanel = new JPanel();
		JPanel auxBoton = new JPanel();
		
		auxPanel.add(panel);
		auxPanel.add(panelRB);
		auxBoton.add(getBoton());
		
		mainPanel.add(auxPanel);
		mainPanel.add(auxBoton);
		
		this.getVentana().add(mainPanel);
		this.getVentana().pack();
	}
	
	void mostrar(JDialog dialog) {
		this.getVentana().setLocation(dialog.getWidth() + (int)dialog.getLocation().getX() + 100, (int)dialog.getLocation().getY() + (int)dialog.getLocation().getY() / 4);
		this.getRadioButtons().get(0).setSelected(true);
		this.getAlto().setText("");
		this.getAncho().setText("");
		this.getVentana().setVisible(true);	
	}
	
	public void buildImage(Imagen imagenActual){
		this.getVentana().dispose();
		this.setDialog(new JDialog());
		BufferedImage imagen = imagenActual.getImagen();
		int width = imagen.getWidth();
		int height = imagen.getHeight();
		
		double widthFactor = 1.0;
		double heightFactor = 1.0;
		
		try {
			widthFactor = new Double(this.getAncho().getText()) / 100;
			heightFactor = new Double(this.getAlto().getText()) / 100;
		} catch(Exception e) {}
		
		width = (int) (imagen.getWidth() * widthFactor);
		height = (int) (imagen.getHeight() * heightFactor);
		
		BufferedImage newImg = new BufferedImage(width, height, imagen.getType());
		
		//interpolación bilineal
		if(getRadioButtons().get(1).isSelected()) {
			for(int i = 0; i < width; i++) 
				for(int j = 0; j < height; j++) {
					int nuevoColor = 0;
					
					double x = i / widthFactor;
					double y = j / heightFactor;
					
					if(x > width-1)
						x = width-1;
					if(y > height-1)
						y = height-1;
					
					int A, B, C, D;
					A = imagenActual.getValorPixel((int) Math.floor(x),(int) Math.ceil(y));
					B = imagenActual.getValorPixel((int) Math.ceil(x),(int) Math.ceil(y));
					C = imagenActual.getValorPixel((int) Math.floor(x),(int) Math.floor(y));
					D = imagenActual.getValorPixel((int) Math.ceil(x),(int) Math.floor(y));
					
					double p,q;
					
					p = x - Math.floor(x);
					q = y -  Math.floor(y);
					
					nuevoColor = (int) (C + (D-C)*p + (A-C)*q + (B+C-A-D)*p*q);
					nuevoColor = (nuevoColor << 16) | (nuevoColor << 8) | nuevoColor;
					
					newImg.setRGB(i, j,nuevoColor); 
				}
		}
		//interpolación vecino más próximo
		else {
			for(int i = 0; i < width; i++) 
				for(int j = 0; j < height; j++) {
					int nuevoColor = 0;
					
					int x = (int) Math.round(i / widthFactor);
					int y = (int) Math.round(j / heightFactor);
					
					nuevoColor = imagenActual.getValorPixel(x, y);
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
		this.getDialog().setTitle(imagenActual.getContenedor().getTitle() + " de dimension " + width + "x" + height);
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
	public JTextField getAncho() {
		return ancho;
	}
	public void setAncho(JTextField ancho) {
		this.ancho = ancho;
	}
	public JTextField getAlto() {
		return alto;
	}
	public void setAlto(JTextField alto) {
		this.alto = alto;
	}
	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}
}
