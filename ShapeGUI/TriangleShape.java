public class TriangleShape extends Shape {
    TriangleShape() {super();}
    TriangleShape(int x, int y, int w, int h) {super(x, y, w, h);}
    public int getArea() {return width * height / 2;}
}
