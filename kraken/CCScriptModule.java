import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class CCScriptModule extends Kraken implements ActionListener {
	
	static JFrame frame = new JFrame("CCScript Utility");
	static JPanel diffTab = new JPanel();
	static GroupLayout lay = new GroupLayout(diffTab);
	static JFileChooser fco = new JFileChooser();
	static JMenuBar bar = new JMenuBar();
	static File f;
	static File fs;
	public static File ROM;
	public static File hackROM;
	static byte[] rombytes;
	static FileInputStream is;
	static JMenu file = new JMenu("File");
	static JMenuItem close = new JMenuItem("Close");
	static JButton openROM = new JButton("Open Clean ROM");
	static JButton hackROMbutton = new JButton("Open Hacked ROM");
	static JButton diff = new JButton("Diff ROMs");
	static JTextField currentCleanFile = new JTextField("", 24);
	static JTextField currentHackFile = new JTextField("", 24);
	static JTextField outputName = new JTextField("AAA_KRAKEN_BASE_DIFF.ccs", 24);
	static JTextField pointer1 = new JTextField("", 12);
	static JTextField pointer2 = new JTextField("", 12);
	static JLabel ranget = new JLabel("Range Start:");
	static JLabel ranget2 = new JLabel("Range End:");
	static JTabbedPane tabp = new JTabbedPane(SwingConstants.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
	static JOptionPane dia = new JOptionPane();
	static boolean initDone = false;
	
	public void init() {
	if (!initDone) {
		frame.setJMenuBar(bar);
		bar.add(file);
//		file.addSeparator();
		file.add(close);
		close.addActionListener(this);
		close.setAccelerator(ctrlw);
		frame.add(tabp);
		tabp.addTab("ROM Diff to CCS", diffTab);
		initDiffGUI();
		openROM.addActionListener(this);
		hackROMbutton.addActionListener(this);
		diff.addActionListener(this);
		currentCleanFile.setEnabled(false);
		currentHackFile.setEnabled(false);
		fco.setFileFilter(new FileNameExtensionFilter("SNES ROMs: .smc, .sfc", "smc", "sfc"));
		fco.setAcceptAllFileFilterUsed(false);
		frame.pack();
		frame.setVisible(true);
		initDone = true;
	} else { lafUpdate(); frame.setVisible(true); }
	}
	
	public void initDiffGUI() {
		
		diffTab.setLayout(lay);
		lay.setAutoCreateGaps(true);
		lay.setAutoCreateContainerGaps(true);
		lay.setHorizontalGroup(lay.createSequentialGroup().addGroup(lay.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(openROM).addComponent(hackROMbutton).addComponent(diff)).addGroup(lay.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(currentCleanFile).addComponent(currentHackFile).addComponent(outputName)).addGroup(lay.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(ranget).addComponent(ranget2)).addGroup(lay.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(pointer1).addComponent(pointer2)));
		lay.setVerticalGroup(lay.createSequentialGroup().addGroup(lay.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(openROM).addComponent(currentCleanFile).addComponent(ranget).addComponent(pointer1)).addGroup(lay.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(hackROMbutton).addComponent(currentHackFile).addComponent(ranget2).addComponent(pointer2)).addGroup(lay.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(diff).addComponent(outputName)));
		lay.linkSize(openROM, hackROMbutton, diff);
		lay.linkSize(pointer1, pointer2);
		lay.linkSize(currentCleanFile, currentHackFile, outputName);
		lay.linkSize(ranget, ranget2);
		lafUpdate();
	}
	
	public void lafUpdate() {
		SwingUtilities.updateComponentTreeUI(frame);
		frame.pack();
	}
	
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		
		if (src == diff) {
			try {
			if (currentCleanFile.getText() != "" && currentHackFile.getText() != "" && pointer1.getText() != "" && pointer2.getText() != "" && outputName.getText() != "") {
						
						int unorange = Integer.parseInt(pointer1.getText(), 16);
						int dosrange = Integer.parseInt(pointer2.getText(), 16);
						
						if (unorange >= 0xC00000) {
							unorange -= 0xC00000;
						}
						
						if (dosrange >= 0xC00000) {
							dosrange -= 0xC00000;
						}
						
						String outfile = "";
						
						if (outputName.getText().endsWith(".ccs")) {
							outfile = outputName.getText();
						} else {
							outfile = outputName.getText() + ".ccs";
						}
						
						File foutfile = new File(gui.projectString + File.separator + "ccscript" + File.separator + outfile);
						
						if (foutfile.getAbsoluteFile().exists()) {
							if (dia.showConfirmDialog(null, "File " + outfile + " already exists. Would you like to replace it?", "File exists", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
								return;
							}
						}
						
						rtccs.runDiff(ROM, f, unorange, dosrange, foutfile);
				}
			} catch (Exception excep) { dia.showMessageDialog(null, "Please fill in all fields with their correct values.", "You screwed up", JOptionPane.ERROR_MESSAGE); System.err.println(excep.toString()); }
		}
		
		if (src == close) {
			frame.setVisible(false);
		}
		
		if (src == openROM) {
			SwingUtilities.updateComponentTreeUI(fco);
			fco.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int fcs = fco.showOpenDialog(null);
				
				if (fcs == JFileChooser.APPROVE_OPTION) {
				ROM = fco.getSelectedFile();
				currentCleanFile.setText(ROM.toString());
			}
		}
		
		if (src == hackROMbutton) {
			SwingUtilities.updateComponentTreeUI(fco);
			fco.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int fcs = fco.showOpenDialog(null);
				
				if (fcs == JFileChooser.APPROVE_OPTION) {
				f = fco.getSelectedFile();
				currentHackFile.setText(f.toString());
			}
		}
		
	}
}