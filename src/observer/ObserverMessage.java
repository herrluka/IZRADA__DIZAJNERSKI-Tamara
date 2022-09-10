package observer;

import model.Shape;

public class ObserverMessage {
	
	private int numberOfShapesInList;
	private int numberOfSelectedShapes;
	private int numberOfShapesInUndoList;
	private int numberOfShapesInRedoList;
	private int shapeIndex;
	private Shape selectedShape;
	private boolean filePathSet;
	private int numberOFCommandsLoadedFromTextFile;
	
	public ObserverMessage(int numberOfShapesInList, int numberOfSelectedShapes, int numberOfShapesInUndoList, 
			int numberOfShapesInRedoList, Shape selectedShape, boolean filePathSet, int numberOFCommandsLoadedFromTextFile) {
		super();
		this.numberOfShapesInList = numberOfShapesInList;
		this.numberOfSelectedShapes = numberOfSelectedShapes;
		this.numberOfShapesInUndoList = numberOfShapesInUndoList;
		this.numberOfShapesInRedoList = numberOfShapesInRedoList;
		this.selectedShape = selectedShape;
		this.filePathSet = filePathSet;
		this.numberOFCommandsLoadedFromTextFile = numberOFCommandsLoadedFromTextFile;
	}

	public int getNumberOfShapesInList() {
		return numberOfShapesInList;
	}

	public int getNumberOfSelectedShapes() {
		return numberOfSelectedShapes;
	}

	public int getNumberOfShapesInUndoList() {
		return numberOfShapesInUndoList;
	}

	public int getNumberOfShapesInRedoList() {
		return numberOfShapesInRedoList;
	}

	public Shape getSelectedShape() {
		return selectedShape;
	}

	public int getShapeIndex() {
		return shapeIndex;
	}
	
	public boolean getFiltePathSet() {
		return filePathSet;
	}

	public void setShapeIndex(int shapeIndex) {
		this.shapeIndex = shapeIndex;
	}

	public int getNumberOFCommandsLoadedFromTextFile() {
		return numberOFCommandsLoadedFromTextFile;
	}
	
}
