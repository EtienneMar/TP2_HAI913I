package ui.template.CustomJPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.controller.LabelMap;
import ui.controller.SelectProjectController;
import ui.paramater.MyViewParameter;


public class BasicResultsPanel extends JPanel{
	   private MyViewParameter myParam = new MyViewParameter();
	   private static  Map<String, Integer> results;
	   private JButton btnTerminer;
	   private LabelMap labels = new LabelMap();
	   
	public JButton getBtnTerminer() {
		return btnTerminer;
	}


	public void setBtnTerminer(JButton btnTerminer) {
		this.btnTerminer = btnTerminer;
	}


	public BasicResultsPanel( JFrame frame, Map<String, Integer> results , String myType) {
		
		frame.getContentPane().add(this, BorderLayout.CENTER);
		this.setLayout(null);
		this.results=results;
	    


	       JLabel titleLabel = new JLabel( "--> Resultats :");
	       titleLabel.setBounds(myParam.getxBouton(), (int) Math.round((myParam.getyBouton()/2)), myParam.getLargeurBouton(), myParam.getHauteurBouton());
	       titleLabel.setFont(MyViewParameter.getMyFontStyleTitle());
	       this.add(titleLabel);
	       
	       btnTerminer = new JButton("Terminer");
	       btnTerminer.setBounds(myParam.getLargeurFenetre()-myParam.getxBouton()-myParam.getLargeurBouton(), (int) Math.round((myParam.getyBouton()/2)), myParam.getLargeurBouton(), myParam.getHauteurBouton());
	       btnTerminer.setFont(MyViewParameter.getMyFontStyle());
	       btnTerminer.setBackground(Color.lightGray);
	       this.add(btnTerminer);
	       
	       int i =2;
	       for (Map.Entry<String, Integer> entry : results.entrySet()) {
	    	   
	    	   
	           JLabel keyLabel = new JLabel(labels.getBasicAnalysisByID(entry.getKey())+ " : ");
	           keyLabel.setBounds(myParam.getxBouton(), (int) Math.round((myParam.getyBouton()-(myParam.getyBouton()*0.25))*i), myParam.getLargeurBouton()*2, myParam.getHauteurBouton());
	           keyLabel.setFont(MyViewParameter.getMyFontStyle());
	           
	        // Calcul de la position x pour valueLabel en fonction de keyLabel
	           int valueLabelX = keyLabel.getX() + keyLabel.getWidth();

	           JLabel valueLabel = new JLabel(entry.getValue() + "");
	           valueLabel.setBounds(valueLabelX, keyLabel.getY(), myParam.getLargeurBouton(), myParam.getHauteurBouton());
	           keyLabel.setFont(MyViewParameter.getMyFontStyle());
	           this.add(keyLabel);
	           this.add(valueLabel);

	           i++;
	           
	           
	       }


	}

}