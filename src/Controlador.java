import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Controlador {
	private Vista miVista;
	private Informacion informacion;
	private BlancoNegro blancoNegro;
	private BrilloContraste brilloContraste;
	private Histograma histograma;
	private Diferencia diferencia;
	private Gamma gamma;
	
	public Controlador(){
		this.setMiVista(new Vista());
		
		this.setInformacion(new Informacion());
		this.setBlancoNegro(new BlancoNegro());
		this.setBrilloContraste(new BrilloContraste());
		this.setHistograma(new Histograma());
		this.setDiferencia(new Diferencia());
		this.setGamma(new Gamma());
		
		this.getMiVista().getItemImage().addActionListener(new Oyente());
		this.getMiVista().getItemHistograma().addActionListener(new Oyente());
		this.getMiVista().getItemHistogramaAcumulativo().addActionListener(new Oyente());
		this.getMiVista().getBotonBlancoNegro().addActionListener(new Oyente());
		this.getMiVista().getItemShowInfo().addActionListener(new Oyente());
		this.getMiVista().getItemBrightnessContrast().addActionListener(new Oyente());
		this.getMiVista().getItemDiference().addActionListener(new Oyente());
		this.getMiVista().getItemGamma().addActionListener(new Oyente());
		this.getBrilloContraste().getBrilloTextField().addActionListener(new Oyente());
		this.getBrilloContraste().getDefaultBrillo().addActionListener(new Oyente());
		this.getBrilloContraste().getDefaultContraste().addActionListener(new Oyente());
		this.getBrilloContraste().getContrasteTextField().addActionListener(new Oyente());
		
		this.getBrilloContraste().getBrilloSlider().addChangeListener(new SliderListener());
		this.getBrilloContraste().getContrasteSlider().addChangeListener(new SliderListener());	
		this.getGamma().getSlider().addChangeListener(new SliderListener());
		
		getBrilloContraste().getVentana().addWindowListener(new OyenteVentana());
	}
	
	void iniciarComponentes() throws IOException{
		this.getMiVista().iniciarBotones();
		this.getBrilloContraste().init();
		this.getGamma().init();
		
		this.getMiVista().init();
	}
	
	void addEventosRaton(){
		for(Imagen imagen: this.getMiVista().getImagenes())
			imagen.getContenedor().addMouseListener(new EventoRaton());		
	}
	
	class Oyente implements ActionListener{
		public Oyente()
		{}
		
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == getMiVista().getItemImage()){
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle("Choose an image:");
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.setFileFilter(new FileNameExtensionFilter("jpg, png, gif, tif", "jpg", "png", "gif", "tif"));
				int sel = jfc.showOpenDialog(null);
				if (sel == JFileChooser.APPROVE_OPTION) {
			        getMiVista().setRutaImagen(jfc.getSelectedFile().getAbsolutePath());
			        try {
						getMiVista().openJDialog();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				addEventosRaton();
			}
			
			else if(e.getSource() == getBrilloContraste().getBrilloTextField())
				getBrilloContraste().actualizarSliderBrillo();
			
			else if(e.getSource() == getBrilloContraste().getContrasteTextField())
				getBrilloContraste().actualizarSliderContraste();
			
			else if(getMiVista().getImagenes().size() > 0) {
				if(e.getSource() == getMiVista().getBotonBlancoNegro()) {
					getBlancoNegro().init();
					
					getBlancoNegro().convertir(getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getBlancoNegro().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemShowInfo()) {
					JDialog dialog = getMiVista().getFocoImagenActual().getContenedor();
					
					getInformacion().init(getMiVista().getFocoImagenActual());
					getInformacion().mostrar(dialog.getWidth() + (int)dialog.getLocation().getX() + 100, (int)dialog.getLocation().getY());
				}
			
				else if(e.getSource() == getMiVista().getItemBrightnessContrast()) {
					Imagen imagenActual = getMiVista().getFocoImagenActual();
					
					getBrilloContraste().setBrilloInicial(getMiVista().getFocoImagenActual().getBrillo());
					getBrilloContraste().setContrasteInicial(getMiVista().getFocoImagenActual().getContraste());
					
					if(!imagenActual.getModificada())
						getBrilloContraste().actualizarPanel(imagenActual);
					getBrilloContraste().mostrar(imagenActual.getContenedor());
				}
				
				else if(e.getSource() == getMiVista().getItemDiference()) {
					getDiferencia().setImagen1(getMiVista().getFocoImagenActual());
					
					try {
						getDiferencia().addImagenDiferencia();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					getDiferencia().setImagen2(getBlancoNegro().get(getDiferencia().getImagen2()));
					getMiVista().addImagen(getDiferencia().generar().getContenedor());
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemGamma()) {
					getGamma().mostrar(getMiVista().getFocoImagenActual().getContenedor());
				}
				
				else if(e.getSource() == getBrilloContraste().getDefaultBrillo()) 
					getBrilloContraste().getBrilloSlider().setValue((int) getBrilloContraste().getBrilloInicial());

				else if(e.getSource() == getBrilloContraste().getDefaultContraste()) 
					getBrilloContraste().getContrasteSlider().setValue((int) getBrilloContraste().getContrasteInicial());
			
				else if(e.getSource() == getMiVista().getItemHistograma()) 
					getHistograma().mostrarHistogramaAbsoluto(getMiVista().getFocoImagenActual());

				else if(e.getSource() == getMiVista().getItemHistogramaAcumulativo())
					getHistograma().mostrarHistogramaAcumulativo(getMiVista().getFocoImagenActual());
			}
		}
    }
	
	class EventoRaton implements MouseListener{
		@Override
		public void mousePressed(MouseEvent e){
			getMiVista().setFocoImagenActual(new Imagen((JDialog) e.getSource()));
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
	
		}		
	}
	
	class SliderListener implements ChangeListener {
	    public void stateChanged(ChangeEvent e) {
	    		if(e.getSource() == getBrilloContraste().getBrilloSlider()) {
	    			getBrilloContraste().actualizarTextBrillo();
	    			getMiVista().modificarImagen(getBrilloContraste().modificarBrilloContraste(getMiVista().getFocoImagenActual()));
	    		}
	        
	    		else if(e.getSource() == getBrilloContraste().getContrasteSlider()) {
	    			getBrilloContraste().actualizarTextContraste();
	    			getMiVista().modificarImagen(getBrilloContraste().modificarBrilloContraste(getMiVista().getFocoImagenActual()));
	    		}
	    		
	    		else if(e.getSource() == getGamma().getSlider()) {
	    			getGamma().actualizarTextField();
	    			getMiVista().modificarImagen(getGamma().modificar(getMiVista().getFocoImagenActual()));
	    		}
	    }
	}
	
	class TextFieldListener implements DocumentListener{
		@Override
		public void insertUpdate(DocumentEvent e) {
			if(e.getDocument() == getBrilloContraste().getBrilloTextField().getDocument())
				getBrilloContraste().actualizarSliderBrillo();
			
			else if(e.getDocument() == getBrilloContraste().getContrasteTextField().getDocument())
				getBrilloContraste().actualizarSliderContraste();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	class OyenteVentana implements WindowListener{

		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			if(e.getSource() == getBrilloContraste().getVentana())
				getMiVista().getFocoImagenActual().setImageIconActual(getMiVista().getImageIconActual());
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}
	}
		   
	public Vista getMiVista() {
		return miVista;
	}

	public Informacion getInformacion() {
		return informacion;
	}

	public void setInformacion(Informacion informacion) {
		this.informacion = informacion;
	}

	public BlancoNegro getBlancoNegro() {
		return blancoNegro;
	}

	public void setBlancoNegro(BlancoNegro blancoNegro) {
		this.blancoNegro = blancoNegro;
	}

	public void setMiVista(Vista miVista) {
		this.miVista = miVista;
	}

	public BrilloContraste getBrilloContraste() {
		return brilloContraste;
	}

	public void setBrilloContraste(BrilloContraste brilloContraste) {
		this.brilloContraste = brilloContraste;
	}

	public Histograma getHistograma() {
		return histograma;
	}

	public void setHistograma(Histograma histograma) {
		this.histograma = histograma;
	}

	public Diferencia getDiferencia() {
		return diferencia;
	}

	public void setDiferencia(Diferencia diferencia) {
		this.diferencia = diferencia;
	}

	public Gamma getGamma() {
		return gamma;
	}

	public void setGamma(Gamma gamma) {
		this.gamma = gamma;
	}
}
