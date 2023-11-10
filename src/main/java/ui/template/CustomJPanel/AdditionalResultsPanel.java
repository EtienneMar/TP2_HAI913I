package ui.template.CustomJPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ui.controller.LabelMap;
import ui.controller.Resultat;
import ui.controller.SelectProjectController;
import ui.paramater.MyViewParameter;


public class AdditionalResultsPanel extends JPanel {
	
    private MyViewParameter myParam = new MyViewParameter();
    private JButton btnTerminer;
    private LabelMap labels = new LabelMap();
    private JPanel contentPanel; // Panel pour le contenu
    private int i = 2;
    private int j = 0;
    private int myY = myParam.getyBouton() * i;
    private JLabel valueLabel;

    public JButton getBtnTerminer() {
        return btnTerminer;
    }

    public AdditionalResultsPanel(JFrame frame) {
        frame.getContentPane().add(this, BorderLayout.CENTER);
        this.setLayout(new BorderLayout());
        
        valueLabel = new JLabel();

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("--> Resultats :");
        titleLabel.setFont(MyViewParameter.getMyFontStyleTitle());
        topPanel.add(titleLabel);
        btnTerminer = new JButton("Terminer");
        btnTerminer.setFont(MyViewParameter.getMyFontStyle());
        btnTerminer.setBackground(Color.lightGray);
        topPanel.add(btnTerminer);

        contentPanel = new JPanel();
        contentPanel.setLayout(null);

  
        JScrollPane scrollPane = new JScrollPane(contentPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 


        this.add(topPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void printResults(Map<String, Resultat> results, String myType, int n) {
        labels.setAdditionalAnalysis11(n);

        results.forEach((k, res) -> {
            JLabel keyLabel = new JLabel(labels.getAdditionalAnalysisByID(k) + " : ");
            keyLabel.setBounds(myParam.getxBouton(), (int) Math.round(myY), myParam.getLargeurBouton() * 2,
                    myParam.getHauteurBouton());
            keyLabel.setFont(MyViewParameter.getMyFontStyle());

     
            int valueLabelX = keyLabel.getX() + keyLabel.getWidth();

            j = 0;
            res.getResultats().forEach((kk, vv) -> {
                if (vv != 0) {
                    valueLabel = new JLabel(kk + " -> " + vv);
                } else {
                    valueLabel = new JLabel(kk);
                }

                valueLabel.setBounds(valueLabelX, keyLabel.getY() + j, myParam.getLargeurBouton(),
                        myParam.getHauteurBouton());
                valueLabel.setFont(MyViewParameter.getMyFontStyle());

                contentPanel.add(valueLabel);
                myY = valueLabel.getY() + 40;
                j += 20;
            });
            contentPanel.add(keyLabel);
            i++;
        });

        int width = myParam.getLargeurFenetre(); 
        int height = myY + myParam.getHauteurBouton(); 
        contentPanel.setPreferredSize(new Dimension(width, height));
    }

}
