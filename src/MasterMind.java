import java.io.IOException;
import swiftbot.*;
import java.util.Collections;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

////../Users/ismaililmadhoun/eclipse-workspace/MasterMind/src directory 
//ssh pi@192.168.50.71
//scp MasterMind.java  pi@192.168.50.98:/home/pi/Documents to compile the code
//java -cp SwiftBotAPI-5.1.0.jar: MasterMind.java to run the code

//import javax.imageio.ImageIO;

public class MasterMind 
{



    public static void main(String[] args) throws IOException {
    	
        MastermindGame mastermindGame = new MastermindGame();
        mastermindGame.startGame();
        
    	
    }
}

class MastermindGame 
{
    private SwiftBotAPI API;
    private Scanner reader;
    private int playerScore, computerScore;
    private boolean playerWinner; 
    private boolean isButtonAPressed;
    private boolean isButtonBPressed;
    private boolean isButtonXPressed;
    private boolean isButtonYPressed;
    private boolean rulesDisplayed = false;


    
    
    public MastermindGame() 
    {
        API = new SwiftBotAPI();
        reader = new Scanner(System.in);
        playerScore = 0; //when starting the game the scores are 0-0
        computerScore = 0;
    }

    public void startGame() throws IOException, NumberFormatException 
    {
        
        while (true) 
        {
        	if(!rulesDisplayed)//rules displayed is in the "if" because i dont want to print the same rules again and again when an input in invalid.
        	{
                // Display rules only if they haven't been displayed yet
                printMastermind();
                printColoredBorder("Default Mode Rules", "\u001B[34m"); // blue color
                System.out.println("");
                DefaultModeRules();//rules
                printColoredBorder("Customized Mode Rules", "\u001B[35m"); // pink color
                System.out.println("");//line down
                CustomizedModeRules();// rules
                printColoredBorder("Press [" + colorText("A", "\u001B[32m") + "] for Default Mode", "\u001B[36m");
                printColoredBorder("Press [" + colorText("B", "\u001B[33m") + "] for Customized Mode", "\u001B[37m");
                rulesDisplayed = true; // Set the flag to true once rules are displayed
            //String mode = reader.next();
        	}
            waitForButtonPress();//watitng for a button to be pressed with a status of true or false.. if pressed its true...
            
            if (isButtonAPressed == true)
            {
                printColoredBorder(" Default Mode Starting", "\u001B[34m"); //  dark blue color

                MastermindRound round = new MastermindRound(API, isButtonAPressed ? 4 : 0, isButtonAPressed ? 12 : 0);
                round.setMastermindGame(this); // Pass the current instance of MastermindGame to MastermindRound
                round.playRound();
            } else if (isButtonBPressed == true)
            {
            	
                printColoredBorder(" Customized Game Starting", "\u001B[35m"); // Purple color
                int numColors = 0;
                int maxGuesses = 0;
                // Customized Mode: Choose the number of colours and maximum guesses
                while (true) //in a loop just incase of invalid inputs giving the player infinite chances...
                {
                    try {
                        System.out.println("Enter the number of colours in the code (3 - 6): ");
                        numColors = Integer.parseInt(reader.next());

                        if (numColors < 3 || numColors > 6) 
                        {
                            throw new IllegalArgumentException("Number of colours must be between 3 and 6.");
                        }

                        System.out.println("Enter the maximum number of guesses allowed: ");
                        maxGuesses = Integer.parseInt(reader.next());

                        if (maxGuesses <= 0) 
                        {
                            throw new IllegalArgumentException("Maximum guesses must be greater than 0.");
                        }

                        break; // Break the loop if input is valid
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid input. " + e.getMessage() + " Please try again.");
                    }
                }

                // Customized Mode: Start the game with user-defined settings
                MastermindRound round = new MastermindRound(API, numColors, maxGuesses);// after taking inputs it applies it to the round rules..
                round.setMastermindGame(this);
                round.playRound();
            }else{
            	
                System.out.println(colorText("Invalid input. Please enter 'A' or 'B'.", "\u001B[31m")); // Red color
                continue; // Ask the user for new input
                
            }

                if (playerWinner) 
                {  // Check player's victory status from MastermindGame
                    printColoredBorder("You have cracked the code!", "\u001B[36m"); //  light blue color
                    System.out.println("\u001B[31m __   __                                \u001B[0m");
                    System.out.println("\u001B[32m\\ \\ / /__  _   _  __      _____  _ __  \u001B[0m");
                    System.out.println("\u001B[33m \\ V / _ \\| | | | \\ \\ /\\ / / _ \\| '_ \\ \u001B[0m");// you won
                    System.out.println("\u001B[34m  | | (_) | |_| |  \\ V  V / (_) | | | |\u001B[0m");
                    System.out.println("\u001B[35m  |_|\\___/ \\__,_|   \\_/\\_/ \\___/|_| |_|\u001B[0m");
                    } else {
                    incrementComputerScore();
                    System.out.println("\u001B[31m __   __            _           _   \u001B[0m");
                    System.out.println("\u001B[32m\\ \\ / /__  _   _  | | ___  ___| |_ \u001B[0m");
                    System.out.println("\u001B[33m \\ V / _ \\| | | | | |/ _ \\/ __| __|\u001B[0m");//you lsot
                    System.out.println("\u001B[34m  | | (_) | |_| | | | (_) \\__ \\ |_ \u001B[0m");
                    System.out.println("\u001B[35m  |_|\\___/ \\__,_| |_|\\___/|___/\\__|\u001B[0m");
                    }
                displayScores();//displayes scores of the round...

                if (!askToPlayAgain()) //true of player wants to play again
                {
                    break; // indicates that the player wants to stop playing and breaks the loop...
                }else {
                	rulesDisplayed = false; // if the player wants to continue playing it will display the rules again...
                }
            }
        }
    private void printColoredBorder(String title, String colorCode) // takes parametes of what title and changes the oclours ...x(a, b)
    {
        System.out.println(colorCode + "*******************************");
        System.out.println("* " + title);
        System.out.println("*******************************\u001B[0m"); // Reset color
    }

