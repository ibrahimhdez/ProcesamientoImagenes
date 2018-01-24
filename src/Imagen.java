import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Imagen {
	private JDialog contenedor;
	private JPanel panel;
	private Recortar recortar;
	private BufferedImage imagen;
	private ImageIcon imageIconActual;
	private ImageIcon imageIconInicial;
	private Boolean modificada;
	private String rutaImagen;
	private int[] histograma;
	private float brillo;
	private float contraste;
	private float entropia;
	private double indiceGamma;
	
	public Imagen() {
		this.setContenedor(new JDialog());
		this.setPanel(new JPanel());
		this.setRecortar(new Recortar());
		this.setBrillo(0);
		this.setContraste(0);
		this.setEntropia(0);
		this.setModificada(false);
		this.setIndiceGamma(1.0);
	}
	
	public Imagen(JDialog contenedor) {
		this.setContenedor(contenedor);
		this.setPanel(new JPanel());
		this.setImageIconInicial(new ImageIcon(contenedor.getIconImages().get(0)));
		this.setImageIconActual(new ImageIcon(contenedor.getIconImages().get(0)));
		this.setImagen(new BufferedImage(this.getImageIconActual().getImage().getWidth(null), this.getImageIconActual().getImage().getHeight(null), BufferedImage.TYPE_3BYTE_BGR));
		this.setModificada(false);
		
		Graphics g = this.getImagen().createGraphics();
		g.drawImage(this.getImageIconActual().getImage(), 0, 0, null);   
		
		this.setHistograma();
		this.setBrillo();
		this.setContraste();
		this.setEntropia();
		this.setIndiceGamma(1.0);
		this.setRecortar(new Recortar());
	}
	
	public Imagen(BufferedImage imagen) {
		this.setContenedor(new JDialog());
		this.setPanel(new JPanel());
		this.setImageIconInicial(new ImageIcon(imagen));
		this.setImageIconActual(new ImageIcon(imagen));
		this.setImagen(imagen);
		this.setBrillo();
		this.setContraste();
		this.setEntropia();
		this.setIndiceGamma(1.0);
		this.setRecortar(new Recortar());
		this.getContenedor().add(new JLabel(new ImageIcon(imagen)));
		this.getContenedor().pack();
		this.setHistograma();
	}
	
	public Imagen(Image image) {
		this.setContenedor(new JDialog());
	}

	void actualizarBufferedImage() {
		this.setImagen(new BufferedImage(this.getImageIconActual().getImage().getWidth(null), this.getImageIconActual().getImage().getHeight(null), BufferedImage.TYPE_3BYTE_BGR));
		
		Graphics g = this.getImagen().createGraphics();
		g.drawImage(this.getImageIconActual().getImage(), 0, 0, null);  
		
		this.setHistograma();
		this.setBrillo();
		this.setContraste();
	}
	
	void setHistograma() {
		BufferedImage imagen = this.getImagen();
		Color red = Color.red;
	    Color color;
	    int[] histo = new int[256];

	    for (int i = 0; i < imagen.getWidth(); i++) {
	      for (int j = 0; j < imagen.getHeight(); j++) {
	        color = new Color(imagen.getRGB(i, j));
	        if(color.getRGB() != red.getRGB())
	          histo[color.getGreen()] += 1;
	      }
	    }
	    this.setHistograma(histo);
	}
	
	void setBrillo() {
		BufferedImage imagen = this.getImagen();
		float temp = 0;
	    float size = imagen.getHeight() * imagen.getWidth();
	    int[] histo = getHistograma();
	    
	    for (int i = 0; i < 256; i++) 
	      temp += (float) (histo[i] * i);

	    this.setBrillo((float) (temp / size));
	}
	
	void setContraste() {
		BufferedImage imagen = this.getImagen();
		float temp1 = 0;
	    float size1 = imagen.getHeight() * imagen.getWidth();
	    float u = getBrillo();
	    int[] histo = getHistograma();
	   
	    for (int i = 0; i < 256; i++) 
	      temp1 += Math.pow((i - u), 2) * histo[i];
	  
	    this.setContraste((float) Math.sqrt(temp1 / size1));
	}
	
	void setEntropia() {
	    float temp = 0;
	    float size = this.getImagen().getHeight() * this.getImagen().getWidth();
	    
	    for (int i = 0; i < 256; i++) {
	      float p = (float) (getHistograma(i)) / (size);
	      if (p != 0) 
	        temp += p * (Math.log(p) / Math.log(2));
	    }
	    
	    this.entropia = -temp;
	}
	
	JDialog construirImagen(Imagen imagenActual, int[] pixeles) {
		BufferedImage imagen = imagenActual.getImagen();
		JDialog dialog = new JDialog();
		final int ANCHO = imagen.getWidth();
	    final int ALTO = imagen.getHeight();
	    
	    for (int i = 0; i < ANCHO; i++) {
	      for (int j = 0; j < ALTO; j++) {
	        int pixelActual = new Color(imagen.getRGB(i, j)).getRed();
	        int nuevoPixel = pixeles[pixelActual];
	        int result = nuevoPixel << 16;
	        result += nuevoPixel << 8;
	        result += nuevoPixel;
	        imagen.setRGB(i, j, result);
	      }
	    }
	    
	    Image toolkitImage = imagen;
	    
	    Graphics g = imagen.getGraphics();
		g.drawImage(toolkitImage, 0, 0, null);
		g.dispose();
		
		@SuppressWarnings("serial")
		JPanel panel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g); 
				g.drawImage(imagen, 0, 0, null);
				getRecortar().pintarRectangulo(g);
			}
		};
		dialog.add(panel);
		dialog.setIconImage(imagen);
		dialog.setTitle(imagenActual.getContenedor().getTitle() + " nuevo");
		dialog.setLocation((int)imagenActual.getContenedor().getLocation().getX(), (int)imagenActual.getContenedor().getLocation().getY() + imagenActual.getContenedor().getHeight() + 50);
		dialog.setSize(imagen.getWidth(), imagen.getHeight() + 23);
	//	dialog.setVisible(true);
		JPanel aux = (JPanel)dialog.getContentPane();
		aux.repaint();
		dialog.setVisible(true);
		dialog.setResizable(false);
		
		return dialog;
	}
	
	int getValorPixel(int x, int y) {
		Color color = new Color(this.getImagen().getRGB(arreglarCoordenadasX(x), arreglarCoordenadasY(y)));
		
		return (int)((color.getRed() + color.getGreen() + color.getBlue()) / 3);
	}
	
	int getValorPixelRGB(int x, int y) {
		return this.getImagen().getRGB(arreglarCoordenadasX(x), arreglarCoordenadasY(y));
	}
	
	private int arreglarCoordenadasX(int coordenada) {
		if(coordenada < 0)
			coordenada = 0;
		
		else if(coordenada >= this.getImagen().getWidth())
			coordenada = this.getImagen().getWidth() - 1; 
		
		return coordenada;
	}
	
	private int arreglarCoordenadasY(int coordenada) {
		if(coordenada < 0)
			coordenada = 0;
		
		else if(coordenada >= this.getImagen().getHeight())
			coordenada = this.getImagen().getHeight() - 1; 
		
		return coordenada;
	}
	
	Image imageActual() {
		return this.getContenedor().getIconImages().get(0);
	}
	
	public JDialog getContenedor() {
		return contenedor;
	}

	public void setContenedor(JDialog contenedor) {
		this.contenedor = contenedor;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public Recortar getRecortar() {
		return recortar;
	}

	public void setRecortar(Recortar recortar) {
		this.recortar = recortar;
	}

	public BufferedImage getImagen() {
		return imagen;
	}
	
	public void setImagen(BufferedImage imagen) {
		this.imagen = imagen;
	}
	
	public ImageIcon getImageIconActual() {
		return imageIconActual;
	}
	
	public void setImageIconActual(ImageIcon imageIconActual) {
		this.imageIconActual = imageIconActual;
		this.getContenedor().setIconImage(imageIconActual.getImage());
		actualizarBufferedImage();
	}
	
	public ImageIcon getImageIconInicial() {
		return imageIconInicial;
	}
	
	public void setImageIconInicial(ImageIcon imageIconInicial) {
		this.imageIconInicial = imageIconInicial;
	}
	
	public Boolean getModificada() {
		return modificada;
	}

	public void setModificada(Boolean modificada) {
		this.modificada = modificada;
	}

	public float getBrillo() {
		return brillo;
	}

	public void setBrillo(float brillo) {
		this.brillo = brillo;
	}

	public float getContraste() {
		return contraste;
	}

	public void setContraste(float contraste) {
		this.contraste = contraste;
	}

	public int[] getHistograma() {
		return histograma;
	}
	
	public int getHistograma(int i) {
		return histograma[i];
	}

	public void setHistograma(int[] histograma) {
		this.histograma = histograma;
	}

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	public float getEntropia() {
		return entropia;
	}

	public void setEntropia(float entropia) {
		this.entropia = entropia;
	}

	public double getIndiceGamma() {
		return indiceGamma;
	}

	public void setIndiceGamma(double indiceGamma) {
		this.indiceGamma = indiceGamma;
	}

}
