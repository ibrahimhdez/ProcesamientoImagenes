import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JDialog;
import javax.swing.JPanel;

import java.awt.BasicStroke;

public class Recortar {
	private final int BORDE_JDIALOG = 22;
	private JDialog dialog;
	private Boolean pintar;
	private Point puntoInicio;
	private Point puntoIntermedio;
	private Point puntoFinal;
	private Rectangle rectangle;
	
	public Recortar() {
		this.setDialog(new JDialog());
		this.setPintar(false);
		this.setPuntoInicio(new Point());
		this.setPuntoFinal(new Point());
	}
	
	void init() {
		this.setDialog(new JDialog());
		arreglarPuntos();
	}
	
	void pintarRectangulo(Graphics g) {
		if(pintar) {	
			int rectSizeX = (int)this.getPuntoFinal().getX() - (int)this.getPuntoInicio().getX();
			int rectSizeY = (int)this.getPuntoFinal().getY() - (int)this.getPuntoInicio().getY();
			
			//Condicional para borrar rectángulo 
			if((rectSizeX != 0) && (rectSizeY != 0)) {
				Graphics2D g2d = (Graphics2D) g;
				
				g2d.setColor(Color.RED);
				g2d.setStroke(new BasicStroke(3));
				
				//Arreglos necesarios para pintar rectángulo según la dirección del ratón
				if((rectSizeX < 0) && (rectSizeY > 0)) {
					rectSizeX = Math.abs(rectSizeX);
					g2d.drawRect((int)this.getPuntoFinal().getX(), (int)this.getPuntoInicio().getY() - BORDE_JDIALOG, rectSizeX, rectSizeY);
				}
				
				else if((rectSizeX > 0) && (rectSizeY < 0)) {
					rectSizeY = Math.abs(rectSizeY);
					g2d.drawRect((int)this.getPuntoInicio().getX(), (int)this.getPuntoFinal().getY() - BORDE_JDIALOG, rectSizeX, rectSizeY);
				}
				
				else if((rectSizeX < 0) && (rectSizeY < 0)) {
					rectSizeX = Math.abs(rectSizeX);
					rectSizeY = Math.abs(rectSizeY);
					g2d.drawRect((int)this.getPuntoFinal().getX(), (int)this.getPuntoFinal().getY() - BORDE_JDIALOG, rectSizeX, rectSizeY);
				}
					
				else
					g2d.drawRect((int)this.getPuntoInicio().getX(), (int)this.getPuntoInicio().getY() - BORDE_JDIALOG, rectSizeX, rectSizeY);
			}
		}
	}
	
	void arreglarPuntos() {
		int inicioX = 0, inicioY = 0, finalX = 0, finalY = 0;
		
		if((int)this.getPuntoInicio().getX() < (int)this.getPuntoFinal().getX()) {
			inicioX = (int)this.getPuntoInicio().getX();
			finalX = (int)this.getPuntoFinal().getX();
		}
		
		else {
			inicioX = (int)this.getPuntoFinal().getX();
			finalX = (int)this.getPuntoInicio().getX();
		}
		
		if((int)this.getPuntoInicio().getY() < (int)this.getPuntoFinal().getY()) {
			inicioY = (int)this.getPuntoInicio().getY();
			finalY = (int)this.getPuntoFinal().getY();
		}
		
		else {
			inicioY = (int)this.getPuntoFinal().getY();
			finalY = (int)this.getPuntoInicio().getY();
		}
			
		this.setPuntoInicio(new Point(inicioX, inicioY - BORDE_JDIALOG));
		this.setPuntoFinal(new Point(finalX, finalY - BORDE_JDIALOG));
	}
	
	void recortarImagen(Imagen imagenActual){
		BufferedImage imagen = imagenActual.getImagen();
		
		BufferedImage newImg = cropMyImage(imagen, (int)this.getPuntoFinal().getX() - (int)this.getPuntoInicio().getX(),
				(int)this.getPuntoFinal().getY() - (int)this.getPuntoInicio().getY(), (int)this.getPuntoInicio().getX(), (int)this.getPuntoInicio().getY());
		
		@SuppressWarnings("serial")
		JPanel panel = new JPanel() {
    			@Override
    			public void paintComponent(Graphics g) {
    				super.paintComponent(g); 
    				g.drawImage(newImg, 0, 0, null);
    				imagenActual.getRecortar().pintarRectangulo(g);
    			}
		};
		
		this.getDialog().add(panel);
		this.getDialog().setIconImage(newImg);
		this.getDialog().setTitle(imagenActual.getContenedor().getTitle() + " recortada");
		this.getDialog().setLocation((int)imagenActual.getContenedor().getLocation().getX() + imagenActual.getContenedor().getWidth() + 50, (int)imagenActual.getContenedor().getLocation().getY());
		this.getDialog().setSize(newImg.getWidth(), newImg.getHeight() + 45);
		this.getDialog().setVisible(true);
		this.getDialog().setResizable(false); 
	}
	
	public BufferedImage cropMyImage(BufferedImage img, int cropWidth, int cropHeight, int cropStartX, int cropStartY) {
		    BufferedImage clipped = null;
		    Dimension size = new Dimension(cropWidth, cropHeight);

		    createClip(img, size, cropStartX, cropStartY);
		    clipped = img.getSubimage((int)this.getRectangle().getLocation().getX(), (int)this.getRectangle().getLocation().getY(), (int)this.getRectangle().getWidth(), (int)this.getRectangle().getHeight());

		    return clipped;
		  }
	
	private void createClip(BufferedImage img, Dimension size, int clipX, int clipY){
		if (clipX < 0)
			clipX = 0;

		if (clipY < 0) 
			clipY = 0;
	    
		 if ((size.width + clipX) <= img.getWidth() && (size.height + clipY) <= img.getHeight()) {
			 this.setRectangle(new Rectangle(size));
		     this.getRectangle().setLocation(clipX, clipY);
		 } 
		    
		 else {
			 if ((size.width + clipX) > img.getWidth())
				size.width = img.getWidth() - clipX;

		     if ((size.height + clipY) > img.getHeight())
		    	 	size.height = img.getHeight() - clipY;

		     this.setRectangle(new Rectangle(size));
		     this.getRectangle().setLocation(clipX, clipY);
		}
	}

	
	public JDialog getDialog() {
		return dialog;
	}

	public void setDialog(JDialog dialog) {
		this.dialog = dialog;
	}

	public Boolean getPintar() {
		return pintar;
	}

	public void setPintar(Boolean pintar) {
		this.pintar = pintar;
	}

	public Point getPuntoInicio() {
		return puntoInicio;
	}

	public void setPuntoInicio(Point puntoInicio) {
		this.puntoInicio = puntoInicio;
	}

	public Point getPuntoIntermedio() {
		return puntoIntermedio;
	}

	public void setPuntoIntermedio(Point puntoIntermedio) {
		this.puntoIntermedio = puntoIntermedio;
	}

	public Point getPuntoFinal() {
		return puntoFinal;
	}

	public void setPuntoFinal(Point puntoFinal) {
		this.puntoFinal = puntoFinal;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
}
