import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Point;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import javax.swing.Timer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

public class Controlador {
	private static final int BORDE_JDIALOG = 22;
	private Vista miVista;
	private Imagen imagen;
	private Informacion informacion;
	private BlancoNegro blancoNegro;
	private BrilloContraste brilloContraste;
	private Histograma histograma;
	private Diferencia diferencia;
	private Gamma gamma;
	private Section section;
	private Sampling sampling;
	private Cuantizacion cuantizacion;
	private Scale scale;
	private Rotate rotate;
	private Filter filter;
	private BufferedImage diferenciasOriginal;
	private Timer timer;
	private Timer timerCoordenadas;
	private Boolean mostrarCoordenadas;
	
	public Controlador(){
		this.setMiVista(new Vista());
		this.setImagen(new Imagen());
		this.setInformacion(new Informacion());
		this.setBlancoNegro(new BlancoNegro());
		this.setBrilloContraste(new BrilloContraste());
		this.setHistograma(new Histograma());
		this.setDiferencia(new Diferencia());
		this.setGamma(new Gamma());
		this.setSection(new Section());
		this.setSampling(new Sampling());
		this.setCuantizacion(new Cuantizacion());
		this.setScale(new Scale());
		this.setRotate(new Rotate());
		this.setFilter(new Filter());
		this.setMostrarCoordenadas(new Boolean(false));
		iniciarTimer();
		iniciarTimerCoordenadas();
		
		this.getMiVista().getItemImage().addActionListener(new Oyente());
		this.getMiVista().getItemHistograma().addActionListener(new Oyente());
		this.getMiVista().getItemHistogramaAcumulativo().addActionListener(new Oyente());
		this.getMiVista().getItemEspecificacionHistograma().addActionListener(new Oyente());
		this.getMiVista().getBotonBlancoNegro().addActionListener(new Oyente());
		this.getMiVista().getBotonTijera().addActionListener(new Oyente());
		this.getMiVista().getBotonDuplicar().addActionListener(new Oyente());
		this.getMiVista().getItemShowInfo().addActionListener(new Oyente());
		this.getMiVista().getItemBrightnessContrast().addActionListener(new Oyente());
		this.getMiVista().getItemDiference().addActionListener(new Oyente());
		this.getMiVista().getItemGamma().addActionListener(new Oyente());
		this.getMiVista().getItemSection().addActionListener(new Oyente());
		this.getMiVista().getItemSampling().addActionListener(new Oyente());
		this.getMiVista().getItemQuantization().addActionListener(new Oyente());
		this.getMiVista().getItemScale().addActionListener(new Oyente());
		this.getMiVista().getItemFlipH().addActionListener(new Oyente());
		this.getMiVista().getItemFlipV().addActionListener(new Oyente());
		this.getMiVista().getItemFlipZ().addActionListener(new Oyente());
		this.getMiVista().getItemRotate().addActionListener(new Oyente());
		this.getMiVista().getItemConvolve().addActionListener(new Oyente());
		this.getMiVista().getItemAverage3x3().addActionListener(new Oyente());
		this.getMiVista().getItemAverage5x5().addActionListener(new Oyente());
		this.getMiVista().getItemAverage7x7().addActionListener(new Oyente());
		this.getMiVista().getItemSobelH().addActionListener(new Oyente());
		this.getMiVista().getItemSobelV().addActionListener(new Oyente());
		this.getMiVista().getItemGradientH().addActionListener(new Oyente());
		this.getMiVista().getItemGradientV().addActionListener(new Oyente());
		this.getMiVista().getItemGaussian1().addActionListener(new Oyente());
		this.getMiVista().getItemGaussian2().addActionListener(new Oyente());
		this.getMiVista().getItemGaussian3().addActionListener(new Oyente());
		this.getBrilloContraste().getBrilloTextField().addActionListener(new Oyente());
		this.getBrilloContraste().getDefaultBrillo().addActionListener(new Oyente());
		this.getBrilloContraste().getDefaultContraste().addActionListener(new Oyente());
		this.getBrilloContraste().getContrasteTextField().addActionListener(new Oyente());
		this.getSampling().getButton().addActionListener(new Oyente());
		this.getCuantizacion().getBoton().addActionListener(new Oyente());
		this.getSection().getBotonEjecutar().addActionListener(new Oyente());
		this.getScale().getBoton().addActionListener(new Oyente());
		this.getRotate().getBoton().addActionListener(new Oyente());
		this.getRotate().getBotonOk().addActionListener(new Oyente());
		this.getFilter().getBoton().addActionListener(new Oyente());
		
		this.getBrilloContraste().getBrilloSlider().addChangeListener(new SliderListener());
		this.getBrilloContraste().getContrasteSlider().addChangeListener(new SliderListener());	
		this.getDiferencia().getSliderUmbral().addChangeListener(new SliderListener());
		this.getGamma().getSlider().addChangeListener(new SliderListener());
		
		getBrilloContraste().getVentana().addWindowListener(new OyenteVentana());
		getGamma().getDialog().addWindowListener(new OyenteVentana());
	}
	
