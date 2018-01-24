import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class Rotate {
	private final int JDIALOG_BORDE = 22;
	private JDialog ventana;
	private JDialog dialog;
	private JButton boton;
	private JButton botonOk;
	private JLabel etiqueta;
	private ArrayList<JRadioButton> radioButtons;
	private ButtonGroup buttonGroup;
	private JTextField rotacion;
	private JCheckBox rotarPintar;
	public  double[][] MatrizTransformacionD = new double[3][3];
	
	public Rotate() {
		this.setVentana(new JDialog());
		this.setDialog(new JDialog());
		this.setBoton(new JButton("Rotate"));
		this.setBotonOk(new JButton("Ok (manual)"));
		this.setEtiqueta(new JLabel("Choose the angle of rotation:"));
		this.setRadioButtons(new ArrayList<JRadioButton>());
		this.setButtonGroup(new ButtonGroup());
		this.setRotacion(new JTextField(3));
		this.setRotarPintar(new JCheckBox("Rotate y Paint"));
		this.setRadioButtons(new ArrayList<JRadioButton>());
	}

	public void init() {
		JPanel panel = new JPanel();
		
		this.getVentana().setSize(230, 145);
		this.getVentana().setTitle("Rotate Transform");
		
		panel.add(this.getEtiqueta());
		
		for(int i = 1; i < 4; i++) {
			JRadioButton radioButton = new JRadioButton((i * 90) + "º");
			getRadioButtons().add(radioButton);
		}
			
		for(int i = 0; i < getRadioButtons().size(); i++)
			this.getButtonGroup().add(this.getRadioButtons().get(i));
		
		Enumeration<AbstractButton> enumeration = this.getButtonGroup().getElements();     
        while (enumeration.hasMoreElements()){
            JRadioButton radioButton = (JRadioButton) enumeration.nextElement();
            panel.add(radioButton);
        }
		
		panel.add(this.getRotacion());
		panel.add(this.getBotonOk());
		panel.add(this.getBoton());
		this.getVentana().add(panel);
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
					
					newImg.setRGB(i, j, nuevoColor); 
				}
		}
		else if(tipo == 1) {
			cadena = "Vertically";
			for(int i = 0; i < width; i++) 
				for(int j = 0; j < height; j++) {
					int nuevoColor = imagenActual.getValorPixel(i, height-1-j);
					nuevoColor = (nuevoColor << 16) | (nuevoColor << 8) | nuevoColor;
					
					newImg.setRGB(i, j, nuevoColor); 
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
		this.getDialog().setTitle(imagenActual.getContenedor().getTitle() + " with flip Z");
		this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
		this.getDialog().setSize(newImg.getWidth(), newImg.getHeight() + JDIALOG_BORDE);
		this.getDialog().setLocationByPlatform(true);
		this.getDialog().setVisible(true);
		this.getDialog().setResizable(false);
	}
	
	public void flipAngle(Imagen imagenActual) {
		this.setDialog(new JDialog());
		BufferedImage imagen = imagenActual.getImagen();
		BufferedImage newImg;
		
		String textAngle = "";
		for(int i=0; i<getRadioButtons().size();i++)
			if(getRadioButtons().get(i).isSelected()) 
				textAngle = getRadioButtons().get(i).getText();

		int width = imagen.getWidth();
		int height = imagen.getHeight();
		
		if(!textAngle.equals("180º")) 
			newImg = new BufferedImage(height, width, imagen.getType());
		
		else
			newImg = new BufferedImage(width, height, imagen.getType());
		
		for(int i = 0; i < height; i++) 
			for(int j = 0; j < width; j++) {
				int nuevoColor = imagenActual.getValorPixel(j, i);
				nuevoColor = (nuevoColor << 16) | (nuevoColor << 8) | nuevoColor;
				
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
		this.getDialog().setTitle(imagenActual.getContenedor().getTitle() + " with flip " + textAngle);
		this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
		this.getDialog().setSize(newImg.getWidth(), newImg.getHeight() + JDIALOG_BORDE);
		this.getDialog().setLocationByPlatform(true);
		this.getDialog().setVisible(true);
		this.getDialog().setResizable(false);
	}
	
	public void turnDirect(Imagen imagenActual, Boolean anguloManual) {
		this.setDialog(new JDialog());
	    Color white = Color.WHITE;
	    BufferedImage imagen = imagenActual.getImagen();
	    String textAngle = "";
	    Integer grados;
	    
	    	if(!anguloManual) {
	    		for(int i=0; i<getRadioButtons().size();i++)
				if(getRadioButtons().get(i).isSelected()) 
					textAngle = getRadioButtons().get(i).getText();
			textAngle = textAngle.substring(0, textAngle.length() - 1);
			grados = new Integer(textAngle);
		
			if(grados == 90)
				grados = 270;
			else if(grados == 270)
				grados = 90;
	    }
	    	
	    	else 
	    		grados = new Integer(this.getRotacion().getText());
		
	    int[][] A, B, C, D;
	    int x, y;
	    int[][] pto = new int[2][1];
	    int[][] ori = new int[2][1];
	    int[][] max = new int[2][1];
	   
	    pto[0][0] = imagen.getWidth();
	    pto[1][0] = 0;
	    B = this.mapeo_directo(pto, grados);
	    pto[0][0] = 0;
	    pto[1][0] = 0;
	    A = this.mapeo_directo(pto, grados);
	    pto[0][0] = 0;
	    pto[1][0] = imagen.getHeight();
	    C = this.mapeo_directo(pto, grados);

	    pto[0][0] = imagen.getWidth() ;
	    pto[1][0] = imagen.getHeight();
	    D = this.mapeo_directo(pto, grados);
	    ori[0][0] = minimo(minimo(B[0][0], C[0][0]), D[0][0]);
	    ori[1][0] = minimo(minimo(B[1][0], C[1][0]), D[1][0]);
	    max[0][0] = maximo(maximo(maximo(B[0][0], C[0][0]),D[0][0]),A[0][0]);
	    max[1][0] = maximo(maximo(maximo(B[1][0], C[1][0]),D[1][0]),A[1][0]);
	    int ancho = Math.abs(max[1][0] - minimo(ori[1][0],A[1][0]))+2;
	    int alto = Math.abs(max[0][0] - minimo(ori[0][0],A[0][0]))+2; 
	    
	    ori[0][0] = (ori[0][0] < 0) ? (-ori[0][0]) : 0;
	    ori[1][0] = (ori[1][0] < 0) ? (-ori[1][0]) : 0;
	    BufferedImage newImg = GraphicsEnvironment.getLocalGraphicsEnvironment()
	        .getDefaultScreenDevice().getDefaultConfiguration()
	        .createCompatibleImage(alto, ancho, Transparency.OPAQUE);

	    // calculamos los puntos de las esquinas de nuestra imagen para generar el
	    // paralelogramo que la cirscuncribe
	    for (int i = 0; i < newImg.getWidth(); i++) {
	      for (int j = 0; j < newImg.getHeight(); j++) {
	        newImg.setRGB(i, j, white.getRGB());
	      }
	    }
	  
	    for (int i = 0; i < imagen.getWidth(); i++) {
	      for (int j = 0; j < imagen.getHeight(); j++) {
	        pto[0][0] = i;
	        pto[1][0] = j;
	        pto = this.mapeo_directo(pto, grados);
	        x = pto[0][0] + ori[0][0];
	        y = pto[1][0] + ori[1][0];

	        newImg.setRGB(x, y, imagen.getRGB(i, j));
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
		String textGrados = "";
		if(grados == 90)
			textGrados = "270";
		else if(grados == 270)
			textGrados = "90";
		else
			textGrados = grados + "";
		this.getDialog().setTitle(imagenActual.getContenedor().getTitle() + " turn " + textGrados + "º");
		this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
		this.getDialog().setSize(newImg.getWidth(), newImg.getHeight() + JDIALOG_BORDE);
		this.getDialog().setLocationByPlatform(true);
		this.getDialog().setVisible(true);
		this.getDialog().setResizable(false);
	  }
	
	private int[][] mapeo_directo(int[][] pto, int grados) {
	    double[][] rota = new double[2][2];
	    double grado = Math.toRadians(grados);
	    rota[0][0] = Math.cos(grado);
	    rota[0][1] = -Math.sin(grado);
	    rota[1][0] = Math.sin(grado);
	    rota[1][1] = Math.cos(grado);

	    return mult_mat(rota, pto);
	}
	
	private int[][] mult_mat(double[][] A, int[][] B) {
	    int[][] pto_n = new int[2][1];
	    int suma;
	    for (int i = 0; i < A.length; i++) {
	      suma = 0;
	      for (int j = 0; j < B.length; j++) {
	        suma += (int) (A[i][j] * B[j][0]);
	      }
	      pto_n[i][0] = suma;

	    }

	    return pto_n;
	}
	
	private int minimo(int a, int b) {
		return (a < b) ? a : b;
	}
	
	private int maximo(int a, int b) {
		return (a > b) ? a : b;
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

	public JButton getBotonOk() {
		return botonOk;
	}

	public void setBotonOk(JButton botonOk) {
		this.botonOk = botonOk;
	}

	public JLabel getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(JLabel etiqueta) {
		this.etiqueta = etiqueta;
	}

	public void setButtonGroup(ButtonGroup buttonGroup) {
		this.buttonGroup = buttonGroup;
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
