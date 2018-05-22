package GUI;

import domain.character;
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

/**
 *
 * @author hansel
 */
public class Maze extends Application implements Runnable {

    private final int WIDTH = 1200;
    private final int HEIGHT = 650;
    private Canvas mazeCanvas;
    private Pane pane;
    private GraphicsContext gcMaze;
    private FileChooser fileChooserImage;
    private Logic logic;
    private boolean val = false;
    private Thread thread;
    private character pl;

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
        Button run = new Button("RUN");
        run.relocate(1250, 200);
        
        Button easy = new Button("easy");
        easy.relocate(1250, 300);
        
        Button medium = new Button("medium");
        medium.relocate(1250, 400);
        
        Button hard = new Button("hard");
        hard.relocate(1250, 500);
                
        thread = new Thread(this);
        thread.start();
        logic = new Logic();
        mazeCanvas = new Canvas(WIDTH, HEIGHT);
        gcMaze = mazeCanvas.getGraphicsContext2D();
        this.fileChooserImage = new FileChooser();
        this.fileChooserImage.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Extends", "*.jpg", "*.jpeg"));
        pane = new Pane(mazeCanvas);
        logic.selectImage(primaryStage, gcMaze, fileChooserImage, mazeCanvas);
        logic.createMaze();
        logic.drawMaze(gcMaze);

        this.mazeCanvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getSource() == mazeCanvas) {
                    logic.changeType((int) event.getX(), (int) event.getY(), gcMaze);
                    logic.printType((int) event.getX(), (int) event.getY());
                }
            }
        });
        
        run.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pl = new character(30, logic.init());
                val = true;
                pl.start();
            }
        });

        
        pane.getChildren().add(run);
        pane.getChildren().add(easy);
        pane.getChildren().add(medium);
        pane.getChildren().add(hard);
        Scene scene = new Scene(pane, WIDTH + 150, HEIGHT);
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

        logic.drawMaze(gc);
        gc.setFill(Color.BLACK);
        gc.fillRect(this.pl.getX(), this.pl.getY(), 30, 30);
    }

    public static void main(String[] args) {
        launch(args);
    } // main
}
