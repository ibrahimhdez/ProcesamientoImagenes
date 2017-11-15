import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Vista extends JFrame{
	private static final long serialVersionUID = 1L;
	private VentanaBrilloContraste ventanaBrilloContraste;
	private JMenuBar barraMenu;
    private JMenu menuFile, itemNew;
    private JMenu menuImage, itemAdjust;
    private JMenu menuShow;
    private JMenuItem itemImage;
    private JMenuItem itemShowInfo;
    private JMenuItem itemBrightnessContrast;
    private JMenuItem itemHistograma, itemHistogramaAcumulativo;
    private JButton botonBlancoNegro;
    private JLabel etiquetaImagen;
    private Panel miPanel;
    private Integer numeroImagen;
    private ArrayList<JDialog> misImagenes;
    private JDialog focoImagenActual;
    private JDialog brilloContraste;
    private JLabel brilloEtiqueta;
    private JSlider brilloSlider;
    private JTextField brilloTextField;
    private JLabel contrasteEtiqueta;
    private JSlider contrasteSlider;
    private JTextField contrasteTextField;
    private JButton defaultBrillo;
    private JButton defaultContraste;
    private ImageIcon imageIconActual;

    public Vista() {
        super("Procesamiento Imágenes");
      
        this.setBarraMenu(new JMenuBar());     
       
        this.setMenuFile(new JMenu("File"));
        this.setItemNew(new JMenu("New"));
        this.setItemImage(new JMenuItem("Image"));
        
        this.setMenuShow(new JMenu("Show"));
        this.setItemHistograma(new JMenuItem("Absolute Histogram"));
        this.setItemHistogramaAcumulativo(new JMenuItem("Accumulative Histogram"));
        
        this.setMenuImage(new JMenu("Image"));
        this.setItemShowInfo(new JMenuItem("Show info..."));
        this.setItemAdjust(new JMenu("Adjust"));
        this.setItemBrightnessContrast(new JMenuItem("Brightness/Contrast"));
        this.setBrilloEtiqueta(new JLabel("Brillo"));
        this.setBrilloSlider(new JSlider(JSlider.VERTICAL, -255, 255, 100));
        this.setBrilloTextField(new JTextField(4));
        this.setDefaultBrillo(new JButton("Default"));
        this.setDefaultContraste(new JButton("Default"));
        this.setContrasteEtiqueta(new JLabel("Contraste"));
        this.setContrasteTextField(new JTextField(4));
        this.setContrasteSlider(new JSlider(JSlider.VERTICAL, -255, 255, 100));
        this.setBotonBlancoNegro(new JButton());
        
        this.setNumeroImagen(1);
        this.setEtiquetaImagen(new JLabel());
        this.setMiPanel(new Panel());
        this.setMisImagenes(new ArrayList<>());
        this.setBrilloContraste(new JDialog());
    }
    
    void openJDialog() throws IOException{
    		JDialog dialog = new JDialog();     
    		JPanel panel = new JPanel();
    		Image imagen = ImageIO.read(new File(this.getMiPanel().getRutaImagen()));
    	
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle("Imagen " + this.numeroImagen);
        this.numeroImagen++;
               
        if((imagen.getWidth(dialog) > 900) || (imagen.getHeight(dialog) > 1000))
        		imagen = imagen.getScaledInstance(imagen.getWidth(dialog) / 2, imagen.getHeight(dialog) / 2, 0);
        
        panel.add(new JLabel(new ImageIcon(imagen)));

        dialog.add(panel);
        dialog.setIconImage(imagen);
        dialog.pack();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        dialog.setResizable(false);
       
        addImagen(dialog);
    }
    
	void iniciarBotones() throws IOException{
		Image imagenBlancoNegro = ImageIO.read(new File("imagenesBotones/bw.jpg"));
		
		imagenBlancoNegro = imagenBlancoNegro.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
		
		this.getBotonBlancoNegro().setIcon(new ImageIcon(imagenBlancoNegro));
		this.getBotonBlancoNegro().setBounds(new Rectangle(0, 0, 30, 30));
    }
	
	void convertirBlancoNegro(){
		JDialog dialog = new JDialog();
		BufferedImage bufferedImage = new BufferedImage(imageActual().getWidth(null), imageActual().getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics g = bufferedImage.getGraphics();
		g.drawImage(imageActual(), 0, 0, null);
		
		for(int i = 0; i < bufferedImage.getWidth(); i++)
			for(int j = 0; j < bufferedImage.getHeight(); j++) {
				Color color = new Color(bufferedImage.getRGB(i, j));
				int mediaPixel = (int)((color.getRed() + color.getGreen() + color.getBlue()) / 3);
				int colorSRGB = (mediaPixel << 16) | (mediaPixel << 8) | mediaPixel;
		
				bufferedImage.setRGB(i, j,colorSRGB);
         }

		dialog.add(new JLabel(new ImageIcon(bufferedImage)));
		dialog.setIconImage(bufferedImage);
		dialog.setTitle(this.getFocoImagenActual().getTitle() + " blanco y negro");
		dialog.setLocation((int)this.getFocoImagenActual().getLocation().getX() + this.getFocoImagenActual().getWidth() + 20, (int)this.getFocoImagenActual().getLocation().getY());
		dialog.pack();
        dialog.setLocationByPlatform(true);
        dialog.setVisible(true);
        dialog.setResizable(false);
        addImagen(dialog);
	}
	
	void mostrarPanelBrilloContraste() {
		this.getBrilloContraste().setLocation(this.getFocoImagenActual().getWidth() + (int)this.getFocoImagenActual().getLocation().getX() + 100, (int)this.getFocoImagenActual().getLocation().getY());
		this.getBrilloContraste().setVisible(true);
	}
	
	void actualizarPanelBrilloContraste() {
		float brilloActual = getBrillo(getBufferedImageActual());
		float contrasteActual = getContraste(getBufferedImageActual());
		
		this.getBrilloSlider().setValue((int) brilloActual);
		this.getContrasteSlider().setValue((int) contrasteActual);
	}
	
	void mostrarHistograma() {
		JPanel panel = new JPanel();
		JDialog dialog = new JDialog();
		Hashtable<Integer, Integer> pixeles = null; //obtenerArrayPixeles();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
	    /*for(int i = 0; i < 255; i++)
	    		if(pixeles.containsKey(i))
	    			dataset.setValue(pixeles.get(i), 1 + "", i + "");
	    		else
	    			dataset.setValue(1, 1 + "", 0 + ""); */
		
		int[] histograma = getHistograma(getBufferedImageActual());
		
		for(int i = 0; i < histograma.length; i++)
			dataset.setValue(histograma[i], 1 + "", i + "");
		
		JFreeChart chart = ChartFactory.createBarChart3D("Histograma Absoluto","Pixel", "Value", dataset, PlotOrientation.VERTICAL, true,true, false);
		chart.setBackgroundPaint(Color.white);	        
		chart.getTitle().setPaint(Color.black); 
			        
		CategoryPlot p = chart.getCategoryPlot(); 
		p.setRangeGridlinePaint(Color.black);
			         
		ChartPanel chartPanel = new ChartPanel(chart);	        
		panel.add(chartPanel);
		
		dialog.add(panel);
		dialog.setSize(700, 400);
		dialog.setVisible(true);
	}
	
	void mostrarHistogramaAcumulativo() {
		JPanel panel = new JPanel();
		JDialog dialog = new JDialog();
		Hashtable<Integer, Integer> pixeles = obtenerArrayPixelesAcumulativo();
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		
		
	    /*for(int i = 0; i < 255; i++)
	    		if(pixeles.containsKey(i))
	    			dataset.setValue(pixeles.get(i), 1 + "", i + "");
	    		else
	    			dataset.setValue(1, 1 + "", 0 + "");*/
		
		JFreeChart chart = ChartFactory.createBarChart3D("Histograma Acumulativo","Pixel", "Value", dataset, PlotOrientation.VERTICAL, true,true, false);
		chart.setBackgroundPaint(Color.white);	        
		chart.getTitle().setPaint(Color.black); 
			        
		CategoryPlot p = chart.getCategoryPlot(); 
		p.setRangeGridlinePaint(Color.black);
			         
		ChartPanel chartPanel = new ChartPanel(chart);	
		panel.add(chartPanel);
		
		dialog.setResizable(false);
		dialog.add(panel);
		dialog.setSize(700, 400);
		dialog.setVisible(true);
	}
	
	Hashtable<Integer, Integer> obtenerArrayPixelesAcumulativo() {
		Hashtable<Integer, Integer> hash = null;//obtenerArrayPixeles();
		hash = sumaHash(hash);
	
		return hash;
	}
	
	Hashtable<Integer, Integer> sumaHash(Hashtable<Integer, Integer> hash) {
		Hashtable<Integer, Integer> aux = new Hashtable<>();
		Integer num = new Integer(0);
		
		for(int i = 0; i < hash.size(); i++) {
			for(int j = 0; j < i; j++)
				if(hash.containsKey(j))
					num += hash.get(j);
			
			aux.put(i, num);
			num = 0;
		}
		return aux;
	}
	
	void modificarBrilloContraste() {
		BufferedImage imagen = getBufferedImageActual();
		  
		float A = (float) ((float) (this.getContrasteSlider().getValue()) / (getContraste(imagen)));
		float B = this.getBrilloSlider().getValue() - (float) (getBrillo(imagen) * A);
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

		  this.getFocoImagenActual().getContentPane().removeAll();
		  this.getFocoImagenActual().getContentPane().add(new JLabel(new ImageIcon(newImg)));
		  this.getFocoImagenActual().revalidate();
		  this.setImageIconActual(new ImageIcon(newImg));
		  		
		/*int sumatorio = 0;
		 float A = (float) ((float) (ncontr) / (this.contraste));
		 float B = nbrillo - (float) (brillo * A);
		
		 ArrayList<Integer> lut = new ArrayList<Integer>();

		 for (int i = 0; i < 256; i++) {
			 lut.add(compruebarango(0, 255, (int) (Math.round(A * i + B))));
		 }
		
		Graphics g = imagen.createGraphics();
		g.drawImage(imageActual(), 0, 0, null);
		
		for(int i = 0; i < imagen.getWidth(); i++)
			for(int j = 0; j < imagen.getHeight(); j++) {
				sumatorio += imagen.getRGB(i, j);
				Color color = new Color(imagen.getRGB(i, j));
				
			}
		
		System.out.println(sumatorio / (imagen.getWidth() * imagen.getHeight())); */
		
		
		
		//double valorContraste = this.getContrasteSlider().getValue();
		/*valorContraste /= 100;
		
		BufferedImageOp operacion = new RescaleOp((float)valorContraste, this.getBrilloSlider().getValue(), null);
		imagen = operacion.filter(imagen, null);
		
		this.getFocoImagenActual().getContentPane().removeAll();
		this.getFocoImagenActual().getContentPane().add(new JLabel(new ImageIcon(imagen)));
		
		this.getFocoImagenActual().revalidate();   */
	}
	
	void actualizarIconImage() {
		this.getFocoImagenActual().setIconImage(this.getImageIconActual().getImage());
	}
	
	int[] getHistograma(BufferedImage imagen) {
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
	    return histo;
	}
	
	float getBrillo(BufferedImage imagen) {
		float temp = 0;
	    float size = imagen.getHeight() * imagen.getWidth();
	    int[] histo = getHistograma(imagen);
	    
	    for (int i = 0; i < 256; i++) 
	      temp += (float) (histo[i] * i);
	    
	    return (float) (temp / size);
	}
	
	float getContraste(BufferedImage imagen) {
		float temp1 = 0;
	    float size1 = imagen.getHeight() * imagen.getWidth();
	    float u = getBrillo(imagen);
	    int[] histo = getHistograma(imagen);
	   
	    for (int i = 0; i < 256; i++) 
	      temp1 += Math.pow((i - u), 2) * histo[i];
	  
	    return (float) Math.sqrt(temp1 / size1);
	}
	
	void addImagen(JDialog dialog){
		dialog.addWindowListener(new WindowAdapter(){
    			public void windowClosed(WindowEvent e){
    				getMisImagenes().remove(e.getSource());
    			}
		});
    
		this.setFocoImagenActual(dialog);
		this.getMisImagenes().add(dialog);
	}
	
	Image imageActual(){
		return this.getFocoImagenActual().getIconImages().get(0);
	}
	
	BufferedImage getBufferedImageActual() {
		BufferedImage aux = new BufferedImage(imageActual().getWidth(null), imageActual().getHeight(null), BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = aux.createGraphics();
		g.drawImage(imageActual(), 0, 0, null);   
		
		return aux;
	}
	
    void init() {
    		this.setResizable(false);
    		this.getMiPanel().setLayout(null);    
        this.getMiPanel().add(this.getEtiquetaImagen());
        this.getMiPanel().add(this.getBotonBlancoNegro());
        add(this.getMiPanel());
        
        this.getBarraMenu().add(this.getMenuFile());
        this.getBarraMenu().add(this.getMenuShow());
        this.getBarraMenu().add(this.getMenuImage());
         
        this.getMenuFile().add(this.getItemNew());
        this.getItemNew().add(this.getItemImage());
        
        this.getMenuShow().add(this.getItemHistograma());
        this.getMenuShow().add(this.getItemHistogramaAcumulativo());
        
        this.getMenuImage().add(this.getItemShowInfo());
        this.getMenuImage().add(this.getItemAdjust());
        this.getItemAdjust().add(this.getItemBrightnessContrast());
         
        this.setJMenuBar(this.getBarraMenu());
        this.setSize(500, 74);
        this.setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 10);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }   
    
    void iniciarBrilloContraste() {
    		JPanel panel = new JPanel();
    		panel.setLayout(null);
    		 		
    		this.getBrilloContraste().setTitle("Brillo/Contraste");
    		this.getBrilloContraste().setSize(200, 340);
    		this.getBrilloContraste().setResizable(false);
    		
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
    		
    		this.getBrilloContraste().add(panel);
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
    
    void refrescar(){
    		this.getMiPanel().add(this.getEtiquetaImagen());
        add(this.getMiPanel());
    }
    
    private Integer compruebarango(int min, int max, int value) {
        int value_check = Math.abs(value);
        if (value < min) {
          return min;
        } else if (value_check > max) {
          return max;
        }
        return value_check;
      }
    
    private boolean isNumeric(String cadena){
    		try {
    			Integer.parseInt(cadena);
    			return true;
    		} catch (NumberFormatException nfe){
    			return false;
    		}
    }
    
    void mostrarInformacion() {
    	
    }
    
    public JMenuBar getBarraMenu() {
		return barraMenu;
	}
	public void setBarraMenu(JMenuBar barraMenu) {
		this.barraMenu = barraMenu;
	}
	public JMenu getMenuImagen() {
		return menuFile;
	}
	public void setMenuImagen(JMenu menuImagen) {
		this.menuFile = menuImagen;
	}
	public JMenu getItemNew() {
		return itemNew;
	}
	public void setItemNew(JMenu itemNew) {
		this.itemNew = itemNew;
	}
	public JMenuItem getItemImage() {
		return itemImage;
	}
	public void setItemImage(JMenuItem itemImage) {
		this.itemImage = itemImage;
	}

	public JLabel getEtiquetaImagen() {
		return etiquetaImagen;
	}
	public void setEtiquetaImagen(JLabel etiquetaImagen) {
		this.etiquetaImagen = etiquetaImagen;
	}

	public Panel getMiPanel() {
		return miPanel;
	}
	public void setMiPanel(Panel miPanel) {
		this.miPanel = miPanel;
	}
	
	public JButton getBotonBlancoNegro(){
		return botonBlancoNegro;
	}
	
	public void setBotonBlancoNegro(JButton botonBlancoNegro){
		this.botonBlancoNegro = botonBlancoNegro;
	}

	public Integer getNumeroImagen() {
		return numeroImagen;
	}

	public void setNumeroImagen(Integer numeroImagen) {
		this.numeroImagen = numeroImagen;
	}

	public ArrayList<JDialog> getMisImagenes() {
		return misImagenes;
	}

	public void setMisImagenes(ArrayList<JDialog> misImagenes) {
		this.misImagenes = misImagenes;
	}

	public JDialog getFocoImagenActual() {
		return focoImagenActual;
	}

	public void setFocoImagenActual(JDialog focoImagenActual) {
		this.focoImagenActual = focoImagenActual;
	}

	public JMenu getMenuFile() {
		return menuFile;
	}

	public void setMenuFile(JMenu menuFile) {
		this.menuFile = menuFile;
	}

	public JMenuItem getItemBrightnessContrast() {
		return itemBrightnessContrast;
	}

	public void setItemBrightnessContrast(JMenuItem itemBrightnessContrast) {
		this.itemBrightnessContrast = itemBrightnessContrast;
	}

	public JDialog getBrilloContraste() {
		return brilloContraste;
	}

	public void setBrilloContraste(JDialog brilloContraste) {
		this.brilloContraste = brilloContraste;
	}

	public JMenu getMenuImage() {
		return menuImage;
	}

	public void setMenuImage(JMenu menuImage) {
		this.menuImage = menuImage;
	}

	public JMenu getItemAdjust() {
		return itemAdjust;
	}

	public void setItemAdjust(JMenu itemAdjust) {
		this.itemAdjust = itemAdjust;
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

	public JTextField getBrilloTextField() {
		return brilloTextField;
	}

	public void setBrilloTextField(JTextField brilloTextField) {
		this.brilloTextField = brilloTextField;
	}

	public JTextField getContrasteTextField() {
		return contrasteTextField;
	}

	public void setContrasteTextField(JTextField contrasteTextField) {
		this.contrasteTextField = contrasteTextField;
	}

	public JMenu getMenuShow() {
		return menuShow;
	}

	public void setMenuShow(JMenu menuShow) {
		this.menuShow = menuShow;
	}

	public JMenuItem getItemHistograma() {
		return itemHistograma;
	}

	public void setItemHistograma(JMenuItem itemHistograma) {
		this.itemHistograma = itemHistograma;
	}

	public JMenuItem getItemHistogramaAcumulativo() {
		return itemHistogramaAcumulativo;
	}

	public void setItemHistogramaAcumulativo(JMenuItem itemHistogramaAcumulativo) {
		this.itemHistogramaAcumulativo = itemHistogramaAcumulativo;
	}

	public JMenuItem getItemShowInfo() {
		return itemShowInfo;
	}

	public void setItemShowInfo(JMenuItem itemShowInfo) {
		this.itemShowInfo = itemShowInfo;
	}

	public VentanaBrilloContraste getVentanaBrilloContraste() {
		return ventanaBrilloContraste;
	}

	public void setVentanaBrilloContraste(VentanaBrilloContraste ventanaBrilloContraste) {
		this.ventanaBrilloContraste = ventanaBrilloContraste;
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

	public ImageIcon getImageIconActual() {
		return imageIconActual;
	}

	public void setImageIconActual(ImageIcon imageIconActual) {
		this.imageIconActual = imageIconActual;
	}
}
