import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This is a class
 * Created 2022-04-11
 *
 * @author Magnus Silverdal
 */
public class Picture extends  Canvas implements Runnable{
    private final int height = 600;
    private final int width = 800;
    private boolean isRunning;
    int fps = 30;
    private Thread thread;
    private BufferStrategy bs;
    int treeX = 200;
    int treeY = 200;
    int cloudX = 100;
    int cloudY = 50;
    int sunX = 600;
    int sunY = 50;
    int sunW = 100;
    int sunH = 100;
    int[] mountainY= {250,150,250};
    int[] mountainX= {150,400,750};
    int houseX = 500;
    int houseVX = 0;


    public Picture() {
        JFrame frame = new JFrame("Picture");
        this.setSize(width, height);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        isRunning = false;

        frame.addKeyListener(new KL());


    }
    public synchronized void start() {
        thread = new Thread(this);
        isRunning = true;
        thread.start();
    }
    public synchronized void stop() {
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void paint() {
        bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        draw(g);
        g.dispose();
        bs.show();
    }
    private void update() {
        if (houseX + houseVX < width+4*marioimg.getWidth() && houseX + houseVX > 0)
            houseX += houseVX;

    }

    public void paint(Graphics g) {
        sunY += 1;
        cloudX += 5;
        treeX += 2;


        if(sunY > 200)
            sunY = 0;
        if (cloudX > 850)
            cloudX = 0;
        if (treeX > 850)
            treeX = -100;

        draw(g);
        repaint();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public void draw(Graphics g) {
        drawsky(g);
        drawsun(g);
        drawgrass(g);
        drawmountain(g);
        drawcloud(g);
        drawTree(g, treeX,treeY);
        drawTree(g, treeX+30,treeY);
        drawTree(g, treeX+60,treeY);
        drawhouse(g);
    }

    private void drawTree(Graphics g, int x, int y) {
        g.setColor(new Color(0x00DD00));
        g.fillRect(x,y,20,40);
        g.setColor(Color.white);
        g.fillRect(x+8,y+40,4,20);
    }

    private void drawgrass(Graphics g){
        g.setColor(new Color(0x00DD00));
        g.fillRect(0, 250, 800, 550);
    }

    private void drawsky(Graphics g){
        g.setColor(new Color(0x9999ff));
        g.fillRect(0, 0, 800, 250);
    }
    private void drawhouse(Graphics g){
        g.setColor(Color.white);
        g.fillRect(houseX, 200, 100, 100);
        g.setColor(Color.black);
        g.fillRect(houseX +55, 270, 15, 30);
        g.setColor(Color.black);
        int[] xs = {houseX-10,houseX+50,houseX+110};
        int[] ys = {200,150,200};
        Polygon shape = new Polygon(xs, ys, 3);
        g.fillPolygon(shape);
    }
    private void drawcloud(Graphics g){
        g.setColor(Color.white);
        g.fillOval(cloudX, cloudY, cloudY, cloudY);
        g.setColor(Color.white);
        g.fillOval(cloudX+ 35, cloudY, cloudY, cloudY);
        g.setColor(Color.white);
        g.fillOval(cloudX+ 70, cloudY, cloudY, cloudY);
        g.setColor(Color.white);
        g.fillOval(cloudX+ 35, cloudY-30, cloudY, cloudY);
    }
    private void drawmountain(Graphics g) {
        g.setColor(Color.gray);
        g.fillPolygon(mountainX, mountainY, 3);
    }

    private void drawsun(Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval(sunX, sunY, sunW, sunH);

    }

    public static void main(String[] args) {
        Picture exempel = new Picture();
    }

    @Override
    public void run() {
        double deltaT = 1000.0 / fps;
        long lastTime = System.currentTimeMillis();

        while (isRunning) {
            long now = System.currentTimeMillis();
            if (now - lastTime > deltaT) {
                update();
                lastTime = now;
            }
            paint();
        }
        stop();
    }

    private class KL implements KeyListener {
        @Override
        public void keyTyped(KeyEvent keyEvent) {

        }

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == 'a') {
                houseVX = -5;
            }
            if (keyEvent.getKeyChar() == 'd') {
                houseVX = 5;
            }
            if (keyEvent.getKeyChar() == 'w') {
                houseVX = 0;
            }
            if (keyEvent.getKeyChar() == 's') {
                houseVX = 0;
            }
        }

        @Override
        public void keyReleased(KeyEvent keyEvent) {
            if (keyEvent.getKeyChar() == 'a') {
                houseVX = 0;
            }
            if (keyEvent.getKeyChar() == 'd') {
                houseVX = 0;
            }
            if (keyEvent.getKeyChar() == 'w') {
                houseVX = 0;
            }
            if (keyEvent.getKeyChar() == 's') {
                houseVX = 0;
            }
        }
    }
}


