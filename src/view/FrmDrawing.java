package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.DrawingController;
import model.Circle;
import model.Donut;
import model.Line;
import model.Point;
import model.Rectangle;
import model.Shape;
import observer.ObserverMessage;

import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Insets;

public class FrmDrawing extends JFrame implements Observer {

    private DrawingView view = new DrawingView();
    private DrawingController controller;
    
    private JPanel toolBar;
	private JToggleButton btnDodavanje;
	private JButton btnBrisanje;
	private JButton btnIzmeni;
	private JButton btnToBack;
	private JButton btnToFront;
	private JButton btnBringToBack;
	private JButton btnBringToFront;
	private JButton btnEdgeColor;
	private JButton btnInnerColor;
	private JButton btnUndo;
	private JButton btnRedo;
	private JButton btnLoadTextCommand;
	private JToggleButton btnSelektovanje;
	private ArrayList<Shape> shapes;
	private JPanel contentPane;
	private JComboBox<String> shapeChooser; 

    private String mode = "Dodavanje";
    private JScrollPane scrollPane;
    private JList<String> listOfLogs;
    private DefaultListModel<String> listOfLogsModel;
    private JPanel bottom;
    
    private JMenuBar menu;
	private JMenu menuFile;
	private JMenuItem newOption;
	private JMenuItem openSerializedOption;
	private JMenuItem saveOption;
	private JMenuItem saveTextualOption;
	private JMenuItem saveSerializedOption;
	private JMenuItem openTextualOption;


