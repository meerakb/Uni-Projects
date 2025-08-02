public abstract class Shape {
    public static final ShapeType DEFAULT_SHAPETYPE = ShapeType.RECTANGLE;
    public static final int DEFAULT_PANEL_WIDTH = 800;
    public static final int DEFAULT_PANEL_HEIGHT = 400;
    protected int x = 0, y = 0, width = 10, height = 10;
    public static int numberOfShapes = 0;
    protected String label = "";
    Shape() {label = String.format("%d", numberOfShapes++);}
    Shape(int x1, int y1, int w, int h) {
        x = x1;
        y = y1;
        width = w;
        height = h;
        label = String.format("%d", numberOfShapes++);
    }
    public abstract int getArea();
    @Override
    public String toString() {
        return String.format("%s: %s", getClass().getName(), label);
    }
    protected NestedShape parent;
    public NestedShape getParent() {return this.parent;}
    public void setParent(NestedShape s) {parent = s;}
    public Shape[] getPath() {return getPathToRoot(this, 0);}
    public Shape[] getPathToRoot(Shape aShape, int depth) {
        Shape[] returnShapes;
        if (aShape == null) {
            if(depth == 0) return null;
            else returnShapes = new Shape[depth];
        }
        else {
            depth++;
            returnShapes = getPathToRoot(aShape.getParent(), depth);
            returnShapes[returnShapes.length - depth] = aShape;
        }
        return returnShapes;
    }
}
