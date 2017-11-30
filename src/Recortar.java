import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import java.awt.BasicStroke;

public class Recortar {
	private final int BORDE_JDIALOG = 22;
	private Boolean pintar;
	private Point puntoInicio;
	private Point puntoIntermedio;
	private Point puntoFinal;
	
	public Recortar() {
		this.setPintar(false);
		this.setPuntoInicio(new Point());
		this.setPuntoFinal(new Point());
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
