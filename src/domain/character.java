package domain;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 *
 * @author hansel
 */
public class character extends Thread {

    private int posiX, posiY, x, y, size;
    private ArrayList<Block> road, past;
    private int direction;

    public character(int size, Block start) {
        posiX = start.getX();
        posiY = start.getY();
        x = posiX * size;
        y = posiY * size;
        this.size = size;
        this.road = new ArrayList<>();
        this.road.add(start);
        this.past = new ArrayList<>();
    }
    Boolean flag = true;
    int cont = 0;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void run() {

        while (flag) {
            direction = new Random().nextInt(4);
            System.err.println(road.get(cont).getNext().get(0).getX());
            if (next(direction)) {

                switch (direction) {
                    case 0:
                        while (this.road.get(cont).in(x, y)) {
                            y += 1;
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(character.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    case 1:
                        while (this.road.get(cont).in(x, y)) {
                            x += 1;
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(character.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    case 2:
                        while (this.road.get(cont).in(x, y)) {
                            y -= 1;
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(character.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    case 3:
                        while (this.road.get(cont).in(x, y)) {
                            x -= 1;
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(character.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                }
                this.past.add(this.road.get(cont));
                cont++;
            }

        }
    }

    public boolean next(int dir) {
        int aux;
        if (dir == 0 || dir == 1) {
            aux = 1;
        } else {
            aux = -1;
        }

        if (dir == 0 || dir == 2) {
            for (int i = 0; i < this.road.get(cont).getNext().size(); i++) {
                if (this.road.get(cont).getNext().get(i).getY() == posiY + aux) {
                    if (validation(i)) {
                        this.road.add(this.road.get(cont).getNext().get(i));
                        posiY += aux;
                        return true;
                    }
                }
            }
        } else {
            for (int i = 0; i < this.road.get(cont).getNext().size(); i++) {
                if (this.road.get(cont).getNext().get(i).getX() == posiX + aux) {
                    if (validation(i)) {
                        this.road.add(this.road.get(cont).getNext().get(i));
                        posiX += aux;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean validation(int i) {
        for (int j = 0; j < this.past.size(); j++) {
            if (this.road.get(cont).getNext().get(i).getX() == this.past.get(j).getX() && this.road.get(cont).getNext().get(i).getY() == this.past.get(j).getY()) {
                return false;
            }
        }
        return true;
    }

    public void draw(GraphicsContext gc) {

        gc.setFill(Color.AQUA);
        gc.fillRect(x, y, size, size);
    }

}
