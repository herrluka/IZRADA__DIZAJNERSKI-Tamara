package readers;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

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

public class CommandParser {
	
	private DrawingModel model;
	
	public CommandParser(DrawingModel model) {
		this.model = model;
	}
	
	public Command parseCommand(String line) throws Exception {
		String command = line.split("_")[0];
		String withoutCommand = line.split("_")[1];
		if(command.equals("Add")) {
			return parseAdd(withoutCommand);
		} else if(command.equals("Remove")) {
			return parseRemove(withoutCommand);
		} else if (command.equals("Update")) {
			return parseUpdate(withoutCommand);
		} else if (command.equals("One step to back")) {
			return parseOneStepBack(withoutCommand);
		} else if (command.equals("One step to front")) {
			return parseOneStepToFront(withoutCommand);
		} else if (command.equals("Bring to end")) {
			return parseBringToEnd(withoutCommand);
		} else if (command.equals("Bring to front")) {
			return parseBringToFront(withoutCommand);
		} else if (command.equals("Select")) {
			return parseSelect(withoutCommand);
		} else if (command.equals("Deselect")) {
			return parseDeselect(withoutCommand);
		}
		return null;
	}
	
	private Command parseDeselect(String text) throws Exception {
		String[] shapeStrings = text.split(";");
		List<Shape> helperList = new ArrayList<Shape>();
		for(String row : shapeStrings) {
			Shape shape = parseShape(row);
			helperList.add(findShapeInModelListWhichIsEqual(shape));
		}
		return new DeselectCommand(helperList);
	}
	
	private Command parseSelect(String text) throws Exception {
		Shape shape = parseShape(text);
		Command command = new SelectCommand(findShapeInModelListWhichIsEqual(shape));
		return command;
	}
	
	private Command parseBringToFront(String oldIndex) {
		return new BringToFrontCommand(model, Integer.parseInt(oldIndex));
	}
		
	private Command parseBringToEnd(String oldIndex) {
		return new BringToBackCommand(model, Integer.parseInt(oldIndex));	
	}
	
	private Command parseOneStepBack(String oldIndex) {
		return new OneStepToBackCommand(model, Integer.parseInt(oldIndex));
	}
	
	private Command parseOneStepToFront(String oldIndex) {
		return new OneStepToFrontCommand(model, Integer.parseInt(oldIndex));
	}
	
	private Command parseUpdate(String text) throws Exception{
		Command command = null;
		Shape oldShape = parseShape(text.split(";")[0]);
		Shape newShape = parseShape(text.split(";")[1]);
		if (oldShape instanceof Point && newShape instanceof Point) {
			for (Shape shape : this.model.getShapes()) {
				if (shape instanceof Point && ((Point) oldShape).equals((Point)shape)) {
					oldShape = shape;
				}
			}
			command = new UpdatePointCommand((Point)oldShape, (Point)newShape);
		} else if(oldShape instanceof Line && newShape instanceof Line) {
			for(Shape shape : this.model.getShapes()) {
				if(shape instanceof Line && ((Line) oldShape).equals(shape)) {
					oldShape = shape;
				}
			}
			command = new UpdateLineCommand((Line)oldShape, (Line)newShape);
		} else if(oldShape instanceof Rectangle && newShape instanceof Rectangle) {
			for(Shape shape : this.model.getShapes()) {
				if(shape instanceof Rectangle && ((Rectangle) oldShape).equals((Rectangle)shape)) {
					oldShape = shape;
				}
			}
			command = new UpdateRectangleCommand((Rectangle)oldShape, (Rectangle)newShape);
		} else if(oldShape instanceof Donut && newShape instanceof Donut) {
			for(Shape shape : this.model.getShapes()) {
				if(shape instanceof Donut && ((Donut) oldShape).equals((Donut)shape)) {
					oldShape = shape;
				}
			}
			command = new UpdateDonutCommand((Donut)oldShape, (Donut)newShape);
		} else if(oldShape instanceof Circle && newShape instanceof Circle) {
			for(Shape shape : this.model.getShapes()) {
				if(shape instanceof Circle && ((Circle) oldShape).equals((Circle)shape)) {
					oldShape = shape;
				}
			}
			command = new UpdateCircleCommand((Circle)oldShape, (Circle)newShape);
		} else if(oldShape instanceof HexagonAdapter && newShape instanceof HexagonAdapter) {
			for(Shape shape : this.model.getShapes()) {
				if(shape instanceof HexagonAdapter && ((HexagonAdapter) oldShape).equals((HexagonAdapter)shape)) {
					oldShape = shape;
				}
			}
			command = new UpdateHexagonCommand((HexagonAdapter)oldShape,(HexagonAdapter)newShape);
		}
		return command;
	}
	
