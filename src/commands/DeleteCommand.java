package commands;

import java.util.List;

import model.DrawingModel;
import model.Shape;

public class DeleteCommand implements Command {
	
	private List<Shape> shapes;
	private DrawingModel model;
	
	public DeleteCommand(List<Shape> s, DrawingModel model) {
		this.shapes = s;
		this.model = model;
	}
	
	@Override
	public void execute() {
		int i = 0;
		for(Shape shape : shapes) {
			shape.setPositionInList(model.getShapes().indexOf(shape) + i);
			model.remove(shape);
			i++;
		}
	}

	@Override
	public void unexecute() {
		for(Shape shape : shapes) {
			model.addOnPosition(shape, shape.getPositionInList());
		}
		
	}
	
	@Override
	public String toString() {
		String str= "";
		for(Shape s : shapes) {
			if(!s.equals(shapes.get(shapes.size() - 1))) {
				str += s.toString() + ";";
			} else { 
				str += s.toString();
			}
			
		}
		return "Remove_" + str;
	}
	
}
