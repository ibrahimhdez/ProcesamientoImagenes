import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class Gamma {
	private JDialog dialog;
	private JPanel panel;
	private JTextField textField;
	private JSlider slider;
	
	public Gamma() {
		this.setDialog(new JDialog());
		this.setPanel(new JPanel());
		this.setTextField(new JTextField());
		this.setSlider(new JSlider(JSlider.HORIZONTAL, 0, 500, 100));
	}
	
	void init() {
		this.getPanel().setLayout(null);
		
		this.getDialog().setTitle("Gamma");
		this.getDialog().setSize(220, 100);
		
		actualizarTextField();
	
		this.getSlider().setBounds(25, 8, 170, 22);
		this.getTextField().setBounds(80, 40, 50, 20);
		
		this.getPanel().add(this.getSlider());
		this.getPanel().add(this.getTextField());
		this.getDialog().add(this.getPanel());	
	}
	
	void mostrar(JDialog dialog) {
		actualizarTextField();
		this.getDialog().setLocation(dialog.getWidth() + (int)dialog.getLocation().getX() + 100, (int)dialog.getLocation().getY() + (int)dialog.getLocation().getY() / 4);
		this.getDialog().setVisible(true);	
	}
	
	void actualizarTextField() {
		double valor = this.getSlider().getValue();
		valor /= 100;
		this.getTextField().setText(valor + "");;
	}
	
	Imagen  modificar(Imagen imagenActual) {
		BufferedImage imagen = imagenActual.getImagen();
		double a = 0.0, b = 0.0, newPixel = 0.0;
		double gamma = this.getSlider().getValue() / 100.0;
		
		BufferedImage newImg = GraphicsEnvironment
				 .getLocalGraphicsEnvironment()
				 .getDefaultScreenDevice()
				 .getDefaultConfiguration()
				 .createCompatibleImage(imagen.getWidth(), imagen.getHeight(),
				 Transparency.OPAQUE);
		
		 for(int i = 0; i < imagen.getWidth(); i++) 
			 for(int j = 0; j < imagen.getHeight(); j++) {
				 Color color = new Color(imagen.getRGB(i, j));
				 int pixel = (int)((color.getRed() + color.getGreen() + color.getBlue()) / 3);
				 
				 a = pixel / 255.0;
				 b = Math.pow(a, gamma);
				 newPixel = b * 255.0 * 100;
				
				 newImg.setRGB(i, j, new Color((int)newPixel / 100, (int)newPixel / 100, (int)newPixel / 100).getRGB());
			 }
			
		 return new Imagen(newImg);
	}

	public JDialog getDialog() {
		return dialog;
	}

	public void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JSlider getSlider() {
		return slider;
	}

	public void setSlider(JSlider slider) {
		this.slider = slider;
	}
}
