package controller;

import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.JOptionPane;

import commands.AddCircleCommand;
import commands.AddDonutCommand;
import commands.AddHexagonCommand;
import commands.AddLineCommand;
import commands.AddPointCommand;
import commands.AddRectangleCommand;
import commands.BringToBackCommand;
import commands.BringToFrontCommand;
import commands.Command;
import commands.DeleteCommand;
import commands.DeselectCommand;
import commands.OneStepToBackCommand;
import commands.OneStepToFrontCommand;
import commands.SelectCommand;
import commands.UpdateCircleCommand;
import commands.UpdateDonutCommand;
import commands.UpdateHexagonCommand;
import commands.UpdateLineCommand;
import commands.UpdatePointCommand;
import commands.UpdateRectangleCommand;
import model.Circle;
import model.Donut;
import model.DrawingModel;
import model.HexagonAdapter;
import model.Line;
import model.Point;
import model.Rectangle;
import model.Shape;
import observer.ObserverMessage;
import readers.LoggReader;
import readers.SerializedReader;
import strategy.Save;
import strategy.SaveSerialized;
import strategy.SaveStrategy;
import strategy.SaveTextual;
import view.DIDialogCircle;
import view.DIDialogDonut;
import view.DIDialogLine;
import view.DIDialogPoint;
import view.DIDialogRectangle;
import view.DlDialogHexagon;
import view.FrmDrawing;

@SuppressWarnings("deprecation")
public class DrawingController extends Observable {

	private FrmDrawing frame;
	private DrawingModel model;
	private List<Command> undoList = new ArrayList<Command>();
	private List<Command> redoList = new ArrayList<Command>();
	private SaveStrategy saveStrategy = new SaveStrategy();
	private String currentFilePath = "";
	private List<String> loadedCommandsFromTextFile = new ArrayList<String>();
	LoggReader reader;

	private Point lineStart;

	public DrawingController(DrawingModel model, FrmDrawing frame) {

		this.frame = frame;
		this.model = model;
		
		reader = new LoggReader(this.model);
	}