    private String colorText(String text, String colorCode) // takes a text and changes its colour 
    {
        return colorCode + text + "\u001B[0m"; // Reset color
    }
    private void printMastermind()//prints the display mastermind ... 
    {
        System.out.println("\u001B[31m __  __           _            __  __ _           _ \u001B[0m");
        System.out.println("\u001B[32m|  \\/  | __ _ ___| |_ ___ _ __|  \\/  (_)_ __   __| |\u001B[0m");
        System.out.println("\u001B[33m| |\\/| |/ _` / __| __/ _ \\ '__| |\\/| | | '_ \\ / _` |\u001B[0m");
        System.out.println("\u001B[34m| |  | | (_| \\__ \\ ||  __/ |  | |  | | | | | | (_| |\u001B[0m");
        System.out.println("\u001B[35m|_|  |_|\\__,_|___/\\__\\___|_|  |_|  |_|_|_| |_|\\__,_|\u001B[0m");
        System.out.println();
    }
    private void waitForButtonPress() // turns true when a button is pressed so then its buttons could be manipulated 
    {
        isButtonAPressed = false;
        isButtonBPressed = false;
        isButtonXPressed = false;
        isButtonYPressed = false;

        // Disable existing functions on buttons
        API.disableButton(Button.A);
        API.disableButton(Button.B);
        API.disableButton(Button.X);
        API.disableButton(Button.Y);

        // Enable new functions on buttons
        API.enableButton(Button.A, () -> {
            // Set the flag to true when Button A is pressed
            isButtonAPressed = true;
        });
        API.enableButton(Button.B, () -> {
            // Set the flag to true when Button B is pressed
            isButtonBPressed = true;
        });
        API.enableButton(Button.X, () -> {
            // set the flag to true when Button X is pressed
            isButtonXPressed = true;
        });
        API.enableButton(Button.Y, () -> {
            // set the flag to true when Button Y is pressed
            isButtonYPressed = true;
        });

        // wait for button A, B, X, or Y to be pressed
        while (!isButtonAPressed && !isButtonBPressed && !isButtonXPressed && !isButtonYPressed) {
            // add a delay to avoid excessive cpu usage in the loop
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {//general exception
                e.printStackTrace();
            }
        }
    }
    private boolean askToPlayAgain()  // asks the player if they want to play again by changes their inputs after running thorugh waitforbuttons method
    {
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Do you want to play again? Press 'Y' to continue or 'X' to quit.");
            waitForButtonPress();

            if (isButtonXPressed) {
                System.out.println("Thanks for playing! Goodbye.");
                return false; // Indicate that the player wants to quit
            } else if (isButtonYPressed) {
                return true; // Indicate that the player wants to play again
            } else {
                System.out.println("\u001B[31mInvalid input! Please press 'Y' or 'X'.\u001B[0m");
                // Player entered an invalid input, loop to ask again
            }
        }

