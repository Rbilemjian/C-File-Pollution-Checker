//Raffi Bilemjian
public class LinkedList
{
  Node head;
  Node tail;
  LinkedList(String s)
  {
    this.head = new Node(s);
    this.tail = this.head;
  }
  LinkedList()
  {
    this.head = null;
    this.tail = null;
  }
  public void insert(String s)
  {
    Node n = new Node(s);
    if(head == null)
    {
      head = n;
      tail = n;
    }
    else
    {
      tail.setNext(n);
      tail = n;
    }
  }
  public boolean find(String s)
  {
    Node curr = head;
    while(curr!=null)
    {
      if(curr.getData().equals(s))
        return true;
      curr = curr.getNext();
    }
    return false;
  }
  public void delete(String s)
  {
    Node prev = head;
    if(prev.getData().equals(s))
    {
      head = prev.getNext();
      prev.setNext(null);
      return;
    }
    Node curr = head.getNext();
    while(curr!=null)
    {
      if(curr.getData().equals(s))
      {
        prev.setNext(curr.getNext());
        return;
      }
      curr = curr.getNext();
      prev = prev.getNext();
    }
  }
  public void displayList()
  {
    System.out.println();
    Node curr = head;
    System.out.println("Current LinkedList:");
    while(curr!=null)
    {
      System.out.println(curr);
      curr = curr.getNext();
    }
  }
  public int size()
  {
    int size = 0;
    Node curr = head;
    while(curr!=null)
    {
      size++;
      curr = curr.next;
    }
    return size;
  }
}
  
    
      
    
    
    
    