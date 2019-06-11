
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.StyledDocument;

public class SimpleNotePad extends JFrame implements ActionListener {
	//arrays for storing and accessing recent files
	ArrayList<File> recentFiles = new ArrayList<File>(); //array of recent files
	ArrayList<JMenuItem> menuRecent = new ArrayList<JMenuItem>(); //array of recent JMenuItems
	ArrayList<String> recentNames = new ArrayList<String>(); //array of strings that were the recent files
	
	JMenuBar menuBar = new JMenuBar();
	JTextArea textArea = new JTextArea();
	JMenu fileMenu = new JMenu("File");
	JMenu editMenu = new JMenu("Edit");
	JMenu recentMenu = new JMenu("Recent");
	JTextPane d = new JTextPane();
	StyledDocument doc = d.getStyledDocument();
	JMenuItem newFile = new JMenuItem("New File");
	JMenuItem saveFile = new JMenuItem("Save File");
	JMenuItem printFile = new JMenuItem("Print File");
	JMenuItem openFile = new JMenuItem("Open File");
	JMenuItem copy = new JMenuItem("Copy");
	JMenuItem paste = new JMenuItem("Paste");
	JMenuItem replace = new JMenuItem("Replace");

	public SimpleNotePad() {
		setTitle("A Simple Notepad Tool");
		JMenuItem[] menuFile = { newFile, saveFile, printFile, openFile, recentMenu };
		JMenuItem[] menuEdit = { copy, paste, replace };
		JMenuItem[] item = { newFile, saveFile, printFile, copy, paste, openFile, replace, recentMenu };
		String[] functions = { "new", "save", "print", "copy", "paste", "Open", "Replace", "recent" };
		
		addInMenuItems(item, functions); //adding in all the JMenu items to pane
		NotePadMethods.addToFileMenu(menuFile, fileMenu); //adding menu items to file menu
		NotePadMethods.addToFileMenu(menuEdit, editMenu); //adding menu items to edit menu
		
		//recent menu listener to do something when it is scrolled over
		recentMenu.addMenuListener(new MenuListener() {
			@Override
			public void menuSelected(MenuEvent e) {
				addInMenuItems(menuRecent, recentNames);
				NotePadMethods.addToFileMenu(menuRecent, recentMenu);
			}
			@Override
			public void menuDeselected(MenuEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void menuCanceled(MenuEvent e) {
				// TODO Auto-generated method stub
			}
		});
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		setJMenuBar(menuBar);
		add(new JScrollPane(d));
		setPreferredSize(new Dimension(600, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		pack();
	}
	
	//creates new action listener to each menu item and set its action command 
	public void addInMenuItems(JMenuItem[] itemList, String[] nameList) {
		int j = 0;
		for (JMenuItem i : itemList) {
			i.addActionListener(this);
			i.setActionCommand(nameList[j]);
			j++;
		}
	}
	
	//does same as above method but just with linked lists
	public void addInMenuItems(ArrayList<JMenuItem> itemList, ArrayList<String> nameList) {
		int j = 0;
		for (JMenuItem i : itemList) {
			i.addActionListener(this);
			i.setActionCommand(nameList.get(j));
			j++;
		}
	}
	
	public void changeRecentArrays(File file) {
		if (recentFiles.size() <= 4) {
			recentFiles.add(0, file);
			menuRecent.add(0, NotePadMethods.newJMenuItem(file));
			recentNames.add(0, file.getName());
		} else {
			recentMenu.remove(menuRecent.get(4));
			recentFiles.remove(4);
			menuRecent.remove(4);
			recentFiles.add(0, file);
			menuRecent.add(0, NotePadMethods.newJMenuItem(file));
			recentNames.add(0, file.getName());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		File newfile = null;
		if (e.getActionCommand().equals("new")) {
			d.setText("");
		} else if (e.getActionCommand().equals("save")) {
			NotePadMethods.save();
		} else if (e.getActionCommand().equals("print")) {
			NotePadMethods.print();
		} else if (e.getActionCommand().equals("copy")) {
			d.copy();
		} else if (e.getActionCommand().equals("paste")) {
			d.paste();
		} else if (e.getActionCommand().equals("Replace")) {
			NotePadMethods.replace(d);
		} else if (e.getActionCommand().equals("Open")) {
		    newfile = NotePadMethods.open(doc, d); //opening a new file and returns the file opened
			//below is organizing and going to recent arrays to add and remove info as needed
		    changeRecentArrays(newfile);
		}
		//doing something when one of the recent files want to be accessed.
		int j = 0;
		for(int i = 0; i <= recentNames.size()-1; i++) {
			if(e.getActionCommand().equals(recentNames.get(i))) {
				newfile = recentFiles.get(i);
				NotePadMethods.openRecentFile(doc, d, recentFiles.get(i));
				j = 1;
			}
			j=0;
		}
		if(j == 1) {
			changeRecentArrays(newfile);
		}
	}
	public static void main(String[] args) {
		SimpleNotePad app = new SimpleNotePad();
	}
}