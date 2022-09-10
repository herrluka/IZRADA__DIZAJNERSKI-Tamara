package commands;

import model.DrawingModel;
import model.HexagonAdapter;

public class AddHexagonCommand implements Command {
	
	private HexagonAdapter hexagon;
	private DrawingModel model;


	public AddHexagonCommand(HexagonAdapter hexagon, DrawingModel model) {
		this.hexagon = hexagon;
		this.model = model;
	}

	@Override
	public void execute() {

		model.add(hexagon);
	}

	@Override
	public void unexecute() {

		model.remove(hexagon);

	}
	
	@Override
	public String toString() {
		return "Add_" + hexagon.toString();
	}
	
}
