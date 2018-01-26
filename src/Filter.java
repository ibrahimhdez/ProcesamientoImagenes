import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Filter {
	private final int JDIALOG_BORDE = 22;
	private JDialog ventana;
	private JDialog dialog;
	private JButton boton;
	private JTextArea mask;
	
	public Filter() {
		this.setVentana(new JDialog());
		this.setBoton(new JButton("Convolve"));
		this.setMask(new JTextArea(20,25));
	}

	public void init() {
		this.getVentana().setTitle("Convolve");
		
		JPanel mainPanel = new JPanel();
		JScrollPane panelArea = new JScrollPane(getMask());
		JPanel panelBotones = new JPanel();
		JPanel panelConvolve = new JPanel();
		
		JButton open = new JButton("Open");
		JButton save = new JButton("Save");
		
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				open();
			}
		});
		
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		
		panelBotones.add(open);
		panelBotones.add(save);
		
		panelConvolve.add(getBoton());
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		mainPanel.add(panelArea);
		mainPanel.add(panelBotones);
		mainPanel.add(panelConvolve);
		
		this.getVentana().add(mainPanel);
		this.getVentana().pack();
		
	}
	
	public void save() {
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
		jfc.setDialogTitle("Save the filter:");
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setFileFilter(new FileNameExtensionFilter("fil", "fil"));
		int sel = jfc.showSaveDialog(null);
		
		if (sel == JFileChooser.APPROVE_OPTION) {
	        try (FileWriter fw = new FileWriter(jfc.getSelectedFile() + ".fil")) {
	        	
	        	fw.write(getMask().getText());
	        	fw.flush();
	    	    fw.close();
	    	    
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void open() {
		String fileRoute = "";
		
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
		jfc.setDialogTitle("Choose a filter:");
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setFileFilter(new FileNameExtensionFilter("fil", "fil"));
		int sel = jfc.showOpenDialog(null);
		if (sel == JFileChooser.APPROVE_OPTION) {
	        fileRoute = jfc.getSelectedFile().getAbsolutePath();
	        try {
	        	String texto = "";
	        	String linea = "";
	        	
	    		FileReader file = new FileReader(fileRoute);
	    	    BufferedReader buffer = new BufferedReader(file);
	    	    
	    	    while((linea = buffer.readLine()) != null)
	    	    	texto += linea + "\n";
	    	    
	    	    buffer.close();
	    	    
	    	    getMask().setText(texto);
	    	    
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public boolean checkFilter() {
		boolean valido = true;

		int nColumnas = 0, nLineas = 0;
		
		for(String linea : getMask().getText().split("\\n")) {
			nLineas++;
			StringTokenizer st = new StringTokenizer(linea);
			if(nLineas == 1) {
				nColumnas = st.countTokens();
				if(nColumnas % 2 == 0)
					return false;
			}
			if(st.countTokens() != nColumnas)
				return false;
		}
		
		if(nLineas % 2 == 0)
			return false;
		
		return valido;
	}
	
	void mostrar(JDialog dialog) {
		this.getVentana().setLocation(dialog.getWidth() + (int)dialog.getLocation().getX() + 100, (int)dialog.getLocation().getY() + (int)dialog.getLocation().getY() / 4);
		this.getVentana().setVisible(true);	
	}
	
	public boolean convolveCustom(Imagen imagenActual) {
		if(checkFilter()) {
			this.getVentana().setVisible(false);	
			this.setDialog(new JDialog());
			BufferedImage imagen = imagenActual.getImagen();
			BufferedImage newImg = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());
			
			//Construyendo el filtro a partir de la máscara
			ArrayList<ArrayList<Integer>> filtro = new ArrayList<ArrayList<Integer>>();
			int nColumnas = 0, nFilas = 0;
			
			for(String linea : getMask().getText().split("\\n")) {
				StringTokenizer st = new StringTokenizer(linea);
				ArrayList<Integer> aux = new ArrayList<Integer>();
				while (st.hasMoreElements()) {
					aux.add(Integer.parseInt(st.nextToken()));
				}
				filtro.add(aux);
			}
			
			
			nFilas = filtro.size();
			nColumnas = filtro.get(0).size();
			
			int aux = 0;
			
			for(int i=0; i<filtro.size(); i++)
				for(int j=0; j<filtro.get(i).size();j++)
					aux += filtro.get(i).get(j);
			
			float factor = 1 / aux;
			
			int filasIgnoradas = (int) Math.floor(nFilas/2);
			int columnasIgnoradas = (int) Math.floor(nColumnas/2);
			
			//Aplicando el filtro
			
			for(int i = 0; i < imagen.getWidth(); i++) 
				for(int j = 0; j < imagen.getHeight(); j++) {
					int nuevoColor = 0;
					
					if(i<filasIgnoradas || i>imagen.getWidth()-1-filasIgnoradas)
						nuevoColor = imagenActual.getValorPixel(i, j);
					else 
						if(j<columnasIgnoradas || j>imagen.getHeight()-1-columnasIgnoradas)
							nuevoColor = imagenActual.getValorPixel(i, j);
						else {
							
							int valorPixel = 0, filaFiltro=0, colFiltro;
							for(int x= i-filasIgnoradas; x<=i+filasIgnoradas; x++) {
								colFiltro=0;
								for(int y=j-columnasIgnoradas; y<=j+columnasIgnoradas; y++) {
									valorPixel += filtro.get(filaFiltro).get(colFiltro) * imagenActual.getValorPixel(x, y);
									colFiltro++;
								}
								filaFiltro++;
							}
							nuevoColor = Math.round(valorPixel * factor);
						}
					
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
			this.getDialog().setTitle("Imagen con filtro");
			this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
			this.getDialog().setSize(newImg.getWidth(), newImg.getHeight() + JDIALOG_BORDE);
			this.getDialog().setLocationByPlatform(true);
			this.getDialog().setVisible(true);
			this.getDialog().setResizable(false);
			
			return true;
		}
		else {
			mostrarMensaje();
			return false;
		}
	}
	
	public void selectFilter(int index, Imagen imagenActual) {
		ArrayList<ArrayList<Integer>> filtro = new ArrayList<ArrayList<Integer>>();
		float factor = 0;
		
		switch(index) {
			case 0: {
				factor = (float) 1/9;
				for(int i=0; i<3;i++) {
					filtro.add(new ArrayList<Integer>());
					for(int j=0; j<3;j++)
						filtro.get(i).add(1);
				}
			}break;
			case 1: {
				factor = (float) 1/25;
				for(int i=0; i<5;i++) {
					filtro.add(new ArrayList<Integer>());
					for(int j=0; j<5;j++)
						filtro.get(i).add(1);
				}
			}break;
			case 2: {
				factor = (float) 1/49;
				for(int i=0; i<7;i++) {
					filtro.add(new ArrayList<Integer>());
					for(int j=0; j<7;j++)
						filtro.get(i).add(1);
				}
			}break;
			case 3:{ 
				factor = (float) 1/2;
				filtro.add(new ArrayList<Integer>());
				filtro.get(0).add(-1);
				filtro.get(0).add(0);
				filtro.get(0).add(1);
			}break;
			case 4: {
				factor = (float) 1/2;
				int aux = -1;
				for(int i=0; i<3;i++) {
					filtro.add(new ArrayList<Integer>());
					filtro.get(i).add(aux);
					aux++;
				}
			}break;
			case 5:{ 
				factor = (float) 1/4;
				for(int i=0; i<3;i++) 
					filtro.add(new ArrayList<Integer>());
				filtro.get(0).add(-1);
				filtro.get(0).add(0);
				filtro.get(0).add(1);
				filtro.get(1).add(-2);
				filtro.get(1).add(0);
				filtro.get(1).add(2);
				filtro.get(2).add(-1);
				filtro.get(2).add(0);
				filtro.get(2).add(1);
			}break;
			case 6: {
				factor = (float) 1/4;
				for(int i=0; i<3;i++) 
					filtro.add(new ArrayList<Integer>());
				filtro.get(0).add(-1);
				filtro.get(0).add(-2);
				filtro.get(0).add(-1);
				filtro.get(1).add(0);
				filtro.get(1).add(0);
				filtro.get(1).add(0);
				filtro.get(2).add(1);
				filtro.get(2).add(2);
				filtro.get(2).add(1);
			}break;
		}
		
		convolve(factor, filtro, imagenActual);
		
	}
	
	public void convolve(float factor, ArrayList<ArrayList<Integer>> filtro, Imagen imagenActual) {
			this.getVentana().setVisible(false);	
			this.setDialog(new JDialog());
			BufferedImage imagen = imagenActual.getImagen();
			BufferedImage newImg = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());

			int nColumnas = 0, nFilas = 0;		
			
			nFilas = filtro.size();
			nColumnas = filtro.get(0).size();
			
			
			int filasIgnoradas = (int) Math.floor(nFilas/2);
			int columnasIgnoradas = (int) Math.floor(nColumnas/2);
			
			System.out.println(filtro);
			System.out.println(nFilas + " " + nColumnas + " " + filasIgnoradas+ " " +columnasIgnoradas+ " ");
			
			//Aplicando el filtro
			
			for(int i = 0; i < imagen.getWidth(); i++) 
				for(int j = 0; j < imagen.getHeight(); j++) {
					int nuevoColor = 0;
					
					if(i<filasIgnoradas || i>imagen.getWidth()-1-filasIgnoradas)
						nuevoColor = imagenActual.getValorPixel(i, j);
					else 
						if(j<columnasIgnoradas || j>imagen.getHeight()-1-columnasIgnoradas)
							nuevoColor = imagenActual.getValorPixel(i, j);
						else {
							
							int valorPixel = 0, filaFiltro=0, colFiltro;
							for(int x= i-filasIgnoradas; x<=i+filasIgnoradas; x++) {
								colFiltro=0;
								for(int y=j-columnasIgnoradas; y<=j+columnasIgnoradas; y++) {
									valorPixel += filtro.get(filaFiltro).get(colFiltro) * imagenActual.getValorPixel(x, y);
									colFiltro++;
								}
								filaFiltro++;
							}
							System.out.println(factor);
							nuevoColor = Math.round(valorPixel * factor);
						}
					
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
			this.getDialog().setTitle("Imagen con filtro");
			this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
			this.getDialog().setSize(newImg.getWidth(), newImg.getHeight() + JDIALOG_BORDE);
			this.getDialog().setLocationByPlatform(true);
			this.getDialog().setVisible(true);
			this.getDialog().setResizable(false);

	}
	
	public void selectFilterGaussian(int index, Imagen imagenActual) {
		ArrayList<ArrayList<Integer>> filtro = new ArrayList<ArrayList<Integer>>();
		filtro.add(new ArrayList<Integer>());
		float factor = 0;
		
		switch(index) {
			case 1:{ 
				factor =  (float)1/2506;
				filtro.get(0).add(11);
				filtro.get(0).add(135);
				filtro.get(0).add(607);
				filtro.get(0).add(1000);
				filtro.get(0).add(607);
				filtro.get(0).add(135);
				filtro.get(0).add(11);
			}break;
			case 2:{ 
				factor = (float)1/5012;
				filtro.get(0).add(2);
				filtro.get(0).add(11);
				filtro.get(0).add(44);
				filtro.get(0).add(135);
				filtro.get(0).add(325);
				filtro.get(0).add(607);
				filtro.get(0).add(882);
				filtro.get(0).add(1000);
				filtro.get(0).add(882);
				filtro.get(0).add(607);
				filtro.get(0).add(325);
				filtro.get(0).add(135);
				filtro.get(0).add(44);
				filtro.get(0).add(11);
				filtro.get(0).add(2);
			}break;
			case 3:{ 
				factor = (float) 1/7520;
				filtro.get(0).add(1);
				filtro.get(0).add(4);
				filtro.get(0).add(11);
				filtro.get(0).add(29);
				filtro.get(0).add(66);
				filtro.get(0).add(135);
				filtro.get(0).add(249);
				filtro.get(0).add(411);
				filtro.get(0).add(607);
				filtro.get(0).add(801);
				filtro.get(0).add(946);
				filtro.get(0).add(1000);
				filtro.get(0).add(946);
				filtro.get(0).add(801);
				filtro.get(0).add(607);
				filtro.get(0).add(411);
				filtro.get(0).add(249);
				filtro.get(0).add(135);
				filtro.get(0).add(66);
				filtro.get(0).add(29);
				filtro.get(0).add(11);
				filtro.get(0).add(4);
				filtro.get(0).add(1);
			}break;
		}
		
		convolveGaussian(factor, filtro, imagenActual);
	}
	
	public void convolveGaussian(float factor, ArrayList<ArrayList<Integer>> filtro, Imagen imagenActual) {
		this.getVentana().setVisible(false);	
		this.setDialog(new JDialog());
		BufferedImage imagen = imagenActual.getImagen();
		BufferedImage auxImg = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());
		BufferedImage newImg = new BufferedImage(imagen.getWidth(), imagen.getHeight(), imagen.getType());

		int nColumnas = 0, nFilas = 0;						
		nFilas = filtro.size();
		nColumnas = filtro.get(0).size();
		
		int filasIgnoradas = (int) Math.floor(nFilas/2);
		int columnasIgnoradas = (int) Math.floor(nColumnas/2);
		
		//Aplicando el filtro
		
		for(int i = 0; i < imagen.getWidth(); i++) 
			for(int j = 0; j < imagen.getHeight(); j++) {
				int nuevoColor = 0;
				
				if(i<filasIgnoradas || i>imagen.getWidth()-1-filasIgnoradas)
					nuevoColor = imagenActual.getValorPixel(i, j);
				else 
					if(j<columnasIgnoradas || j>imagen.getHeight()-1-columnasIgnoradas)
						nuevoColor = imagenActual.getValorPixel(i, j);
					else {
						
						int valorPixel = 0, filaFiltro=0, colFiltro;
						for(int x= i-filasIgnoradas; x<=i+filasIgnoradas; x++) {
							colFiltro=0;
							for(int y=j-columnasIgnoradas; y<=j+columnasIgnoradas; y++) {
								valorPixel += filtro.get(filaFiltro).get(colFiltro) * imagenActual.getValorPixel(x, y);
								colFiltro++;
							}
							filaFiltro++;
						}
						nuevoColor = Math.round(valorPixel * factor);
					}
				
				nuevoColor = (nuevoColor << 16) | (nuevoColor << 8) | nuevoColor;
				
				auxImg.setRGB(i, j, nuevoColor); 
			}
		
		Imagen auxImagen = new Imagen(auxImg);
		ArrayList<ArrayList<Integer>> filtroT = new ArrayList<ArrayList<Integer>>(1000);
		System.out.println(filtro);
	    for (int x=0; x < filtro.get(0).size(); x++) {
	    	filtroT.add(new ArrayList<Integer>(1000));
	    	filtroT.get(x).add(filtro.get(0).get(x));
	    }
	    
		nFilas = filtroT.size();
		nColumnas = filtroT.get(0).size();
		
		filasIgnoradas = (int) Math.floor(nFilas/2);
		columnasIgnoradas = (int) Math.floor(nColumnas/2);
		
		//Aplicando el filtro
		
		for(int i = 0; i < imagen.getWidth(); i++) 
			for(int j = 0; j < imagen.getHeight(); j++) {
				int nuevoColor = 0;
				
				if(i<filasIgnoradas || i>imagen.getWidth()-1-filasIgnoradas)
					nuevoColor = auxImagen.getValorPixel(i, j);
				else 
					if(j<columnasIgnoradas || j>imagen.getHeight()-1-columnasIgnoradas)
						nuevoColor = auxImagen.getValorPixel(i, j);
					else {
						
						int valorPixel = 0, filaFiltro=0, colFiltro;
						for(int x= i-filasIgnoradas; x<=i+filasIgnoradas; x++) {
							colFiltro=0;
							for(int y=j-columnasIgnoradas; y<=j+columnasIgnoradas; y++) {
								valorPixel += filtroT.get(filaFiltro).get(colFiltro) * auxImagen.getValorPixel(x, y);
								colFiltro++;
							}
							filaFiltro++;
						}
						nuevoColor = Math.round(valorPixel * factor);
					}
				
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
		this.getDialog().setTitle("Imagen con filtro");
		this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
		this.getDialog().setSize(newImg.getWidth(), newImg.getHeight() + JDIALOG_BORDE);
		this.getDialog().setLocationByPlatform(true);
		this.getDialog().setVisible(true);
		this.getDialog().setResizable(false);

}
	
	public void mostrarMensaje() {
		JOptionPane.showMessageDialog(null, "Error en la definición del filtro.\n"
				+ "El número de filas y columnas debe ser impar.\n"
				+ "Todas las filas deben tener el mismo número de columnas.");
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

	public JTextArea getMask() {
		return mask;
	}

	public void setMask(JTextArea mask) {
		this.mask = mask;
	}

	public int getJDIALOG_BORDE() {
		return JDIALOG_BORDE;
	}
}
