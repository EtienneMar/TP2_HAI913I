package ui.template.CustomJPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.CouplingGraph;
import processor.CouplingAnalyzeJDT;
import processor.SpoonCouplingAnalyzer;
import ui.controller.LabelMap;
import ui.controller.SelectProjectController;
import ui.paramater.MyViewParameter;
import ui.template.FolderChooserTemplate;


public class MetricPanel extends JPanel{

	private MyViewParameter myParam = new MyViewParameter();
	private static CouplingGraph couplingGraphInterne;
	private JButton btnTerminer;
	private JButton btnRechercher;
	private JTextField textField1;
	private JTextField textField2;
	private JLabel resultLabel = new JLabel("");
	private LabelMap labels = new LabelMap();

	public MetricPanel(JFrame frame, String mypath, String typeAnalyse) throws NullPointerException, IOException {
		if(typeAnalyse.equals("EclipseJDT")){
			CouplingAnalyzeJDT couplingGraph = new CouplingAnalyzeJDT(mypath);
			couplingGraphInterne = new CouplingGraph();
			this.setCouplingGraph(couplingGraph.getCouplingGraphe());
		}
		if(typeAnalyse.equals("Spoon")){
			SpoonCouplingAnalyzer spoonGraph = new SpoonCouplingAnalyzer(mypath);
			couplingGraphInterne = new CouplingGraph();
			this.setCouplingGraph(spoonGraph.getCouplingGraph());
		}
		
		
		System.out.println(checkMetricCouplage("hotel.rest.server.Application", "hotel.rest.server.repository.AgencePartenaireRepository"));
		frame.getContentPane().add(this, BorderLayout.CENTER);
		this.setLayout(null);


		JLabel titleLabel = new JLabel( "--> Resultats :");
		titleLabel.setBounds(myParam.getxBouton(), (int) Math.round((myParam.getyBouton()/2)), myParam.getLargeurBouton(), myParam.getHauteurBouton());
		titleLabel.setFont(MyViewParameter.getMyFontStyleTitle());
		this.add(titleLabel);

		btnTerminer = new JButton("Terminer");
		btnTerminer.setBounds(myParam.getLargeurFenetre()-myParam.getxBouton()-myParam.getLargeurBouton(), (int) Math.round((myParam.getyBouton()/2)), myParam.getLargeurBouton(), myParam.getHauteurBouton());
		btnTerminer.setFont(MyViewParameter.getMyFontStyle());
		btnTerminer.setBackground(Color.lightGray);
		btnTerminer.addActionListener(buttonTerminerListener);
		this.add(btnTerminer);

		textField1 = new JTextField();
		textField1.setBounds(myParam.getxBouton(), (int) Math.round((myParam.getyBouton() * 2)), myParam.getLargeurBouton(), myParam.getHauteurBouton());
		textField1.setFont(MyViewParameter.getMyFontStyleTitle());
		this.add(textField1);

		// Créez le deuxième champ de saisie de texte
		textField2 = new JTextField();
		textField2.setBounds(myParam.getxBouton(), (int) Math.round((myParam.getyBouton() * 3)), myParam.getLargeurBouton(), myParam.getHauteurBouton());
		textField2.setFont(MyViewParameter.getMyFontStyleTitle());
		this.add(textField2);

		btnRechercher = new JButton("Rechercher");
		btnRechercher.setBounds(myParam.getLargeurFenetre()-myParam.getxBouton()-myParam.getLargeurBouton(), (int) Math.round((myParam.getyBouton()*2.5)), myParam.getLargeurBouton(), myParam.getHauteurBouton());
		btnRechercher.setFont(MyViewParameter.getMyFontStyle());
		btnRechercher.setBackground(Color.lightGray);
		this.add(btnRechercher);
		
		btnRechercher.addActionListener(btnRechercherListener);

		resultLabel.setBounds(myParam.getxBouton(), (int) Math.round((myParam.getyBouton() * 5)), myParam.getLargeurBouton()*3, myParam.getHauteurBouton());
		resultLabel.setFont(MyViewParameter.getMyFontStyleTitle());
		this.add(resultLabel);
	}


	public CouplingGraph getCouplingGraph() {
		return couplingGraphInterne;
	}


	public void setCouplingGraph(CouplingGraph couplingGraph) {
		this.couplingGraphInterne = couplingGraph;
	}


	public JButton getBtnTerminer() {
		return btnTerminer;
	}


	public void setBtnTerminer(JButton btnTerminer) {
		this.btnTerminer = btnTerminer;
	}

	private String checkMetricCouplage(String classeNameA, String classeNameB) {
		String resultText = null;
		if (!couplingGraphInterne.isExistingVertex(classeNameA) && !couplingGraphInterne.isExistingVertex(classeNameB)) {
			resultText = "Les classes : " + classeNameA + " et " + classeNameB + " n'existe pas dans le projet";
		}else if (!couplingGraphInterne.isExistingVertex(classeNameA)){
			resultText = "La classe : " + classeNameA +  " n'existe pas dans le projet";
		}else if (!couplingGraphInterne.isExistingVertex(classeNameB)){
			resultText = "La classe : " + classeNameB +  " n'existe pas dans le projet";
		}else {
			float metricCouplage = couplingGraphInterne.getMetricCouplageByName(classeNameA, classeNameB);
			metricCouplage = (float) (Math.round(metricCouplage * 100.0) / 100.0);
			if(metricCouplage == -1.00) {
				resultText = "Il n'existe pas d'appel entre les deux classes"; 
			}else {
				resultText = "La métrique de couplage entre " + classeNameA + " et " + classeNameB + " = " + metricCouplage;
			}
		}
		return resultText;
	}
	
	public ActionListener btnRechercherListener = new ActionListener() 
	{
		public void actionPerformed(ActionEvent e) {
			String textFieldClasseA = textField1.getText();
			String textFieldClasseB = textField2.getText();
			if (textFieldClasseA.isEmpty() && textFieldClasseB.isEmpty()) {
				resultLabel.setText("Aucune classe n'a été indiquée");
			} else if (textFieldClasseA.isEmpty()) {
				resultLabel.setText("La classe 1 n'a été indiquée");
			} else if (textFieldClasseB.isEmpty()) {
				resultLabel.setText("La classe 2 n'a été indiquée");
			} else {
				resultLabel.setText(checkMetricCouplage(textFieldClasseA, textFieldClasseB));
			}
		}
	};

	public ActionListener buttonTerminerListener = new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	        // Réinitialiser les champs de texte
	        textField1.setText("");
	        textField2.setText("");

	        // Réinitialiser le label de résultat
	        resultLabel.setText("");
	        couplingGraphInterne.getListEdge().clear();
	        // Ajoutez ici d'autres réinitialisations si nécessaire
	        // ...

	        // Vous pouvez également ajouter du code ici si vous souhaitez effectuer
	        // d'autres actions, comme retourner à un autre panel, etc.
	    }
	};

}