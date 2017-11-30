import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Transparency;
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
	
	void recortarImagen(Imagen imagenActual) {
		BufferedImage imagen = imagenActual.getImagen();
		
		Graphics g = imagen.getGraphics();
		g.drawImage(imagenActual.imageActual(), 0, 0, null);
		
		int x = 0, y = 0;
		BufferedImage newImg = GraphicsEnvironment
				 .getLocalGraphicsEnvironment()
				 .getDefaultScreenDevice()
				 .getDefaultConfiguration()
				 .createCompatibleImage((int)this.getPuntoFinal().getX() - (int)this.getPuntoInicio().getX(), (int)this.getPuntoFinal().getY() - (int)this.getPuntoInicio().getY(),
				 Transparency.OPAQUE);
		
		for(int i = 0; i < imagen.getWidth(); i++)
			for(int j = 0; j < imagen.getHeight(); j++) {
				if((i >= (int)this.getPuntoInicio().getX()) && (i < (this.getPuntoFinal().getX())) 
						&& (j >= this.getPuntoInicio().getY()) && (j < this.getPuntoFinal().getY())){
					newImg.setRGB(x, y, imagen.getRGB(i, j));
					if(x < newImg.getWidth() - 1)
						x++;
					else {
						x = 0;
						y++;
					}	
				}	
			}
		
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
}
