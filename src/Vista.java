import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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


public class Vista extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int JDIALOG_BORDE = 22;
	private Imagen imagen;
	private JPanel panel;
	private Recortar recortar;
	private JMenuBar barraMenu;
    private JMenu menuFile, itemNew;
    private JMenu menuImage, itemAdjust, itemDigitalize, itemMenuRotate;
    private JMenu menuShow;
    private JMenu menuProcess;
    private JMenuItem itemImage;
    private JMenuItem itemShowInfo;
    private JMenuItem itemBrightnessContrast;
    private JMenuItem itemHistograma, itemHistogramaAcumulativo;
    private JMenuItem itemDiference;
    private JMenuItem itemGamma;
    private JMenuItem itemSection;
    private JMenuItem itemScale;
    private JMenuItem itemSampling;
    private JMenuItem itemQuantization;
    private JMenuItem itemFlipH;
    private JMenuItem itemFlipV;
    private JMenuItem itemFlipZ;
    private JMenuItem itemRotate;
    private JButton botonBlancoNegro;
    private JButton botonTijera;
    private String rutaImagen;
    private Integer numeroImagen;
    private ArrayList<Imagen> imagenes;
    private Imagen focoImagenActual;
    private ImageIcon imageIconActual;
    private JLabel etiquetaCoordenadas;
    private JLabel etiquetaColorCoordenadas;

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
        this.setItemDiference(new JMenuItem("Diference"));
        this.setItemDigitalize(new JMenu("Digitalize"));
        this.setItemSampling(new JMenuItem("Sampling"));
        this.setItemQuantization(new JMenuItem("Quantization"));
        this.setItemScale(new JMenuItem("Scale"));
        this.setItemMenuRotate(new JMenu("Rotate"));
        this.setItemFlipH(new JMenuItem("FLip Horizontally"));
        this.setItemFlipV(new JMenuItem("FLip Vertically"));
        this.setItemFlipZ(new JMenuItem("FLip Z"));
        this.setItemRotate(new JMenuItem("Rotate degrees"));
        
        this.setBotonBlancoNegro(new JButton());
        this.setBotonTijera(new JButton());
        
        this.setMenuProcess(new JMenu("Process"));
        this.setItemGamma(new JMenuItem("Gamma"));
        this.setItemSection(new JMenuItem("Section"));
            
        this.setPanel(new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
        		public void paintComponent(Graphics g) {
        			super.paintComponent(g);         				
        			addLabel(g);
        			addColorRect(g);
        		}
        });
        this.setNumeroImagen(1);
        this.setImagenes(new ArrayList<>());
       	this.setEtiquetaCoordenadas(new JLabel());
       	this.setEtiquetaColorCoordenadas(new JLabel());
    }
    
    private void clearEtiquetaPanel() {
    		this.getPanel().remove(this.getEtiquetaCoordenadas());
    		this.getPanel().remove(this.getEtiquetaColorCoordenadas());
    }
    
    private void addLabel(Graphics g) {
		this.getPanel().add(this.getEtiquetaCoordenadas());
		this.getPanel().add(this.getEtiquetaCoordenadas());
    }
    
    private void addColorRect(Graphics g) {
    		String cadena = this.getEtiquetaCoordenadas().getText();
    		
    		if(cadena.length() > 0) {
    			Graphics2D g2d = (Graphics2D) g;
    			String x = cadena.substring(3, 6);
    			String y = "";

    			x = x.replace("Y", "");
    			x = x.replace(" ", "");
    			
    			if(x.length() < 3)
    				y = cadena.substring(8, 12);
    			else
    				y = cadena.substring(10, 13);
    			
    			y = y.replace("V", "");
    			y = y.replace(" ", "");
    			 
    			g2d.setColor(new Color(this.getFocoImagenActual().getValorPixelRGB(new Integer(x), new Integer(y))));
    			g2d.fill(new Rectangle2D.Double(this.getEtiquetaCoordenadas().getWidth() + 42, 5, 30, 19));
    		}		
    }
    
    void actualizarEtiqueta(String cadena, int valor) {
		clearEtiquetaPanel();
		this.getEtiquetaCoordenadas().setText(cadena);
		this.getEtiquetaCoordenadas().setBounds(75, -85, 200, 200);		
    }
    
    void init() {
		this.setResizable(false);
		this.getPanel().setLayout(null);
		this.getPanel().add(this.getBotonBlancoNegro());
		this.getPanel().add(this.getBotonTijera());
		this.add(this.getPanel());
    
		this.getBarraMenu().add(this.getMenuFile());
		this.getBarraMenu().add(this.getMenuShow());
		this.getBarraMenu().add(this.getMenuImage());
		this.getBarraMenu().add(this.getMenuProcess());
     
		this.getMenuFile().add(this.getItemNew());
		this.getItemNew().add(this.getItemImage());
    
		this.getMenuShow().add(this.getItemHistograma());
		this.getMenuShow().add(this.getItemHistogramaAcumulativo());
    
		this.getMenuImage().add(this.getItemShowInfo());
		this.getMenuImage().add(this.getItemAdjust());
		this.getItemAdjust().add(this.getItemBrightnessContrast());
		this.getMenuImage().add(this.getItemDiference());
		this.getMenuImage().addSeparator();
		this.getMenuImage().add(this.getItemDigitalize());
		this.getItemDigitalize().add(this.getItemSampling());
		this.getItemDigitalize().add(this.getItemQuantization());
		this.getMenuImage().addSeparator();
		this.getMenuImage().add(this.getItemScale());
		this.getMenuImage().add(this.getItemMenuRotate());
		this.getItemMenuRotate().add(this.getItemFlipH());
		this.getItemMenuRotate().add(this.getItemFlipV());
		this.getItemMenuRotate().add(this.getItemFlipZ());
		this.getItemMenuRotate().add(this.getItemRotate());
		
		this.getMenuProcess().add(this.getItemGamma());
		this.getMenuProcess().add(this.getItemSection());
     
		this.setJMenuBar(this.getBarraMenu());
		this.setSize(500, 74);
		this.setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 10);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
    }   
    
    void iniciarBotones() throws IOException{
		Image imagenBlancoNegro = ImageIO.read(new File("imagenesBotones/bw.jpg"));
		Image imagenTijera = ImageIO.read(new File("imagenesBotones/tijera.jpg"));
		
		imagenBlancoNegro = imagenBlancoNegro.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		imagenTijera = imagenTijera.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		
		this.getBotonBlancoNegro().setIcon(new ImageIcon(imagenBlancoNegro));
		this.getBotonBlancoNegro().setBounds(new Rectangle(0, 0, 30, 30));
		
		this.getBotonTijera().setIcon(new ImageIcon(imagenTijera));
		this.getBotonTijera().setBounds(new Rectangle(31, 0, 30, 30));
    }
    
    void openJDialog() throws IOException{
    		JDialog dialog = new JDialog();     
    		this.setRecortar(new Recortar());
    		
    		BufferedImage imagen1 = ImageIO.read(new File(this.getRutaImagen()));
    	
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle("Imagen " + this.numeroImagen);
        this.numeroImagen++;
        Image toolkitImage = imagen1;
                  
        while((toolkitImage.getWidth(dialog) > 750) || (toolkitImage.getHeight(dialog) > 750)) 
        		toolkitImage = imagen1.getScaledInstance(toolkitImage.getWidth(dialog) / 2, toolkitImage.getHeight(dialog) / 2, Image.SCALE_SMOOTH);

    		BufferedImage newImage = new BufferedImage(toolkitImage.getWidth(dialog), toolkitImage.getHeight(dialog), 
    		      BufferedImage.TYPE_INT_ARGB);
    		
    		Graphics g = newImage.getGraphics();
    		g.drawImage(toolkitImage, 0, 0, null);
    		g.dispose();
    		
        @SuppressWarnings("serial")
		JPanel panel = new JPanel() {
        		@Override
        		public void paintComponent(Graphics g) {
        			super.paintComponent(g); 
        			g.drawImage(newImage, 0, 0, null);
        			getRecortar().pintarRectangulo(g);
        		}
        };

        dialog.add(panel);
        dialog.setIconImage(toolkitImage);
        dialog.setSize(toolkitImage.getWidth(null), toolkitImage.getHeight(null) + JDIALOG_BORDE); //Incrementando tamaño para insertar coordenadas y valor del píxel.
        dialog.setResizable(false);
       
        addImagen(dialog);
    }
    	
	void addImagen(JDialog dialog){
		Imagen aux = new Imagen(dialog);
	
		dialog.addWindowListener(new WindowAdapter(){
    			public void windowClosed(WindowEvent e){
    				for(Imagen imagen: getImagenes())
    					if(e.getSource() == imagen.getContenedor()) {
    						getImagenes().remove(imagen);
    						break;
    					}
    			}
		});
    
		aux.setRecortar(this.getRecortar());
		aux.setPanel((JPanel)dialog.getContentPane());
		this.setFocoImagenActual(aux);
		this.getFocoImagenActual().setRutaImagen(this.getRutaImagen());
		this.getImagenes().add(aux);
		this.getFocoImagenActual().getContenedor().setVisible(true);
	}
	
	void addImagen(Imagen imagen, String titulo, int xLocation, int yLocation) {
		JDialog dialog = imagen.getContenedor();
		
		 @SuppressWarnings("serial")
			JPanel panel = new JPanel() {
	        		@Override
	        		public void paintComponent(Graphics g) {
	        			super.paintComponent(g); 
	        			g.drawImage(imagen.getImagen(), 0, 0, null);
	        			getRecortar().pintarRectangulo(g);
	        		}
	        };

	    dialog.add(panel);
		dialog.setIconImage(imagen.getImagen());
		dialog.setTitle(titulo);
		dialog.setLocation(xLocation, yLocation);
		dialog.pack();
		dialog.setLocationByPlatform(true);
		dialog.setResizable(false);
		
		dialog.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
				for(Imagen imagen: getImagenes())
					if(e.getSource() == imagen.getContenedor()) {
						getImagenes().remove(imagen);
						break;
					}
			}
		});
		
		imagen.setContenedor(dialog);
		imagen.getContenedor().setVisible(true);
		this.setFocoImagenActual(imagen);
		this.getFocoImagenActual().setRutaImagen(this.getRutaImagen());
		this.getImagenes().add(imagen);
	}
	
	void modificarImagen(Imagen newImg) {
		this.getFocoImagenActual().getContenedor().getContentPane().removeAll();
		
		@SuppressWarnings("serial")
		JPanel panel = new JPanel() {
    			@Override
    			public void paintComponent(Graphics g) {
    				super.paintComponent(g); 
    				g.drawImage(newImg.getImagen(), 0, 0, null);
    				getRecortar().pintarRectangulo(g);
    			}
		};

    		this.getFocoImagenActual().getContenedor().add(panel);
    		this.getFocoImagenActual().getContenedor().revalidate();
    		this.getFocoImagenActual().getContenedor().setIconImage(newImg.getImagen());
		this.setImageIconActual(new ImageIcon(newImg.getImagen()));
		this.getFocoImagenActual().setModificada(true);
		this.getFocoImagenActual().setBrillo(newImg.getBrillo());
		this.getFocoImagenActual().setContraste(newImg.getContraste());
		this.getFocoImagenActual().setHistograma();
	}	
	
    public ArrayList<Imagen> getImagenes() {
		return imagenes;
	}

	public void setImagenes(ArrayList<Imagen> imagenes) {
		this.imagenes = imagenes;
	}

	public Imagen getImagen() {
		return imagen;
	}

	public void setImagen(Imagen imagen) {
		this.imagen = imagen;
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

	public JButton getBotonBlancoNegro(){
		return botonBlancoNegro;
	}
	
	public void setBotonBlancoNegro(JButton botonBlancoNegro){
		this.botonBlancoNegro = botonBlancoNegro;
	}

	public JButton getBotonTijera() {
		return botonTijera;
	}

	public void setBotonTijera(JButton botonTijera) {
		this.botonTijera = botonTijera;
	}

	public Integer getNumeroImagen() {
		return numeroImagen;
	}

	public void setNumeroImagen(Integer numeroImagen) {
		this.numeroImagen = numeroImagen;
	}

	public Imagen getFocoImagenActual() {
		return focoImagenActual;
	}

	public void setFocoImagenActual(Imagen focoImagenActual) {
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

	public ImageIcon getImageIconActual() {
		return imageIconActual;
	}

	public void setImageIconActual(ImageIcon imageIconActual) {
		this.imageIconActual = imageIconActual;
	}

	public String getRutaImagen() {
		return rutaImagen;
	}

	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

	public JMenuItem getItemDiference() {
		return itemDiference;
	}

	public void setItemDiference(JMenuItem itemDiference) {
		this.itemDiference = itemDiference;
	}

	public JMenu getMenuProcess() {
		return menuProcess;
	}

	public void setMenuProcess(JMenu menuProcess) {
		this.menuProcess = menuProcess;
	}

	public JMenuItem getItemGamma() {
		return itemGamma;
	}

	public void setItemGamma(JMenuItem itemGamma) {
		this.itemGamma = itemGamma;
	}
	
	public JMenuItem getItemSection() {
		return itemSection;
	}

	public void setItemSection(JMenuItem itemSection) {
		this.itemSection = itemSection;
	}

	/**
	 * @return the menuDigitalize
	 */
	public JMenu getItemDigitalize() {
		return itemDigitalize;
	}

	/**
	 * @param menuDigitalize the menuDigitalize to set
	 */
	public void setItemDigitalize(JMenu menuDigitalize) {
		this.itemDigitalize = menuDigitalize;
	}

	/**
	 * @return the itemSampling
	 */
	public JMenuItem getItemSampling() {
		return itemSampling;
	}

	/**
	 * @param itemSampling the itemSampling to set
	 */
	public void setItemSampling(JMenuItem itemSampling) {
		this.itemSampling = itemSampling;
	}

	/**
	 * @return the itemQuantization
	 */
	public JMenuItem getItemQuantization() {
		return itemQuantization;
	}

	/**
	 * @param itemQuantization the itemQuantization to set
	 */
	public void setItemQuantization(JMenuItem itemQuantization) {
		this.itemQuantization = itemQuantization;
	}
	
	public JLabel getEtiquetaCoordenadas() {
		return etiquetaCoordenadas;
	}

	public void setEtiquetaCoordenadas(JLabel etiquetaCoordenadas) {
		this.etiquetaCoordenadas = etiquetaCoordenadas;
	}

	public JLabel getEtiquetaColorCoordenadas() {
		return etiquetaColorCoordenadas;
	}

	public void setEtiquetaColorCoordenadas(JLabel etiquetaColorCoordenadas) {
		this.etiquetaColorCoordenadas = etiquetaColorCoordenadas;
	}

	public JMenuItem getItemScale() {
		return itemScale;
	}

	public void setItemScale(JMenuItem itemScale) {
		this.itemScale = itemScale;
	}

	public JMenu getItemMenuRotate() {
		return itemMenuRotate;
	}

	public void setItemMenuRotate(JMenu itemMenuRotate) {
		this.itemMenuRotate = itemMenuRotate;
	}

	public JMenuItem getItemFlipH() {
		return itemFlipH;
	}

	public void setItemFlipH(JMenuItem itemFlipH) {
		this.itemFlipH = itemFlipH;
	}

	public JMenuItem getItemFlipV() {
		return itemFlipV;
	}

	public void setItemFlipV(JMenuItem itemFlipV) {
		this.itemFlipV = itemFlipV;
	}

	public JMenuItem getItemFlipZ() {
		return itemFlipZ;
	}

	public void setItemFlipZ(JMenuItem itemFlipZ) {
		this.itemFlipZ = itemFlipZ;
	}

	public JMenuItem getItemRotate() {
		return itemRotate;
	}

	public void setItemRotate(JMenuItem itemRotate) {
		this.itemRotate = itemRotate;
	}
}
