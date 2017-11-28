import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class BrilloContraste {
	private JDialog ventana;
	private JLabel brilloEtiqueta;
    private JSlider brilloSlider;
    private JTextField brilloTextField;
    private JLabel contrasteEtiqueta;
    private JSlider contrasteSlider;
    private JTextField contrasteTextField;
    private JButton defaultBrillo;
    private JButton defaultContraste;
	private float brilloInicial;
	private float contrasteInicial;
	
	public BrilloContraste() {
		this.setVentana(new JDialog());
        this.setBrilloEtiqueta(new JLabel("Brillo"));
        this.setBrilloSlider(new JSlider(JSlider.VERTICAL, 0, 255, 100));
        this.setBrilloTextField(new JTextField(4));
        this.setDefaultBrillo(new JButton("Default"));
        this.setDefaultContraste(new JButton("Default"));
        this.setContrasteEtiqueta(new JLabel("Contraste"));
        this.setContrasteTextField(new JTextField(4));
        this.setContrasteSlider(new JSlider(JSlider.VERTICAL, 0, 255, 100));
        this.setBrilloInicial(0);
        this.setContrasteInicial(0);
	}
	
	void init() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		 		
		this.getVentana().setTitle("Brillo/Contraste");
		this.getVentana().setSize(200, 340);
		this.getVentana().setResizable(false);
		
		//ETIQUETAS
		this.getBrilloEtiqueta().setBounds(30, 10, 50, 20);
		panel.add(this.getBrilloEtiqueta());
		this.getContrasteEtiqueta().setBounds(105, 10, 70, 20);
		panel.add(this.getContrasteEtiqueta());
		
		//JSLIDERS
		this.getBrilloSlider().setBounds(37, 50, 22, 170);
		panel.add(this.getBrilloSlider());
		this.getContrasteSlider().setBounds(132, 50, 22, 170);
		panel.add(this.getContrasteSlider());
		
		//JTEXTFIELDS
		this.getBrilloTextField().setBounds(20, 235, 60, 20);
		this.getBrilloTextField().setText(this.getBrilloSlider().getValue() + "");
		panel.add(this.getBrilloTextField());
		this.getContrasteTextField().setBounds(117, 235, 60, 20);
		double valor = this.getContrasteSlider().getValue();
		valor /= 100;
		this.getContrasteTextField().setText(valor + "");
		panel.add(this.getContrasteTextField());
		
		//DEFAULTS BUTTONS
		this.getDefaultBrillo().setBounds(10, 275, 80, 25);
		panel.add(this.getDefaultBrillo());
		this.getDefaultContraste().setBounds(107, 275, 80, 25);
		panel.add(this.getDefaultContraste());
		
		this.getVentana().add(panel);
	}
	
	Imagen modificarBrilloContraste(Imagen imagenActual) {
		imagenActual.setImageIconActual(imagenActual.getImageIconInicial());
		BufferedImage imagen = imagenActual.getImagen(); 
		Imagen imagenResultado;
		  
		float A = (float) ((float) (this.getContrasteSlider().getValue()) / (imagenActual.getContraste()));
		float B = this.getBrilloSlider().getValue() - (float) (imagenActual.getBrillo() * A);
		ArrayList<Integer> lut = new ArrayList<Integer>();

		for(int i = 0; i < 256; i++) 
			lut.add(compruebarango(0, 255, (int) (Math.round(A * i + B))));
		    
		BufferedImage newImg = GraphicsEnvironment
				 .getLocalGraphicsEnvironment()
				 .getDefaultScreenDevice()
				 .getDefaultConfiguration()
				 .createCompatibleImage(imagen.getWidth(), imagen.getHeight(),
				 Transparency.OPAQUE);

		 for(int i = 0; i < imagen.getWidth(); i++) 
			 for(int j = 0; j < imagen.getHeight(); j++) {
		    	 	Color c1 = new Color(imagen.getRGB(i, j));
		    	 	int c_value = c1.getBlue();
		    	 	int ncol = lut.get(c_value);
		        
		    	 	newImg.setRGB(i, j, new Color(ncol, ncol, ncol).getRGB());
		      }
		 
		 imagenResultado = new Imagen(newImg);
		 //imagenResultado.setBrillo(this.getBrilloSlider().getValue());
		 //imagenResultado.setContraste(this.getContrasteSlider().getValue());
		 imagenResultado.setRutaImagen(imagenActual.getRutaImagen());
		 
		 return imagenResultado;
	}
	
	void mostrar(JDialog dialog) {
		this.getVentana().setLocation(dialog.getWidth() + (int)dialog.getLocation().getX() + 100, (int)dialog.getLocation().getY());
		this.getVentana().setVisible(true);
	}
	
	void actualizarPanel(Imagen imagen) {
		float brilloActual = imagen.getBrillo();
		float contrasteActual = imagen.getContraste();
		
		this.getBrilloSlider().setValue((int) brilloActual);
		this.getContrasteSlider().setValue((int) contrasteActual);
	}
	
	void actualizarTextBrillo() {
		this.getBrilloTextField().setText(this.getBrilloSlider().getValue() + "");
	}
	
	 void actualizarTextContraste() {
 		double valor = this.getContrasteSlider().getValue();
 		valor /= 100;
 		this.getContrasteTextField().setText(valor + "");
	 }
	 
	 void actualizarSliderBrillo() {
 		if(isNumeric(this.getBrilloTextField().getText()))
 			this.getBrilloSlider().setValue(new Integer(this.getBrilloTextField().getText()));
	 }
 
	 void actualizarSliderContraste() {
 		String numero = this.getContrasteTextField().getText().replace(".", "") + "0";
 		
 		if(isNumeric(numero))
 			this.getContrasteSlider().setValue(Integer.parseInt(numero));
	 }
	 
	 private boolean isNumeric(String cadena){
 		try {
 			Integer.parseInt(cadena);
 			return true;
 		} catch (NumberFormatException nfe){
 			return false;
 		}
	 }
	 
	 private Integer compruebarango(int min, int max, int value) {
		 int value_check = Math.abs(value);
	      if (value < min) 
	    	  	return min;
	      else if (value_check > max) 
	        return max;
	        
	        return value_check;
	}

	public JDialog getVentana() {
		return ventana;
	}

	public void setVentana(JDialog ventana) {
		this.ventana = ventana;
	}

	public JLabel getBrilloEtiqueta() {
		return brilloEtiqueta;
	}

	public void setBrilloEtiqueta(JLabel brilloEtiqueta) {
		this.brilloEtiqueta = brilloEtiqueta;
	}

	public JSlider getBrilloSlider() {
		return brilloSlider;
	}

	public void setBrilloSlider(JSlider brilloSlider) {
		this.brilloSlider = brilloSlider;
	}

	public JTextField getBrilloTextField() {
		return brilloTextField;
	}

	public void setBrilloTextField(JTextField brilloTextField) {
		this.brilloTextField = brilloTextField;
	}

	public JLabel getContrasteEtiqueta() {
		return contrasteEtiqueta;
	}

	public void setContrasteEtiqueta(JLabel contrasteEtiqueta) {
		this.contrasteEtiqueta = contrasteEtiqueta;
	}

	public JSlider getContrasteSlider() {
		return contrasteSlider;
	}

	public void setContrasteSlider(JSlider contrasteSlider) {
		this.contrasteSlider = contrasteSlider;
	}

	public JTextField getContrasteTextField() {
		return contrasteTextField;
	}

	public void setContrasteTextField(JTextField contrasteTextField) {
		this.contrasteTextField = contrasteTextField;
	}

	public JButton getDefaultBrillo() {
		return defaultBrillo;
	}

	public void setDefaultBrillo(JButton defaultBrillo) {
		this.defaultBrillo = defaultBrillo;
	}

	public JButton getDefaultContraste() {
		return defaultContraste;
	}

	public void setDefaultContraste(JButton defaultContraste) {
		this.defaultContraste = defaultContraste;
	}

	public float getBrilloInicial() {
		return brilloInicial;
	}

	public void setBrilloInicial(float brilloInicial) {
		this.brilloInicial = brilloInicial;
	}

	public float getContrasteInicial() {
		return contrasteInicial;
	}

	public void setContrasteInicial(float contrasteInicial) {
		this.contrasteInicial = contrasteInicial;
	}
}
