package commands;

import model.DrawingModel;

public class OneStepToBackCommand implements Command {
	
	private DrawingModel model;
	private int index;
	
	
	public OneStepToBackCommand(DrawingModel model, int index) {
		super();
		this.model = model;
		this.index = index;
	}

	@Override
	public void execute() {
		model.swapShapes(index, index - 1);	
	}

	@Override
	public void unexecute() {
		model.swapShapes(index, index - 1);
	}
	
	@Override
	public String toString() {
		return "One step to back_" + index;
	}
	
}
