import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Cuantizacion {
	private final int JDIALOG_BORDE = 22;
	private JDialog ventana;
	private JDialog dialog;
	private JLabel etiqueta;
	private JTextField textField;
	private JButton boton;
	
	public Cuantizacion() {
		this.setVentana(new JDialog());
		this.setDialog(new JDialog());
		this.setEtiqueta(new JLabel());
		this.setTextField(new JTextField());
		this.setBoton(new JButton());
	}
	
	void init() {
		JPanel panel = new JPanel();
		JPanel auxPanel = new JPanel();
	
		panel.setLayout(new GridLayout(3, 1));
		this.setEtiqueta(new JLabel("Choose a number between 7 and 1:"));
		this.setTextField(new JTextField(3));
		this.getBoton().setText("Accept");
		
		auxPanel.add(this.getEtiqueta());
		panel.add(auxPanel);
		auxPanel = new JPanel();
		auxPanel.add(this.getTextField());
		panel.add(auxPanel);
		auxPanel = new JPanel();
		auxPanel.add(this.getBoton());
		panel.add(auxPanel);
		
		this.getVentana().add(panel);
		this.getVentana().pack();
	}
	
	void mostrar(Point p, Rectangle r) {
		this.getTextField().setText("8");
		this.getVentana().setLocation((int)(p.getX() + r.getWidth()) + 20, (int)(p.getY() + (r.getHeight() / 2) - this.getVentana().getHeight() / 2));
		this.getVentana().setVisible(true);
	}
	
	void generar(Imagen imagenActual) {
		if(comprobarTextField()) {
			this.setDialog(new JDialog());
			BufferedImage imagen = imagenActual.getImagen();
			BufferedImage newImg = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());
			int valor = new Integer(this.getTextField().getText());
			valor = (int) Math.pow(2, valor);
			
			for(int i = 0; i < imagen.getWidth(); i++) 
				for(int j = 0; j < imagen.getHeight(); j++) {
					int valorPixel = imagenActual.getValorPixel(i, j);
					int nuevoColor = 0;
					
					valorPixel = (int) (valorPixel / (255 / valor)) * ((255 / valor) );
					nuevoColor = (valorPixel << 16) | (valorPixel << 8) | valorPixel;
					
					newImg.setRGB(i, j, nuevoColor); 
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
			this.getDialog().setTitle(imagenActual.getContenedor().getTitle() + " " + (int)(Math.log(valor) / Math.log(2)) + "bits");
			this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
			this.getDialog().setSize(newImg.getWidth(), newImg.getHeight() + JDIALOG_BORDE);
			this.getDialog().setLocationByPlatform(true);
			this.getDialog().setVisible(true);
			this.getDialog().setResizable(false);
		}	
	}
	
	Boolean comprobarTextField() {
		int valor = 9999;
		
		try {
			valor = new Integer(this.getTextField().getText());
		} catch(Exception e) {}
		
		return ((valor >= 1) && (valor <= 8));
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

	public JLabel getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(JLabel etiqueta) {
		this.etiqueta = etiqueta;
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JButton getBoton() {
		return boton;
	}

	public void setBoton(JButton boton) {
		this.boton = boton;
	}
}
