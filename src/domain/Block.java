package domain;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author hansel
 */
public class Block {

    private int posiX, posiY, size;
    private String type;
    private ArrayList<Block> next;
    private BufferedImage image;

    public Block(BufferedImage image, int x, int y, int size, String type) {
        this.image = image;
        this.posiX = x;
        this.posiY = y;
        this.size = size;
        this.type = type;
    }

    public int getX() {
        return posiX;
    }

    public void setX(int x) {
        this.posiX = x;
    }

    public int getY() {
        return posiY;
    }

    public void setY(int y) {
        this.posiY = y;
    }

    public String getType() {
        return type;
    }

    public ArrayList<Block> getNext() {
        return next;
    }

    public void setNext(ArrayList<Block> next) {
        this.next = next;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public boolean in(int xMouse, int yMouse) {

        return (((xMouse >= this.posiX * size && xMouse < this.posiX * size + this.size) || (xMouse + size > this.posiX * size && xMouse + size < this.posiX * size + this.size)) && ((yMouse >= this.posiY * size && yMouse < this.posiY * size + this.size) || (yMouse + size >= this.posiY * size && yMouse + size < this.posiY * size + this.size)));
    } 

    public boolean pressMouse(int xMouse, int yMouse) {
        if ((xMouse >= this.posiX * this.size && xMouse <= this.posiX * this.size + this.size)
                && (yMouse >= this.posiY * this.size && yMouse <= this.posiY * this.size + this.size)) {
            return true;
        }
        return false;
    }

    public void setType(String type) {
        this.type = type;
    }

}
