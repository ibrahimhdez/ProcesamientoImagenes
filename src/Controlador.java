import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

public class Controlador {
	private Vista miVista;
	
	public Controlador(){
		this.setMiVista(new Vista());
		
		this.getMiVista().getItemImage().addActionListener(new Oyente());
		this.getMiVista().getItemHistograma().addActionListener(new Oyente());
		this.getMiVista().getItemHistogramaAcumulativo().addActionListener(new Oyente());
		this.getMiVista().getBotonBlancoNegro().addActionListener(new Oyente());
		this.getMiVista().getItemShowInfo().addActionListener(new Oyente());
		this.getMiVista().getItemBrightnessContrast().addActionListener(new Oyente());
		this.getMiVista().getBrilloTextField().addActionListener(new Oyente());
		this.getMiVista().getContrasteTextField().addActionListener(new Oyente());
		
		this.getMiVista().getBrilloSlider().addChangeListener(new SliderListener());
		this.getMiVista().getContrasteSlider().addChangeListener(new SliderListener());		
	}
	
	void iniciarComponentes() throws IOException{
		this.getMiVista().iniciarBotones();
		this.getMiVista().iniciarBrilloContraste();
		this.getMiVista().init();
	}
	
	void addEventosRaton(){
		for(JDialog dialog: this.getMiVista().getMisImagenes())
			dialog.addMouseListener(new EventoRaton());		
	}
	
	class Oyente implements ActionListener{
		public Oyente()
		{}
		
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == getMiVista().getItemImage()){
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				jfc.setDialogTitle("Choose an image:");
				jfc.setAcceptAllFileFilterUsed(false);
				jfc.setFileFilter(new FileNameExtensionFilter("jpg, png, gif", "jpg", "png", "gif"));
				int sel = jfc.showOpenDialog(null);
				if (sel == JFileChooser.APPROVE_OPTION) {
			        getMiVista().getMiPanel().setRutaImagen(jfc.getSelectedFile().getAbsolutePath());
			        try {
						getMiVista().openJDialog();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				addEventosRaton();
			}
			
			else if(e.getSource() == getMiVista().getBrilloTextField())
				getMiVista().actualizarSliderBrillo();
			
			
			else if(e.getSource() == getMiVista().getContrasteTextField())
				getMiVista().actualizarSliderContraste();
			
			else if(getMiVista().getMisImagenes().size() > 0) {
				if(e.getSource() == getMiVista().getBotonBlancoNegro())
					getMiVista().convertirBlancoNegro();
				
				else if(e.getSource() == getMiVista().getItemShowInfo())
					getMiVista().mostrarInformacion();
			
				else if(e.getSource() == getMiVista().getItemBrightnessContrast()) 
					getMiVista().mostrarPanelBrilloContraste();
			
				else if(e.getSource() == getMiVista().getItemHistograma()) 
					getMiVista().mostrarHistograma();
				
				else if(e.getSource() == getMiVista().getItemHistogramaAcumulativo())
					getMiVista().mostrarHistogramaAcumulativo();
			}
		}
    }
	
	class EventoRaton implements MouseListener{
		@Override
		public void mousePressed(MouseEvent e){
			getMiVista().setFocoImagenActual((JDialog) e.getSource());
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
	    		if(e.getSource() == getMiVista().getBrilloSlider()) {
	    			getMiVista().actualizarTextBrillo();
	    			getMiVista().modificarBrilloContraste();
	    		}
	        
	    		else if(e.getSource() == getMiVista().getContrasteSlider()) {
	    			getMiVista().actualizarTextContraste();
	    			getMiVista().modificarBrilloContraste();
	    		}
	    }
	}
	
	
	class TextFieldListener implements DocumentListener{
		@Override
		public void insertUpdate(DocumentEvent e) {
			if(e.getDocument() == getMiVista().getBrilloTextField().getDocument())
				getMiVista().actualizarSliderBrillo();
			
			else if(e.getDocument() == getMiVista().getContrasteTextField().getDocument())
				getMiVista().actualizarSliderContraste();
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
		   
	public Vista getMiVista() {
		return miVista;
	}

	public void setMiVista(Vista miVista) {
		this.miVista = miVista;
	}
}
