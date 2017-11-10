package paint.eg.edu.alexu.csd.oop.draw.cs62_67.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;

import paint.eg.edu.alexu.csd.oop.draw.DrawingEngine;
import paint.eg.edu.alexu.csd.oop.draw.Shape;
import paint.eg.edu.alexu.csd.oop.draw.cs62_67.startProgram;
import paint.eg.edu.alexu.csd.oop.draw.cs62_67.model.Circle;
import paint.eg.edu.alexu.csd.oop.draw.cs62_67.model.Ellipse;
import paint.eg.edu.alexu.csd.oop.draw.cs62_67.model.LineSegment;
import paint.eg.edu.alexu.csd.oop.draw.cs62_67.model.Rectangle;
import paint.eg.edu.alexu.csd.oop.draw.cs62_67.model.ShapeFactory;
import paint.eg.edu.alexu.csd.oop.draw.cs62_67.model.Square;
import paint.eg.edu.alexu.csd.oop.draw.cs62_67.model.Triangle;
import paint.eg.edu.alexu.csd.oop.draw.cs62_67.view.GUI;
import paint.eg.edu.alexu.csd.oop.draw.cs62_67.view.ShapeCreationBtn;
import paint.eg.edu.alexu.csd.oop.draw.cs62_67.view.ShapeCreationButtonsPanel;

public class MainController{
    
	private DrawingEngine engine;
    private ShapeFactory factory;
    private GUI Paint;
    
    private Shape shape;
    private Shape dragedShape;
    private PaintSurface surface = new PaintSurface();
    private ShapeCreationButtonsPanel shapesCreationPanel;
    private startProgram newPrgram;
    
	public MainController(DrawingEngine engine,ShapeFactory factory,GUI Paint){
		this.engine = engine;
		this.factory = factory;
		this.Paint = Paint;
		this.Paint.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		shapesCreationPanel = new ShapeCreationButtonsPanel(engine.getSupportedShapes());
		shapesCreationPanel.addButtonsListeners(new ShapeCreationBtnListner());
		this.Paint.getContentPane().add(shapesCreationPanel, BorderLayout.WEST);
		this.Paint.getContentPane().add(surface, BorderLayout.CENTER);
		//this.Paint.addCircleListner(new CircleListner());
		//this.Paint.addLineListner(new LineListner());
		//this.Paint.addSquareListner(new SquareListner());
		//this.Paint.addRectangleListener(new RectangleListner());
		//this.Paint.addEllipseListner(new EllipseListner());
		//this.Paint.addTriangleListner(new TriangleListner());
		this.Paint.addExitListener(new ExitListener());
		this.Paint.addUndoListener(new UndoListener());
		this.Paint.addRedoListener(new RedoListener());
		this.Paint.addNewListener(new NewListener());
	}
	
	public void orderShape(String type){
		this.shape = factory.createShape(type);
		this.dragedShape = factory.createShape(type);
	}
	
	
	private class PaintSurface extends JComponent {
		private	Point startDrag, endDrag;
		private Point[] Coordinates = new Point[3];
		private int counter = 0;
		public PaintSurface() {
			this.addMouseListener(new MouseAdapter() {
			    @Override
				public void mousePressed(MouseEvent e) {
			    	if(!(shape instanceof Triangle)){
				    	startDrag = new Point(e.getX(), e.getY());
				        endDrag = startDrag;
			    	}
			    	else{
			    		Shape currentShape = factory.createShape(shape);
			    		Coordinates[counter] = e.getPoint();
			    		counter++;
			    		if (counter % 3 == 0){
			    			setProperties(currentShape, startDrag, endDrag);
						    engine.addShape(currentShape);
			    			counter = 0;
			    		}
			    	}
			    	repaint();
			    }
	
				@Override
				public void mouseReleased(MouseEvent e) {
					if(!(shape instanceof Triangle)) {
						Shape currentShape = factory.createShape(shape);
						setProperties(currentShape,startDrag, e.getPoint());
					    engine.addShape(currentShape);
					    startDrag = null;
					    endDrag = null;
				    	repaint();
					}
				}
			});

			this.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseDragged(MouseEvent e) {
			    	endDrag = new Point(e.getX(), e.getY());
			        repaint();
			    }
			});
		}
		  
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
		    engine.refresh(g2);
		    if (startDrag != null && endDrag != null) {
		        g2.setPaint(Color.LIGHT_GRAY);
		        setProperties(dragedShape, startDrag, endDrag);
		      	dragedShape.draw(g2);
		     }
		}
		public void setProperties(Shape shape,Point start, Point end){
			shape.setPosition(startDrag);
			Map<String, Double> prep = shape.getProperties();
			if(shape instanceof Circle){	
				prep.put("xAxis", end.getX() - start.getX());
				prep.put("yAxis", end.getX() - start.getX());
			}
			else if(shape instanceof Triangle){	
				prep.put("x1", Coordinates[0].getX());
				prep.put("y1", Coordinates[0].getY());
				prep.put("x2", Coordinates[1].getX());
				prep.put("y2", Coordinates[1].getY());
				prep.put("x3", Coordinates[2].getX());
				prep.put("y3", Coordinates[2].getY());
			}
			else if(shape instanceof Square){	
				prep.put("xAxis", end.getX() - start.getX());
				prep.put("yAxis", end.getX() - start.getX());
			}
			else if(shape instanceof Rectangle){	
					prep.put("xAxis", end.getY() - start.getY());
					prep.put("yAxis", end.getX() - start.getX());
			}
			else if(shape instanceof Ellipse){	
				prep.put("xAxis", end.getX() - start.getX());
				prep.put("yAxis", end.getY() - start.getY());
			}
			else if(shape instanceof LineSegment){
				prep.put("x1", end.getX());
				prep.put("y1", end.getY());
				prep.put("x2", start.getX());
				prep.put("y2", start.getY());
			}
			shape.setProperties(prep);
		}
	}
	public class ShapeCreationBtnListner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("NOT DONE");
			ShapeCreationBtn source = (ShapeCreationBtn) e.getSource();
			orderShape(source.getText());
			System.out.println("DONE");
		}
		
	}
	/*
	class CircleListner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			orderShape("Circle");
		}
		
	}
	class LineListner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			orderShape("LineSegment");
		}
		
	}
	class SquareListner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			orderShape("Square");
			
		}
		
	}
	class RectangleListner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			orderShape("Rectangle");
		}
	}
	class EllipseListner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			orderShape("Ellipse");
		}
		
	}
	class TriangleListner implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			orderShape("Triangle");
		}
		
	}*/
	class ExitListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
			
		}
		
	}
	class UndoListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				engine.undo();
			}catch (Exception e1) {
				// TODO: handle exception
			}
			surface.repaint();
		}
		
	}
	class RedoListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				engine.redo();
			}catch (Exception e1) {
				// TODO: handle exception
			}
			surface.repaint();
			
		}
		
	}
	class NewListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			newPrgram = new startProgram();
			newPrgram.main(null);
		}
		
	}
}