package ui.template;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import ui.paramater.MyViewParameter;
public class CheckBoxPanelTemplate {
    private MyViewParameter myParam = new MyViewParameter();
    private ButtonGroup buttonGroup = new ButtonGroup();
    private String id;
    List<JCheckBox> checkBoxList = new ArrayList<>();
    private int y = 0; // Position verticale initiale
    private Map<String,String> labels;
	HashSet<String> methodsForProcessor = new HashSet<>();
	private JSpinner spinner;
	
	
	private JLabel keyLabel ;
	
	

	public CheckBoxPanelTemplate(JFrame frame, JPanel panel, Map<String,String> labels, String typeAnalyse) 
    {
        super();
        this.id = typeAnalyse;
        this.labels= labels;
        labels.forEach((k,v)->
        {
        	JCheckBox checkBox = new JCheckBox(v);
            switch (typeAnalyse) 
            {
                case "Analyse de base":
                    checkBox.setBounds(myParam.getxBouton(), myParam.getyBouton() * 4 + y, myParam.getLargeurBouton(), myParam.getHauteurBouton());
                    checkBox.setActionCommand(k);
                    break;

                case "Analyse complémentaire":
                	
                    checkBox.setBounds(myParam.getxBouton() + myParam.getLargeurBouton(),myParam.getyBouton() * 4 + y, myParam.getLargeurBouton(), myParam.getHauteurBouton());
                    checkBox.setActionCommand(k);
                    if(k.equals("11")) {
                    	y+=80;
                    	
                    	   keyLabel = new JLabel("      X  : ");
            	           keyLabel.setBounds(myParam.getxBouton() + myParam.getLargeurBouton(),myParam.getyBouton() * 4 + y, myParam.getLargeurBouton()/6, myParam.getHauteurBouton()/3);
            	           keyLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Police personnalisée
            	        // Calcul de la position x pour valueLabel en fonction de keyLabel
            	          int valueLabelX = keyLabel.getX() + keyLabel.getWidth();
                    	SpinnerModel value =  new SpinnerNumberModel(5, //initial value  
                                   0, //minimum value  
                                   1000, //maximum value  
                                   1); //step  
                       
                    	spinner = new JSpinner(value);   
                    	spinner.setBounds(valueLabelX, myParam.getyBouton() * 4 + y, myParam.getLargeurBouton()/5, myParam.getHauteurBouton()/3);
                    	spinner.setFont(new Font("Arial", Font.PLAIN, 16)); // Police personnalisée
                    	spinner.setVisible(false);
                    	keyLabel.setVisible(false);
                    	panel.add(spinner);
                    	panel.add(keyLabel);
                    	
                    }
              
                    break;
            }
            checkBox.setFont(new Font("Arial", Font.PLAIN, 16)); // Police personnalisée
            checkBox.setVisible(false);
            checkBoxList.add(checkBox);
            panel.add(checkBox);
            //buttonGroup.add(checkBox); // Ajouter la case à cocher au groupe

            y += 60; 
    
        // Ajouter un ItemListener pour chaque case à cocher
        checkBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    methodsForProcessor.add(checkBox.getActionCommand());
                } else if (e.getStateChange() == ItemEvent.DESELECTED) {
                	 methodsForProcessor.remove(checkBox.getActionCommand());
                }
            }
        });
    });
        panel.revalidate();
        panel.repaint(); 

    }
    
    
    
    public HashSet<String>  getMethodsForProcessor(){
		return methodsForProcessor;
    }

    public void showButtonGroup(JPanel panel,boolean isShow, boolean isComp) {

        	 labels.forEach((k,v)->{
        		 for (JCheckBox checkBox : checkBoxList) {
        			 if (k.equals(checkBox.getActionCommand())) {
                         checkBox.setVisible(isShow);
                         
                        if(isComp) {
                         	spinner.setVisible(isShow);
                        	keyLabel.setVisible(isShow);
                        }


                     }
                     else if(!isShow) {
                    	 checkBox.setSelected(false);
                         if(isComp) {
                          	spinner.setVisible(false);
                         	keyLabel.setVisible(false);
                         }


                     }
        		 }
        		 
        	 });

        panel.revalidate(); 
        panel.repaint(); 
    }
    
    
    public int getSpinnerValue() {
    	return (int) spinner.getValue();
    	
    }
}


