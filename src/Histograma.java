import java.awt.Color;

import javax.swing.JDialog;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Histograma {
	private JDialog ventana;
	private JPanel panel;	

	public Histograma() {
		this.setVentana(new JDialog());
		this.setPanel(new JPanel());
	}
	
	void mostrarHistogramaAbsoluto(Imagen imagen) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		iniciarContenedores();
		
		for(int i = 0; i < imagen.getHistograma().length; i++)
			dataset.setValue(imagen.getHistograma(i), 1 + "", i + "");
		
		JFreeChart chart = ChartFactory.createBarChart3D("Histograma Absoluto","Pixel", "Value", dataset, PlotOrientation.VERTICAL, true,true, false);
		chart.setBackgroundPaint(Color.white);	        
		chart.getTitle().setPaint(Color.black); 
			        
		CategoryPlot p = chart.getCategoryPlot(); 
		p.setRangeGridlinePaint(Color.black);
			         
		ChartPanel chartPanel = new ChartPanel(chart);	 
		this.getPanel().add(chartPanel);
		
		this.getVentana().add(this.getPanel());
		this.getVentana().setSize(700, 400);
		this.getVentana().setVisible(true);
	}
	
	void mostrarHistogramaAcumulativo(Imagen imagen) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int[] histogramaAcumulativo = acumularHistograma(imagen.getHistograma());
		iniciarContenedores();
		
		for(int i = 0; i < histogramaAcumulativo.length; i++)
			dataset.setValue(histogramaAcumulativo[i], 1 + "", i + "");
	    
		JFreeChart chart = ChartFactory.createBarChart3D("Histograma Acumulativo","Pixel", "Value", dataset, PlotOrientation.VERTICAL, true,true, false);
		chart.setBackgroundPaint(Color.white);	        
		chart.getTitle().setPaint(Color.black); 
			        
		CategoryPlot p = chart.getCategoryPlot(); 
		p.setRangeGridlinePaint(Color.black);
			         
		ChartPanel chartPanel = new ChartPanel(chart);	
		this.getPanel().add(chartPanel);
		
		this.getVentana().setResizable(false);
		this.getVentana().add(panel);
		this.getVentana().setSize(700, 400);
		this.getVentana().setVisible(true);
	}
	
	int[] especificacion(Imagen imagenA, Imagen imagenB) {
		final int MAX_PIXELS = 256;
	    int[] Vout = new int[MAX_PIXELS];
	    double[] pA = getHistogramaNormalizado(imagenA);
	    double[] pB = getHistogramaNormalizado(imagenB);
	    
	    for (int i = 0; i < MAX_PIXELS; i++) {
	      int j = MAX_PIXELS - 1;
	      do {
	        Vout[i] = j;
	        j--;
	      } while ((j >= 0) && (pA[i] <= pB[j]));
	    }    
	    
	    return Vout;
	}
	
	double[] getHistogramaNormalizado(Imagen imagen) {		
		int numPixeles = imagen.getImagen().getHeight() * imagen.getImagen().getWidth();
		double[] histogramaNormalizado = new double[numPixeles];
		
		for(int i = 0; i < imagen.getHistograma().length; i++) 
			histogramaNormalizado[i] = (double)imagen.getHistograma(i) / numPixeles;
	
		return histogramaNormalizado;
	}
	
	int[] acumularHistograma(int[] histograma) {
		int[] aux = new int[256];
		int num = 0;
		
		for(int i = 0; i < histograma.length; i++) {
			for(int j = 0; j < i; j++)
					num += histograma[j];
			aux[i] = num;
			num = 0;
		}
		
		return aux;
	}
	
	private void iniciarContenedores() {
		this.setPanel(new JPanel());
		this.setVentana(new JDialog());
	}

	public JDialog getVentana() {
		return ventana;
	}


	public void setVentana(JDialog ventana) {
		this.ventana = ventana;
	}


	public JPanel getPanel() {
		return panel;
	}


	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
}