	public void mouseClicked(MouseEvent event) {

		if (frame.getMode().equals("Dodavanje")) {

			switch (frame.getChoosenShape()) {
				case "Krug":
					DIDialogCircle dialogCircle = new DIDialogCircle();
					dialogCircle.openInAddingMode(event.getX(), event.getY(), frame.getEdgeColor(), frame.getInnerColor());
					if (dialogCircle.getCircle() == null) { 
						break;
					}
					
					AddCircleCommand cmdCircle = new AddCircleCommand(dialogCircle.getCircle(), model);
					cmdCircle.execute();
					frame.addLogToList(cmdCircle.toString());
					undoList.add(cmdCircle);
					
					break;
			case "Pravougaonik":
				DIDialogRectangle dialogRectangle = new DIDialogRectangle();
				dialogRectangle.openInAddingMode(event.getX(), event.getY(), frame.getEdgeColor(), frame.getInnerColor());
				
				if (dialogRectangle.getRectangle() == null) { 
					break;
				}
				
				AddRectangleCommand cmdRect = new AddRectangleCommand(dialogRectangle.getRectangle(), model);
				cmdRect.execute();
				frame.addLogToList(cmdRect.toString());
				undoList.add(cmdRect);
				
				break;
			case "Linija":
				if (lineStart != null) {
					Line line = new Line(lineStart, new Point(event.getX(), event.getY()));
					line.setColor(frame.getEdgeColor());
					
					AddLineCommand cmdLine = new AddLineCommand(line, model);
					cmdLine.execute();
					frame.addLogToList(cmdLine.toString());
					undoList.add(cmdLine);
					
					lineStart = null;
				} else {
					lineStart = new Point(event.getX(), event.getY());
				}
				
				break;
			case "Tacka":
				Point point = new Point(event.getX(), event.getY());
				point.setColor(frame.getEdgeColor());
				
				AddPointCommand cmdPoint = new AddPointCommand(point, model);
				cmdPoint.execute();
				frame.addLogToList(cmdPoint.toString());
				undoList.add(cmdPoint);
				
				break;
			case "Krug sa rupom":
				DIDialogDonut dialogDonut = new DIDialogDonut();
				dialogDonut.openInAddingMode(event.getX(), event.getY(), frame.getEdgeColor(), frame.getInnerColor());
				
				if (dialogDonut.getDonut() == null) {
					break;
				}
				
				AddDonutCommand cmdDonut = new AddDonutCommand(dialogDonut.getDonut(), model);
				cmdDonut.execute();
				frame.addLogToList(cmdDonut.toString());
				undoList.add(cmdDonut);
				
				break;
			case "Heksagon":
				DlDialogHexagon dialogHexagon= new DlDialogHexagon();
				dialogHexagon.openInAddingMode(event.getX(), event.getY(), frame.getEdgeColor(), frame.getInnerColor());
				
				if (dialogHexagon.getHexagon() == null) {
					break;
				}
				
				AddHexagonCommand cmdHexagon = new AddHexagonCommand(dialogHexagon.getHexagon(), model);
				cmdHexagon.execute();
				frame.addLogToList(cmdHexagon.toString());
				undoList.add(cmdHexagon);
				
				break;
			}
		} else if (frame.getMode().equals("Selekcija")) {
			boolean shapeContainingPointFound = false;
			List<Shape> list = model.getShapes();
			for (int i= list.size() - 1; i >= 0; i--) {
				Shape shape = list.get(i);
				if (shape.contains(event.getX(), event.getY())) {
					shapeContainingPointFound = true;
					if(shape.isSelected() == false) {
						SelectCommand cmd = new SelectCommand(shape);
						cmd.execute();
						frame.addLogToList(cmd.toString());
						undoList.add(cmd);
						break;
					}
					else {
						List<Shape> deselectList = new ArrayList<Shape>();
						deselectList.add(shape);
						DeselectCommand cmd = new DeselectCommand(deselectList);
						cmd.execute();
						frame.addLogToList(cmd.toString());
						undoList.add(cmd);
						break;
					}
				}
			}
			
			if (shapeContainingPointFound == false) {
				List<Shape> deselectList = new ArrayList<Shape>();
				for (Shape s : model.getShapes()) {
					if (s.isSelected()) {
						deselectList.add(s);
					}
				}
				
				if(deselectList.size() > 0) {
					DeselectCommand cmd = new DeselectCommand(deselectList);
					cmd.execute();
					frame.addLogToList(cmd.toString());
					undoList.add(cmd);
				}
			} 
		}
		
		loadedCommandsFromTextFile = new ArrayList<String>();
		redoList.clear();
		frame.getView().repaint();
		
		sendMessageToObserver();

	}
	
