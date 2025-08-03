public class RectangleShape extends Shape {
    RectangleShape() {super();}
    RectangleShape(int x, int y, int w, int h) {super(x, y, w, h);}
    public int getArea() {return width * height;}
}
