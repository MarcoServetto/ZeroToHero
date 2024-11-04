package htmlMangle;

record Range(double minX, double maxX, double minY, double maxY){
  static boolean checkPercent(double x){
    assert x>=0;
    assert x<=100;
    return true;
  }
  static boolean checkRange(double min, double max){
    assert checkPercent(min) && checkPercent(max);
    assert min<max;
    return true;
  }
  Range{
    assert checkRange(minX,maxX);
    assert checkRange(minY,maxY);
  }
  String asStyle() {
    return String.format(
      "style=\"top:%.2f%%;left:%.2f%%;width:%.2f%%;height:%.2f%%;\"",
        minX,
        minY,
        (maxY - minY),
        (maxX - minX));
    }
  }