package ui.template.CustomJPanel;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ui.controller.SelectProjectController;
import ui.paramater.MyViewParameter;

public class MainPanel extends JPanel{
	private ButtonGroup buttonGroup = new ButtonGroup(); // Créez un groupe de boutons radio
	private MyViewParameter myParam = new MyViewParameter();
	
	private JButton btnSelectFolder;
	private JLabel lblSelectionnerUnType;
	private JRadioButton rdbtnAnalyse_1;
	private JRadioButton rdbtnAnalyse_2;
	private JRadioButton rdbtnPrintGraph;
	private JRadioButton rdbtnPrintMetric;
	private  JButton btnValider;
	
	public MainPanel( JFrame frame) {
		
		frame.getContentPane().add(this, BorderLayout.CENTER);
		this.setLayout(null);

        // Créez un bouton pour ouvrir la boîte de dialogue de sélection de dossier
        btnSelectFolder = new JButton("Sélectionner un projet Java");
        btnSelectFolder.setBounds(myParam.getxBouton(), myParam.getyBouton(), myParam.getLargeurBouton(), myParam.getHauteurBouton());
        btnSelectFolder.setFont(MyViewParameter.getMyFontStyleTitle());
        this.add(btnSelectFolder);
        
        lblSelectionnerUnType = new JLabel("Selectionner un type d'analyse :");
        lblSelectionnerUnType.setBounds(myParam.getxBouton(), myParam.getyBouton()*2, myParam.getLargeurBouton(), myParam.getHauteurBouton());
        lblSelectionnerUnType.setFont(MyViewParameter.getMyFontStyleTitle());
        this.add(lblSelectionnerUnType);
        
        rdbtnAnalyse_1 = new JRadioButton("Analyse de base");
        rdbtnAnalyse_1.setBounds(myParam.getxBouton(), myParam.getyBouton()*3, myParam.getLargeurBouton(), myParam.getHauteurBouton());
        rdbtnAnalyse_1.setFont(MyViewParameter.getMyFontStyleTitle());
        this.add(rdbtnAnalyse_1);
        buttonGroup.add(rdbtnAnalyse_1); // Ajoutez le bouton radio au groupe
        

        
        rdbtnAnalyse_2 = new JRadioButton("Analyse complémentaire");
        rdbtnAnalyse_2.setBounds(myParam.getxBouton()+myParam.getLargeurBouton(), myParam.getyBouton()*3, myParam.getLargeurBouton(), myParam.getHauteurBouton());
        rdbtnAnalyse_2.setFont(MyViewParameter.getMyFontStyleTitle());
        this.add(rdbtnAnalyse_2);
        buttonGroup.add(rdbtnAnalyse_2); // Ajoutez le bouton radio au groupe
        
        rdbtnPrintGraph = new JRadioButton("Graphe couplage");
        rdbtnPrintGraph.setBounds(myParam.getxBouton()+myParam.getLargeurBouton()*2, myParam.getyBouton()*3, myParam.getLargeurBouton(), myParam.getHauteurBouton());
        rdbtnPrintGraph.setFont(MyViewParameter.getMyFontStyleTitle());
        this.add(rdbtnPrintGraph);
        buttonGroup.add(rdbtnPrintGraph); // Ajoutez le bouton radio au groupe
        
        rdbtnPrintMetric = new JRadioButton("Metrique couplage");
        rdbtnPrintMetric.setBounds(myParam.getxBouton()+myParam.getLargeurBouton()*2, myParam.getyBouton()*4, myParam.getLargeurBouton(), myParam.getHauteurBouton());
        rdbtnPrintMetric.setFont(MyViewParameter.getMyFontStyleTitle());
        this.add(rdbtnPrintMetric);
        buttonGroup.add(rdbtnPrintMetric); // Ajoutez le bouton radio au groupe
        
        btnValider = new JButton("Lancer l'analyse");
        btnValider.setBounds(myParam.getLargeurFenetre()-myParam.getxBouton()-myParam.getLargeurBouton(), myParam.getyBouton(), myParam.getLargeurBouton(), myParam.getHauteurBouton());
        btnValider.setFont(MyViewParameter.getMyFontStyleTitle());
        btnValider.setBackground(Color.lightGray);
        this.add(btnValider);
        
	}
	
	
	public void addAllListener(SelectProjectController myController) {


        btnSelectFolder.addActionListener(myController.btnSelectFoldeListener);
        btnValider.addActionListener(myController.buttonListener);
        rdbtnAnalyse_1.addActionListener(myController.radioListener);
        rdbtnAnalyse_2.addActionListener(myController.radioListener);
        rdbtnPrintGraph.addActionListener(myController.radioListener);
        rdbtnPrintMetric.addActionListener(myController.radioListener);
        
	}


	public boolean isRdbtnGraphSelected() {
		return rdbtnPrintGraph.isSelected();
	}


	public boolean isRdbtnMetricSelected() {
		return rdbtnPrintMetric.isSelected();
	}

}
