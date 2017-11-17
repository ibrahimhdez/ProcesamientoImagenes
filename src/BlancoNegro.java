import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class BlancoNegro {
	private JDialog dialog;
	
	public BlancoNegro() {
		this.setDialog(new JDialog());
	}
	
	void init() {
		this.setDialog(new JDialog());
	}
	
	void convertir(Imagen imagenActual){
		BufferedImage imagen = imagenActual.getImagen();
		
		Graphics g = imagen.getGraphics();
		g.drawImage(imagenActual.imageActual(), 0, 0, null);
		
		for(int i = 0; i < imagen.getWidth(); i++)
			for(int j = 0; j < imagen.getHeight(); j++) {
				Color color = new Color(imagen.getRGB(i, j));
				int mediaPixel = (int)((color.getRed() + color.getGreen() + color.getBlue()) / 3);
				int colorSRGB = (mediaPixel << 16) | (mediaPixel << 8) | mediaPixel;
		
				imagen.setRGB(i, j,colorSRGB);
         }

		this.getDialog().add(new JLabel(new ImageIcon(imagen)));
		this.getDialog().setIconImage(imagen);
		this.getDialog().setTitle(imagenActual.getContenedor().getTitle() + " blanco y negro");
		this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
		this.getDialog().pack();
		this.getDialog().setLocationByPlatform(true);
		this.getDialog().setVisible(true);
		this.getDialog().setResizable(false);
	}

	public JDialog getDialog() {
		return dialog;
	}

	public void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}
}
