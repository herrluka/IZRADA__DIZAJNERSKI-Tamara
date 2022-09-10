package commands;

import model.DrawingModel;
import model.Line;
import model.Point;

public class AddLineCommand implements Command {
	private Line line;
	private DrawingModel model;


	public AddLineCommand(Line line, DrawingModel model) {
		this.line = line;
		this.model = model;
	}

	@Override
	public void execute() {

		model.add(line);
	}

	@Override
	public void unexecute() {

		model.remove(line);

	}
	
	@Override
	public String toString() {
		return "Add_" + line.toString();
	}
}
