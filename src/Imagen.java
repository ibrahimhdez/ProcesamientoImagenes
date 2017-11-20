import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class Imagen {
	private JDialog contenedor;
	private BufferedImage imagen;
	private ImageIcon imageIconActual;
	private ImageIcon imageIconInicial;
	private Boolean modificada;
	private String rutaImagen;
	private int[] histograma;
	private float brillo;
	private float contraste;
	private float entropia;
	
	public Imagen() {
		this.setContenedor(new JDialog());
		this.setImageIconInicial(new ImageIcon());
		this.setImageIconActual(new ImageIcon());
		this.setBrillo(0);
		this.setContraste(0);
		this.setEntropia(0);
		this.setModificada(false);
	}
	
	public Imagen(JDialog contenedor) {
		this.setContenedor(contenedor);
		this.setImageIconInicial(new ImageIcon(contenedor.getIconImages().get(0)));
		this.setImageIconActual(new ImageIcon(contenedor.getIconImages().get(0)));
		this.setImagen(new BufferedImage(this.getImageIconActual().getImage().getWidth(null), this.getImageIconActual().getImage().getHeight(null), BufferedImage.TYPE_3BYTE_BGR));
		this.setBrillo(0);
		this.setContraste(0);
		this.setEntropia(0);
		this.setModificada(false);
		
		Graphics g = this.getImagen().createGraphics();
		g.drawImage(this.getImageIconActual().getImage(), 0, 0, null);   
		
		this.setHistograma();
		this.setBrillo();
		this.setContraste();
		this.setEntropia();
	}
	
	public Imagen(BufferedImage imagen) {
		this.setContenedor(new JDialog());
		this.setImageIconInicial(new ImageIcon(imagen));
		this.setImageIconActual(new ImageIcon(imagen));
		this.setImagen(imagen);
		this.setBrillo();
		this.setContraste();
		this.setEntropia();
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
	
	Image imageActual() {
		return this.getContenedor().getIconImages().get(0);
	}
	
	public JDialog getContenedor() {
		return contenedor;
	}

	public void setContenedor(JDialog contenedor) {
		this.contenedor = contenedor;
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

}