	void iniciarComponentes() throws IOException{
		this.getMiVista().iniciarBotones();
		this.getBlancoNegro().init();
		this.getBrilloContraste().init();
		this.getGamma().init();
		this.getDiferencia().init();
		this.getSampling().init();
		this.getCuantizacion().init();
		this.getMiVista().init();
		this.getScale().init();
		this.getRotate().init();
		this.getFilter().init();
	}
	
	private void iniciarTimer() {
		setTimer(new Timer(10, new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    		Point posRaton = MouseInfo.getPointerInfo().getLocation();
		    		Point posContenedor = getMiVista().getFocoImagenActual().getContenedor().getLocation();
		    	
		    		getMiVista().getFocoImagenActual().getRecortar().setPuntoFinal(new Point((int)(posRaton.getX() - posContenedor.getX()), (int)(posRaton.getY() - posContenedor.getY())));
		    		getMiVista().getFocoImagenActual().getContenedor().repaint();	
		    }
		 }));
	}
	
	private void iniciarTimerCoordenadas() {
		setTimerCoordenadas(new Timer(10, new ActionListener() {
		    public void actionPerformed(ActionEvent evt) {
		    		int posXRaton =  (int) (MouseInfo.getPointerInfo().getLocation().getX() - getMiVista().getFocoImagenActual().getContenedor().getLocation().getX());
				int posYRaton =  (int) (MouseInfo.getPointerInfo().getLocation().getY() - getMiVista().getFocoImagenActual().getContenedor().getLocation().getY()) - BORDE_JDIALOG;
					
		    		getMiVista().actualizarEtiqueta("X: " + posXRaton + " Y: " + posYRaton + "  Value: " + getMiVista().getFocoImagenActual().getValorPixel(posXRaton, posYRaton), getMiVista().getFocoImagenActual().getValorPixel(posXRaton, posYRaton));
		    		getMiVista().getContentPane().repaint();	
		    }
		 }));
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
					getBlancoNegro().convertir(getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getBlancoNegro().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getBotonTijera()) {
					Imagen imagenActual = getMiVista().getFocoImagenActual();
				
					getMiVista().getFocoImagenActual().getRecortar().setPintar(false);	
					imagenActual.getRecortar().init();		
					imagenActual.getRecortar().recortarImagen(imagenActual);
					getMiVista().addImagen(imagenActual.getRecortar().getDialog());
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getBotonDuplicar()) {
					Imagen imagenActual = new Imagen(getMiVista().getFocoImagenActual().copyBufferedImage(getMiVista().getFocoImagenActual().getImagen()));
					JDialog dialog = imagenActual.getContenedor();
					dialog.setTitle(getMiVista().getFocoImagenActual().getContenedor().getTitle() + " duplicada");
					getMiVista().addImagen(dialog);
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
					
					//!!! Comentar el siguiente condicional para trabajar con la imagen actual, no la inicial.
					//if(!imagenActual.getModificada())
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
					
					if(getDiferencia().getEjecutar()) {
						getDiferencia().setImagen2(getBlancoNegro().get(getDiferencia().getImagen2()));
						getMiVista().addImagen(getDiferencia().generar().getContenedor());
						getDiferencia().mostrarVentanaUmbral();
						addEventosRaton();
						setDiferenciasOriginal(getDiferencia().getDiferencias());
						getDiferencia().setEjecutar(false);
					}
				}
				
				else if(e.getSource() == getMiVista().getItemGamma()) {
					getGamma().getSlider().setValue((int)(getMiVista().getFocoImagenActual().getIndiceGamma() * 100));
					getGamma().mostrar(getMiVista().getFocoImagenActual().getContenedor());
				}
				
				else if(e.getSource() == getMiVista().getItemSection()) {
					int valor = 1;
					try {
						valor = new Integer(JOptionPane.showInputDialog("Enter the number of sections (min 1): "));
						if(valor < 1)
							valor = 1;
					} catch(Exception a) {}
					
					getSection().init(valor);
					getSection().mostrar(getMiVista().getFocoImagenActual().getContenedor());
				}
				
				else if(e.getSource() == getMiVista().getItemSampling()) 
					getSampling().mostrar(getMiVista().getFocoImagenActual().getContenedor());
				
				
				else if(e.getSource() == getMiVista().getItemQuantization()) {
					JDialog dialogActual = getMiVista().getFocoImagenActual().getContenedor();
					
					getCuantizacion().mostrar(dialogActual.getLocation(), dialogActual.getBounds());
				}
				
				else if(e.getSource() == getMiVista().getItemScale()) {
					getScale().mostrar(getMiVista().getFocoImagenActual().getContenedor());
				}
				
				else if(e.getSource() == getMiVista().getItemFlipH()) {
					getRotate().flip(getMiVista().getFocoImagenActual(), 0);
					getMiVista().addImagen(getRotate().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemFlipV()) {
					getRotate().flip(getMiVista().getFocoImagenActual(), 1);
					getMiVista().addImagen(getRotate().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemFlipZ()) {
					getRotate().flip(getMiVista().getFocoImagenActual(), 2);
					getMiVista().addImagen(getRotate().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemRotate()) {
					getRotate().mostrar(getMiVista().getFocoImagenActual().getContenedor());
				}
				
				//Filtros
				else if(e.getSource() == getMiVista().getItemConvolve()) {
					getFilter().mostrar(getMiVista().getFocoImagenActual().getContenedor());
				}
				
				else if(e.getSource() == getMiVista().getItemAverage3x3()) {
					getFilter().selectFilter(0, getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getFilter().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemAverage5x5()) {
					getFilter().selectFilter(1, getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getFilter().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemAverage7x7()) {
					getFilter().selectFilter(2, getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getFilter().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemSobelH()) {
					getFilter().selectFilter(3, getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getFilter().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemSobelV()) {
					getFilter().selectFilter(4, getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getFilter().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemGradientH()) {
					getFilter().selectFilter(5, getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getFilter().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemGradientV()) {
					getFilter().selectFilter(6, getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getFilter().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemGaussian1()) {
					getFilter().selectFilterGaussian(1, getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getFilter().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemGaussian2()) {
					getFilter().selectFilterGaussian(2, getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getFilter().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getMiVista().getItemGaussian3()) {
					getFilter().selectFilterGaussian(3, getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getFilter().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getBrilloContraste().getDefaultBrillo()) 
					getBrilloContraste().getBrilloSlider().setValue((int) getBrilloContraste().getBrilloInicial());

				else if(e.getSource() == getBrilloContraste().getDefaultContraste()) 
					getBrilloContraste().getContrasteSlider().setValue((int) getBrilloContraste().getContrasteInicial());
			
				else if(e.getSource() == getMiVista().getItemHistograma()) 
					getHistograma().mostrarHistogramaAbsoluto(getMiVista().getFocoImagenActual());

				else if(e.getSource() == getMiVista().getItemHistogramaAcumulativo())
					getHistograma().mostrarHistogramaAcumulativo(getMiVista().getFocoImagenActual());
				
				else if(e.getSource() == getMiVista().getItemEspecificacionHistograma()) {
					Imagen imagenActual = getMiVista().getFocoImagenActual();
					int[] pixels;
					try {
						getBlancoNegro().convertir(new Imagen(getMiVista().openImage()));
						getMiVista().addImagen(getBlancoNegro().getDialog()); 
						pixels = getHistograma().especificacion(imagenActual, getMiVista().getFocoImagenActual());
						JDialog dialog = getImagen().construirImagen(getMiVista().getFocoImagenActual(), pixels);
						dialog.setVisible(true);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
					addEventosRaton();
				}
				
				else if(e.getSource() == getSampling().getButton()){
					getSampling().buildImage(getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getSampling().getDialog());
					addEventosRaton();
				}
				
				else if(e.getSource() == getCuantizacion().getBoton()) {
					getCuantizacion().generar(getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getCuantizacion().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getSection().getBotonEjecutar()) {
					if(getSection().buildImage(getMiVista().getFocoImagenActual())){
						getMiVista().addImagen(getSection().getDialog()); 
						addEventosRaton();
					}
				}
				
				else if(e.getSource() == getScale().getBoton()) {
					getScale().buildImage(getMiVista().getFocoImagenActual());
					getMiVista().addImagen(getScale().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getRotate().getBoton()) {
					getRotate().turnDirect(getMiVista().getFocoImagenActual(), false);
					getMiVista().addImagen(getRotate().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getRotate().getBotonOk()) {
					getRotate().turnDirect(getMiVista().getFocoImagenActual(), true);
					getMiVista().addImagen(getRotate().getDialog()); 
					addEventosRaton();
				}
				
				else if(e.getSource() == getFilter().getBoton()) {
					if(getFilter().convolveCustom(getMiVista().getFocoImagenActual())) {
						getMiVista().addImagen(getFilter().getDialog()); 
						addEventosRaton();
					}
				}
			}
		}
    }
	
	class EventoRaton implements MouseListener{
		@Override
		public void mousePressed(MouseEvent e){
			for(Imagen imagen: getMiVista().getImagenes())
				if(e.getSource() == imagen.getContenedor())
					getMiVista().setFocoImagenActual(imagen);
			
			if(!getMiVista().getFocoImagenActual().getRecortar().getPintar())
				getMiVista().getFocoImagenActual().getRecortar().setPintar(true);
			getMiVista().getFocoImagenActual().getRecortar().setPuntoInicio(e.getPoint());
			
			if(!getTimer().isRunning())
				getTimer().start();
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if(getTimer().isRunning()) 
				getTimer().stop();

			getMiVista().getFocoImagenActual().getRecortar().setPuntoFinal(e.getPoint());
			getMiVista().getFocoImagenActual().getContenedor().repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {		
			if((!getTimerCoordenadas().isRunning()) && (e.getSource() == getMiVista().getFocoImagenActual().getContenedor()))
				getTimerCoordenadas().start();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if(getTimerCoordenadas().isRunning()) 
				getTimerCoordenadas().stop();
			
			getMiVista().actualizarEtiqueta("", 0);
			getMiVista().getContentPane().repaint();
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
	    			double indiceGamma = getGamma().getSlider().getValue() / 100.0;
	    			getMiVista().getFocoImagenActual().setIndiceGamma(indiceGamma);
	    		}
	    		
	    		else if(e.getSource() == getDiferencia().getSliderUmbral()) 
	    			getMiVista().modificarImagen(new Imagen(getDiferencia().marcarDiferencias(getDiferenciasOriginal())));
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
			
			else if(e.getSource() == getGamma().getDialog()) {
				BufferedImage bi = new BufferedImage(getMiVista().getImageIconActual().getIconWidth(), getMiVista().getImageIconActual().getIconHeight(), BufferedImage.TYPE_INT_RGB);
				
				Graphics g = (Graphics) bi.createGraphics();
				g.drawImage(getMiVista().getImageIconActual().getImage(), 0, 0, null);  
				getMiVista().getImageIconActual().paintIcon(null, g, 0,0);
				g.dispose();
				
				getMiVista().getFocoImagenActual().setImagen(bi);
				getMiVista().getFocoImagenActual().setHistograma();  
			} 		
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
	
	public Timer getTimer() {
		return timer;
	}
	
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
		   
	public Timer getTimerCoordenadas() {
		return timerCoordenadas;
	}

	public void setTimerCoordenadas(Timer timerCoordenadas) {
		this.timerCoordenadas = timerCoordenadas;
	}

	public Vista getMiVista() {
		return miVista;
	}

	public Imagen getImagen() {
		return imagen;
	}

	public void setImagen(Imagen imagen) {
		this.imagen = imagen;
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

	public Sampling getSampling() {
		return sampling;
	}
	
	public void setSampling(Sampling sampling) {
		this.sampling = sampling;
	}

	public Cuantizacion getCuantizacion() {
		return cuantizacion;
	}

	public void setCuantizacion(Cuantizacion cuantizacion) {
		this.cuantizacion = cuantizacion;
	}

	public Boolean getMostrarCoordenadas() {
		return mostrarCoordenadas;
	}

	public void setMostrarCoordenadas(Boolean mostrarCoordenadas) {
		this.mostrarCoordenadas = mostrarCoordenadas;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Scale getScale() {
		return scale;
	}

	public void setScale(Scale scale) {
		this.scale = scale;
	}

	public Rotate getRotate() {
		return rotate;
	}

	public void setRotate(Rotate rotate) {
		this.rotate = rotate;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public BufferedImage getDiferenciasOriginal() {
		return diferenciasOriginal;
	}

	public void setDiferenciasOriginal(BufferedImage diferenciasOriginal) {
		this.diferenciasOriginal = diferenciasOriginal;
	}
}
