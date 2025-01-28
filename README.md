# MasterMind Game

## Introduction
MasterMind is a color-based guessing game where the player must deduce the secret code chosen by the computer within a set number of guesses. The player can play in **Default Mode** or **Customized Mode** with flexible settings.

## Features
- Play in **Default Mode** or **Customized Mode**.
- Use buttons for input and gameplay.
- Color-based feedback (+ for correct color and position, - for correct color but wrong position).
- Track player and computer scores across multiple rounds.
- Adjustable difficulty in **Customized Mode** (number of colors and guesses).
- Visually enhanced with color outputs in the console.

## Installation

1. Clone or download the project files.
2. Ensure you have the SwiftBot API library set up and added to your project.
3. Compile the code using the following command:
    ```bash
    javac -cp SwiftBotAPI-5.1.0.jar: MasterMind.java
    ```
4. Run the game with the following command:
    ```bash
    java -cp SwiftBotAPI-5.1.0.jar: MasterMind
    ```

## Game Modes

### Default Mode
- The Swiftbot prompts the player to hold color cards in front of the camera.
- The player has **12 attempts** to guess the secret code, which consists of **4 colors** chosen randomly from 6 available colors (Red, Green, Blue, Orange, Yellow, and Pink).
- Feedback after each guess will be given as `+` for correct position and color, and `-` for correct color but wrong position.

### Customized Mode
- In this mode, the player can customize:
  - Number of colors in the code (between 3 and 6).
  - Maximum number of guesses allowed.
- Player health is calculated based on attempts, number of colors, and the range of colors.

## Game Flow
1. The player is prompted to choose between Default or Customized Mode by pressing the respective button.
2. If in Default Mode, the player proceeds with a fixed setup (4 colors, 12 attempts).
3. If in Customized Mode, the player can input custom settings before starting.
4. After each guess, feedback is given in terms of `+` and `-` symbols.
5. If the player guesses the code correctly within the allowed attempts, they win. Otherwise, the game shows the correct code.
6. The player can choose to play again after each round.

## Controls
- **Button A**: Select Default Mode.
- **Button B**: Select Customized Mode.
- **Button X**: Exit the game.
- **Button Y**: Play again after the round.

## Example Run

Default Mode Starting
Hold color card 1 in front of the camera Hold color card 2 in front of the camera Hold color card 3 in front of the camera Hold color card 4 in front of the camera Your guess is: RGBY Feedback: ++--

The secret code was: RGBY You have cracked the code! Player Score: 1 Computer Score: 0 Do you want to play again? Press 'Y' to continue or 'X' to quit.

markdown
Copy
Edit

## Rules

### Default Mode Rules:
- The Swiftbot will ask you to scan color cards one by one.
- You have **12 guesses** to guess a 4-color code.
- Feedback is provided after each guess using `+` and `-`.

### Customized Mode Rules:
- You can customize the game at the start (number of colors and maximum guesses).
- Player health is calculated dynamically based on the settings.

## Developer
This game was developed as part of a project using the Swiftbot API. If you have any questions or suggestions, feel free to reach out.

---

**Enjoy playing MasterMind!**
