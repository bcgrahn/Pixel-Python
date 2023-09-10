/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grahn;

import javax.swing.ImageIcon;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class GameBoard extends JPanel implements ActionListener {

    private final int gridSize = 900;                           
    private final int randAppleXValue = 63;
    private final int randAppleYValue = 32;
    private final int numOfLily = 45;
    private final int numOfPyramids = 35;
    private final int numOfWall = 140;
    private final int xArr[] = new int[gridSize];
    private final int yArr[] = new int[gridSize];
    private final int objectXArr[] = new int[numOfWall];
    private final int objectYArr[] = new int[numOfWall];
    private final int gameWidth = 1600;                       
    private final int gameHeight = 900;           
    private final int unitSize = 25;               
    private final int powerUpProbability = 8;
    private final int powerProbability = 4;

    private Timer gameTimer;
    
    private Image unitImage;
    private Image appleImage;
    private Image headImage;
    private Image goldenAppleImage;
    private Image snakeHead;
    private Image snakeBod;
    private Image object;

    private boolean upDirec = false;
    private boolean downDirec = false;
    private boolean leftDirec = false;
    private boolean rightDirec = true;
    private boolean inGame = true;
    private boolean goldenStat = false;
    private boolean powerStat = false;
    private boolean redo = false;
    private boolean redoObject = false;
    private boolean invincibility = false;
    private boolean doublePoints = false;
    private boolean reverse = false;
    private boolean countDown = false; 
    private boolean move = true;

    private int appleYPos;
    private int appleXPos;
    private int numSnakeUnits;
    private int goldenYPos;
    private int goldenXPos;
    private int numObject = 0;
    private int rNum = 0;
    private int score = 0;
    private int lives = 3;
    private int timer = 20;
    private int delay = 140;
    private int speedDelay;
    private int usualDelay;
    private int tempDelay;
    private int flashCount = 2;
    private int objectXPos;
    private int objectYPos;
    private int xPos;
    private int yPos;

    private String difficulty;
    private String what;
    private String appleLocation;
    private String bodLocation;
    private String headLocation;
    private String powerMsg = "";
    private String appleType = "A";

    private useDatabase ud = new useDatabase();
    private CountDownTimer t = new CountDownTimer ();

    public GameBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.BLUE);
        setFocusable(true);
        setPreferredSize(new Dimension(gameWidth, gameHeight));

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);                    // creates a blank cursor
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
        cursorImg, new Point(0, 0), "blank cursor");
        setCursor(blankCursor);

        addNewGameLayout();
        loadSnakeImages();
        loadAppleImages();
        beginGame();

    }

    private void addNewGameLayout()                                             //Loads either the normal, hard, or easy game layout
    {
        what = "Difficulty";
        difficulty = ud.fetchWhat(what);                                        // fetches difficulty and sends arross column heading

        if (difficulty.equalsIgnoreCase("Hard")) {
            delay = 70;
            usualDelay = 70;
            speedDelay = 140;
            appleLocation = "hardApple.png";
            bodLocation = "hardBod.png";
            headLocation = "hardHead.png";
            setBackground(Color.orange);
            addObjects(numOfPyramids, "wall.png");                              // sends across number of pyramids and image name

        } else if (difficulty.equalsIgnoreCase("Easy")) {
            
            usualDelay = 140;
            speedDelay = 160;
            appleLocation = "easyApple.png";
            bodLocation = "easyBod.png";
            headLocation = "easyHead.png";
            setBackground(Color.black);

        } else {
            delay = 110;
            usualDelay = 110;
            speedDelay = 140;
            appleLocation = "normalApple.png";
            bodLocation = "normalBod.png";
            headLocation = "normalHead.png";
            setBackground(Color.cyan);
            addObjects(numOfLily, "lily.png");                                  // sends across number of lilys and image name

        }
    }
    
    private void addObjects(int num, String image)                              //Adds either lilys or pyramids. recives number of obejcts and image name
    {
        ImageIcon iI = new ImageIcon(image);
        object= iI.getImage();

        for (int i = 0; i < num; i++) {
            locateObject();
        }
    }
    
    private void locateObject()                                                 //randomly generates the positions of the lilys or pyramids
    {
        redoObject = false;

        int num = (int) (Math.random() * randAppleXValue - 3) + 3;                   
        objectXPos = ((num * unitSize));

        num = (int) (Math.random() * randAppleYValue - 3) + 6;                       
        objectYPos = ((num * unitSize));

        checkObjectTrue();
    }
    
    private void checkObjectTrue()                                              //checks if the objects co-ordinates aren't of any previously generated ones
    {
        for (int i = 0; i < numObject; i++) {                                

            if ((objectXPos == objectXArr[i]) && (objectYPos == objectYArr[i])) { //check for lily            
                redoObject = true;
            }
            
            if(difficulty.equalsIgnoreCase("Hard"))                             //check for pyramid
            {
                if ((objectXPos == objectXArr[i]) && ((objectYPos - (1 * unitSize)) == objectYArr[i])) {             
                   redoObject = true;
                }
                if (((objectXPos + (1 * unitSize)) == objectXArr[i]) && ((objectYPos - (1 * unitSize)) == objectYArr[i])) {             
                   redoObject = true;
                }
                if (((objectXPos + (1 * unitSize)) == objectXArr[i]) && (objectYPos == objectYArr[i])) {             
                   redoObject = true;
                }
            }
             
        }

        if (redoObject == true) {
            locateObject();
        } else {
            
            if (difficulty.equalsIgnoreCase("Normal"))                          //if generating lilys
            {
                objectXArr[numObject] = objectXPos;
                objectYArr[numObject] = objectYPos;
                numObject++;
            }
            else                                                                //if generating pyramids
            {
                objectXArr[numObject] = objectXPos;
                objectYArr[numObject] = objectYPos;
            
                objectXArr[(numObject + 1)] = objectXPos;
                objectYArr[(numObject + 1)] = objectYPos - (1 * unitSize);

                objectXArr[(numObject + 2)] = objectXPos + (1 * unitSize);
                objectYArr[(numObject + 2)] = objectYPos - (1 * unitSize);

                objectXArr[(numObject + 3)] = objectXPos + (1 * unitSize);
                objectYArr[(numObject + 3)] = objectYPos;

                numObject = numObject + 4;
            }
            
        }

    }
    
    private void loadSnakeImages() {                                            //Loads snake images relating to the difficulty level chosen

        ImageIcon iB = new ImageIcon(bodLocation);                              //uses image name
        unitImage = iB.getImage();
        ImageIcon iH = new ImageIcon(headLocation);
        headImage = iH.getImage();

    }
    
    private void loadAppleImages()
    {
         ImageIcon iA = new ImageIcon(appleLocation);
         appleImage = iA.getImage();
         ImageIcon iG = new ImageIcon("goldenApple.png");                       // uses image name
         goldenAppleImage = iG.getImage();
    }

    private void beginGame() {                                                  //initialise snake and generates apples

        numSnakeUnits = 8;                              

        for (int i = 0; i < numSnakeUnits; i++) {                               //generates snake with 3 units and 5 virtual units (used when reversing) in the top left hand corner
            xArr[i] = 100 - i * 10;                      
            yArr[i] = 50;
        }
        
        locateTheApple();
        
        chooseGoldenApple();

        gameTimer = new Timer(delay, this);                                     //starts game timer (the delay specifies the rate at which everyhting is processed and thus the speed of the snake)
        gameTimer.start();
    }

    private void locateTheApple() {                                             //randomly generates apple co-ordinates
        redo = false;
        int num = (int) (Math.random() * randAppleXValue);
        xPos = ((num * unitSize));

        num = (int) (Math.random() * randAppleYValue) + 4;
        yPos = ((num * unitSize));
        
        if(appleType.equalsIgnoreCase("A"))                                     //if normal apple
        {
            appleXPos = xPos;
            appleYPos = yPos;
        }
        
        if(appleType.equalsIgnoreCase("G"))                                     //if golden apple
        {
            goldenXPos = xPos;
            goldenYPos = yPos;
        }

        checkAppleTrue();
    }

    private void checkAppleTrue() {                                             //checks the apples co-ordinates to see if it clashes with either the snakes, lilys or pyramids co-ordinates
        for (int i = numSnakeUnits; i > 0; i--) {                                

            if ((xPos == xArr[i]) && (yPos == yArr[i])) {             
                redo = true;
            }

        }
        
        for (int i = 0; i < numObject; i++) {                                   //checks if the apples co-ordinates are the same as the lilys or pyramids

            if ((xPos == objectXArr[i]) && (yPos == objectYArr[i])) {             
                redo = true;
            }

        }


        if (redo == true) {
            locateTheApple();                                                   //if co-ordinates clash the apples co-ordinates are regenerated
        }
    }

    private void chooseGoldenApple() {                                          //determines the probability of generating a power up (12.5% chance of generating a power up each time a normal apple is generated)
        if (goldenStat == false) {
            int val = (int) (Math.random() * powerUpProbability);
            if (val == 1) {
                goldenStat = true;
                appleType = "G";
                locateTheApple();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {                                    //draws snake, score, objects etc... on the playfield
        super.paintComponent(g);
        draw(g, ("LIVES : " + lives), 1450, 35);                                // sends across the message heading and co-ordinates
   
        draw(g, ("SCORE : " + score), 10, 35);

        draw(g, powerMsg, 665, 35);

        if (countDown == true)                                                  //Only starts  20 second coutdown for specific powers
        {
            drawCountDown(g);
        }
        initiateDrawing(g);

    }
    
    private void draw(Graphics g, String text, int x, int y)                    //draws either the score, power or number of lives. receives the heading and co-ordinates
    {
        Font size = new Font("Helvetica", Font.BOLD, 30);
        String message = text;

        FontMetrics metric = getFontMetrics(size);

        if (difficulty.equalsIgnoreCase("easy")) {
            g.setColor(Color.white);
        } else {
            g.setColor(Color.black);
        }

        g.setFont(size);                                                                     
        g.drawString(message, (x), y);  
    }
    
    private void drawCountDown(Graphics g) {                                    //fetches and displays the countdown next to the power message on the playing field
        
        timer = t.getTime();
        
        draw(g, (timer + ""), 965, 35);
        
       if (timer == 0)
       {
           invincibility = false;
           doublePoints = false;
           countDown = false;
           powerMsg = "";
           changeSpeed(usualDelay);                                             // sends across the usual delay depending on the difficulty
         
       }

    }

    private void initiateDrawing(Graphics g) {                                  //Draws Snake and Apples

        if (inGame == true) {

            g.drawImage(appleImage, appleXPos, appleYPos, this);                //Displays apple

            if (goldenStat == true) {
                g.drawImage(goldenAppleImage, goldenXPos, goldenYPos, this);
            }
            
            if(!difficulty.equalsIgnoreCase("Easy"))
            {
                for (int i = 0; i < numObject; i++) {                           //Displays the lilys or pyramids
                g.drawImage(object, objectXArr[i], objectYArr[i], this);
                }
            }
     
            if (invincibility == true)                                          //if invincibility power is activated these are the snake images to be used
            {
                ImageIcon inH = new ImageIcon("invincibleHead.png");
                snakeHead = inH.getImage();
                ImageIcon inB = new ImageIcon("invincibleBod.png");
                snakeBod = inB.getImage();
            } 
            else
            {    
                if (doublePoints == true)                                       //if doube points is activated then these are the snake images to be used
                {
                  ImageIcon dH = new ImageIcon("doubleHead.png");
                  snakeHead = dH.getImage();
                  ImageIcon dB = new ImageIcon("doubleBod.png");
                  snakeBod = dB.getImage();
                  
                  
                  flash(snakeHead, snakeBod);                                   // sends across image names
                 
                } 
                else                                                            // used to flash the snake if the snake hits a pyramid or lily
                {
                  
                  flash(headImage, unitImage);                                  //
                }
            }
            
            for (int i = numSnakeUnits - 6; i >= 0; i--) {                      //Displays all the snake units except the head and last 5 virtual units used for reversing

                if (i == 0) {
                    g.drawImage(snakeHead, xArr[i], yArr[i], this);             //Displays the head
                } else {
                    g.drawImage(snakeBod, xArr[i], yArr[i], this);              //Displays the body units
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            endGame(g);                                                         // ends the game if inGame is equal to false
        }
        
    }
    
    private void flash(Image h, Image b)                                        //simulatanously switches between normal images and clear images to provide the appearance of flashing when needed. receives image names
    {
        if (move == false && flashCount < 10)
        {
            ImageIcon iI = new ImageIcon("invisiSnake.png");
                        
            if(flashCount % 2 == 0)
            {
                snakeHead = iI.getImage();
                snakeBod = iI.getImage();
            }
            else
            {
                snakeHead = h;
                snakeBod = b; 
            }
            flashCount++;
        }
        else
        {    
            snakeHead = h;
            snakeBod = b;
            move = true;
            
            if (flashCount >= 10)
            {
              moveSnake();
            }  
            flashCount = 2;
        } 
        
    }

    private void endGame(Graphics g) {                                          //when the player runs out of lives the game will end and the option of saving your score will be brought up

        Font size = new Font("Helvetica", Font.BOLD, 14);
        String message = "Game Over";

        ud.UpdateScore(score);

        FontMetrics metric = getFontMetrics(size);

        g.setFont(size);
        g.setColor(Color.white);                                                                     
        g.drawString(message, (gameWidth - metric.stringWidth(message)) / 2, gameHeight / 2);

        setVisible(false);

        saveScore ss = new saveScore();
        ss.setVisible(true);  
        

    }

    @Override
    public void actionPerformed(ActionEvent e) {                                //This method constantly loops with regard to the gameTimer

        if (inGame) {

            checkHitApple();
            
            if (goldenStat == true)
            {
              checkHitGoldenApple();
            }
            
            checkHit();

            if (reverse == false) {
                moveSnake();
            } else {
                reverseSnake();
                rNum++;
            }

            if (rNum > 5) {
                reverse = false;
                rNum = 0;
                changeTimer(tempDelay);
            }

        }

        repaint();
    }

    private void checkHitApple() {                                              //checks if snake's head co-ordinates match that of an apple

        if ((xArr[0] == appleXPos) && (yArr[0] == appleYPos)) {

            numSnakeUnits++;

            if (doublePoints == true) {
                score = score + 2;
            } else {
                score++;
            }

            ud.UpdateScore(score);                                              // sends across the score
            
            if(t.getTime() <= 0)                                                //Only starts regenerating golden apples if the coundown of the previous power has come to 0
            {
                 chooseGoldenApple();
            }
            
            appleType = "A";
            locateTheApple();                                                   //locates new apple
        }
    }

    private void checkHitGoldenApple() {                                        //checks if snake's head co-ordinates match that of a golden apple

        if ((xArr[0] == goldenXPos) && (yArr[0] == goldenYPos)) {
            goldenStat = false;
            powerType();                                                        //If yes; the program randomly chooses a power type
        }
    }

    private void powerType() {                                                  //choose a power ocordingly to the number generated(Invincibility, Extra life, Speed change or Double points

        powerStat = true;
        int prob = (int) (Math.random() * powerProbability);

        if (prob == 1) {
            powerMsg = "INVINCIBILITY";
            countDown = true; 
            t.startTimer();
            invincibility = true;
            doublePoints = false;
            
        } else if (prob == 2) {
            powerMsg = "EXTRA LIFE";
            lives++;
            invincibility = false;
            doublePoints = false;
        } else if (prob == 3) {
            powerMsg = "SPEED CHANGE";
            countDown = true;                                    
            t.startTimer();
            invincibility = false;
            doublePoints = false;
            
            changeSpeed(speedDelay);

        } else {
            powerMsg = "DOUBLE POINTS";
            countDown = true;         
            t.startTimer(); 
            invincibility = false;
            doublePoints = true;
        }

    }
    
    private void changeSpeed(int tempDelay)                                     //Changes game timers delay in order to change the speed of the snake. receives delay factor
    {  
        gameTimer.stop();     
        gameTimer = new Timer(tempDelay, this);
        gameTimer.start();
       
    }

    private void checkHit() {                                                   //Checks if the snake hits either itself, a lily, a pyramid or the edge of the board
        if (invincibility == false && move == true) {
            for (int i = numSnakeUnits - 5; i > 0; i--) {                           

                if ((i > 4) && (xArr[0] == xArr[i]) && (yArr[0] == yArr[i])) {
                    if (lives == 1) {
                        inGame = false;
                    } else {
                        lives--;
                        move = false;

                    }
                }
            }
            
            
            if(!difficulty.equalsIgnoreCase("Easy"))                            //Check hit Lily or pyramid
            {
                for (int i = 0; i < numObject; i++) {
                if ((xArr[0] == objectXArr[i]) && (yArr[0] == objectYArr[i])) {
                    if (lives == 1) {
                        inGame = false;
                    } else {
                       lives--;
                       move = false;
                    }

                }

            }
            }
            
     
        }

        if (yArr[0] >= gameHeight || yArr[0] < 0 || xArr[0] >= gameWidth || xArr[0] < 0) {   //Check if the snake hit the edge of the board
            if (lives == 1) {
                inGame = false;
            } else {
                reverse = true;
                if (invincibility == false) {
                    lives--;
                }
                changeTimer(250);
            }                                                    
        }


        if (inGame == false) {
            gameTimer.stop();
        }
    }
    

    private void changeTimer(int newDelay) {                                    //Changes timer speed by changing the delay factor. recieves delay factor
        tempDelay = delay;
        gameTimer.stop();
        delay = newDelay;
        gameTimer = new Timer(delay, this);
        gameTimer.start();
    }

    private void moveSnake() {                                                  //moves the snake one unit at a time in a specific direction
      if (move == true)
      {
        for (int i = numSnakeUnits; i > 0; i--) {
            xArr[i] = xArr[(i - 1)];
            yArr[i] = yArr[(i - 1)];
        }

        if (leftDirec == true) {                                               
            xArr[0] = xArr[0] - unitSize;
        }

        if (rightDirec == true) {
            xArr[0] = xArr[0] + unitSize;
        }

        if (upDirec == true) {
            yArr[0] = yArr[0] - unitSize;
        }

        if (downDirec == true) {
            yArr[0] = yArr[0] + unitSize;
        }
      }  
    }

    private void reverseSnake() {                                               //reverses the snake using the  virtual units when the snake hits the edge of the board
        for (int i = 0; i < numSnakeUnits; i++) {
            xArr[i] = xArr[(i + 1)];
            yArr[i] = yArr[(i + 1)];
        }

        if (leftDirec) {                                                
            xArr[numSnakeUnits] = xArr[numSnakeUnits] + unitSize;
        }

        if (rightDirec) {
            xArr[numSnakeUnits] = xArr[numSnakeUnits] - unitSize;
        }

        if (upDirec) {
            yArr[numSnakeUnits] = yArr[numSnakeUnits] + unitSize;
        }

        if (downDirec) {
            yArr[numSnakeUnits] = yArr[numSnakeUnits] - unitSize;
        }
    }

    private class TAdapter extends KeyAdapter {                                 //extends the class and creates a key listener

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (reverse == false) {

                if ((key == KeyEvent.VK_LEFT) && (!rightDirec)) {               // if left key is pressed
                    leftDirec = true;
                    upDirec = false;
                    downDirec = false;
                }

                if ((key == KeyEvent.VK_RIGHT) && (!leftDirec)) {               //if right key is pressed
                    rightDirec = true;
                    upDirec = false;
                    downDirec = false;
                }

                if ((key == KeyEvent.VK_UP) && (!downDirec)) {                  //if up key is pressed
                    upDirec = true;
                    rightDirec = false;
                    leftDirec = false;
                }

                if ((key == KeyEvent.VK_DOWN) && (!upDirec)) {                  //if down key is pressed
                    downDirec = true;
                    rightDirec = false;
                    leftDirec = false;
                }
            }
        }
    }
}
