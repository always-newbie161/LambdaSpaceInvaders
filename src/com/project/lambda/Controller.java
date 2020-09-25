package com.project.lambda;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    @FXML
    public Pane menuPane;
    @FXML
    public ImageView nking;

    @FXML
    public Label start;
    @FXML
    public Label exit;
    @FXML
    public Label about;
    @FXML
    private Pane ground;
    @FXML
    private Label level;
    @FXML
    private Label scoreboard;
    @FXML
    private ImageView present;
    @FXML
    private ImageView next;

    @FXML
    private ImageView l2ig;
    @FXML
    private ImageView l3ig;
    @FXML
    private ImageView dracarys;
    @FXML
    private ImageView drogon;
    @FXML
    private Rectangle fullbar;
    @FXML
    private Rectangle knightbar;
    @FXML
    private ImageView wdracarys;
    @FXML
    private ImageView fire;
    @FXML
    private Pane dragpane;
    @FXML
    private Rectangle invadebar;


    public Scene scene;
    public AnimationTimer timer;
    boolean gameOver = false;
    public MediaPlayer mediaPlayer;

    public final List<warrior> knights = new ArrayList<>();
    private final List<warrior> enemies = new ArrayList<>();
    private static int score = 0;
    private static int clevel = 0;
    private int en_pres = 0;
    private double t = 0;
    private int frames = 0;
    private int basedamage = 0;
    private static DoubleProperty dp,dp2;
    private static final List<Integer> vwights =new ArrayList<>();
    boolean[] vflags = new boolean[4];
    private final Random r = new Random();
    int id=0;

    List<pixel> pixels = new ArrayList<>();
    public boolean allowed = true;


    public void createContent() {
        Menu fileMenu = new Menu("File");

        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(event -> resetGame());
        MenuItem pauseGame = new MenuItem("Pause Game");
        pauseGame.setOnAction(event -> timer.stop());
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem exitGame = new MenuItem("Exit Game");
        exitGame.setOnAction(event -> exitGame());

        fileMenu.getItems().addAll(newGame, pauseGame, separatorMenuItem, exitGame);
        MenuBar menubar = new MenuBar();
        menubar.getMenus().add(fileMenu);
        menubar.prefWidthProperty().bind(menuPane.widthProperty());
        menubar.prefHeightProperty().bind(menuPane.heightProperty());
        menuPane.getChildren().add(menubar);


        onMusic("GOTbgm.mp3");
        int y = 300;
        int x = 50;
        for (int i = 1; i <= 20; i++) {
            for (int j = 1; j <= 500; j++) {
                if (j % 135 <= 95)
                    pixels.add(new pixel(x, y, "#009dff"));
                x++;
            }
            x = 50;
            y++;
        }
        for (pixel p : pixels) {
            ground.getChildren().add(p);
        }
        knights.add(new warrior(createImage("dothraki.png"), 50, "Knight", 300, 443, 2, 1, "dw.png"));
        knights.add(new warrior(createImage("unsullied.png"), 50, "Knight", 300, 443, 2, 0.5, "uns_sp.png"));
        knights.add(new warrior(createImage("starkarmy.png"), 50, "Knight", 300, 443, 1, 0.5, "starksw.png"));
        knights.add(new warrior(createImage("wild.png"), 50, "Knight", 300, 443, 1, 1, "wildw.png"));


        scoreboard.setText("0");
        score = 0;
        ground.getChildren().add(knights.get(0));
        updateSideBar();
        setControls();

        dp = new SimpleDoubleProperty(1.0);
        dp2 = new SimpleDoubleProperty(0);
        DoubleBinding bind = fullbar.widthProperty().multiply(dp);
        DoubleBinding bind2 = fullbar.widthProperty().multiply(dp2);
        knightbar.widthProperty().bind(bind);
        invadebar.widthProperty().bind(bind2);
        drogon.setOnMouseClicked(e -> {
            if(ground.getChildren().contains(dracarys))
            dracarys.setVisible(true);
        });

        generateVMovers();

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver) {
                    doThisInEveryFrame();
                } else {
                    mediaPlayer.pause();
                    this.stop();
                }
            }
        };
        Arrays.fill(vflags,false);
        clevel = 0;
        id=0;
        timer.start();
        level.setText(String.valueOf(clevel));
        createEnemies();
    }

    public void onMusic(String s) {
        if (mediaPlayer != null)
            mediaPlayer.stop();
        Media sound = new Media(new File(s).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.seek(Duration.ZERO));
        mediaPlayer.play();

    }

    private void createEnemies() {
        Image image2 = createImage("wight.png");
        for (int i = 0; i < 5; i++) {
            warrior e = new warrior(image2, 20, "enemy", 160 + i * 70, 100, 1, 1, "whiteweapon.png");
            ground.getChildren().add(e);
            enemies.add(e);
        }
        for (int i = 0; i < 6; i++) {
            warrior e = new warrior(image2, 20, "enemy", 160 + i * 56, 70, 1, 1, "whiteweapon.png");
            ground.getChildren().add(e);
            enemies.add(e);
        }
        en_pres = enemies.size();

    }

    private List<warrior> warriors() {
        return ground.getChildren().stream().filter(node -> node.getClass() == warrior.class).map(n -> (warrior) n).collect(Collectors.toList());
    }

    private List<Circle> flames() {
        return ground.getChildren().stream().filter(n -> n.getClass() == Circle.class).map(n -> (Circle) n).collect(Collectors.toList());
    }

    private void doThisInEveryFrame() {

        frames++;
        if (frames > 120) {
            frames = 1;
        }
        if(frames ==100){
            if(clevel>0) {
                if (id < 4) {
                    if(id==2){
                        wdracarys.setVisible(true);
                    }
                    vflags[id] = true;
                    if(clevel==2)
                        vflags[++id] = true;
                    System.out.println(frames + " " + id);
                }
                id++;

            }
        }
        if (en_pres == 0 && frames == 100) {
            if (clevel == 1)
                l2ig.setVisible(false);
            else
                l3ig.setVisible(false);

            createEnemies();
            frames = 1;
        }


        t += 0.016;


            if(wdracarys.getTranslateY()==286) {
                wdracarys.setVisible(false);
                wdracarys.setTranslateY(0);
            }
            if(wdracarys.isVisible()){
                wdracarys.setTranslateY(wdracarys.getTranslateY() + 5);
                for (pixel p : pixels) {
                    if (wdracarys.getBoundsInParent().intersects(p.getBoundsInParent()) && ground.getChildren().contains(p)) {
                        ground.getChildren().remove(p);

                    }

                }
                if (ground.getChildren().contains(wdracarys) && wdracarys.getBoundsInParent().intersects(knights.get(0).getBoundsInParent())) {
                    ground.getChildren().remove(knights.get(0));
                    knights.remove(0);
                    dp.set(1);
                    if (knights.isEmpty()) {
                        createAlert(1);
                        gameOver = true;

                    } else {
                        ground.getChildren().add(knights.get(0));
                        updateSideBar();
                        setControls();
                    }
                    wdracarys.setVisible(false);
                }
            }

        if(dracarys.isVisible()) {
            if (dracarys.getTranslateY() ==-195) {
                dracarys.setVisible(false);
                dracarys.setTranslateY(0);
                ground.getChildren().remove(dracarys);
                dragpane.getChildren().remove(fire);

            }

            else {
                dracarys.setTranslateY(dracarys.getTranslateY() - 5);
                for (pixel p : pixels) {
                    if (dracarys.getBoundsInParent().intersects(p.getBoundsInParent()) && ground.getChildren().contains(p)) {
                        ground.getChildren().remove(p);

                    }

                }
                warriors().stream().filter(e -> (e.type.equals("enemy") || e.type.equals("n_king"))).forEach(enemy -> {
                    if (dracarys.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                        ground.getChildren().remove(enemy);
                        score++;
                        en_pres--;
                        scoreboard.setText(String.valueOf(score));
                        updateLevel();
                    }
                });
            }
        }
        warriors().forEach(i -> {
            switch (i.type) {

                case "Knightbullet":
                    i.moveUp();
                    warriors().stream().filter(e -> (e.type.equals("enemy") || e.type.equals("n_king"))).forEach(enemy -> {
                        if (i.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                            ground.getChildren().remove(i);
                            enemy.setLives((enemy.getLives() - i.b_power));
                            if (enemy.getLives() == 0.5) {
                                enemy.setImage(createImage("wight2.png"));
                            }
                            if (enemy.getLives() <= 0) {
                                score++;
                                if (enemy.type.equals("n_king"))
                                    score += 4;
                                scoreboard.setText(String.valueOf(score));
                                ground.getChildren().remove(enemy);
                                en_pres--;
                            }
                            updateLevel();
                        }
                    });
                    for (pixel p : pixels) {
                        if (i.getBoundsInParent().intersects(p.getBoundsInParent()) && ground.getChildren().contains(p)) {
                            ground.getChildren().remove(i);
                            ground.getChildren().remove(p);

                        }

                    }


                    break;

                case "enemybullet":
                    i.moveDown();
                    if (!(knights.isEmpty()) && i.getBoundsInParent().intersects(knights.get(0).getBoundsInParent())) {
                        ground.getChildren().remove(i);
                        dp.set(dp.get()-0.2);
                        if(dp.get() <=0.2) {
                            ground.getChildren().remove(knights.get(0));
                            knights.remove(0);
                            dp.set(1);
                            if (knights.isEmpty()) {
                                createAlert(1);
                                gameOver = true;

                            } else {
                                ground.getChildren().add(knights.get(0));
                                updateSideBar();
                                setControls();
                            }
                        }
                    }

                    for (pixel p : pixels) {
                        if (i.getBoundsInParent().intersects(p.getBoundsInParent()) && ground.getChildren().contains(p)) {

                            ground.getChildren().remove(i);
                            ground.getChildren().remove(p);

                        }

                    }


                    break;
                case "enemy":
                    int id =enemies.indexOf(i);
                    if (clevel == 0)
                        hmove(i);

                    else{
                        if (vwights.contains(id) && vflags[vwights.indexOf(id)]) {
                            vmove(i);
                        }
                        else {
                            hmove(i);
                        }
                    }
                    if (t > 2)
                        if (Math.random() < 0.3) {
                            fire(i);
                        }
                    break;


            }

        });
        flames().forEach(i -> {
            if (i.getRadius() == 2.1) {
                i.setTranslateY(i.getTranslateY() + 0.5);
                if (i.getTranslateY() >= 230)
                    ground.getChildren().remove(i);
            } else {
                i.setTranslateY(i.getTranslateY() - 0.5);
                if (i.getTranslateY() <= -210)
                    ground.getChildren().remove(i);
            }

        });

        if (t > 2) {
            for (int i = 0; i < 5; i++) {
                int x = r.nextInt(600);
                Circle f = new Circle();
                Circle icef = new Circle();
                f.setRadius(2);
                icef.setRadius(2.1);
                f.setCenterX(x);
                icef.setCenterX(x);
                icef.setCenterY(70);
                f.setCenterY(530);
                f.setFill(Color.valueOf("#e25822"));
                icef.setFill(Color.valueOf("#009dff"));
                ground.getChildren().add(f);
                ground.getChildren().add(icef);
            }
            allowed = true;
            t = 0;
            t=0;
        }
    }

    public Image createImage(String name) {
        String filepath = "src/Rimages/" + name;
        return (new Image(new File(filepath).toURI().toString()));

    }

    private void createAlert(int cause) {
        Alert popup = new Alert(Alert.AlertType.CONFIRMATION);
        String s = "Your score is " + score;
        popup.setTitle("GameOver!");
        if(cause==0)
            popup.setHeaderText("You made it.No wights remaining! \n"+s);
        else if(cause==1)
            popup.setHeaderText("All the knights are killed. \n"+s);
        else
            popup.setHeaderText("Winterfell is invaded by the deadarmy. \n"+s);

        popup.setContentText("Do you want to play again?");
        ButtonType yesBtn = new ButtonType("yes");
        ButtonType noBtn = new ButtonType("no");
        popup.getButtonTypes().setAll(yesBtn, noBtn);
        Platform.runLater(() -> {
            Optional<ButtonType> clickedbtn = popup.showAndWait();
            if (clickedbtn.isPresent() && clickedbtn.get() == noBtn) {
                exitGame();
            } else {
                resetGame();
            }
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    void fire(warrior dragon) {
        warrior bullet = new warrior(createImage(dragon.weapon), dragon.type.equals("knight") ? 20 : 20, (dragon.type.equals("n_king") ? "enemy" : dragon.type) + "bullet", (int) dragon.getTranslateX() + (int) (dragon.getFitWidth() / 2), (int) dragon.getTranslateY(), 1, dragon.b_power, "nil");
        if (dragon.weapon.equals("starksw.png")) {
            bullet.setPreserveRatio(false);
            bullet.setFitHeight(30);
        } else if (dragon.weapon.equals("wildw.png")) {
            bullet.setFitWidth(40);
        }
        ground.getChildren().add(bullet);
    }

    public void resetGame() {
        timer.stop();
        frames = 1;
        clevel = 0;
        basedamage = 0;
        dracarys.setVisible(false);
        gameOver = false;
        ground.getChildren().removeIf(n -> !(n.getClass() == ImageView.class));
        knights.clear();
        enemies.clear();
        createContent();


    }

    public static class warrior extends ImageView {
        final String type;
        boolean moveleft = false;
        boolean moveright = false;
        double lives;
        double b_power;
        String weapon;

        public warrior(Image image, int w, String type, int x, int y, double lives, double b_power, String weapon) {
            this.setImage(image);
            this.setFitWidth(w);
            this.setPreserveRatio(true);
            this.setSmooth(true);
            this.setCache(true);
            this.type = type;
            this.setTranslateX(x);
            this.setTranslateY(y);
            this.lives = lives;
            this.b_power = b_power;
            this.weapon = weapon;
        }

        public void setLives(double lives) {
            this.lives = lives;
        }

        public double getLives() {
            return this.lives;
        }

        void moveLeft() {
            this.setTranslateX(this.getTranslateX() - 5);
            this.moveleft = true;
        }

        void moveRight() {
            this.setTranslateX(this.getTranslateX() + 5);
            this.moveright = true;
        }

        void moveUp() {

            setTranslateY(getTranslateY() - 5);
        }

        void moveDown() {
            if (this.type.contains("bullet"))
                setTranslateY(getTranslateY() + 5);
            else
                setTranslateY(getTranslateY() + 1);

        }

    }

    public static class pixel extends Rectangle {
        public pixel(int x, int y, String colorCode) {
            this.setWidth(1);
            this.setHeight(1);
            this.setLayoutX(x);
            this.setLayoutY(y);
            this.setFill(Color.valueOf(colorCode));
        }

    }

    public void setControls() {
        Controller.warrior k = knights.get(0);
        System.out.println(k.weapon);
        System.out.println("controls on");
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case LEFT:
                    if (k.getTranslateX() >= 0) {
                        k.moveLeft();
                        if (k.moveleft)
                            k.moveLeft();
                    }
                    break;
                case RIGHT:
                    if (k.getTranslateX() <= 550) {
                        k.moveRight();
                        if (k.moveright)
                            k.moveRight();
                    }
                    break;
                case SPACE:
                    if (allowed) {
                        allowed = false;
                        fire(k);
                    }
                    break;
            }
        });


    }


    public void hmove(warrior i) {

        if (frames <= 30 || frames > 90)
            i.moveLeft();
        else
            i.moveRight();
    }

    public void generateVMovers(){
        vwights.clear();
        Arrays.fill(vflags,false);
        while(vwights.size()!=4){
            int id =r.nextInt(11);
            if(!vwights.contains(id))
            vwights.add(id);
        }
        System.out.println(vwights);
    }
    public void vmove(warrior i) {

        if (i.getTranslateY() == 600) {

            ground.getChildren().remove(i);
            en_pres--;
            basedamage++;
            dp2.set(dp2.get()+Math.min(1-dp2.get(),0.2));
            if (basedamage == 5) {
                createAlert(2);
                gameOver = true;
            }
            else {
                updateLevel();
            }
        } else
            i.moveDown();
    }

    private void updateLevel(){
        if (en_pres == 0) {
            if (clevel < 2) {
                Arrays.fill(vflags,false);
                clevel++;
                id=0;
                frames = 1;
                if (clevel == 1)
                    l2ig.setVisible(true);
                else
                    l3ig.setVisible(true);
                level.setText(String.valueOf(clevel));
                enemies.clear();

            } else {
                gameOver = true;
                createAlert(0);
            }
        }
    }

    private void updateSideBar() {
        present.setImage(knights.get(0).getImage());
        present.setFitWidth(50);
        next.setImage(knights.size() > 1 ? knights.get(1).getImage() : createImage("nil"));
        next.setFitWidth(50);
    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }
}
