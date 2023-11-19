package ui.controller;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import model.CouplingGraph;
import processor.CouplingAnalyzeJDT;
import processor.MenuProcessor;
import ui.template.CheckBoxPanelTemplate;
import ui.template.FolderChooserTemplate;
import ui.template.CustomJPanel.AdditionalResultsPanel;
import ui.template.CustomJPanel.BasicResultsPanel;
import ui.template.CustomJPanel.MainPanel;
import ui.template.CustomJPanel.MetricPanel;


public class SelectProjectController  {

	private String my_path = "/home/mathieu/Documents/Projet/HAI913I_TP_AST";
	private JFrame frame;
	private MainPanel menuPanel;
	private JPanel cardPanel;
	private CheckBoxPanelTemplate checkBoxPanelBasique;
	private CheckBoxPanelTemplate checkBoxPanelComplementaire;
	private CardLayout cardLayout;
	private LabelMap labels = new LabelMap();
	private HashSet<String> methodsForProcessor = new HashSet<>();
	private Map<String,Integer> basicAnalyzeResults;
	private  Map<String,Resultat> complementaireAnalyzeResults;


	public SelectProjectController(JFrame frame, MainPanel menuPanel,CardLayout cardLayout,JPanel cardPanel) 
	{
		super();
		this.frame = frame;
		this.menuPanel = menuPanel;
		this.cardLayout = cardLayout;
		this.cardPanel = cardPanel; 
		System.out.println(labels.getBasicAnalysisMap());
		checkBoxPanelBasique = new CheckBoxPanelTemplate(frame,menuPanel,labels.getBasicAnalysisMap(),"Analyse de base");
		checkBoxPanelComplementaire = new CheckBoxPanelTemplate(frame,menuPanel,labels.getAdditionalAnalysisMap(),"Analyse complémentaire");
	}



	// Ajoutez un gestionnaire d'événements aux boutons radio
	public ActionListener radioListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) {
			JRadioButton selectedRadioButton = (JRadioButton) e.getSource();
			if (selectedRadioButton.isSelected()){
				switch (selectedRadioButton.getText()) 
				{
				case "Analyse de base":
					checkBoxPanelBasique.showButtonGroup(menuPanel,true,false);
					checkBoxPanelComplementaire.showButtonGroup(menuPanel,false,true);
					selectedRadioButton.getText();	                    
					break; 
				case "Analyse complémentaire":
					checkBoxPanelComplementaire.showButtonGroup(menuPanel,true,true);
					checkBoxPanelBasique.showButtonGroup(menuPanel,false,false);
					selectedRadioButton.getText();	                    
					break;
				default:
					checkBoxPanelBasique.showButtonGroup(menuPanel,false,false);
					checkBoxPanelComplementaire.showButtonGroup(menuPanel,false,true);                
				}
			}        
		}
	};


	/**
	 * Gestinnaire d'évévements 
	 * Permet de lancer l'analyse du projet
	 * Necessite d'avoir selectioner:
	 * 		- un path valide 
	 * 		- choisi des analyses a réaliser 
	 */
	public ActionListener buttonListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(my_path != "" ) {
				MenuProcessor menuProcessor = new MenuProcessor(my_path);
				if(!checkBoxPanelBasique.getMethodsForProcessor().isEmpty()) 
				{
					methodsForProcessor=checkBoxPanelBasique.getMethodsForProcessor();
					basicAnalyzeResults = menuProcessor.selectBasicAnalytics(methodsForProcessor);
					// Récupérer la valeur actuelle du Spinner 
					BasicResultsPanel  panel2 = new BasicResultsPanel(frame,basicAnalyzeResults,"Analyse de base");
					cardPanel.add(panel2, "Panel2");
					panel2.getBtnTerminer().addActionListener(buttonQuitListener);

					cardLayout.show(cardPanel, "Panel2"); // Affichez  le panel2 
				}
				else if(!checkBoxPanelComplementaire.getMethodsForProcessor().isEmpty()) {
					methodsForProcessor=checkBoxPanelComplementaire.getMethodsForProcessor();
					AdditionalResultsPanel  panel3 = new AdditionalResultsPanel(frame);

					cardPanel.add(panel3, "Panel3");

					complementaireAnalyzeResults = menuProcessor.selectComplAnalytics(methodsForProcessor,checkBoxPanelComplementaire.getSpinnerValue());
					panel3.printResults(complementaireAnalyzeResults, "Analyse complémentaire",checkBoxPanelComplementaire.getSpinnerValue());
					panel3.getBtnTerminer().addActionListener(buttonQuitListener);
					cardLayout.show(cardPanel, "Panel3"); // Affichez  le panel2


				} else if(menuPanel.isRdbtnGraphSelectedJDT()) {
					GraphController graph =  new GraphController();
					try {
						graph.GraphPanel(my_path, "EclipseJDT");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else if(menuPanel.isRdbtnMetricSelectedJDT()) {
					try {
					
						MetricPanel metricPanel =  new MetricPanel(frame, my_path, "EclipseJDT");
						metricPanel.getBtnTerminer().addActionListener(buttonQuitListener);
						cardPanel.add(metricPanel, "metricPanel");
						cardLayout.show(cardPanel, "metricPanel");
						
					} catch (NullPointerException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}else if(menuPanel.isRdbtnGraphSelectedSpoon()) {
					GraphController graph =  new GraphController();
					try {
						graph.GraphPanel(my_path, "Spoon");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				else if(menuPanel.isRdbtnMetricSelectedSpoon()) {
					try {
						
						MetricPanel metricPanel =  new MetricPanel(frame, my_path, "Spoon");
						metricPanel.getBtnTerminer().addActionListener(buttonQuitListener);
						cardPanel.add(metricPanel, "metricPanel");
						cardLayout.show(cardPanel, "metricPanel");
					} catch (NullPointerException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
			else {
				JOptionPane.showMessageDialog(frame, "Vous n'avez pas selectioné de projet");
			}
		}
	};


	// Ajoutez un gestionnaire d'événements au bouton
	public ActionListener buttonQuitListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			cardLayout.show(cardPanel, "Panel1");
		}
	};


	/**
	 * Gestinnaire d'évévements 
	 * Permet d'afficher de choisir un projet pour l'analyse
	 */
	public ActionListener btnSelectFoldeListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) 
		{
			JFileChooser folderChooser = new FolderChooserTemplate();
			int returnValue = folderChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) 
			{
				// Obtenez le dossier sélectionné
				File selectedFolder = folderChooser.getSelectedFile();
				// Je stock le path dans une valiable pour la suite
				my_path = selectedFolder.getAbsolutePath();
			}
		}
	};




}
