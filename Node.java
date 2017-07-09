//Raffi Bilemjian
public class Node
{
  String data;
  Node next;
  Node(String data, Node next)
  {
    this.data = data;
    this.next = next;
  }
  Node(String data)
  {
    this.data = data;
    this.next = null;
  }
  public Node getNext()
  {
    return next;
  }
  public void setNext(Node n)
  {
    this.next = n;
  }
  public void setData(String s)
  {
    this.data = s;
  }
  public String getData()
  {
    return this.data;
  }
  public String toString()
  {
    return "" + this.data;
  }
}