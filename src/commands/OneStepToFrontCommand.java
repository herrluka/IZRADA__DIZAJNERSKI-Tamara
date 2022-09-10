package commands;

import model.DrawingModel;

public class OneStepToFrontCommand implements Command {
	
	private DrawingModel model;
	private int index;
	
	
	public OneStepToFrontCommand(DrawingModel model, int index) {
		this.model = model;
		this.index = index;
	}

	@Override
	public void execute() {
		model.swapShapes(index, index + 1);
	}

	@Override
	public void unexecute() {
		model.swapShapes(index,index + 1);
	}
	
	@Override
	public String toString() {
		return "One step to front_" + index;
	}
	
}
