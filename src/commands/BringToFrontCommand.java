package commands;

import model.DrawingModel;
import model.Shape;

public class BringToFrontCommand implements Command {
	
	private DrawingModel model;
	private Shape shape;
	private int currentIndex;
	
	
	public BringToFrontCommand(DrawingModel model, int currentIndex) {
		this.model = model;
		this.currentIndex = currentIndex;
	}

	@Override
	public void execute() {
		this.shape = model.get(currentIndex);
		model.remove(shape);
		model.add(shape);
	}

	@Override
	public void unexecute() {
		model.remove(shape);
		model.addOnPosition(shape,currentIndex);
		
	}
	
	@Override
	public String toString() {
		return "Bring to front_" + currentIndex;
	}

}