	public void editShape() {
		Shape selectedShape = model.getSelectedShape();

		if (selectedShape != null) {
			if (selectedShape instanceof Donut) {
				DIDialogDonut dialog = new DIDialogDonut();
				Donut selectedDonut = (Donut) selectedShape;
				dialog.openInEditingMode(selectedDonut);
				
				if (dialog.getDonut() == null) {
					return;
				}
				
				Donut donutAfterEditing = dialog.getDonut();
				
				UpdateDonutCommand cmd = new UpdateDonutCommand(selectedDonut, donutAfterEditing);
				cmd.execute();
				frame.addLogToList(cmd.toString());
				undoList.add(cmd);
			} else if (selectedShape instanceof Rectangle) {
				DIDialogRectangle dialog = new DIDialogRectangle();
				Rectangle selectedRectangle = (Rectangle) selectedShape; 
				
				dialog.openInEditingMode(selectedRectangle);
				
				if (dialog.getRectangle() == null) {
					return;
				}
				
				Rectangle rectangleAfterEditing = dialog.getRectangle();
				UpdateRectangleCommand cmd = new UpdateRectangleCommand(selectedRectangle, rectangleAfterEditing);
				cmd.execute();
				frame.addLogToList(cmd.toString());
				undoList.add(cmd);
			} else if (selectedShape instanceof Line) {
				DIDialogLine dialog = new DIDialogLine();
				Line selectedLine = (Line) selectedShape;
				
				dialog.openInEditingMode(selectedLine);
				
				if (dialog.getLine() == null) {
					return;
				}
				
				Line lineAfrerEditing = dialog.getLine();
				UpdateLineCommand cmd = new UpdateLineCommand(selectedLine, lineAfrerEditing);
				cmd.execute();
				frame.addLogToList(cmd.toString());
				undoList.add(cmd);
			} else if (selectedShape instanceof Point) {
				DIDialogPoint dialog = new DIDialogPoint();
				Point selectedPoint = (Point) selectedShape;
				
				dialog.openInEditingMode(selectedPoint);
				
				if (dialog.getPoint() == null) {
					return;
				}
				
				Point pointAfterEditing = dialog.getPoint();
				UpdatePointCommand cmd = new UpdatePointCommand(selectedPoint, pointAfterEditing);
				cmd.execute();
				frame.addLogToList(cmd.toString());
				undoList.add(cmd);
			} else if (selectedShape instanceof Circle) {
				DIDialogCircle dialog = new DIDialogCircle();
				Circle selectedCircle = (Circle) selectedShape;
				
				dialog.openInEditingMode(selectedCircle);
				
				if (dialog.getCircle() == null) {
					return;
				}
				
				Circle circleAfterEditing = dialog.getCircle();
				UpdateCircleCommand cmd = new UpdateCircleCommand(selectedCircle, circleAfterEditing);
				cmd.execute();
				frame.addLogToList(cmd.toString());
				undoList.add(cmd);
			} else if (selectedShape instanceof HexagonAdapter) {
				DlDialogHexagon dialog = new DlDialogHexagon();
				HexagonAdapter selectedHexagon = (HexagonAdapter) selectedShape;
				
				dialog.openInEditingMode(selectedHexagon);
				
				if (dialog.getHexagon() == null) {
					return;
				}
				
				HexagonAdapter hexagonAfterEditing = dialog.getHexagon();
				UpdateHexagonCommand cmd = new UpdateHexagonCommand(selectedHexagon, hexagonAfterEditing);
				cmd.execute();
				frame.addLogToList(cmd.toString());
				undoList.add(cmd);
			}
		}
		
		loadedCommandsFromTextFile = new ArrayList<String>();
		redoList.clear();
		
		frame.getView().repaint();
		sendMessageToObserver();
	}
	
	public void deleteSelectedShapes() {
        int reply = JOptionPane.showConfirmDialog(null, "Da li ste sigurni da zelite da obrisete element?", "Brisanje elementa", JOptionPane.YES_NO_OPTION);
        if (reply != JOptionPane.YES_OPTION) {	                    
        	return;
        }						
		
		ArrayList<Shape> listForRemoving = new ArrayList<Shape>();

		for (Shape s: model.getShapes()) {

			if (s.isSelected()) { 
				listForRemoving.add(s);
			}						
		}

		DeleteCommand cmd = new DeleteCommand(listForRemoving, model);
		cmd.execute();
		
		loadedCommandsFromTextFile = new ArrayList<String>();
		redoList.clear();
		
		frame.getView().repaint();
		undoList.add(cmd);
		frame.addLogToList(cmd.toString());
		sendMessageToObserver();
	}

	public void moveOneStepToFront() {
		Shape selectedShape = model.getSelectedShape();
		int currentIndex = model.getShapes().indexOf(selectedShape);
		
		OneStepToFrontCommand cmd = new OneStepToFrontCommand(model, currentIndex);
		cmd.execute();
		
		frame.addLogToList(cmd.toString());
		redoList.clear();
		undoList.add(cmd);
		
		loadedCommandsFromTextFile = new ArrayList<String>();
		frame.getView().repaint();
		sendMessageToObserver();
	}
	
	public void moveOneStepToBack() {
		Shape selectedShape = model.getSelectedShape();
		int currentIndex = model.getShapes().indexOf(selectedShape);
		
		OneStepToBackCommand cmd = new OneStepToBackCommand(model, currentIndex);
		cmd.execute();
		
		loadedCommandsFromTextFile = new ArrayList<String>();
		redoList.clear();
		
		frame.addLogToList(cmd.toString());
		undoList.add(cmd);
		frame.getView().repaint();
		sendMessageToObserver();
	}
	
