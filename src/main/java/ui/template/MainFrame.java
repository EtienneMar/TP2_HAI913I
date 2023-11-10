package ui.template;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import ui.controller.SelectProjectController;
import ui.paramater.MyViewParameter;
import ui.template.CustomJPanel.MainPanel;



public class MainFrame {
	
	private MyViewParameter myParam = new MyViewParameter();
    private JFrame frame  = new JFrame();
    private SelectProjectController controller;
  
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame window = new MainFrame();
                    window.frame.setVisible(true);
                
                    
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MainFrame() {
        initialize();
    }

    public void initialize() {
        
        frame.setBounds(myParam.getxFenetre(), myParam.getyFenetre(), myParam.getLargeurFenetre(), myParam.getHauteurFenetre());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Créez un JPanel contenant un CardLayout
        JPanel cardPanel = new JPanel(new CardLayout());
        
        MainPanel  panel1 = new MainPanel(frame);
        
       
        //Ajoutez les panneaux au conteneur principal avec des noms d'identification
        cardPanel.add(panel1, "Panel1");
      

        // Ajoutez le conteneur principal au frame
        frame.add(cardPanel);

   
        //CardLayout pour basculer entre les panneaux
        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        cardLayout.show(cardPanel, "Panel1"); // Affichez initialement le panel1
        
        controller = new SelectProjectController(frame,panel1,cardLayout,cardPanel);
        
  
       
        panel1.addAllListener(controller);
       

    }
}
