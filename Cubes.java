//########################################################
//                      CYBERKING
//########################################################

import java.io.*;
import java.util.*;

public class Cubes
{
  static int layoutWidth=50;
  public static String[] config = new String[5];
  public static int[] dimStore = new int[5];
  public static FileOutputStream output = null;

  public static void main(String[] args) throws IOException
  {
    Date preExecution = new Date();

    System.out.println("\n\n                      CRYSTALS");

    System.out.print("\n\tOutput to file: ");
    try
    {
      String i;
      do
      {
        i = readInput();
        if (i.equalsIgnoreCase("y") || i.equalsIgnoreCase("n") || i.equals(""))
        {
          config[0] = i;
          if (i.equalsIgnoreCase("y"))
          {
            System.out.print("\n\tOutput file path: ");
            config[1] = readInput();
          }
        }
        else
        {
          System.out.println("Invalid input!\nUse: (y/n) (yes/no). Default 'n'.");
        }
      }
      while ( !(i.equalsIgnoreCase("y")) && !(i.equalsIgnoreCase("n")) && !(i.equals("")) );
    }
    catch (Exception e)
    { System.out.println("Error: " + e.getMessage()); }


    System.out.print("\n\tPrint 2-CUBE: ");
    try
    {
      String i;
      do
      {
        i = readInput();
        if (i.equalsIgnoreCase("y") || i.equalsIgnoreCase("n") || i.equals(""))
        {
          config[2] = i;

          if (i.equalsIgnoreCase("y"))
          {
            System.out.print("\n\t2-CUBE Size: "); //System.out.print( "\n\tLxW: " );
            int[] size;

            do
            {
              size = readDimensions(1);

              if (check(size) == -2)
              { System.out.println("Error: Values must be within range(5-30)"); }

            }
            while (check(size) < 0);
            for (int x = 0;x < 1;x++)
            { dimStore[x] = size[x]; }
          }
        }
        else
        {
          System.out.println("Invalid input!\nUse: (y/n). Default 'n'.");
        }
      }
      while ( !(i.equalsIgnoreCase("y")) && !(i.equalsIgnoreCase("n")) && !(i.equals("")) );
    }
    catch (Exception e)
    { System.out.println("Error: " + e.getMessage()); }


    System.out.print("\n\tPrint 3-CUBE: ");
    try
    {
      String i;
      do
      {
        i = readInput();
        if (i.equalsIgnoreCase("y") || i.equalsIgnoreCase("n") || i.equals(""))
        {
          config[3] = i;

          if (i.equalsIgnoreCase("y"))
          {
            System.out.print("\n\t3-CUBE Size: "); //System.out.print( "\n\tLxWxH: " );
            int[] size;

            do
            {
              size = readDimensions(1);

              if (check(size) == -2)
              { System.out.println("Error: Values must be within range(2-30)"); }

            }
            while (check(size) < 0);
            for (int x = 2,y = 0;x < 3;x++,y++)
            { dimStore[x] = size[y]; }
          }
        }
        else
        {
          System.out.println("Invalid input!\nUse: (y/n). Default 'n'.");
        }
      }
      while ( !(i.equalsIgnoreCase("y")) && !(i.equalsIgnoreCase("n")) && !(i.equals("")) );
    }
    catch (Exception e)
    { System.out.println("Error: " + e.getMessage()); }


    try
    {
      if (config[2].equalsIgnoreCase("y"))
      {
        if (config[0].equalsIgnoreCase("y"))
        {
          output = new FileOutputStream(config[1]);

          for (char i:twoCube(layoutWidth, dimStore[0], dimStore[0]))
          {
            if (i == '~')
            { break; }
            else
            { output.write(i); }
          }
        }
        else
        { twoCube(layoutWidth, dimStore[0], dimStore[0]); }
      }

      if (config[3].equalsIgnoreCase("y"))
      {
        if (config[0].equalsIgnoreCase("y"))
        {
          if (output == null)
          { output = new FileOutputStream(config[1]); }

          for (char i:threeCube(layoutWidth, dimStore[2], dimStore[2], dimStore[2]))
          {
            if (i == '~')
            { break; }
            else
            { output.write(i); }
          }
        }
        else
        { threeCube(layoutWidth, dimStore[2], dimStore[2], dimStore[2]); }
      }
    }
    catch (FileNotFoundException e)
    { System.out.println("\nCan't write to: " + e.getMessage()); }
    catch (Exception e)
    { System.out.println("Error: " + e.getMessage()); }
    finally
    { if (output != null)
      { output.close(); } }


    Date postExecution = new Date();

    System.out.println("\n\n\n\t\tExecuted in : " + (postExecution.getTime() - preExecution.getTime()) + "ms");
  }
////////////////////////////////////////////////////////
  public static int check(int[] arr)
  {
    int ret = 0;

    for (int i:arr)
    {
      if (i == -1)
      { ret = -1; break; }

      if (i < 5 || i > 30)
      { ret = -2; break; }
    }

    return ret;
  }
////////////////////////////////////////////////////////
  public static int[] readDimensions(int dim)
  {
    int[] dimensions = new int[dim];
    boolean test = true;


    try
    {
      InputStreamReader input = new InputStreamReader(System.in);
      StringBuffer tmp = new StringBuffer();
      int count = 0;
      char i;

      while ((i = (char) input.read()) != '\n')
      {
        if (Character.toString(i).equalsIgnoreCase("x"))
        {
          if (count < dim && tmp.toString() != "")
          {
            dimensions[count] = Integer.valueOf(tmp.toString());
            tmp = new StringBuffer("");
            count++;
          }
          else
          { test = false; break; }
        }
        else if (Character.isDigit(i))
        {
          tmp.append(i);
        }
        else
        { test = false; break; }
      }
      if ((dim - count) == 1)
      { dimensions[count] = Integer.valueOf(tmp.toString()); }
      input.close();

      if (!(test))
      {
        if (dim == 2)
        { System.out.println("Error: Invalid input.\n Use format: LxW"); }
        else if (dim == 3)
        { System.out.println("Error: Invalid input.\n Use format: LxWxH"); }
        else
        { System.out.println("Error: Invalid input."); }

        Arrays.fill(dimensions, -1);
      }

    }
    catch (Exception e)
    {
      System.out.println("Error: " + e.getMessage());
      Arrays.fill(dimensions, -1);
    }

    return dimensions;
  }
////////////////////////////////////////////////////////
  public static String readInput() throws Exception
  {
    String input = "";
    InputStreamReader ask = new InputStreamReader(System.in);
    try
    {
      StringBuffer name = new StringBuffer();
      char x;

      while ((x = (char) ask.read()) != '\n')
      {
        name.append(x);
      }

      if (name.toString().equalsIgnoreCase("q"))
      {
        System.exit(1);
      }
      if (name.toString().startsWith("INT"))
      {
        name.replace(0, 3, "/storage/emulated/0");
      }
      else if (name.toString().startsWith("EXT"))
      {
        name.replace(0, 3, "/storage/sdcard0");
      }
      input = name.toString();
    }
    catch (Exception e)
    { System.out.print("Error: " + e.getMessage()); }
    return input;
  }
////////////////////////////////////////////////////////
  public static void line()
  { System.out.println(); }
////////////////////////////////////////////////////////
  public static String space(int j)
  {
    String space = new String();

    for (int i=0;i < j;i++)
    { System.out.print("  "); space += "  "; }
    return space;
  }
////////////////////////////////////////////////////////
  public static String star(int j)
  {
    String star = new String();

    for (int i=0;i < j;i++)
    { System.out.print(" *"); star += " *"; sleep(); }
    return star;
  }
////////////////////////////////////////////////////////
  public static String draw(int a, String b)
  {
    String draw = new String();

    for (int i=0;i < a;i++)
    {
      if (b == "=" || b == "   " || b == "  " || b == " ")
      { System.out.print(b); draw += b; }
      else
      { System.out.print(b); draw += b; sleep(); }
    }
    return draw;
  }
////////////////////////////////////////////////////////
  public static void sleep()
  {
    try
    {
      Thread.sleep(50);
    }
    catch (Exception e)
    { System.out.println("Error: " + e.getMessage()); }
  }
////////////////////////////////////////////////////////
  public static char[] twoCube(int width, int x, int y)
  {
    char[] twoCube = new char[10000];
    String temp;
    int count;

    Arrays.fill(twoCube, '\n');

    line(); count = 0;
    temp = draw(width, "="); temp.getChars(0, temp.length(), twoCube, count); count += temp.length();
    line(); count++;
    temp = "                  2-CUBE\n"; System.out.print(temp); temp.getChars(0, temp.length(), twoCube, count); count += temp.length();
    temp = draw(width, "="); temp.getChars(0, temp.length(), twoCube, count); count += temp.length();
    line();count++; line(); count++;

    for (int i=0;i < y;i++)
    {
      temp = star(x); temp.getChars(0, temp.length(), twoCube, count); count += temp.length();
      line(); count++;
    }


    count += 2; twoCube[count++] = '~';
    return twoCube;
  }
////////////////////////////////////////////////////////
  public static char[] threeCube(int width, int x, int y, int z)
  {
    char[] threeCube = new char[10000];
    String temp;
    int count;

    Arrays.fill(threeCube, '\n');

    line(); count = 0;
    temp = draw(width, "="); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
    line(); count++;
    temp = "                  3-CUBE\n"; System.out.print(temp); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
    temp = draw(width, "="); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
    line(); count++; line(); count++;

    int length = y; int v=2;
    for (int i=z;i > 0;i--)
    {
      temp = space(i); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
      if (i == z || i == 1)
      {
        temp = draw(x, "  *"); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
        if (i == 1 && y < z)
        { 
          temp = space(y - 2); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
          temp = star(1); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
        }
        if (i == 1 && y > z)
        {
          temp = space(z - 2); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
          temp = star(1); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
        }
        temp = draw(((z - i) - 1), " '"); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
        if (i == 1 && y == z)
        {
          temp = star(1); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
        }
      }
      else
      {
        temp = draw(1, "  *"); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
        //temp=draw( x - 2, "   " ); temp.getChars( 0, temp.length( ), threeCube, count ); count += temp.length( );
        if (length > 0)
        {
          temp = space(((z - i) - 1)); temp += draw(1, " '"); temp += draw((((x - 2) * 3) - ((((z - i) - 1) * 2) + 2)), " ");
          temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
        }
        if (y < z && length <= 0)
        {
          temp = space((y - 2)); temp += draw(1, " '"); temp += draw((((x - 2) * 3) - (((y - 2) * 2) + 2)), " ");
          temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
        }

        temp = draw(1, "  *"); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();

        if (length > 0)
        {
          temp = space((z - i) - 1); 
          temp += star(1);
          temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
        }
        if (y < z && length <= 0)
        {
          temp = space(y - 2);
          temp += star(1);
          temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
        }

      }
      line(); count++;
      length--;
    }
    for (int i=0;i < (y - 1);i++)
    {
      temp = space(1); temp.getChars(0, temp.length(), threeCube, count); count += temp.length();

      if (i == (y - 2))
      { temp = draw(x, "  *"); temp.getChars(0, temp.length(), threeCube, count); count += temp.length(); }
      else
      {
        temp = draw(1, "  *");
        //temp+=draw(x - 2, "   ");
        temp += space((y - (i + 1)) - 1);
        temp += draw(1, "'");
        temp += draw((((x - 2) * 3) - ((((y - (i + 1)) - 1) * 2) + 1)), " ");
        temp += draw(1, "  *");
        temp.getChars(0, temp.length(), threeCube, count); count += temp.length();

        if (y > z)
        {
          if (i < (y - z))
          {
            temp = space(z - 2);
            temp += star(1);
            temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
          }
          if (i >= (y - z))
          {
            temp = space((y - (i + 1)) - 2);
            temp += star(1);
            temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
          }
        }
        else
        {
          temp = space((y - (i + 1)) - 2);
          temp += star(1);
          temp.getChars(0, temp.length(), threeCube, count); count += temp.length();
        }
      }
      line(); count++;
    }
    count++; threeCube[count] = '~';
    return threeCube;
  }
  /*///////////////////////////////////////////////////////
   public static char[] threeCube( int width, int x, int y, int z )
   {
   char[] threeCube = new char[10000];
   String temp;
   int count;

   Arrays.fill( threeCube, '\n' );

   line( ); count = 0;
   temp = draw( width, "=" ); temp.getChars( 0, temp.length( ), threeCube, count ); count += temp.length( );
   line( ); count++;
   temp = "                  3-CUBE\n"; System.out.print( temp ); temp.getChars( 0, temp.length( ), threeCube, count ); count += temp.length( );
   temp = draw( width, "=" ); temp.getChars( 0, temp.length( ), threeCube, count ); count += temp.length( );
   line( ); count++;


   int length=y;
   for ( int i=z;i > 0;i-- )
   {
   space( i );
   if ( i == z || i == 1 )
   {
   draw( x, "  *" );
   if ( i == 1 && y < z )
   { 
   space( y - 2 );
   star( 1 );
   }
   if ( i == 1 && y > z )
   {
   space( z - 2 );
   star( 1 );
   }
   space( ( z - i ) - 1 );
   if ( i == 1 && y == z )
   {
   star( 1 );
   }
   }
   else
   {
   draw( 1, "  *" );
   draw( x - 2, "   " );
   draw( 1, "  *" );

   if ( length > 0 )
   {
   space( ( z - i ) - 1 );
   star( 1 );
   }
   if ( y < z && length <= 0 )
   {
   space( y - 2 );
   star( 1 );
   }

   }
   line( );
   length--;
   }
   for ( int i=0;i < ( y - 1 );i++ )
   {
   space( 1 );

   if ( i == ( y - 2 ) )
   { draw( x, "  *" ); }
   else
   {
   draw( 1, "  *" );
   draw( x - 2, "   " );
   draw( 1, "  *" );

   if ( y > z )
   {
   if ( i < ( y - z ) )
   {
   space( z - 2 );
   star( 1 );
   }
   if ( i >= ( y - z ) )
   {
   space( ( y - ( i + 1 ) ) - 2 );
   star( 1 );
   }
   }
   else
   {
   space( ( y - ( i + 1 ) ) - 2 );
   star( 1 );
   }
   }
   line( );
   }
   count ++; threeCube[count++] = '~';
   return threeCube;
   }
   ///////////////////////////////////////////////////////*/
  public static char[] fourCube(int width, int x, int y, int z, int w)
  {
    char[] fourCube = new char[10000];
    String temp;
    int count;

    Arrays.fill(fourCube, '\n');

    line(); count = 0;
    temp = draw(width, "="); temp.getChars(0, temp.length(), fourCube, count); count += temp.length();
    line(); count++;
    temp = "                  4-CUBE\n"; System.out.print(temp); temp.getChars(0, temp.length(), fourCube, count); count += temp.length();
    temp = draw(width, "="); temp.getChars(0, temp.length(), fourCube, count); count += temp.length();
    line(); count++;


    int length=y;
    for (int i=z;i > 0;i--)
    {
      space(i);
      if (i == z || i == 1)
      {
        draw(x, "  *");
        if (i == 1 && y < z)
        { 
          space(y - 2);
          star(1);
        }
        if (i == 1 && y > z)
        {
          space(z - 2);
          star(1);
        }
        space((z - i) - 1);
        if (i == 1 && y == z)
        {
          star(1);
        }
      }
      else
      {
        draw(1, "  *");
        draw(x - 2, "   ");
        draw(1, "  *");

        if (length > 0)
        {
          space((z - i) - 1);
          star(1);
        }
        if (y < z && length <= 0)
        {
          space(y - 2);
          star(1);
        }

      }
      line();
      length--;
    }

    fourCube[count] = '~';
    return fourCube;
  }
////////////////////////////////////////////////////////
}
