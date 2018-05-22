package GUI;

import domain.Block;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author hansel
 */
public class Logic {

    private final int WIDTH = 1200;
    private final int HEIGHT = 650;
    private int pixelSize;
    private Block maze[][];
    private BufferedImage image;

    public Logic() {
        getDificult();
        this.maze = new Block[WIDTH / pixelSize][HEIGHT / pixelSize];
    }

    public Block init() {
        return this.maze[0][0];
    }

    public void changeType(int x, int y, GraphicsContext gc) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < this.maze[0].length; j++) {
                if (this.maze[i][j].pressMouse(x, y)) {
                    if (this.maze[i][j].getType().equals("wall")) {
                        this.maze[i][j].setType("floor");
                    } else {
                        this.maze[i][j].setType("wall");
                    }

                    break;
                }
            }
        }
        drawMaze(gc);
        searchNewRoads();
    }

    public void printType(int x, int y) {
        for (int f = 0; f < maze.length; f++) {
            for (int c = 0; c < maze[0].length; c++) {
                if (maze[f][c].pressMouse(x, y)) {
                    System.out.println(maze[f][c].getType());
                }
            }
        }
    }

    public BufferedImage resize(BufferedImage image, int newWidth, int newHeight) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage newImage = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D g = newImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, newWidth, newHeight, 0, 0, width, height, null);
        g.dispose();
        return newImage;
    }

    public void selectImage(Stage primaryStage, GraphicsContext gc, FileChooser fileChooser, Canvas canvas) {
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            try {
                BufferedImage aux = ImageIO.read(file);
                this.image = resize(aux, WIDTH, HEIGHT);
            } catch (IOException ex) {
                Logger.getLogger(Maze.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void createMaze() {
        BufferedImage aux;
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                aux = this.image.getSubimage((i * pixelSize), (j * pixelSize), pixelSize, pixelSize);
                if ((i + j) % 2 == 0) {
                    maze[i][j] = new Block(aux, i, j, pixelSize, "wall");
                } else {
                    maze[i][j] = new Block(aux, i, j, pixelSize, "floor");
                }
            }
        }
        searchNewRoads();
    }

    private void getDificult() {
        int aux = (int) (Math.random() * (3 - 1) + 1);
        switch (aux) {
            case 1:
                pixelSize = 120;
                break;
            case 2:
                pixelSize = 80;
                break;
            case 3:
                pixelSize = 40;
                break;
            default:
                break;
        }
    }

    public void drawMaze(GraphicsContext gc) {
        //gc.clearRect(0, 0, WIDTH, HEIGHT);
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j].getType().equals("wall")) {
                    gc.drawImage(SwingFXUtils.toFXImage(maze[i][j].getImage(), null), i * pixelSize, j * pixelSize);
                } else {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(i * pixelSize, j * pixelSize, pixelSize, pixelSize);
                }
            }
        }

    }

    private ArrayList<Block> roads(int x, int y) {
        ArrayList<Block> next = new ArrayList<>();
        if (x + 1 < maze.length && maze[x + 1][y].getType().equals("empty")) {

            next.add(maze[x + 1][y]);
        }
        if (x - 1 > 0 && maze[x - 1][y].getType().equals("empty")) {

            next.add(maze[x - 1][y]);
        }
        if (y + 1 < maze[0].length && maze[x][y + 1].getType().equals("empty")) {

            next.add(maze[x][y + 1]);
        }
        if (y - 1 > 0 && maze[x][y - 1].getType().equals("empty")) {

            next.add(maze[x][y - 1]);
        }

        return next;
    }

    private void searchNewRoads() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j].setNext(roads(i, j));
            }
        }
    }

}