        return false; // shoudlnt reach here ... 
    } 
    public void setPlayerWinner(boolean playerWinner)  // sets the player as winner when playerwinner is true
    {
        this.playerWinner = playerWinner;
    }          


     private void DefaultModeRules() 
     {//displays the default mode rules // remind to add timer between each line...
        String rules = "\u001B[33mThe Swiftbot will prompt you to hold the first colour card in front of the camera. Once the photo is taken, it will ask for the second card, and so on, until all four color cards are scanned.\u001B[0m\n\n" +
                "The Swiftbot will randomly choose four colours from a range of six - " +
                "\u001B[31mRed\u001B[37m, " +
                "\u001B[32mGreen\u001B[37m, " +
                "\u001B[34mBlue\u001B[37m, " +
                "\u001B[31mOrange\u001B[37m, " +
                "\u001B[33mYellow\u001B[37m\u001B[0m\u001B[37m, and " +
                "\u001B[35mPink\u001B[37m. The colors will not repeat in the code.\n" +
                "\u001B[37mThe program will display the overall player vs computer score after each game. For example, if you win a game, the score will be updated to 1-0.\n" +
                "You have \u001B[31m12 guesses\u001B[0m in each round. If you can't guess the code within these attempts, the Swiftbot will reveal the code, and you'll lose the game.\n\n" +
                "\u001B[34mAfter each guess, the Swiftbot will provide feedback using '+' and '-' symbols. '+' means the color is correct and in the right position, while '-' means the color is correct but in the wrong position.\u001B[0m\n\n" +
                "\u001B[33mThe '+' symbol will appear in the feedback first, making it challenging for you to know exactly what colors you got wrong and right!.\u001B[0m\n\n" +
                "\u001B[32mIf you guess the correct code, you win!\u001B[0m";

        System.out.println(rules);
    }
     
     public void displayPlayerHealth(int playerHealth) // displayes players health when in customized mode
     {

         System.out.println("Player Health: " + playerHealth);
     }
     
     private void CustomizedModeRules() // cutomized mode rules
     {
    	    String rules =
    	                   "\u001B[33m\u001B[0m \u001B[37mUnlike Default Mode, here you can customize the game settings at the start of the game.\u001B[0m\n" +
    	                   "\u001B[33mTry to guess the secret code with the fewest attempts!\u001B[0m\n" +
    	                   "\u001B[33mPlayer health is calculated based on guesses, colors range, and code colors.\u001B[0m";

    	    System.out.println(rules);
    	}
     public void incrementPlayerScore() 
     {
         playerScore++;
     }

     public void incrementComputerScore()
     {
         computerScore++;
     }
     public void displayScores() 
     {
         System.out.println("Player Score: " + playerScore);
         System.out.println("Computer Score: " + computerScore);
     }
}

class MastermindRound { // calss of the round .. contains most algorithms
    private ArrayList<String> code;
    private SwiftBotAPI swiftbot;
    private int codeLength;
    private int maxTries;
    private int currentTry;
    private Scanner nextRound;
    private MastermindGame mastermindGame;
    private boolean playerWinner;
    
    
    public void setMastermindGame(MastermindGame mastermindGame) 
    {
        this.mastermindGame = mastermindGame;
    }

    public MastermindRound(SwiftBotAPI swiftbot, int codeLength, int maxTries) {
        this.swiftbot = swiftbot;
        this.codeLength = codeLength;
        this.code = generateCode();
        this.maxTries = maxTries;
        this.currentTry = 1;
        nextRound = new Scanner(System.in);
        this.playerWinner = false;
    }


