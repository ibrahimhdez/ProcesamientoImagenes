import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class Diferencia {
	final int UMBRAL = 7;
	private Imagen imagen1;
	private Imagen imagen2;
	private Imagen imagenResultado;
	private BufferedImage diferencias;
	private JDialog dialog;
	private JDialog ventanaUmbral;
	private JLabel etiquetaUmbral;
	private JSlider sliderUmbral;
	private Boolean ejecutar;
	
	public Diferencia() {
		this.setImagen1(new Imagen());
		this.setImagen2(new Imagen());
		this.setImagenResultado(new Imagen());
		this.setDialog(new JDialog());
		this.setVentanaUmbral(new JDialog());
		this.setEtiquetaUmbral(new JLabel("Choose the threshold:"));
		this.setSliderUmbral(new JSlider(JSlider.HORIZONTAL, 0, 255, 128));
		this.setEjecutar(false);
	}
	
	public Diferencia(Imagen imagen1) {
		this.setImagen1(imagen1);
		this.setDialog(new JDialog());
	}
	
	void init() {
		JPanel panel = new JPanel();

		panel.add(this.getEtiquetaUmbral());
		panel.add(this.getSliderUmbral());
		
		this.getVentanaUmbral().add(panel);
		this.getVentanaUmbral().setTitle("Threshold");
		this.getVentanaUmbral().setResizable(false);
		this.getVentanaUmbral().setSize(200, 100);
	}
	
	void mostrarVentanaUmbral() {
		this.getVentanaUmbral().setLocation(this.getImagenResultado().getContenedor().getX() + this.getImagenResultado().getContenedor().getWidth() + 20, 10);
		this.getVentanaUmbral().setVisible(true);
	}
	
	 void addImagenDiferencia() throws IOException {
		this.setDialog(new JDialog());
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		JDialog auxDialog = new JDialog();
		JPanel panel = new JPanel();
		Image imagen = null;
		
		jfc.setDialogTitle("Choose an image:");
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.setFileFilter(new FileNameExtensionFilter("jpg, png, gif", "jpg", "png", "gif"));
		int sel = jfc.showOpenDialog(null);
		if (sel == JFileChooser.APPROVE_OPTION) 
			imagen = ImageIO.read(new File(jfc.getSelectedFile().getAbsolutePath()));
		
		if(imagen != null) {
			this.setEjecutar(true);
			
			if((imagen.getWidth(dialog) > 900) || (imagen.getHeight(dialog) > 1000))
				imagen = imagen.getScaledInstance(imagen.getWidth(dialog) / 2, imagen.getHeight(dialog) / 2, 0);
		
			auxDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			panel.add(new JLabel(new ImageIcon(imagen)));

			auxDialog.add(panel);
        		auxDialog.setIconImage(imagen);
        		auxDialog.pack();
        		auxDialog.setLocationByPlatform(true);
        		auxDialog.setResizable(false);
			
        		this.setImagen2(new Imagen(auxDialog));
        		this.getImagen2().getContenedor().setTitle("Imagen Diferencia");
		}
	}
	 
	Imagen generar() {
		BufferedImage resultado;
		BufferedImage imagen1 = this.getImagen1().getImagen();
		BufferedImage imagen2 = this.getImagen2().getImagen();
		int height, width;
				
		height = (imagen1.getHeight() > imagen2.getHeight()) ? imagen1.getHeight(): imagen2.getHeight();
		width = (imagen1.getWidth() > imagen2.getWidth()) ? imagen1.getWidth(): imagen2.getWidth();
		
		resultado = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			
		for(int i = 0; i < imagen1.getWidth(); i++)
			for(int j = 0; j < imagen1.getHeight(); j++) 
				if(j <= imagen2.getHeight() - 1)
					if(i <= imagen2.getWidth() - 1)
						resultado.setRGB(i, j, Math.abs(imagen1.getRGB(i, j) - imagen2.getRGB(i, j)));
					else 
						resultado.setRGB(i, j, imagen1.getRGB(i, j));
		
		this.setImagenResultado(new Imagen(marcarDiferencias(resultado)));
		this.setDiferencias(resultado);
		this.getImagenResultado().getContenedor().setTitle("Imagen resultado");
		this.getImagenResultado().getContenedor().setLocation(this.getImagen1().getContenedor().getLocation());
		mostrarImagenDiferencia();
		
		return this.getImagenResultado();
	}
	
	private void mostrarImagenDiferencia() {
		JDialog dialog1 = this.getImagen1().getContenedor();
		
		dialog1.setLocation(20, 20);
		this.getImagen2().getContenedor().setLocation((int)dialog1.getLocation().getX() + dialog1.getWidth() + 50, (int)dialog1.getLocation().getY());
		this.getImagen2().getContenedor().setTitle("Diferencia");
		this.getImagen2().getContenedor().setVisible(true);
	}
	
	BufferedImage marcarDiferencias(BufferedImage imagenActual) {		
		BufferedImage imagen = this.getImagen1().copyBufferedImage(imagenActual);
		Color red = new Color(255, 0, 0);
		
		for(int i = 0; i < imagen.getWidth(); i++)
			for(int j = 0; j < imagen.getHeight(); j++) {
				Color color = new Color(imagen.getRGB(i, j));
				int mediaPixel = (int)((color.getRed() + color.getGreen() + color.getBlue()) / 3);
				if(mediaPixel > this.getSliderUmbral().getValue())
					imagen.setRGB(i, j, red.getRGB());
				else
					imagen.setRGB(i, j, this.getImagen1().getImagen().getRGB(i, j));
			}
		
		return imagen;
	}

	public Imagen getImagen1() {
		return imagen1;
	}

	public void setImagen1(Imagen imagen1) {
		this.imagen1 = imagen1;
	}

	public Imagen getImagen2() {
		return imagen2;
	}

	public void setImagen2(Imagen imagen2) {
		this.imagen2 = imagen2;
	}

	public Imagen getImagenResultado() {
		return imagenResultado;
	}

	public void setImagenResultado(Imagen imagenResultado) {
		this.imagenResultado = imagenResultado;
	}

	public BufferedImage getDiferencias() {
		return diferencias;
	}

	public void setDiferencias(BufferedImage diferencias) {
		this.diferencias = diferencias;
	}

	public JDialog getDialog() {
		return dialog;
	}

	public void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	public JDialog getVentanaUmbral() {
		return ventanaUmbral;
	}

	public void setVentanaUmbral(JDialog ventanaUmbral) {
		this.ventanaUmbral = ventanaUmbral;
	}

	public JLabel getEtiquetaUmbral() {
		return etiquetaUmbral;
	}

	public void setEtiquetaUmbral(JLabel etiquetaUmbral) {
		this.etiquetaUmbral = etiquetaUmbral;
	}

	public JSlider getSliderUmbral() {
		return sliderUmbral;
	}

	public void setSliderUmbral(JSlider sliderUmbral) {
		this.sliderUmbral = sliderUmbral;
	}

	public Boolean getEjecutar() {
		return ejecutar;
	}

	public void setEjecutar(Boolean ejecutar) {
		this.ejecutar = ejecutar;
	}
}
