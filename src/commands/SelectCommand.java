package commands;

import model.Shape;

public class SelectCommand implements Command {


	private Shape shape;
	
	
	public SelectCommand(Shape shape) {
		this.shape = shape;
	}

	@Override
	public void execute() {
		shape.setSelected(true);
		
	}

	@Override
	public void unexecute() {
		shape.setSelected(false);
		
	}
	
	@Override
	public String toString() {
		return "Select_" + shape;
	}

}
