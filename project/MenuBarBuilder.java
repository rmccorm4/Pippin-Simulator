package project;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuBarBuilder implements Observer{
	private JMenuItem assemble = new JMenuItem("Assemble Source...");
	private JMenuItem load = new JMenuItem("Load Program...");
	private JMenuItem exit = new JMenuItem("Exit");
	private JMenuItem go = new JMenuItem("Go");
	private JMenuItem job0 = new JMenuItem("Job 0");
	private JMenuItem job1 = new JMenuItem("Job 1");
	private ViewsOrganizer view;

	public MenuBarBuilder(ViewsOrganizer gui) {
		view = gui;
		gui.addObserver(this);
	}
	
	//Creates file menu
	public JMenu createFileMenu() {
		JMenu menu = new JMenu("File");
		//Allow ALT-F to open the menu
		menu.setMnemonic(KeyEvent.VK_F);
		
		//Allow ALT-F, ALT-A, CTRL-A to do the same thing as clicking
		//on assemble label
		assemble.setMnemonic(KeyEvent.VK_A);
		assemble.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		assemble.addActionListener(e -> view.assembleFile());
		//Put this assemble JMenuItem into the file menu
		menu.add(assemble);
		
		//Allow CTRL-L to do the same thing as clicking
		//on load label
		load.setMnemonic(KeyEvent.VK_L);
		load.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		load.addActionListener(e -> view.loadFile());
		//Put this load JMenuItem into the file menu
		menu.add(load);
		
		//Allow CTRL-E to do the same thing as clicking
		//on exit label
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		exit.addActionListener(e -> view.exit());
		//Put this exit JMenuItem into the file menu
		menu.add(exit);
		
		//Set a separating line between "Load program..." and "Exit"
		menu.addSeparator();
		
		return menu;
	}
	
	public JMenu createExecuteMenu() {
		JMenu menu = new JMenu("Execute");
		//Allow ALT-F to open the menu
		menu.setMnemonic(KeyEvent.VK_X);
		
		//Allow CTRL-G to do the same thing as clicking
		//on go label
		go.setMnemonic(KeyEvent.VK_G);
		go.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		go.addActionListener(e -> view.execute());
		//Put this go JMenuItem into the execute menu
		menu.add(go);
		return menu;
	}
	
	public JMenu createJobsMenu() {
		JMenu menu = new JMenu("Change Job");
		//Allow ALT-F to open the menu
		menu.setMnemonic(KeyEvent.VK_J);
		
		//Allow CTRL-0 to do the same thing as clicking
		//on Job 0 label
		job0.setMnemonic(KeyEvent.VK_0);
		job0.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_0, ActionEvent.CTRL_MASK));
		job0.addActionListener(e -> view.setJob(0));
		//Put this job0 JMenuItem into the Change Job menu
		menu.add(job0);
		
		//Allow CTRL-1 to do the same thing as clicking
		//on execute label
		job1.setMnemonic(KeyEvent.VK_1);
		job1.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.CTRL_MASK));
		job1.addActionListener(e -> view.setJob(1));
		//Put this job1 JMenuItem into the Change Job menu
		menu.add(job1);
		return menu;
	}
	
	public void update(Observable arg0, Object arg1) {
		assemble.setEnabled(view.getCurrentState().getAssembleFileActive());
		load.setEnabled(view.getCurrentState().getLoadFileActive());
		go.setEnabled(view.getCurrentState().getStepActive());
		job0.setEnabled(view.getCurrentState().getChangeJobActive());
		job1.setEnabled(view.getCurrentState().getChangeJobActive());
	}
}
