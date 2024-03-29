import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public abstract class Shapes extends JComponent{
    static final int MAX_HEIGHT = 1000;
    static final int MAX_WIDTH = 1000;
    protected int width;
    protected int rad;
    int x,y;
    int nosides;
    int []polyx;
    int[] polyy;
    String objectType;
    int scalex = getWidth() / Shapes.MAX_WIDTH;
    int scaley = getHeight() / Shapes.MAX_HEIGHT;

    public static Color rainbow() {
        Random rainbow = new Random();
        int red = rainbow.nextInt(255);
        int blue = rainbow.nextInt(255);
        int green = rainbow.nextInt(255);
        return new Color(red, blue, green);
    }

}




class createShapes implements Runnable {
    int width, height;
    Frame frame;
    ArrayList<Shapes> shapes = new ArrayList<>();

    createShapes(int x, int y, Frame frame) {
        this.width = x;
        this.height = y;
        this.frame = frame;
    }

    public void run() {
        for (int i = 1; i < 99; i++) {
            int x = Math.abs((int) (Math.random() * width))+1;
            int y = Math.abs((int) (Math.random() * height))+1;
            System.out.println(x + " | " + y);
            shapes.add(new PPolygon(x, y));
            shapes.add(new POval(x, y));


        }

        double scalex = (double) frame.getWidth() / Shapes.MAX_WIDTH;
        double scaley = (double) frame.getHeight() / Shapes.MAX_HEIGHT;
        System.out.printf(scalex + " " + scaley);
/*
            for (Shapes no : shapes) {
                no.x *= scalex;
                no.y *= scaley;
                if (no.objectType.equals("Polygon")) {
                    for (int i = 0; i < no.polyx.length; i++) {
                        no.polyx[i] = no.polyx[i] * (int) scalex;
                        no.polyy[i] = no.polyy[i] * (int) scaley;

                    }
                }

           }
           */

        frame.repaint();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }

    }

    public void read() throws IOException {
        try {
            FileWriter fw = new FileWriter("GUIProjects");
            Writer output = new BufferedWriter(fw);
            int sz = shapes.size();
            for (int i = 0; i < sz; i++) {
                output.write(shapes.get(i).toString() + " ");
            }
            output.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void write(){
        String s;
        try{
            BufferedReader input = new BufferedReader(new FileReader("GUIProjects.txt"));
            if(!input.ready()){
                throw new IOException();
            }

            int sz = shapes.size();
            for(int i =0; i<sz; i++){
                System.out.println(shapes.get(i).toString());
            }
        }catch (IOException e){
            System.err.println(e);
        }

    }


}



    class POval extends Shapes {
        Color g1 = Shapes.rainbow();


        POval(int x, int y) {
            super();
            objectType = "Oval";
            this.x = getWidth()*scalex;
            this.y = getHeight()*scaley;
            this.width = (int)(Math.random()*MAX_WIDTH);
            this.rad = (int)(Math.random()*MAX_HEIGHT/2);


        }
        public String objType(){
            return objectType;
        }


        @Override
        public String toString(){
            return  objType() + " " + g1.getRed()+" "+g1.getGreen()+" "+g1.getBlue() + " " + x+" "+y+" "+width+" "+ rad ;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(g1);
            g.drawOval(x, y, width, rad);

        }


    }

    class PPolygon extends Shapes{
        Color g1 = Shapes.rainbow();
        Random rand = new Random();

        PPolygon(int x, int y) {
            super();
            objectType = "Polygon";
            nosides = rand.nextInt(7) + 3;
            polyx = new int[nosides];
            polyy = new int[nosides];
            for (int i = 0; i < nosides; i++) {
                polyx[i] = Math.abs(rand.nextInt(x));
                polyy[i] =  Math.abs(rand.nextInt(y));
            }
        }
        /*

        //set random color
       // int sideno = rainbow.nextInt(7)+3;
        double theta = 2 * Math.PI/sideno;
        int[] arrx = new int[sideno];
        int[] arry = new int[sideno];
        for(int i =0;i<sideno;i++){
            arrx[i] = getWidth()/2 +(int)(Math.cos(theta * i)*50);
            arry[i] = getHeight()/2 + (int)(Math.sin(theta * i)*50);

    }
    */
        public String ObjName(){
            return objectType;
        }
        public Color ShapeRGB(){
           return PPolygon.rainbow();
        }
        public String getDimensions(){
            int countx = 0;
            int county=0;
            int it = 0;
            ArrayList<Integer> dim = new ArrayList<>();
            int size=polyx.length+polyy.length;
            for(int i=0;i<size;i++){
                if(i%2==0){
                    dim.add(polyx[countx++]);
                }
                else{
                    dim.add(polyy[county++]);
                }
            }
            String result="";
            for(int p : dim){
                result=result+String.valueOf(p)+" ";
            }
            return result;
            }


        @Override
        public String toString(){
            return  ObjName() + " " + g1.getRed()+" "+g1.getGreen()+" "+g1.getBlue() + " " + getDimensions() ;
        }



        @Override
        public void paint(Graphics g) {
            super.paintComponent(g);
            g.setColor(g1);
            g.drawPolygon(polyx, polyy, nosides);


        }

    }

    class Main {
        public static void main(String[] args) throws InterruptedException   {
            boolean endprogram = false;
            ArrayList<Shapes> fileread = new ArrayList<>();
            Scanner scan = new Scanner(System.in);
            System.out.println("START - run the program");
            System.out.println("STOP - save the shapes to a file");
            System.out.println("RUN - run the saved shapes");
            // String x = scan.nextLine();
            //String y = scan.nextLine();
            //String z = scan.nextLine();
            JFrame frame = new JFrame();
            frame.setSize(Shapes.MAX_WIDTH, Shapes.MAX_HEIGHT);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.setResizable(true);
            frame.setBackground(Color.black);
            createShapes x1 = new createShapes(Shapes.MAX_WIDTH, Shapes.MAX_HEIGHT, frame);
            Thread threader = new Thread(x1);
            threader.start();
            threader.join();
            try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("Example1.txt"));

            for (Object x : x1.shapes) {
                frame.add((Shapes) (x));
                frame.setVisible(true);

                writer.write(x.toString() +"\n");

            }
                writer.close();}
            catch(IOException e){
                System.out.print(e.getStackTrace());
            }
            try {
                File file = new File("Example1.txt");


                BufferedReader in
                        = new BufferedReader(new FileReader(file));

                String st;
                while ((st = in.readLine()) != null) {
                    String[] output;
                    output = st.split("\\s");
                    fileread.add(creationfile(output));
                }
                in.close();
            }
        catch(IOException e){
                System.out.println(e.getClass().getSimpleName());

            }

            JFrame frame1 = new JFrame();
            frame1.setSize(Shapes.MAX_WIDTH, Shapes.MAX_HEIGHT);
            frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame1.setVisible(true);
            frame1.setResizable(true);
            frame1.setBackground(Color.black);

           for(Object x2 : fileread){
               frame1.add((Shapes)x2);
               frame1.setVisible(true);
           }
        }

    public static Shapes creationfile(String[] s){
            if(s[0].equalsIgnoreCase("Oval")){
                POval output = new POval(Integer.parseInt(s[4]),Integer.parseInt(s[4]));
                output.rad=Integer.parseInt(s[7]);
                output.width=Integer.parseInt(s[6]);
                output.g1=new Color(Integer.parseInt(s[1]),Integer.parseInt(s[2]),Integer.parseInt(s[3]));

                return output;
            }
            else{
                PPolygon output = new PPolygon(1,1);
                output.g1=new Color(Integer.parseInt(s[1]),Integer.parseInt(s[2]),Integer.parseInt(s[3]));
                int size = (s.length-4)/2;

                int polyx[] = new int[size];
                int poly[] = new int[size];
                int countx=0;
                int county=0;
                for(int i =4;i<s.length;i++){
                    if(i%2==0){
                        polyx[countx++]=Integer.parseInt(s[i]);
                    }else{
                        poly[county++]=Integer.parseInt(s[i]);
                    }
                }
                output.polyy=poly;
                output.polyx=polyx;
                output.nosides=size;
                return output;

            }

    }
    }

