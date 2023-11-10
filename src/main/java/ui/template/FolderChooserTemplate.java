package ui.template;

import java.awt.Dimension;

import javax.swing.JFileChooser;

import ui.paramater.MyViewParameter;

public class FolderChooserTemplate extends JFileChooser{

	private MyViewParameter myParam = new MyViewParameter();


	public FolderChooserTemplate() {
		super();
		this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		 this.setPreferredSize(new Dimension(myParam.getLargeurFenetre(), (int) (myParam.getHauteurFenetre()*0.8)));
	     this.setLocation(myParam.getxFenetre(),myParam.getyFenetre());
	}

}
