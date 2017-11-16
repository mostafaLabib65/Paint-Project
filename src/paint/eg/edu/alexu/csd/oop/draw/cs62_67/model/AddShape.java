package paint.eg.edu.alexu.csd.oop.draw.cs62_67.model;

import java.util.ArrayList;

import paint.eg.edu.alexu.csd.oop.draw.ICommand;
import paint.eg.edu.alexu.csd.oop.draw.Shape;

public class AddShape implements ICommand{
	
	private ArrayList<Shape> shapes;
	private Shape shape;
	public AddShape(ArrayList<Shape> shapes, Shape shape){
		this.shapes = shapes;
		this.shape = shape;
	}
	@Override
	public void execute() {
			this.shapes.add(shape);
	}

	@Override
	public void unexecute() {
		this.shapes.remove(shape);
	}
	@Override
	public Shape getNewShape() {
		// TODO Auto-generated method stub
		return shape;
	}
	@Override
	public Shape getOldShape() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
