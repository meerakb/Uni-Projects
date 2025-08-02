import java.util.ArrayList;

public class NestedShape extends RectangleShape {
    private ArrayList<Shape> innerShapes = new ArrayList<Shape>();
    public Shape createInnerShape(ShapeType st, int x, int y, int w, int h) {
        Shape newShape;
        if (x+w > width || y+h > height) {
            w = width / 2;
            h = height / 2;
            x = 0;
            y = 0;
        }
        if (st == ShapeType.RECTANGLE) {
            newShape = new RectangleShape(x, y, w, h);
            newShape.setParent(this);
        }
        else if (st == ShapeType.TRIANGLE) {
            newShape = new TriangleShape(x, y, w, h);
            newShape.setParent(this);
        }
        else {
            newShape = new NestedShape(x, y, w, h);
            newShape.setParent(this);
        }
        innerShapes.add(newShape);
        return newShape;
    }
    NestedShape(int x, int y, int w, int h) {
        super(x, y, w, h);
        createInnerShape(ShapeType.RECTANGLE, 0, 0, w/2, h/2);
    }
    NestedShape(int w, int h) {
        super(0, 0, w, h);
    }
    public Shape getInnerShapeAt(int index) {return innerShapes.get(index);}
    public int getSize() {return innerShapes.size();}
    public void showDetails() {
        System.out.printf("(%d,%d),%dx%d\n", x, y, width, height);
        for (int i = 0; i < getSize(); i++)
            System.out.printf("(%d,%d),%dx%d\n", innerShapes.get(i).x, innerShapes.get(i).y, innerShapes.get(i).width, innerShapes.get(i).height);
    }
    @Override
    public String toString() {
        return String.format("%s: %s, (children: %d)", getClass().getName(), label, getSize());
    }
    public void addInnerShape(Shape s) {
        s.setParent(this);
        innerShapes.add(s);
    }
    public void removeInnerShape(Shape s) {
        s.setParent(null);
        innerShapes.remove(s);
    }
    public void removeInnerShapeAt(int index) {
        innerShapes.get(index).setParent(null);
        innerShapes.remove(index);
    }
    public int indexOf(Shape s) {
        return innerShapes.indexOf(s);
    }
    public ArrayList<Shape> getAllInnerShapes() {return innerShapes;}
    public int getArea() {
        int total = super.getArea();
        for (int i = 0; i < getSize(); i++)
            total += innerShapes.get(i).getArea();
        return total;
    }
}
