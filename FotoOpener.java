import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;


@SuppressWarnings("serial")
public class FotoOpener extends JFrame implements ActionListener{
	public static void main(String[] args) {
		new FotoOpener(args);
	}
	
	String filename;
	int x=1150,y=550;//Größe von panel
//	int xx=1300,yy=670;//Größe vom Frame
	Image aktuellesBild =null;
	myPanel panel = new myPanel();
	Container contentPane = getContentPane();
	
	@SuppressWarnings("static-access")
	public FotoOpener(String[] args) {
		super("PhotoViewer von Maximilian Schindler");
		if(args.length==0) {
			openFiles();
		}else {
			filename = (String) args[0];
			if(filename.contains(":")) { filename = filename.replace( ":" , "/"); }
			if(filename.contains("OSX")) { filename = filename.substring(3);}
			System.out.println(filename);
			aktuellesBild=getToolkit().getImage(filename);//Bild laden
		}
		
		contentPane.setLayout(new BorderLayout());
		
		JMenuBar menubar = new JMenuBar();
		contentPane.add("North", menubar);
		JMenu menu = new JMenu("Datei");
		menubar.add(menu);
		JMenuItem mi;
		//Öffnen
		mi = new JMenuItem("Öffnen");
		setCtrlAccelerator(mi, 'O');
		mi.addActionListener(this);
		menu.add(mi);
		//Speichern
		mi = new JMenuItem("Vollbildschirm");
		setCtrlAccelerator(mi, 'V');
		mi.addActionListener(this);
		menu.add(mi);
		
		menu.addSeparator();//Separator
		
		//Slideshow starten
		mi = new JMenuItem("Slideshow starten");
		setCtrlAccelerator(mi, 'S');
		mi.addActionListener(this);
		menu.add(mi);
		//Slideshow starten
		mi = new JMenuItem("Slideshow stoppen");
		setCtrlAccelerator(mi, 'T');
		mi.addActionListener(this);
		menu.add(mi);
		//Slideshow Zeit einstellen --> Untermenü
		JMenu mj = new JMenu("Zeit einstellen");
		menu.add(mj);
		mj.addActionListener(this);
		JMenuItem m=new JMenuItem("Eine Sekunde");
		setCtrlAccelerator(m, '1');
		m.addActionListener(this);
		mj.add(m);
		m=new JMenuItem("Zwei Sekunden");
		setCtrlAccelerator(m, '2');
		m.addActionListener(this);
		mj.add(m);
		m=new JMenuItem("Drei Sekunden");
		setCtrlAccelerator(m, '3');
		m.addActionListener(this);
		mj.add(m);
		m=new JMenuItem("Vier Sekunden");
		setCtrlAccelerator(m, '4');
		m.addActionListener(this);
		mj.add(m);
		m=new JMenuItem("Fünf Sekunden");
		setCtrlAccelerator(m, '5');
		m.addActionListener(this);
		mj.add(m);
		//Beenden
		mi = new JMenuItem("Beenden");
		setCtrlAccelerator(mi, 'E');
		mi.addActionListener(this);
		menu.add(mi);
		
		Label filelabel = new Label(filename);
		contentPane.add("South", filelabel);
		
		panel.setAlignmentX(panel.CENTER_ALIGNMENT);//Bild in die Mitte schieben
		contentPane.add("Center",panel);
		pack();
		
		setSize(new Dimension(x, y));
		setLocationRelativeTo(null);//Frame in der Bildmitte
		setVisible(true);
		panel.repaint();
	}

	private void openFiles() {
		JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);//Multiselection
        fc.setAcceptAllFileFilterUsed(false);//Nicht alle Dateien öffenbar
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Bild",ImageIO.getReaderFileSuffixes()));
        fc.setCurrentDirectory(new File("/Users/maxi/Pictures"));
        fc.showOpenDialog(null);
        File selectedFile = fc.getSelectedFile();
        filename = selectedFile.toString();
        System.out.println("opened File: " + filename);
	}

	private void setCtrlAccelerator(JMenuItem mi, char acc) {
		KeyStroke ks = KeyStroke.getKeyStroke(acc, Event.CTRL_MASK);
		mi.setAccelerator(ks);
	}
	
	public void actionPerformed(ActionEvent e) {
		String str = (String) e.getActionCommand();
		if(str=="Öffnen") {
			openFiles();
		} else if (str=="Vollbildschirm") { x=1250; setExtendedState(JFrame.MAXIMIZED_BOTH);}
		
		panel.repaint();
		if(str=="Beenden") {dispose(); System.exit(0);}

	}	
		
	class myPanel extends JPanel{
	
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			aktuellesBild=getToolkit().getImage(filename);// String wird Bild
			aktuellesBild = aktuellesBild.getScaledInstance(x, y, Image.SCALE_DEFAULT);//Bild skalieren
			
			//Bilder laden
			MediaTracker mt = new MediaTracker(this);
			mt.addImage(aktuellesBild, 0);
			try {
				mt.waitForAll();
			} catch (InterruptedException exc) { }
			
			//Bild zeichnen lassen
			if(aktuellesBild!= null) {
				g.clearRect(0, 0, x, y);
				g.drawImage(aktuellesBild,0, 0, this);
			}
		}
	}
}