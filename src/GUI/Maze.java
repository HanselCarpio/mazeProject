package GUI;

import domain.Block;
import domain.Character;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.imageio.ImageIO;

/**
 *
 * @author pablo
 */
public class Maze extends Application implements Runnable {

    private final int WIDTH = 1350;
    private final int HEIGHT = 650;
    private Canvas mazeCanvas;
    private Pane pane;
    private GraphicsContext gcMaze;
    private FileChooser fileChooser;
    private boolean val = false;
    private Thread thread;
    private Character pl;
    private final int WIDTHM = 1200;
    private final int HEIGHTM = 650;
    private int pixelSize;
    private Block maze[][];
    private BufferedImage image;
    
    public Maze() {
        getDificult();
        this.maze = new Block[WIDTHM / pixelSize][HEIGHTM / pixelSize];
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Maze Runner");
        init(primaryStage);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
        primaryStage.resizableProperty().set(false);
        primaryStage.show();
    } // start

    public void init(Stage primaryStage) {
        Button run = new Button("run");
        run.setMinSize(100, 30);
        run.relocate(1250, 200);
        
        Button easy = new Button("easy");
        easy.setMinSize(100, 30);
        easy.relocate(1250, 300);
        
        Button medium = new Button("medium");
        medium.setMinSize(100, 30);
        medium.relocate(1250, 400);
        
        Button hard = new Button("hard");
        hard.setMinSize(100, 30);
        hard.relocate(1250, 500);
                
        thread = new Thread(this);
        thread.start();
        mazeCanvas = new Canvas(WIDTH, HEIGHT);
        gcMaze = mazeCanvas.getGraphicsContext2D();
        this.fileChooser = new FileChooser();
        this.fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Extends", "*.jpg", "*.jpeg"));
        pane = new Pane(mazeCanvas);
        selectImage(primaryStage, gcMaze, fileChooser, mazeCanvas);
        createMaze();
        drawMaze(gcMaze);

        this.mazeCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getSource() == mazeCanvas) {
                    changeType((int) event.getX(), (int) event.getY(), gcMaze);
                    printType((int) event.getX(), (int) event.getY());
                }
            }
        });
        
        run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    pl = new Character();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Maze.class.getName()).log(Level.SEVERE, null, ex);
                }
                val = true;
                pl.start();
            }
        });
        
        easy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    pl = new Character();
                    val = true;
                    pl.start();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Maze.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        medium.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    pl = new Character();
                    val = true;
                    pl.start();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Maze.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        hard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    pl = new Character();
                    val = true;
                    pl.start();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Maze.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        
        pane.getChildren().add(run);
        pane.getChildren().add(easy);
        pane.getChildren().add(medium);
        pane.getChildren().add(hard);
        Scene scene = new Scene(pane, WIDTH, HEIGHT);
        primaryStage.setScene(scene);
    } // init

    @Override
    public void run() {
        long start;
        long elapsed;
        long wait;
        int fps = 60;
        long time = 1000 / fps;
        try {
            while (true) {

                start = System.nanoTime();

                if (val) {
                    draw(gcMaze);
                }

                elapsed = System.nanoTime() - start;
                wait = time - elapsed / 1000000;
                if (wait < 0) {
                    wait = 5;
                }
                Thread.sleep(wait);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Maze.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void draw(GraphicsContext gc) throws InterruptedException {
        drawMaze(gc);
        gc.setFill(Color.BLACK);
        gc.fillRect(this.pl.getX(), this.pl.getY(), 30, 30);
    }

    public Block initMaze() {
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
                this.image = resize(aux, WIDTHM, HEIGHTM);
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

    public static void main(String[] args) {
        launch(args);
    } // main
}
