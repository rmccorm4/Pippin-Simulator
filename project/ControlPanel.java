package project;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;


//I think this class implements Observer?
//"The update method calls methods like 
//the following for each of the four buttons"
public class ControlPanel implements Observer{
	 private JButton stepButton = new JButton("Step");
	 private JButton clearButton = new JButton("Clear");
	 private JButton runButton = new JButton("Run/pause");
	 private JButton reloadButton = new JButton("Reload");
	 private ViewsOrganizer view;
	 
	 public ControlPanel(ViewsOrganizer gui) {
			view = gui;
			gui.addObserver(this);
	 }
	 
	 public JComponent createControlDisplay() {
		 JPanel panel = new JPanel();
		 panel.setLayout(new GridLayout(1,0)); //Also tried (new GridLayout(1, 0));
		 panel.add(stepButton);
		 panel.add(clearButton);
		 panel.add(runButton);
		 panel.add(reloadButton);
		 stepButton.addActionListener(e -> view.step());
		 clearButton.addActionListener(e -> view.clearJob());
		 runButton.addActionListener(e -> view.toggleAutoStep());
		 reloadButton.addActionListener(e -> view.reload());
		 JSlider slider = new JSlider(5,1000);
		 slider.addChangeListener(e -> view.setPeriod(slider.getValue()));
		 panel.add(slider);
		 return panel;
	 }
	 public void update(Observable arg0, Object arg1) {
		 runButton.setEnabled(view.getCurrentState().getRunPauseActive());
		 stepButton.setEnabled(view.getCurrentState().getStepActive());
		 clearButton.setEnabled(view.getCurrentState().getClearActive());
		 reloadButton.setEnabled(view.getCurrentState().getReloadActive());
	 }
}