	public void bringToBack() {
		Shape selectedShape = model.getSelectedShape();
		int currentIndex = model.getShapes().indexOf(selectedShape);
		
		BringToBackCommand cmd = new BringToBackCommand(model, currentIndex);
		cmd.execute();
		
		loadedCommandsFromTextFile = new ArrayList<String>();
		redoList.clear();
		
		frame.addLogToList(cmd.toString());
		undoList.add(cmd);
		frame.getView().repaint();
		sendMessageToObserver();
	}
	
	public void bringToFront() {
		Shape selectedShape = model.getSelectedShape();
		int currentIndex = model.getShapes().indexOf(selectedShape);
		
		BringToFrontCommand cmd = new BringToFrontCommand(model, currentIndex);
		cmd.execute();
		
		loadedCommandsFromTextFile = new ArrayList<String>();
		redoList.clear();
		
		frame.addLogToList(cmd.toString());
		undoList.add(cmd);
		frame.getView().repaint();
		sendMessageToObserver();
	}
	
	
	public void undo() {
		redoList.add(undoList.get(undoList.size() - 1));
		Command command = undoList.get(undoList.size() - 1);	
		command.unexecute();
		undoList.remove(undoList.size() - 1);
		
		frame.addLogToList("Undo");
		frame.getView().repaint();
		sendMessageToObserver();
	}
	
	public void redo() {
		undoList.add(redoList.get(redoList.size() - 1));
		Command command = redoList.get(redoList.size() - 1);
		command.execute();
		redoList.remove(redoList.size() - 1);
		
		frame.addLogToList("Redo");
		frame.getView().repaint();
		sendMessageToObserver();
	}
	
	public void sendMessageToObserver() {
		ObserverMessage observerMessage = new ObserverMessage(model.getShapes().size(), model.getNumberOfSelectedShapes(),
				undoList.size(), redoList.size(), model.getSelectedShape(), currentFilePath != null && !currentFilePath.isEmpty(), 
				loadedCommandsFromTextFile.size());
		
		if (model.getSelectedShape() != null) {
			observerMessage.setShapeIndex(model.getShapes().indexOf(model.getSelectedShape()));
		} else {
			observerMessage.setShapeIndex(-1);
		}
		
		this.setChanged();
		this.notifyObservers(observerMessage);
	}

	public void cleanDrawingBoard() {
		model.cleanList();

		undoList.clear();
		redoList.clear();
		frame.cleanLogList();
		
		currentFilePath = null;
		
		loadedCommandsFromTextFile = new ArrayList<String>();
		
		frame.getView().repaint();
		sendMessageToObserver();
	}

	public void save() {
		saveStrategy.save(currentFilePath);
	}
	
	private void saveAs(Save strategy) {
		saveStrategy.setSaver(strategy);
		currentFilePath = saveStrategy.saveAs();
		sendMessageToObserver();
	}

	public void saveSerialized() {
		Save strategy = new SaveSerialized(model.getShapes());
		saveAs(strategy);
	}

	public void saveTextual() {
		Save strategy = new SaveTextual(frame.getLoggListModel());
		saveAs(strategy);
	}
	
	public void readSerialized() {
		SerializedReader reader = new SerializedReader();
		List<Shape> shapeList = null;
		
		try {
			shapeList = reader.read();
			if (shapeList != null) {
				saveStrategy.setSaver(new SaveSerialized(model.getShapes()));
				currentFilePath = reader.getPath();
				
				model.setShapes(shapeList);
				
				undoList.clear();
				redoList.clear();
				
				this.frame.cleanLogList();
				frame.getView().repaint();
				
				sendMessageToObserver();
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File not found.","Error!",JOptionPane.WARNING_MESSAGE);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Class not found.","Error!",JOptionPane.WARNING_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error.","Error!",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void openTextual() {
		loadedCommandsFromTextFile = reader.read();
		
		saveStrategy.setSaver(new SaveTextual(frame.getLoggListModel()));
		sendMessageToObserver();
	}

	public void executeNextLoadedCommand() {
		Command cmd;
		try {
			cmd = reader.readLine(loadedCommandsFromTextFile.get(0));
			
			loadedCommandsFromTextFile.remove(0);
			cmd.execute();
			
			undoList.add(cmd);
			
			frame.addLogToList(cmd.toString());
			sendMessageToObserver();
			
			frame.getView().repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