    public void playRound() 
    {
        while (currentTry <= maxTries) 
        {
            System.out.println("\u001B[36m*************************"); // Cyan color
            System.out.println("* Attempt number: " + currentTry + " *"); // Cyan color
            System.out.println("*************************\u001B[0m"); // Reset color      
        	ArrayList<String> guess = getGuess();

            if (isCodeGuessed(guess)) 
            {
                DisplayFeedback(guess);
                mastermindGame.setPlayerWinner(true);
                mastermindGame.incrementPlayerScore();
                break;  // End the round immediately after correct guess
            }
            DisplayFeedback(guess);
            // Check if the user wants to restart or continue the round
            if (!askToContinueRound() && currentTry + 1 == maxTries) // +1 is because the maxTries is always ahead of currentTry by one.. 
            {
                break;
            }

        }
        int playerHealth = maxTries - currentTry + 1 + codeLength + 6; // the 6 represents the range of colours in the range which are (red, green, blue, yell0w, orange and pink).
        mastermindGame.displayPlayerHealth(playerHealth);
        System.out.println("The secret code was: "+ code);


    }
    
    private boolean askToContinueRound() // asks the user if they want to have their upcoming guesses .. 
    //makes sure that the player is ready before inputing colours 
    {

    	System.out.println("Press '\u001B[32m1\u001B[0m' to go again or '\u001B[31m0\u001B[0m' to restart the round");
        System.out.println("Your choice: ");
        String userChoice = nextRound.next();
        if (userChoice.equals("0")) {
            currentTry = 1; // restart the round without counting as an attempt
            System.out.println("Restarting the round... Let's do this!");
            return false; // indicate that the player wants to restart the round
        } else if (userChoice.equals("1")) {
            currentTry++; // continue with the next attempt
            return true; // indicate that the player wants to continue the round
        } else {
            System.out.println("\u001B[31mInvalid input! Please press '1' or '0'.\u001B[0m");
            return askToContinueRound(); // Ask again in case of invalid input
        }
    }

    public boolean isPlayerWinner() // player is winner when playerwinner is true (goes into MasterMind class)
    {
        return playerWinner;
    }
    private boolean isCodeGuessed(ArrayList<String> guess) 
    {
        return Arrays.equals(guess.toArray(), code.toArray());
    }

    private ArrayList<String> generateCode() 
    {
        ArrayList<String> range = new ArrayList<>();
        range.add("R");
        range.add("G");
        range.add("B");
        range.add("Y");
        range.add("O");
        range.add("P");
        Collections.shuffle(range);
        ArrayList<String> code = new ArrayList<>();
        for(int i = 0; i < codeLength; i++)
        {
        	code.add(range.get(i));
        }
        return code;
    }

    private ArrayList<String> getGuess() 
    { // gets the guess of the user by taking values from detected colour and adding them into the guess AL
        ArrayList<String> guess = new ArrayList<String>();
        String displayGuess = "";
        int i = 1;
        while (i <= codeLength) 
        {
            System.out.println("Hold colour card " + i + " in front of the camera");
            System.out.println("-----------------------------------------");

            printTimer();
            String detectedColor = detectColour(swiftbot);
            guess.add(detectedColor);
            displayGuess += guess.get(i-1);
            // also is print because it prints all the guess as a code.. RBGY
            System.out.println("\u001B[33mYour guess is:\u001B[0m " + displayGuess);//prints the value of the players guess.. .get(i-1) is because of the while loop's i.
            i++;
        }
        return guess;
    }

