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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class Diferencia {
	final int UMBRAL = 3;
	private Imagen imagen1;
	private Imagen imagen2;
	private JDialog dialog;
	
	public Diferencia() {
		this.setImagen1(new Imagen());
		this.setImagen2(new Imagen());
		this.setDialog(new JDialog());
	}
	
	public Diferencia(Imagen imagen1) {
		this.setImagen1(imagen1);
		this.setDialog(new JDialog());
	}
	
	 void addImagenDiferencia() throws IOException {
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
		
		if((imagen.getWidth(dialog) > 900) || (imagen.getHeight(dialog) > 1000))
			imagen = imagen.getScaledInstance(imagen.getWidth(dialog) / 2, imagen.getHeight(dialog) / 2, 0);
		
		auxDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        auxDialog.setTitle("Diferencia Imagen");
        panel.add(new JLabel(new ImageIcon(imagen)));

        auxDialog.add(panel);
        auxDialog.setIconImage(imagen);
        auxDialog.pack();
        auxDialog.setLocationByPlatform(true);
        auxDialog.setResizable(false);
		
		this.setImagen2(new Imagen(auxDialog));
	}
	 
	void generar() {
		BufferedImage resultado;
		BufferedImage imagen1 = this.getImagen1().getImagen();
		BufferedImage imagen2 = this.getImagen2().getImagen();
		Imagen imagenResultado;
		Color red = new Color(255, 0, 0);
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

		for(int i = 0; i < resultado.getWidth(); i++)
			for(int j = 0; j < resultado.getHeight(); j++) {
				Color color = new Color(resultado.getRGB(i, j));
				int mediaPixel = (int)((color.getRed() + color.getGreen() + color.getBlue()) / 3);
				if(mediaPixel > UMBRAL)
					resultado.setRGB(i, j, red.getRGB());
				else
					resultado.setRGB(i, j, imagen1.getRGB(i, j));
			}
			
		imagenResultado = new Imagen(resultado);
		imagenResultado.getContenedor().setVisible(true);
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

	public JDialog getDialog() {
		return dialog;
	}

	public void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}
}
