<<<<<<< HEAD
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
public class Character extends Thread {

    private int posiX, posiY, x, y, size;
    private ArrayList<Block> road, past;
    private int direction;

    public Character(int size, Block start) {
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
     
            this.x+=1;

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
=======
package domain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author hansel
 */
public class Character extends Thread {

    private ArrayList<Image> sprite;
    private int x;
    private int y;
    private int imgNum;
    private Image image;

    // constructor
    public Character() throws FileNotFoundException {
        super("Consumer");
        this.x = x;
        this.y = y;
        this.imgNum = imgNum;
        this.sprite = new ArrayList<Image>();
        //     sharedLocation = shared;
        setSprite();
    }

    public void setSprite() throws FileNotFoundException {
        ArrayList<Image> sprite = getSprite();
        for (int i = 1; i <= 16; i++) {
            sprite.add(new Image(new FileInputStream("src/assets/run" + i + ".png")));
        }
        setSprite(sprite);
    }

    // store values from 1 to 4 in sharedLocation
    @Override
    public void run() {
        ArrayList<Image> sprite = getSprite();
        setImage(sprite.get(1));
        int num = 7;
        int sum = 0;

        for (int a = 0; a < 8; a++) {
            try {

                setImage(sprite.get(8));
                for (int i = 1100; i > 700; i = i - 50) {

                    setImage(sprite.get(num));

                    num++;

                    setX(i);
                    Thread.sleep((int) (Math.random() * 300));

                }

                num = 0;
                for (int j = 750; j < 1150; j = j + 50) {

                    setImage(sprite.get(num));
                    if (num == 9) {
                        num = 9;
                    } else {
                        num++;
                    }
                    setX(j);
                    Thread.sleep((int) (Math.random() * 300));

                }

            } catch (InterruptedException ex) {
            }
        }
    } // end method run

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

    public int getImgNum() {
        return imgNum;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setImgNum(int imgNum) {
        this.imgNum = imgNum;
    }

    public ArrayList<Image> getSprite() {
        return sprite;
    }

    public void setSprite(ArrayList<Image> sprite) {
        this.sprite = sprite;
    }

}
>>>>>>> b68980119bf0efba7341732262ceeb810643b220
