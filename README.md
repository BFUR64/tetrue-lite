<h1 align="center">Tetrue Lite</h1>

<h3 align="center">An open-source Tetris clone for the terminal, built in Java using the Lanterna UI library</h3>

## Features
- Falling Blocks - Standard falling tetrominoes
- Gravity - Drops move down per second
- Rotation - Rotate blocks clockwise and counterclockwise
- Movement - Move blocks left and right
- Hard Drop - Instantly drop a tetromino to the bottom row
- Scoring - Tracks points for cleared lines
- Block Queueing (7 Bag System) - Next pieces preview and fair randomization
- Mobile Friendly Controls - Works on termux

## Limitations
- Limited to Unix-like environments involving the tty (Terminal)
- No game / score tracking after the game is closed
- No configurable settings
- Minimal main menu only, featuring `New Game`, `About`, and `Exit`
- No sound

## Usage / Controls
- UP Arrow Key - Hard drop
- DOWN Arrow Key - Move tetromino down
- LEFT / RIGHT Arrow Keys - Move tetromino left / right within the grid
- END Key - Rotate tetromino clockwise
- HOME Key - Rotate tetromino counter-clockwise

In the main menu, press `0` to exit the application <br>
In the game, press `ESC` to exit the game

## Architecture Overview
TODO: Coming Soon

## Installation / Running (TERMUX ONLY)
TODO: Coming Soon

## Tech Stack
- Java Programming Language (Adoptium OpenJDK 21.0.9)
- Lanterna Library (3.1.3)
