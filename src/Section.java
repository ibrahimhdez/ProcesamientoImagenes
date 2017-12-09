import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Section {
	private final int JDIALOG_BORDE = 22;
	private JDialog ventana;
	private JDialog dialog;
	private JPanel grafica;
	private ArrayList<JTextField> puntos;
	private ArrayList<JTextField> valores;
	private JButton botonEjecutar;
	private JButton botonMostrar;
	private int nTramos;
	
	
	public Section() {
		this.setVentana(new JDialog());
		this.setDialog(new JDialog());
		this.setGrafica(new JPanel());
		this.setPuntos(new ArrayList<JTextField>());
		this.setValores(new ArrayList<JTextField>());
		this.setBotonEjecutar(new JButton());
		this.setBotonMostrar(new JButton());
		this.setnTramos(2);
	}
	
	public void init(int nTramos) {
		this.setnTramos(nTramos);
		
		this.getVentana().setTitle("Section Transform");
		
		JPanel mainPanel = new JPanel();
		JPanel panel = new JPanel();
		JPanel panelGrafica = new JPanel();
		JPanel panelBotones = new JPanel();
		JPanel panelInterfaz = new JPanel();
		
		for(int i=0; i<getnTramos();i++) {
			JTextField auxValor = new JTextField("0");
			JTextField auxPunto = new JTextField("0");
			
			auxValor.addFocusListener(new FocusListener() {
			    public void focusGained(FocusEvent e) {	auxValor.setText("");	}

			    public void focusLost(FocusEvent e) {}
			});
			auxPunto.addFocusListener(new FocusListener() {
			    public void focusGained(FocusEvent e) {	auxPunto.setText("");	}

			    public void focusLost(FocusEvent e) {}
			});

			if(i == getnTramos()-1)
				auxPunto.setText("255");
			
			getValores().add(auxValor);
			getPuntos().add(auxPunto);
			
		}
		
		mainPanel.setLayout(new GridLayout(1, 2));
		
		panel.setLayout(new BorderLayout());
		panelInterfaz.setLayout(new GridLayout(getnTramos(), 4, 5, 5));
		panelBotones.setLayout(new FlowLayout());
		panelGrafica.setLayout(new FlowLayout());
		this.getGrafica().setPreferredSize(new Dimension(255, 255));
		
		this.getBotonEjecutar().setText("Accept");
		this.getBotonMostrar().setText("Show Graphic");
		
		this.getBotonMostrar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comprobarValores())
					pintarGrafica();
				else {
					mostrarMensaje();
				}
			}
		});
		
		this.getGrafica().setBackground(Color.WHITE);
		
		for(int i=0; i<getnTramos();i++) {
			panelInterfaz.add(new JLabel("Punto:"));
			panelInterfaz.add(getPuntos().get(i));
			panelInterfaz.add(new JLabel("Valor:"));
			panelInterfaz.add(getValores().get(i));
		}

		panelBotones.add(this.getBotonEjecutar());
		panelBotones.add(this.getBotonMostrar());
		mainPanel.add(panel);
		mainPanel.add(panelGrafica);
		panel.add(panelInterfaz, BorderLayout.CENTER);
		panel.add(panelBotones, BorderLayout.SOUTH);
		panelGrafica.add(getGrafica(), BorderLayout.CENTER);
		
		this.getVentana().add(mainPanel);
		this.getVentana().pack();
	}
	
	void mostrar(JDialog dialog) {
		this.getVentana().setLocation(dialog.getWidth() + (int)dialog.getLocation().getX() + 100, (int)dialog.getLocation().getY() + (int)dialog.getLocation().getY() / 4);
		this.getVentana().setVisible(true);	
	}
	
	public void buildImage(Imagen imagenActual){
		if(comprobarValores()) {
			this.setDialog(new JDialog());
			BufferedImage imagen = imagenActual.getImagen();
			BufferedImage newImg = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());
			
			ArrayList<Integer> puntos = convertirArray(getPuntos());
			ArrayList<Integer> valores = convertirArray(getValores());
			
			ArrayList<Double> A = new ArrayList<Double>();
			ArrayList<Double> B = new ArrayList<Double>();
			
			
			for(int k=1;k<getnTramos();k++) {
				// A = (y2-y1) / (x2-x1)
				A.add((double) ((valores.get(k)-valores.get(k-1)) / (puntos.get(k)-puntos.get(k-1))));
				// B = y - A*x
				B.add(valores.get(k) - A.get(k-1)*puntos.get(k));
			}
			
			for(int i = 0; i < imagen.getWidth(); i++) 
				for(int j = 0; j < imagen.getHeight(); j++) {
					int valorPixel = imagenActual.getValorPixel(i, j);
					int nuevoColor = 0;
					
					int tramo = 0;
					for(int k=1;k<getnTramos();k++) 
						if(valorPixel <= puntos.get(k)) {
							tramo = k;
							break;
						}
					//Vout = A*Vin + B
					nuevoColor = (int) Math.round(A.get(tramo-1)*valorPixel + B.get(tramo-1));
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
			this.getDialog().setTitle(imagenActual.getContenedor().getTitle() + " de " + getnTramos() + " tramos");
			this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
			this.getDialog().setSize(newImg.getWidth(), newImg.getHeight() + JDIALOG_BORDE);
			this.getDialog().setLocationByPlatform(true);
			this.getDialog().setVisible(true);
			this.getDialog().setResizable(false);
		}
		else {
			mostrarMensaje();
		}
	}
	
	public ArrayList<Integer> convertirArray(ArrayList<JTextField> array) {
		ArrayList<Integer> nuevoArray = new ArrayList<Integer>();
		
		for(int i=0;i<getnTramos();i++) 
			nuevoArray.add(new Integer(array.get(i).getText()));
		
		return nuevoArray;
	}
	
	
	public void mostrarMensaje() {
		JOptionPane.showMessageDialog(null, "Error en la definición de los tramos.\n"
				+ "El primer tramo debe empezar en el PUNTO 0, y el último acabar en 255.\n"
				+ "Cada PUNTO debe ser mayor que el anterior y menor que el siguiente.\n"
				+ "Cada VALOR debe estar entre 0 y 255, ambos inclusive.");
	}
	
	public boolean comprobarValores() {
		boolean aux = true;
		int valor = 9999;
		
		for(int i=0; i<getnTramos() && aux;i++) {
			valor = 9999;
			
			try {
				valor = new Integer(this.getValores().get(i).getText());
			} catch(Exception e) {}
			
			if((valor < 0) || valor >255)
				aux = false;
		}
			
		if(aux) {
			valor = 9999;
			try {
				valor = new Integer(this.getPuntos().get(0).getText());
			} catch(Exception e) {}
			aux = (valor == 0);
			
			valor = 9999;
			try {
				valor = new Integer(this.getPuntos().get(getnTramos()-1).getText());
			} catch(Exception e) {}
			
			aux &= (valor == 255);
			
			for(int i=1; i<getnTramos() && aux;i++) {
				int valorAnterior = 9999;
				
				try {
					valor = new Integer(this.getPuntos().get(i).getText());
					valorAnterior = new Integer(this.getPuntos().get(i-1).getText());
				} catch(Exception e) {}
				
				if(valor <= valorAnterior)
					aux = false;
			}		
		}

		return aux;
	}
	
	public void pintarGrafica() {
		Graphics g = this.getGrafica().getGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 255, 255);
		
		g.setColor(Color.RED);
		int x0, y0, x1, y1;
		x0 = new Integer(this.getPuntos().get(0).getText());
		y0 = 255 - new Integer(this.getValores().get(0).getText());
		
		for(int i=1; i<getnTramos();i++) {
			x1 = new Integer(this.getPuntos().get(i).getText());
			y1 = 255 - new Integer(this.getValores().get(i).getText());
			g.drawLine(x0, y0, x1, y1);
			x0=x1;
			y0=y1;
		}
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
	public ArrayList<JTextField> getPuntos() {
		return puntos;
	}
	public void setPuntos(ArrayList<JTextField> puntos) {
		this.puntos = puntos;
	}
	public ArrayList<JTextField> getValores() {
		return valores;
	}
	public void setValores(ArrayList<JTextField> valores) {
		this.valores = valores;
	}
	public JButton getBotonEjecutar() {
		return botonEjecutar;
	}
	public void setBotonEjecutar(JButton botonEjecutar) {
		this.botonEjecutar = botonEjecutar;
	}
	public JButton getBotonMostrar() {
		return botonMostrar;
	}
	public void setBotonMostrar(JButton botonMostrar) {
		this.botonMostrar = botonMostrar;
	}

	public int getnTramos() {
		return nTramos;
	}

	public void setnTramos(int nTramos) {
		this.nTramos = nTramos;
	}

	public JPanel getGrafica() {
		return grafica;
	}

	public void setGrafica(JPanel grafica) {
		this.grafica = grafica;
	}

}
