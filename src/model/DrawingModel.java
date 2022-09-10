package model;

import java.util.ArrayList;
import java.util.List;

public class DrawingModel {
	private List<Shape> shapes = new ArrayList<Shape>();

	public List<Shape> getShapes(){
		return shapes;
	}

	public void add(Shape shape) {
		shapes.add(shape);
	}

	public void remove(Shape shape) {
		shapes.remove(shape);
	}

	public Shape get(int index) {
		return shapes.get(index);
	}
	
	public void deselectAllShapes() {
		for (Shape shape : shapes) {
			shape.setSelected(false);
		}
	}
	
	public Shape getSelectedShape() {
		for (Shape shape : shapes) {
			if (shape.isSelected()) {
				return shape;
			}
		}
		
		return null;
	}
	
	public void cleanList() {
		shapes.clear();
	}

	public void addOnPosition(Shape shape, int positionInList) {
		shapes.add(positionInList, shape);
	}
	
	public void swapShapes(int firstIndex, int secondIndex) {
		Shape helper = shapes.get(firstIndex);
		shapes.set(firstIndex, shapes.get(secondIndex));
		shapes.set(secondIndex ,helper);
	}
	
	public int getNumberOfSelectedShapes() {
		int count = 0;
		for (Shape shape : shapes) {
			if (shape.isSelected()) {
				count++;
			}
		}
		
		return count;
	}
	
	public void setShapes(List<Shape> list) {
		shapes.clear();
		
		for (Shape s : list) {
			shapes.add(s);
		}
	}
}
