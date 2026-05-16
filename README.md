<h1 align="center">Tetrue Lite</h1>

<h3 align="center">An open-source Tetris clone for the terminal, built in Java using the Tetrue Terminal API</h3>

<div align="center">
  <img width="726" height="523" alt="image" src="https://github.com/user-attachments/assets/f4197ad3-7d01-472a-9ae7-a3ff4feb6d16" />
</div>

## Demo 
https://github.com/user-attachments/assets/e46d20ce-e5b8-4c25-82fe-f63627509c34

## Features
- Falling Blocks - Standard falling tetrominoes
- Gravity - Drops move down per second
- Lock Grace - Allows adjustment before it gets placed
- Rotation - Rotate blocks clockwise and counterclockwise
- Movement - Move blocks left and right
- Hard Drop - Instantly drop a tetromino to the bottom row
- Scoring - Tracks points for cleared lines
- Block Queueing (7 Bag System) - Next pieces preview and fair randomization

## Limitations
- No game / score tracking after the game is closed
- Inaccurate block rotation (Not the same as modern Tetris)
- Inaccurate scoring / gravity
- No gravity speedup when scoring
- No configurable settings
- Minimal main menu only, featuring `New Game`, `About`, and `Exit`
- No sound

## Environment
- Designed and tested primarily on Linux, Windows, and Termux (Android)

## Usage / Controls
- UP Arrow Key - Hard drop
- DOWN Arrow Key - Move tetromino down
- LEFT / RIGHT Arrow Keys - Move tetromino left / right within the grid
- Q Key - Rotate tetromino clockwise
- E Key - Rotate tetromino counter-clockwise

In the main menu, press `0` or `ESC` twice to exit the application <br>
In the game, press `ESC` twice to exit the game

## Installation / Running

### Clone the repository
```bash
git clone git@github.com:BFUR64/tetrue-lite.git
cd tetrue-lite
```

### Build the shadow JAR
#### Windows
```bash
./gradlew build
```

#### Linux / Termux
```bash
sh gradlew build
```

### Run the generated JAR
```bash
java -jar --enable-native-access=ALL-UNNAMED app/build/libs/app-all.jar
```

## Architecture Overview (OUTDATED SINCE v2.0.4)

### Core Loop
<img width="2739" height="1659" alt="Core Loop" src="https://github.com/user-attachments/assets/cf28050a-2e21-4941-8370-798e4852de55" />

### Rendering Layer
<img width="1482" height="582" alt="View Loop" src="https://github.com/user-attachments/assets/f45ce001-bbcd-4825-93e0-0ac6556540c8" />

[Tetrue Lite Game Design Document](https://docs.google.com/document/d/19IZbQ8FgqG-YYqf-uAuDHytYcsIJh5Mo0PuvelidNwE/edit?usp=sharing)

## Tech Stack
- Programming Language: Java 21 (Adoptium OpenJDK 21.0.11)
- Libraries:
  - [Tetrue Terminal](https://github.com/BFUR64/tetrue-terminal) 1.2.3 (Personal Dual Engine Library UI for Termux and Everything Else)
  - [Menu Manager](https://github.com/BFUR64/menu-manager) 0.2.2 (Personal Composite-based Menu Management)
- Build Tools: Gradle 9.3.1

## Development Environment
Originally built on Termux Neovim on Android, because I found it more convenient than my laptop (Ability to work on the go).

Now, it's mostly tested and built on my laptop ever since the move to JLine 3. I still use Termux to ssh into the laptop and code every now and then if I'm on the go.

## Why I Built This (v1 Tetrue Lite)
After 1.5 years of endless architecturing the 'next best' architecture for the project, I realize my honeymoon phase had to end. It doesn't ship. It only promises.

Tetrue Lite is the v7, with all the unnecessary abstractions / over-engineering gutted or removed, with the focus of delivering an MVP, e.g., an actual playable game.

It was a brutal slap in reality when I realized this.
