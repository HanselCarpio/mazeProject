package MazeRunner;

import domain.Block;
import domain.Character;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
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
 * @author hansel
 */
public class MazeRunner extends Application implements Runnable {

    private final int WIDTH = 1350;
    private final int HEIGHT = 720;
    private final int WIDTHM = 1200;
    private final int HEIGHTM = 720;
    private int pixelSize;
    private Block maze[][];
    private BufferedImage image;
    private Canvas canvas;
    private Pane pane;
    private GraphicsContext graphicsContext;
    private FileChooser fileChooser;
    private boolean val = false;
    private Thread play;
    private Character player;
    private int aux;

    public MazeRunner() {
        
        getDificult();
        this.maze = new Block[WIDTHM / pixelSize][HEIGHTM / pixelSize];
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MAZE RUNNER");
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
        play = new Thread(this);
        play.start();
        canvas = new Canvas(WIDTH, HEIGHT);
        Button run = new Button("RUN");
        run.setMinSize(100, 30);
        run.relocate(1250, 200);
        
        Button easy = new Button("EASY");
        easy.setMinSize(100, 30);
        easy.relocate(1250, 300);
        
        Button medium = new Button("MEDIUM");
        medium.setMinSize(100, 30);
        medium.relocate(1250, 400);
        
        Button hard = new Button("HARD");
        hard.setMinSize(100, 30);
        hard.relocate(1250, 500);
        
        run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                player = new Character(getPixelSize(), initPlayer());
                val = true;
                player.start();
            }
        });
        
        easy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    pixelSize= 120;
                    drawMaze(graphicsContext, pixelSize);
                    createMaze(pixelSize);
                    draw(graphicsContext, pixelSize);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MazeRunner.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        
        medium.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    pixelSize= 80;
                    drawMaze(graphicsContext, pixelSize);
                    createMaze(pixelSize);
                    draw(graphicsContext, pixelSize);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MazeRunner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        hard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    pixelSize= 40;
                    drawMaze(graphicsContext, pixelSize);
                    createMaze(pixelSize);
                    draw(graphicsContext, pixelSize);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MazeRunner.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        graphicsContext = canvas.getGraphicsContext2D();
        this.fileChooser = new FileChooser();
        this.fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Extends", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp", "*.wbmp"));
        pane = new Pane(canvas);
        

        selectImage(primaryStage, graphicsContext, fileChooser, canvas);
        createMaze(pixelSize);
        drawMaze(graphicsContext, pixelSize);

        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getSource() == canvas) {
                    changeType((int) event.getX(), (int) event.getY(), graphicsContext);
                    printType((int) event.getX(), (int) event.getY());
                }
            }
        });

        pane.getChildren().add(run);
        pane.getChildren().add(medium);
        pane.getChildren().add(easy);
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
                elapsed = System.nanoTime() - start;
                wait = time - elapsed / 1000000;

                if (val) {
                    draw(graphicsContext, pixelSize);
                }

                Thread.sleep(wait);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(MazeRunner.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void draw(GraphicsContext gc, int pixelSize) throws InterruptedException {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        drawMaze(gc, pixelSize);
        player.draw(gc);
    }

    public Block initPlayer() {
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
        drawMaze(gc, pixelSize);
        searchNewRoads();
    }

    public int getPixelSize() {
        return this.pixelSize;
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
                Logger.getLogger(MazeRunner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void createMaze(int pixelSize) {
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
        aux = (int) (Math.random() * (3 - 1) + 1);
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

    public void drawMaze(GraphicsContext gc, int pixelSize) {

        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j].getType().equals("wall")) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(i * pixelSize, j * pixelSize, pixelSize, pixelSize);

                } else {
                    gc.setFill(Color.FLORALWHITE);
                    gc.fillRect(i * pixelSize, j * pixelSize, pixelSize, pixelSize);
                }

            }
        }

    }

    private ArrayList<Block> roads(int x, int y) {
        ArrayList<Block> next = new ArrayList<>();
        if (x + 1 < maze.length && maze[x + 1][y].getType().equals("floor")) {

            next.add(maze[x + 1][y]);
        }
        if (x - 1 >= 0 && maze[x - 1][y].getType().equals("floor")) {

            next.add(maze[x - 1][y]);
        }
        if (y + 1 < maze[0].length && maze[x][y + 1].getType().equals("floor")) {

            next.add(maze[x][y + 1]);
        }
        if (y - 1 >= 0 && maze[x][y - 1].getType().equals("floor")) {

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
