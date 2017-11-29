import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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

public class Vista extends JFrame{
	private static final long serialVersionUID = 1L;
	private Imagen imagen;
	private JMenuBar barraMenu;
    private JMenu menuFile, itemNew;
    private JMenu menuImage, itemAdjust, itemDigitalize;
    private JMenu menuShow;
    private JMenu menuProcess;
    private JMenuItem itemImage;
    private JMenuItem itemShowInfo;
    private JMenuItem itemBrightnessContrast;
    private JMenuItem itemHistograma, itemHistogramaAcumulativo;
    private JMenuItem itemDiference;
    private JMenuItem itemGamma;
    private JMenuItem itemSampling;
    private JMenuItem itemQuantization;
    private JButton botonBlancoNegro;
    private JButton botonTijera;
    private JLabel etiquetaImagen;
    private String rutaImagen;
    private Integer numeroImagen;
    private ArrayList<Imagen> imagenes;
    private Imagen focoImagenActual;
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
        this.setItemDiference(new JMenuItem("Diference"));
        this.setItemDigitalize(new JMenu("Digitalize"));
        this.setItemSampling(new JMenuItem("Sampling"));
        this.setItemQuantization(new JMenuItem("Quantization"));
        
        this.setBotonBlancoNegro(new JButton());
        this.setBotonTijera(new JButton());
        
        this.setMenuProcess(new JMenu("Process"));
        this.setItemGamma(new JMenuItem("Gamma"));
        
        this.setNumeroImagen(1);
        this.setEtiquetaImagen(new JLabel());
        this.setImagenes(new ArrayList<>());
    }
    
    void init() {
		this.setResizable(false);
		this.setLayout(null);
		add(this.getEtiquetaImagen());
		add(this.getBotonBlancoNegro());
		add(this.getBotonTijera());
    
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
		this.getMenuImage().add(this.getItemDigitalize());
		this.getItemDigitalize().add(this.getItemSampling());
		this.getItemDigitalize().add(this.getItemQuantization());
		
		this.getMenuProcess().add(this.getItemGamma());
     
		this.setJMenuBar(this.getBarraMenu());
		this.setSize(500, 74);
		this.setLocation((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2, (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 10);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
    }   
    
    void openJDialog() throws IOException{
    		JDialog dialog = new JDialog();     
    		JPanel panel = new JPanel();
    		Image imagen = ImageIO.read(new File(this.getRutaImagen()));
    	
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setTitle("Imagen " + this.numeroImagen);
        this.numeroImagen++;
                  
        while((imagen.getWidth(dialog) > 750) || (imagen.getHeight(dialog) > 750))
        		imagen = imagen.getScaledInstance(imagen.getWidth(dialog) / 2, imagen.getHeight(dialog) / 2, 0);
        
        panel.add(new JLabel(new ImageIcon(imagen)));
        dialog.add(panel);
        dialog.setIconImage(imagen);
        dialog.pack();
        dialog.setResizable(false);
       
        addImagen(dialog);
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
		
	void addImagen(JDialog dialog){
		JPanel panel = new JPanel() { 
			
			
			
			private static final long serialVersionUID = 1L;

			public void paint (Graphics g) { 
				super.paint(g); 
				g.setColor(new Color(255, 0, 0));
				g.drawLine(0,0, 500, 500);
			} 
		};
		dialog.add(panel);
		dialog.setSize(500, 500);
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
    
		this.setFocoImagenActual(aux);
		this.getFocoImagenActual().setRutaImagen(this.getRutaImagen());
		this.getImagenes().add(aux);
		this.getFocoImagenActual().getContenedor().setVisible(true);
	}
	
	void addImagen(Imagen imagen, String titulo, int xLocation, int yLocation) {
		JDialog dialog = imagen.getContenedor();
		
		dialog.add(new JLabel(new ImageIcon(imagen.getImagen())));
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
		this.getFocoImagenActual().getContenedor().getContentPane().add(new JLabel(new ImageIcon(newImg.getImagen())));
		this.getFocoImagenActual().getContenedor().revalidate();
		this.setImageIconActual(new ImageIcon(newImg.getImagen()));
		this.getFocoImagenActual().setModificada(true);
		this.getFocoImagenActual().setBrillo(newImg.getBrillo());
		this.getFocoImagenActual().setContraste(newImg.getContraste());
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
}
