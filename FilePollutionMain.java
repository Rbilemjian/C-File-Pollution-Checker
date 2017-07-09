//Raffi Bilemjian
//COMP 282 TuThu 7PM
//Project 1
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
public class FilePollutionMain
{
  public static void main(String[] args)
  {
    String directory = "";
    if(args.length == 2)
    {
      if(args[0].equals("-i"))
      {
        directory = args[1];
        System.out.println("Directory: " + directory);
      }
      else
      {
        System.out.println("Only acceptable command is -i.");
        return;
      }
    }
    else
    {
      System.out.println("Missing -i command and/or directory argument");
      return;
    }
    //directory = "/home/raffi/Desktop/Project1/a";
    File folder = new File(directory);
    File[] listOfFiles = checkDirectory(folder);
    int numfiles = listOfFiles.length;
    System.out.println("Project files in directory:");
    for(int i = 0;i<listOfFiles.length;i++)
      System.out.println(listOfFiles[i].getName());
    LinkedList[] list = new LinkedList[numfiles];
    for(int i = 0;i<numfiles;i++)
    {
      list[i] = parseIncludes(listOfFiles[i]);
      //list[i].displayList();
    }
    LinkedList redundantIncludes = compareIncludes(list,listOfFiles);
    if(redundantIncludes.head == null)
      System.out.println("No redundancies were found.");
    else
      displayRedundancies(redundantIncludes); 
  }
  public static LinkedList compareIncludes
        (LinkedList[] list,File[] listOfFiles)
  {//returns linkedlist of all redundant includes
    LinkedList includes = new LinkedList();
    Node curr,next;
    //String s = "";
    String a,b;
    for(int i = 0;i<list.length-1;i++)
    {
      curr = list[i].head;
      while(curr!=null)
      {
        for(int j = i+1;j<list.length;j++)
        {
          next = list[j].head;
          while(next!=null)
          {
            boolean isHeaderFile = false;
            for(int k = 0;k<listOfFiles.length;k++)
            {
              if(curr.data.equals("[" + 
                      listOfFiles[k].getName().replaceAll(" ","") + "]"))
                isHeaderFile = true;
            }
            if(curr.data.equals(next.data) && isHeaderFile)
            {
              a = (listOfFiles[i].getName() + " includes " + curr.data);
              b = (listOfFiles[j].getName() + " includes " + curr.data);
              if(!includes.find(a))
                includes.insert(a);
              if(!includes.find(b))
                includes.insert(b);
            }
            next = next.getNext();
          }
        }
        curr = curr.getNext();
      }
    }
    return includes;
  }
  public static boolean displayRedundancies(LinkedList list)
  { //displays redundant includes
    System.out.println();
    if(list.size()<2)
      return false;
    Node prev = list.head;
    Node curr = prev.next;
    String s = "";
    int count = 1;
    while(curr!=null)
    {
      String c = curr.data.substring(curr.data.indexOf('[')+1,
              curr.data.length()-1);
      String p = prev.data.substring(prev.data.indexOf('[')+1,
              prev.data.length()-1);
      if(c.equals(p))
      {
        if(count==1)
        {
          s += "-" + prev.data + "\n" + "-" + curr.data + "\n";
        }
        else
          s += "-" + curr.data + "\n";
        count++;
      }
      else
      {
        System.out.println(p + " is included " + count + 
                           " times. Remove " + (count-1) + " of them.");
        System.out.print(s);
        System.out.println();
        s = "";
        count = 1;
      }
      curr = curr.next;
      prev = prev.next;
    }
    if(count>1)
    {
      System.out.println(prev.data.substring(prev.data.indexOf('[')+1,
              prev.data.length()-1) 
                           + " is included " + count + " times. Remove " 
                           + (count-1) + " of them.");
      System.out.print(s);
      System.out.println();
      return true;
    }
    return false;
  }
  public static LinkedList parseIncludes(File f)
  { //parses included header files for each project file
    LinkedList parsedIncludes = new LinkedList();
    try
    {
      BufferedReader br = new BufferedReader(new FileReader(f));
      String fileRead = br.readLine();
      while(fileRead!=null)
      {
        fileRead = removeUnneededCharacters(fileRead);
        if(fileRead.length()>=8 && fileRead.substring(0,8).equals("#include") &&
           (fileRead.contains(".hxx") || fileRead.contains(".h") || 
                fileRead.contains(".hpp")))
        {
          fileRead = "[" + fileRead.substring(9,fileRead.length()-1) + "]";
          parsedIncludes.insert(fileRead);
        }
        fileRead = br.readLine();
      }
      br.close();
    }
    catch(FileNotFoundException fnfe)
    {
      System.out.println("File not found.");
    }
    catch(IOException ioe)
    {
      ioe.printStackTrace();
    }
    return parsedIncludes;
  }
  public static File[] removeUnneededFiles(File[] arr)
  { //Removes non-C++ project files from program's filelist
    int newSize = 0;
    String name;
    for(int i = 0;i<arr.length;i++)
    {
      name = arr[i].getName();
      if((name.contains(".hxx") || name.contains(".h") || name.contains(".hpp")
        ||name.contains(".C") || name.contains(".cpp") || name.contains(".cxx"))
         &&!(name.charAt(name.length()-1) == '~') && !(name.charAt(0) == '.'))
        newSize++;
    }
    if(newSize == arr.length)
      return arr;
    File[] newArr = new File[newSize];
    int index = 0;
    for(int i = 0;i<arr.length;i++)
    {
      name = arr[i].getName();
      if((name.contains(".hxx") || name.contains(".h") || name.contains(".hpp")
        ||name.contains(".C") || name.contains(".cpp") || name.contains(".cxx"))
        &&!(name.charAt(name.length()-1) == '~') && !(name.charAt(0) == '.'))
      {
        newArr[index] = arr[i];
        index++;
      }
    }
    return newArr;
  }
  public static File[] checkDirectory(File folder)
  { //Stops execution and lets user know if inputted directory is invalid
    if(!folder.exists())
    {
      System.out.println("Directory does not exist.");
      System.exit(1);
    }
    File[] listOfFiles = removeUnneededFiles(folder.listFiles());
    int numfiles = listOfFiles.length;
    if(numfiles<2)
    {
      System.out.println(numfiles+" C++ project file(s) in directory.");
      System.out.println("At least 2 needed to check for file pollution.");
      System.exit(1);
    }
    return listOfFiles;
  }   
  public static String removeUnneededCharacters(String fileRead)
  { //removes comments and spaces from #includes lines
    fileRead = fileRead.replaceAll(" ","");
        int index = -1;
        boolean firstWasFound = false;
        if(fileRead.length()>=8)
        {
          for(int i = 8;i<fileRead.length()-1;i++)
          {
            if((fileRead.charAt(i)==('/') && fileRead.charAt(i+1)==('*'))
                 ||(fileRead.charAt(i)=='/' && fileRead.charAt(i+1)=='/'))
           {
             fileRead = fileRead.substring(0,i);
           }
          }
        }
        for(int i = 0;i<fileRead.length()-1;i++)
        {
          if(fileRead.charAt(i)==('/') && fileRead.charAt(i+1) == ('*') && 
                  !firstWasFound)
          {
            firstWasFound = true;
            continue;
          }
          if(fileRead.charAt(i)==('*') && fileRead.charAt(i+1) == ('/') && 
                  firstWasFound)
          {
            index = i+2;
          }
        }
        if(index != -1)
          fileRead = fileRead.substring(index);
        return fileRead;
  }
}