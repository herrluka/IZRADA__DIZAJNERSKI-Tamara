package commands;

import model.DrawingModel;
import model.Shape;

public class BringToBackCommand implements Command {
	
	private DrawingModel model;
	private Shape shape;
	private int currentIndex;

	public BringToBackCommand(DrawingModel model, int index) {
		this.model = model;
		this.currentIndex = index;
	}

	@Override
	public void execute() {
		shape = model.get(currentIndex);
		model.getShapes().remove(shape);
		model.getShapes().add(0, shape);
	}

	@Override
	public void unexecute() {
		model.remove(shape);
		model.addOnPosition(shape, currentIndex);
	}

	@Override
	public String toString() {
		return "Bring to end_" + currentIndex;
	}

}