	private Command parseRemove(String text)  throws Exception{
		String[] shapeStrings = text.split(";");
		List<Shape> helperList = new ArrayList<Shape>();
		for(String row : shapeStrings) {
			Shape shape = parseShape(row);
			helperList.add(findShapeInModelListWhichIsEqual(shape));
		}
		return new DeleteCommand(helperList, model);
	}
	
	private Command parseAdd(String text) throws Exception {
		Shape shape = parseShape(text);
		
		Command command = null;
		if (shape instanceof Point) {
			command = new AddPointCommand((Point)shape, model);
		} else if (shape instanceof Line) {
			command = new AddLineCommand((Line)shape, model);
		} else if (shape instanceof Rectangle) {
			command = new AddRectangleCommand((Rectangle)shape, model);
		} else if (shape instanceof Donut) {
			command = new AddDonutCommand((Donut)shape, model);
		} else if (shape instanceof Circle) {
			command = new AddCircleCommand((Circle)shape, model);
		} else if (shape instanceof HexagonAdapter) {
			command = new AddHexagonCommand((HexagonAdapter)shape, model);
		}
		
		return command;
	}
	
	private Shape parseShape(String text) throws Exception {
		String shape = text.split(":")[0];
		String[] props = text.split(",");
		if(shape.equals("Point")) {
			Point point = new Point();
			point.setX(Integer.parseInt(props[0].split("=")[1]));
			point.setY(Integer.parseInt(props[1].split("=")[1]));
			point.setColor(new Color(Integer.parseInt(props[2].split("=")[1])));
			point.setSelected(Boolean.parseBoolean(props[3].split("=")[1]));	
			return point;
		} else if(shape.equals("Line")) {
			Line line = new Line();
			line.setStartPoint(new Point(Integer.parseInt(props[0].split("=")[1]),Integer.parseInt(props[1].split("=")[1])));
			line.setEndPoint(new Point(Integer.parseInt(props[2].split("=")[1]),Integer.parseInt(props[3].split("=")[1])));
			line.setColor(new Color(Integer.parseInt(props[4].split("=")[1])));
			line.setSelected(Boolean.parseBoolean(props[5].split("=")[1]));
			return line;
		} else if(shape.equals("Rectangle")) {
			Rectangle rectangle = new Rectangle();
			rectangle.setUpperLeftPoint(new Point(Integer.parseInt(props[0].split("=")[1]),Integer.parseInt(props[1].split("=")[1])));
			rectangle.setHeight(Integer.parseInt(props[2].split("=")[1]));
			rectangle.setWidth(Integer.parseInt(props[3].split("=")[1]));
			rectangle.setColor(new Color(Integer.parseInt(props[4].split("=")[1])));
			rectangle.setInnerColor(new Color(Integer.parseInt(props[5].split("=")[1])));
			rectangle.setSelected(Boolean.parseBoolean(props[6].split("=")[1]));
			return rectangle;
		} else if(shape.equals("Circle")) {
			Circle circle = new Circle();
			circle.setCenter(new Point(Integer.parseInt(props[0].split("=")[1]),Integer.parseInt(props[1].split("=")[1])));
			circle.setRadius(Integer.parseInt(props[2].split("=")[1]));
			circle.setColor(new Color(Integer.parseInt(props[3].split("=")[1])));
			circle.setInnerColor(new Color(Integer.parseInt(props[4].split("=")[1])));
			circle.setSelected(Boolean.parseBoolean(props[5].split("=")[1]));
			return circle;
		} else if(shape.equals("Donut")) {
			Donut donut = new Donut();
			donut.setCenter(new Point(Integer.parseInt(props[0].split("=")[1]),Integer.parseInt(props[1].split("=")[1])));
			donut.setRadius(Integer.parseInt(props[2].split("=")[1]));
			donut.setInnerRadius(Integer.parseInt(props[3].split("=")[1]));
			donut.setColor(new Color(Integer.parseInt(props[4].split("=")[1])));
			donut.setInnerColor(new Color(Integer.parseInt(props[5].split("=")[1])));
			donut.setSelected(Boolean.parseBoolean(props[6].split("=")[1]));
			return donut;
		} else if(shape.equals("Hexagon")) {
			HexagonAdapter hexagonAdapter = new HexagonAdapter(
					new Point(Integer.parseInt(props[0].split("=")[1]),Integer.parseInt(props[1].split("=")[1])),
					Integer.parseInt(props[2].split("=")[1]));
			hexagonAdapter.setOutlineColor(new Color(Integer.parseInt(props[3].split("=")[1])));
			hexagonAdapter.setInnerColor(new Color(Integer.parseInt(props[4].split("=")[1])));
			hexagonAdapter.setSelected(Boolean.parseBoolean(props[5].split("=")[1]));
			return hexagonAdapter;
		}
		return null;
	}
	
	private Shape findShapeInModelListWhichIsEqual(Shape shape) {
		for (Shape placeholder : this.model.getShapes()) {
			if (shape.equals(placeholder)) {
				return placeholder;
			}
		}
		
		return null;
	}
	
}
