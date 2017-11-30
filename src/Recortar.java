import java.awt.Graphics;
import java.awt.Graphics2D;

public class Recortar {
	private Boolean pintar;
	
	public Recortar() {
		this.setPintar(false);
	}
	
	void pintarRectangulo(Graphics g) {
		if(pintar) {
			Graphics2D g2d = (Graphics2D) g;

		}
	}

	public Boolean getPintar() {
		return pintar;
	}

	public void setPintar(Boolean pintar) {
		this.pintar = pintar;
	}
}
