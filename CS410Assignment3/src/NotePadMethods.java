import java.awt.Graphics;
import java.awt.Dialog.ModalityType;
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
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.StyledDocument;

public class NotePadMethods {

	static JTextPane d = new JTextPane();
	static StyledDocument doc = d.getStyledDocument();
	
	//creating a new menu item
	public static JMenuItem newJMenuItem(File file) {
		JMenuItem newItem = new JMenuItem(file.getName());
		return newItem;
	}
	
	//adding menu items to menu via arrays 
	public static void addToFileMenu(JMenuItem[] item, JMenuItem menuItem) {
		for (int i = 0; i < item.length; i++) {
			menuItem.add(item[i]);
		}
	}

	//adding menu items to menu via arraylists
	public static void addToFileMenu(ArrayList<JMenuItem> item, JMenuItem menuItem) {
		for (JMenuItem i : item) {
			menuItem.add(i);
		}
	}
	
	//prints the text at hand
	public static void print() {
		try {
			PrinterJob pjob = PrinterJob.getPrinterJob();
			pjob.setJobName("Sample Command Pattern");
			pjob.setCopies(1);
			pjob.setPrintable(new Printable() {
				public int print(Graphics pg, PageFormat pf, int pageNum) {
					if (pageNum > 0)
						return Printable.NO_SUCH_PAGE;
					pg.drawString(d.getText(), 500, 500);
					// paint(pg);
					return Printable.PAGE_EXISTS;
				}
			});
			if (pjob.printDialog() == false)
				return;
			pjob.print();
		} catch (PrinterException pe) {
			JOptionPane.showMessageDialog(null, "Printer error" + pe, "Printing error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	//saves the text at hand onto local computer
	public static void save() {
		File fileToWrite = null;
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION)
			fileToWrite = fc.getSelectedFile();
		try {
			PrintWriter out = new PrintWriter(new FileWriter(fileToWrite));
			out.println(d.getText());
			JOptionPane.showMessageDialog(null, "File is saved successfully...");
			out.close();
		} catch (IOException ex) {
		}

	}
	
	//replaces or inserts user input 
	public static void replace(JTextPane pane) {
		try {
			Input dialog = new Input();
			dialog.setModalityType(ModalityType.APPLICATION_MODAL);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			String content = Input.userInput();
			pane.replaceSelection(content);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	
	//opens a file from your local computer to the note pad
	public static File open(StyledDocument document, JTextPane pane) {
		File file = null;
		JFileChooser fc = new JFileChooser();
		if (JFileChooser.APPROVE_OPTION == fc.showOpenDialog(pane)) {
			file = fc.getSelectedFile();
			pane.setText("");
			Scanner in = null;
			try {
				in = new Scanner(file);
				while (in.hasNext()) {
					String line = in.nextLine();
					document.insertString(document.getLength(), line + "\n", null);

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				in.close();
			}
		}
		return file;
	}
	
	//opens recent file from your local computer
	public static void openRecentFile(StyledDocument document, JTextPane pane, File file) {
		pane.setText("");
		Scanner in = null;
		try {
			in = new Scanner(file);
			while (in.hasNext()) {
				String line = in.nextLine();
				document.insertString(document.getLength(), line + "\n", null);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			in.close();
		}
	}


}
