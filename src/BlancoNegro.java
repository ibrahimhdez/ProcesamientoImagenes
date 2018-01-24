import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class BlancoNegro {
	private static final int JDIALOG_BORDE = 22;
	private JDialog dialog;
	
	public BlancoNegro() {
		this.setDialog(new JDialog());
	}
	
	void init() {
		this.setDialog(new JDialog());
	}
	
	void convertir(Imagen imagenActual){
		BufferedImage imagen = imagenActual.getImagen();
		this.setDialog(new JDialog());
		
		Graphics g = imagen.getGraphics();
		g.drawImage(imagenActual.imageActual(), 0, 0, null);
		
		for(int i = 0; i < imagen.getWidth(); i++)
			for(int j = 0; j < imagen.getHeight(); j++) {
				int valorPixel = imagenActual.getValorPixel(i, j);
				int colorSRGB = (valorPixel << 16) | (valorPixel << 8) | valorPixel;
		
				imagen.setRGB(i, j,colorSRGB);
         }

		@SuppressWarnings("serial")
		JPanel panel = new JPanel() {
    			@Override
    			public void paintComponent(Graphics g) {
    				super.paintComponent(g); 
    				g.drawImage(imagen, 0, 0, null);
    				imagenActual.getRecortar().pintarRectangulo(g);
    			}
		};
		
		this.getDialog().add(panel);
		this.getDialog().setIconImage(imagen);
		this.getDialog().setTitle(imagenActual.getContenedor().getTitle() + " blanco y negro");
		this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
		this.getDialog().setSize(imagen.getWidth(), imagen.getHeight() + JDIALOG_BORDE);
		this.getDialog().setVisible(true);
		this.getDialog().setResizable(false);
	}
	
	Imagen get(Imagen imagenActual) {
		BufferedImage imagen = imagenActual.getImagen();
		JDialog dialog = new JDialog();
		
		Graphics g = imagen.getGraphics();
		g.drawImage(imagenActual.imageActual(), 0, 0, null);
		
		for(int i = 0; i < imagen.getWidth(); i++)
			for(int j = 0; j < imagen.getHeight(); j++) {
				Color color = new Color(imagen.getRGB(i, j));
				int mediaPixel = (int)((color.getRed() + color.getGreen() + color.getBlue()) / 3);
				int colorSRGB = (mediaPixel << 16) | (mediaPixel << 8) | mediaPixel;
		
				imagen.setRGB(i, j,colorSRGB);
         }
		
		@SuppressWarnings("serial")
		JPanel panel = new JPanel() {
    			@Override
    			public void paintComponent(Graphics g) {
    				super.paintComponent(g); 
    				g.drawImage(imagen, 0, 0, null);
    				imagenActual.getRecortar().pintarRectangulo(g);
    			}
		};
		
		dialog.add(panel);
		dialog.setIconImage(imagen);
		dialog.setTitle(imagenActual.getContenedor().getTitle() + " blanco y negro");
		dialog.setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
		dialog.setSize(imagen.getWidth(), imagen.getHeight() + JDIALOG_BORDE);
		dialog.setLocationByPlatform(true);
		dialog.setVisible(true);
		dialog.setResizable(false);
		
		return new Imagen(dialog);
	}

	public JDialog getDialog() {
		return dialog;
	}

	public void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}
}
