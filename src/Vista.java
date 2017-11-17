import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
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
        this.setBotonBlancoNegro(new JButton());
        
        this.setNumeroImagen(1);
        this.setEtiquetaImagen(new JLabel());
        this.setMiPanel(new Panel());
        this.setMisImagenes(new ArrayList<>());
        this.setImagenes(new ArrayList<>());
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
		
	void addImagen(JDialog dialog){
		Imagen aux = new Imagen(dialog);
	
		dialog.addWindowListener(new WindowAdapter(){
    			public void windowClosed(WindowEvent e){
    				for(Imagen imagen: getImagenes())
    					if(e.getSource() == imagen.getContenedor())
    						getImagenes().remove(imagen);
    			}
		});
    
		this.setFocoImagenActual(aux);
		this.getImagenes().add(aux);
	}
	
	void modificarImagen(BufferedImage newImg) {
		this.getFocoImagenActual().getContenedor().getContentPane().removeAll();
		this.getFocoImagenActual().getContenedor().getContentPane().add(new JLabel(new ImageIcon(newImg)));
		this.getFocoImagenActual().getContenedor().revalidate();
		this.setImageIconActual(new ImageIcon(newImg));
		this.getFocoImagenActual().setModificada(true);
	}	
	
    void mostrarInformacion() {
    	
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

	public VentanaBrilloContraste getVentanaBrilloContraste() {
		return ventanaBrilloContraste;
	}

	public void setVentanaBrilloContraste(VentanaBrilloContraste ventanaBrilloContraste) {
		this.ventanaBrilloContraste = ventanaBrilloContraste;
	}
	
	public ImageIcon getImageIconActual() {
		return imageIconActual;
	}

	public void setImageIconActual(ImageIcon imageIconActual) {
		this.imageIconActual = imageIconActual;
	}
}