    private void DisplayFeedback(ArrayList<String> guess) 
    {
            String feedback = generateFeedback(guess);
            if (feedback.isEmpty()) 
            {
                System.out.println("\u001B[31mNo feedback generated. Please review your guess.\u001B[0m"); //red colour 
            } else {
                System.out.println("\u001B[32mFeedback:\u001B[0m " + feedback); // green colour
            }
    }
        private String generateFeedback(ArrayList<String> guess) 
        {
            StringBuilder feedback = new StringBuilder(); // uses StringBuilder because its more efficient than normal string since it modifies its contents without adding new objects each time
            boolean[] matched = new boolean[codeLength]; // To keep track of matched positions

            // Check for correct positions, theres two since i want to add the + symbols first...
            for (int i = 0; i < codeLength; i++) 
            {
                if (guess.get(i).equals(code.get(i)))
                {
                    feedback.append('+');
                    matched[i] = true;
                }
            }

            // Check for correct colors but in the wrong position, 
            for (int i = 0; i < codeLength; i++) {
                if (!matched[i] && code.contains(guess.get(i))) {
                    feedback.append('-');
                }
            }

            return feedback.toString();
        }

    private void printTimer() 
    { // displays a countdown of 5 seconds (for the player to align his card in time.)
        int seconds = 5;
        while (seconds > 0)
        {
        	System.out.print("\rTaking image in " + seconds + " seconds"); //the The \r carriage return character is used to move the cursor to the beginning of the current line. This allows the countdown message to overwrite the previous one on the same line in the console.
            try {
                Thread.sleep(1000); // Sleep for (1 second)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            seconds--;
        }
        System.out.println(); // Move to the next line after the countdown is complete
    }
    private String detectColour(SwiftBotAPI swiftbot) 
    {
      //takes an image and gets the average rgb values (average pixel) and uses the functions isRed,isGreen... to detect the colour
    int sumR=0,sumG=0,sumB=0;
    int avgR,avgG,avgB;
    try {
    BufferedImage img = swiftbot.takeStill(ImageSize.SQUARE_48x48);
    if(img == null)
    {
      System.out.println("ERROR: Image is null");
      System.exit(5);
    }else{
      ImageIO.write(img, "png", new File("/home/pi/colourImage.png"));
      for(int x = 0;x<img.getWidth();++x) 
      {
        for(int y = 0; y < img.getHeight();++y)
        {
      int p = img.getRGB(x,y);

      int r = (p >> 16) & 0xFF;
      int g = (p >> 8) & 0xFF; // turns values into rgb number system 
      int b = p & 0xFF;
      sumR += r;
      sumG += g;
      sumB += b;
     }
      }
      //gets the average rgb values of the whole image 
      int totalPixels = img.getWidth()*img.getHeight(); 
      avgR = sumR/totalPixels;
      avgG = sumG/totalPixels;
      avgB = sumB/totalPixels;
          if (isYellow(avgR, avgG, avgB)) {
              return "Y";
          } else if (isPink(avgR, avgG, avgB)) {
              return "P";
          } else if (isOrange(avgR, avgG, avgB)) {
              return "O";
          } else if (isGreen(avgR, avgG, avgB)) {
              return "G";
          } else if (isBlue(avgR, avgG, avgB)) {
              return "B";
          } else if (isRed(avgR, avgG, avgB)) {
              return "R";
          } else {
              return "Unknown";
          }

       }
    }  
    catch(Exception e)//error handling
    {
        System.out.println("\nCamera not enabled!");
        System.out.println("Try running the following command: ");
        System.out.println("sudo raspi-config nonint do_camera 0\n");
        System.out.println("Then reboot using the following command: ");
        System.out.println("sudo reboot\n");
        System.exit(5);
    }
    return null;
    }
//checks for each colour that has been tested whether the values hold.. thresholding...
    private static boolean isYellow(int red, int green, int blue) 
    {
        return ((red > 100 && red < 255) && (green > 110 && green < 255) && blue < 130);
    }


    private static boolean isPink(int red, int green, int blue) 
    {
        return ((red > 95 && red < 255) && green < 20 && (blue > 135 && blue < 255));
    }

    private static boolean isOrange(int red, int green, int blue) {
        return ((red > 85 && red < 255) && (green < 125 && green > 20) && blue < 60);
    }

    private static boolean isBlue(int red, int green, int blue) 
    {
        return (red < 10 && green  <  40 && blue > 50 );
    }

    private static boolean isGreen(int red, int green, int blue) 
    {
        return (red < 100 && green > 130 && blue < 50);
    }

    private static boolean isRed(int red, int green, int blue)
    {  
        return (red > 70 && green < 20 && blue < 40);
    }

} 