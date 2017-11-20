import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JTextArea;

public class Informacion {
	private JDialog dialog;
	private String formatoFichero;
	private String size;
	private String rango;
	private String rangoDinamico;
	private String brillo;
	private String contraste;
	private String entropia;
	
	public Informacion() {
		this.setDialog(new JDialog());
		this.getDialog().setTitle("Information");
		this.getDialog().setSize(200, 270);
	}
	
	void init(Imagen imagen) {
		this.setFormatoFichero(conocerFormatoImagen(imagen));
		this.setSize(conocerSize(imagen));
		this.setRango(conocerRango(imagen));
		this.setRangoDinamico(conocerRangoDinamico(imagen));
		this.setBrillo(imagen.getBrillo() + "");
		this.setContraste(imagen.getContraste() + "");
		this.setEntropia(imagen.getEntropia() + "");
	}

	void mostrar(int x, int y) {
		JTextArea textArea;
		String informacion = "";
		
		informacion += "\n  Formato:  " + this.getFormatoFichero() + "\n\n";
		informacion += "  Tamaño:  " + this.getSize() + "\n\n";
		informacion += "  Rango:  " + this.getRango() + "\n\n";
		informacion += "  Rango Dinámico:  " + this.getRangoDinamico() + "\n\n";
		informacion += "  Brillo:  " + this.getBrillo() + "\n\n";
		informacion += "  Contraste:  " + this.getContraste() + "\n\n";
		informacion += "  Entropía:  " + this.getEntropia() + "\n\n";
		textArea = new JTextArea(informacion);	
		textArea.setEditable(false);
		textArea.setFont(new Font("Helvetica", Font.PLAIN, 15));
		
		this.getDialog().getContentPane().removeAll();
		this.getDialog().setLocation(x, y);
		this.getDialog().add(textArea);
		this.getDialog().setResizable(false);
		this.getDialog().setVisible(true);
	}
	
	private String conocerFormatoImagen(Imagen imagen) {
		String ruta = imagen.getRutaImagen();
		Boolean encontrado = false;
		
		for(int i = ruta.length() - 1; i >= 0 && !encontrado; i--) 
			if(ruta.charAt(i) == '.') {
				ruta = ruta.substring(++i, ruta.length());
				encontrado = true;
			} 
		
		return ruta;
	}
	
	private String conocerSize(Imagen imagen) {
		return imagen.getImagen().getWidth() + "x" + imagen.getImagen().getHeight();
	}
	
	private String conocerRango(Imagen imagen) {
		Boolean fin = false;
		int minimo = 0, maximo = 0;
		int[] histograma = imagen.getHistograma();
		
		for(int i = 0; i < histograma.length && !fin; i++) 
			if(histograma[i] != 0) {
				minimo = i;
				fin = true;
			}
		
		fin = false;
		
		for(int i = histograma.length - 1; i >= 0 && !fin; i--)
			if(histograma[i] != 0) {
				maximo = i;
				fin = true;
			}
		
		return minimo + "-" + maximo;
	}
	
	private String conocerRangoDinamico(Imagen imagen) {
		int contador = 0;
		
		for(int i = 0; i < imagen.getHistograma().length; i++)
			if(imagen.getHistograma(i) != 0)
				contador++;
		
		return contador + "";
	}

	public JDialog getDialog() {
		return dialog;
	}

	public void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	public String getFormatoFichero() {
		return formatoFichero;
	}

	public void setFormatoFichero(String formatoFichero) {
		this.formatoFichero = formatoFichero;
	}
	
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getRango() {
		return rango;
	}

	public void setRango(String rango) {
		this.rango = rango;
	}

	public String getRangoDinamico() {
		return rangoDinamico;
	}

	public void setRangoDinamico(String rangoDinamico) {
		this.rangoDinamico = rangoDinamico;
	}

	public String getBrillo() {
		return brillo;
	}

	public void setBrillo(String brillo) {
		this.brillo = brillo;
	}

	public String getContraste() {
		return contraste;
	}

	public void setContraste(String contraste) {
		this.contraste = contraste;
	}

	public String getEntropia() {
		return entropia;
	}

	public void setEntropia(String entropia) {
		this.entropia = entropia;
	}
}
