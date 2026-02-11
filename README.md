<h1 align="center">Tetrue Lite</h1>

<h3 align="center">An open-source Tetris clone for the terminal, built in Java using the Lanterna UI library</h3>

<div align="center">
  <img width="300" alt="Screenshot of Tetrue Gameplay" src="https://github.com/user-attachments/assets/1031cdde-156b-4a25-b269-d328dd1aa010"/>
</div>

## Demo 
https://github.com/user-attachments/assets/381943ea-5c4d-4108-a197-6eff122c388b

## Features
- Falling Blocks - Standard falling tetrominoes
- Gravity - Drops move down per second
- Lock Grace - Allows adjustment before it gets placed
- Rotation - Rotate blocks clockwise and counterclockwise
- Movement - Move blocks left and right
- Hard Drop - Instantly drop a tetromino to the bottom row
- Scoring - Tracks points for cleared lines
- Block Queueing (7 Bag System) - Next pieces preview and fair randomization
- Mobile Friendly Controls - Works on termux

## Limitations
- No game / score tracking after the game is closed
- No configurable settings
- Minimal main menu only, featuring `New Game`, `About`, and `Exit`
- No sound

## Environment Issues
- Designed and tested primarily on Termux (Android)
- Unix-like environments only (Linux, MacOs)
- BLOCK_SPARSE (or any gradient based) character renders as dots on Termux, but may appear as gradients or shading on other terminal emulators
- **Windows is not supported**

## Usage / Controls
- UP Arrow Key - Hard drop
- DOWN Arrow Key - Move tetromino down
- LEFT / RIGHT Arrow Keys - Move tetromino left / right within the grid
- END Key - Rotate tetromino clockwise
- HOME Key - Rotate tetromino counter-clockwise

In the main menu, press `0` to exit the application <br>
In the game, press `ESC` to exit the game

## Architecture Overview

### Core Loop
<img width="2739" height="1659" alt="Core Loop" src="https://github.com/user-attachments/assets/cf28050a-2e21-4941-8370-798e4852de55" />

### Rendering Layer
<img width="1482" height="582" alt="View Loop" src="https://github.com/user-attachments/assets/f45ce001-bbcd-4825-93e0-0ac6556540c8" />



## Installation / Running (TERMUX ONLY)
TODO: Step by step instructions assuming a fully working environment with proper build tools and the like already installed

## Tech Stack
- Programming Language: Java 21 (Adoptium OpenJDK 21.0.9)
- Libraries: Lanterna 3.1.3 (Terminal UI)
- Build Tools: Gradle 9.3.1

## Development Environment
Built on Termux Neovim on Android, because I found it more convenient than my laptop (Ability to work on the go). Plus, Lanterna seems to fair well under Unix-like environments unlike Windows, so...

## Why I Built This
After 1.5 years of endless architecturing the 'next best' architecture for the project, I realize my honeymoon phase had to end. It doesn't ship. It only promises.

Tetrue Lite is the v7, with all the unnecessary abstractions / over-engineering gutted or removed, with the focus of delivering an MVP, e.g., an actual playable game.

It was a brutal slap in reality when I realized this.