	public FrmDrawing() {

		setTitle("Vukcevic Tamara IT 37-2017");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(new Dimension(1024,768));
		setExtendedState(JFrame.MAXIMIZED_BOTH); 

		shapes=new ArrayList<Shape>();
		
		view.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.mouseClicked(e);
			}
		});

		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		toolBar=new JPanel();
		btnDodavanje=new JToggleButton("Dodaj");
		btnDodavanje.setSelected(true);
		btnDodavanje.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				btnSelektovanje.setSelected(false);
				btnIzmeni.setEnabled(false);
				btnBrisanje.setEnabled(false);
				clearSelection();
				mode = "Dodavanje";
			}			

		});
		btnBrisanje=new JButton("Brisanje");
		btnBrisanje.setEnabled(false);
		btnBrisanje.addActionListener(new ActionListener() {

			@Override
		public void actionPerformed(ActionEvent e) {
			controller.deleteSelectedShapes();
		}

		});
		btnIzmeni=new JButton("Izmena");
		btnIzmeni.setEnabled(false);
		btnIzmeni.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.editShape();
			}

		});
		btnSelektovanje=new JToggleButton("Selektovanje");
		btnSelektovanje.setEnabled(false);
		btnSelektovanje.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				btnDodavanje.setSelected(false);
				mode = "Selekcija";
			}

		});
		String [] options= {"Krug","Tacka","Pravougaonik","Linija","Krug sa rupom", "Heksagon"};
		shapeChooser = new JComboBox<String>(options);
		
		btnEdgeColor = new JButton("Edge color");
		btnEdgeColor.setBackground(Color.BLACK);
		btnEdgeColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				Color color = JColorChooser.showDialog(null, "Choose a color", btnEdgeColor.getBackground());
				if (color != null) {
					btnEdgeColor.setBackground(color);
				}
			}

		});
		
		btnInnerColor = new JButton("Inner color");
		btnInnerColor.setBackground(Color.WHITE);
		btnInnerColor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				Color color = JColorChooser.showDialog(null, "Choose a color", btnInnerColor.getBackground());
				if (color != null) {
					btnInnerColor.setBackground(color);
				}
			}

		});
		
		btnToFront = new JButton("To front");
		btnToFront.setEnabled(false);
		btnToFront.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				controller.moveOneStepToFront();
			}

		});
		
		btnToBack = new JButton("To back");
		btnToBack.setEnabled(false);
		btnToBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				controller.moveOneStepToBack();
			}

		});
		
		btnBringToBack = new JButton("Bring to back");
		btnBringToBack.setEnabled(false);
		btnBringToBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				controller.bringToBack();
			}

		});
		
		btnBringToFront = new JButton("Bring to front");
		btnBringToFront.setEnabled(false);
		btnBringToFront.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				controller.bringToFront();
			}

		});
		
		btnUndo = new JButton("Undo");
		btnUndo.setEnabled(false);
		btnUndo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				controller.undo();
			}

		});
		
		btnRedo = new JButton("Redo");
		btnRedo.setEnabled(false);
		btnRedo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				controller.redo();
			}

		});
		
		btnLoadTextCommand = new JButton("Load next");
		btnLoadTextCommand.setEnabled(false);
		btnLoadTextCommand.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {				
				controller.executeNextLoadedCommand();
			}

		});

		toolBar.add(shapeChooser);	
		toolBar.add(btnDodavanje);
		toolBar.add(btnBrisanje);
		toolBar.add(btnIzmeni);
		toolBar.add(btnSelektovanje);
		toolBar.add(btnToFront);
		toolBar.add(btnToBack);
		toolBar.add(btnBringToFront);
		toolBar.add(btnBringToBack);
		toolBar.add(btnUndo);
		toolBar.add(btnRedo);
		toolBar.add(btnEdgeColor);
		toolBar.add(btnInnerColor);
		toolBar.add(btnLoadTextCommand);
		contentPane.add(toolBar, BorderLayout.NORTH);	
		contentPane.add(view, BorderLayout.CENTER);

		setContentPane(contentPane);
		
		listOfLogs = new JList<String>();
		listOfLogsModel = new DefaultListModel<String>();
		listOfLogs.setModel(listOfLogsModel);
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(listOfLogs);
		
		bottom = new JPanel();
		bottom.setLayout(new BorderLayout());
		bottom.add(scrollPane, BorderLayout.CENTER);
		bottom.setBorder(new EmptyBorder(10, 10, 10, 10));
		bottom.setBackground(Color.LIGHT_GRAY);
		
		
		JLabel bottomTitleLabel = new JLabel("Logs"); 
		bottomTitleLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		bottom.add(bottomTitleLabel, BorderLayout.NORTH);
		
		contentPane.add(bottom, BorderLayout.SOUTH);
		
		menu = new JMenuBar();
		menu.setMargin(new Insets(0, 0, 70, 0));
		setJMenuBar(menu);
		
		menuFile = new JMenu("File");
		menuFile.setHorizontalAlignment(SwingConstants.LEFT);
		menu.add(menuFile);
		
		newOption = new JMenuItem("New");
		newOption.setFont(new Font("Arial", Font.PLAIN, 16));
		menuFile.add(newOption);
		
		newOption.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.cleanDrawingBoard();
			}
		});
		
		openSerializedOption = new JMenuItem("Open serialized");
		openSerializedOption.setFont(new Font("Arial", Font.PLAIN, 16));
		menuFile.add(openSerializedOption);
		
		openSerializedOption.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.readSerialized();
			}
		});
		
		openTextualOption = new JMenuItem("Open textual");
		openTextualOption.setFont(new Font("Arial", Font.PLAIN, 16));
		menuFile.add(openTextualOption);
		
		openTextualOption.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.openTextual();
			}
		});
		
		saveOption = new JMenuItem("Save");
		saveOption.setFont(new Font("Arial", Font.PLAIN, 16));
		saveOption.setEnabled(false);
		menuFile.add(saveOption);
		
		saveOption.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.save();
			}
		});
		
		saveSerializedOption = new JMenuItem("Save serialized");
		saveSerializedOption.setFont(new Font("Arial", Font.PLAIN, 16));
		menuFile.add(saveSerializedOption);
		
		saveSerializedOption.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.saveSerialized();
			}
		});
		
		saveTextualOption = new JMenuItem("Save textual");
		saveTextualOption.setFont(new Font("Arial", Font.PLAIN, 16));
		menuFile.add(saveTextualOption);
		
		saveTextualOption.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.saveTextual();
			}
		});
		
	}
	

	@Override
	public void update(Observable o, Object arg) {
		ObserverMessage message = (ObserverMessage)arg;
		btnSelektovanje.setEnabled(message.getNumberOfShapesInList() > 0);
		btnIzmeni.setEnabled(message.getNumberOfSelectedShapes() == 1);
		btnBrisanje.setEnabled(message.getNumberOfSelectedShapes() > 0);
		
		btnBringToBack.setEnabled(message.getNumberOfSelectedShapes() == 1 && message.getShapeIndex() != 0);
		btnBringToFront.setEnabled(message.getNumberOfSelectedShapes() == 1 && message.getShapeIndex() != message.getNumberOfShapesInList() - 1);
		btnToBack.setEnabled(message.getNumberOfSelectedShapes() == 1 && message.getShapeIndex() != 0);
		btnToFront.setEnabled(message.getNumberOfSelectedShapes() == 1 && message.getShapeIndex() != message.getNumberOfShapesInList() - 1);
		
		btnUndo.setEnabled(message.getNumberOfShapesInUndoList() > 0);
		btnRedo.setEnabled(message.getNumberOfShapesInRedoList() > 0);
		
		saveOption.setEnabled(message.getFiltePathSet());
		
		btnLoadTextCommand.setEnabled(message.getNumberOFCommandsLoadedFromTextFile() > 0);
	}
	
	public DrawingView getView() {
		return view;
	}
	
	public void setController(DrawingController controller) {
		this.controller = controller;
	}

	public void clearSelection() {
		for (Shape s: shapes) {
			s.setSelected(false);
		}
		repaint();
	}

	public ArrayList<Shape> getShapes() {
		return shapes;
	}


	public void setShapes(ArrayList<Shape> shapes) {
		this.shapes = shapes;
	}

	public String getChoosenShape() {
		return shapeChooser.getSelectedItem().toString();
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public JButton getBtnBrisanje() {
		return btnBrisanje;
	}


	public JButton getBtnIzmeni() {
		return btnIzmeni;
	}
	
	public void addLogToList(String log) {
		listOfLogsModel.addElement(log);
	}
	
	public DefaultListModel<String> getLoggListModel() {
		return listOfLogsModel;
	}
	
	public void cleanLogList() {
		listOfLogsModel.clear();
	}

	public Color getEdgeColor() {
		return btnEdgeColor.getBackground();
	}

	public void setEdgeColor(Color color) {
		this.btnEdgeColor.setBackground(color);
	}

	public Color getInnerColor() {
		return btnInnerColor.getBackground();
	}

	public void setInnerColor(Color color) {
		this.btnInnerColor.setBackground(color);
	}

	
}
