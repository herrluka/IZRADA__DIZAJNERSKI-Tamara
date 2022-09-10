package readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import commands.Command;
import commands.RedoCommand;
import commands.UndoCommand;
import model.DrawingModel;


public class LoggReader {
	
	private DrawingModel model;
	private String filePath;
	private List<Command> undoList = new ArrayList<Command>();
	private List<Command> redoList = new ArrayList<Command>();
	private int currentUndoPostiton;
	private CommandParser parser;
	
	public LoggReader(DrawingModel model) {
		this.model = model;
		parser = new CommandParser(model);
	}
	
	public List<String> read() {
		JFileChooser jFileChooser = new JFileChooser(new File("D:\\"));
		jFileChooser.setDialogTitle("Open file");
		jFileChooser.setFileFilter(new FileNameExtensionFilter("Text file", "txt"));
		
		List<String> loadedCommandsAsStrings = new ArrayList<String>();
		int result = jFileChooser.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			filePath = jFileChooser.getSelectedFile().getAbsolutePath();
			try {
				BufferedReader buffer = new BufferedReader(new FileReader(filePath));
				
				String line;
				while((line = buffer.readLine()) != null) {
					loadedCommandsAsStrings.add(line);
				}
				
				buffer.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null,"File not found","Error",JOptionPane.WARNING_MESSAGE);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"Format problem","Error",JOptionPane.WARNING_MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null,"File not loaded.","Error",JOptionPane.WARNING_MESSAGE);
			}
		}
		
		return loadedCommandsAsStrings;
	}
	
	public Command readLine(String line) throws Exception {
		Command command;
		if(line.equals("Undo")) {
			command = new UndoCommand(undoList.get(currentUndoPostiton));
			redoList.add(undoList.get(currentUndoPostiton));
			currentUndoPostiton--;
		} else if(line.equals("Redo")) {
			command = new RedoCommand(redoList.get(redoList.size() - 1));
			redoList.remove(redoList.size() - 1);
			currentUndoPostiton++;
		} else {
			command = parser.parseCommand(line);
			undoList.add(command);
			redoList.clear();
			currentUndoPostiton = undoList.size() - 1;
		}
		
		return command;
	}
}
