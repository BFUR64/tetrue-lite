<h1 align="center">Tetrue Lite</h1>

<h3 align="center">An open-source Tetris clone for the terminal, built in Java using the Lanterna UI library</h3>
<div align="center">
  <img width="300" alt="Screenshot of Tetrue Gameplay" src="https://github.com/user-attachments/assets/1031cdde-156b-4a25-b269-d328dd1aa010"/>
</div>

## Demo 
- TODO: GIF or MP4 showing basic gameplay and launching from Termux

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
TODO: Screenshot or a PNG from draw.io with a high-level overview

## Installation / Running (TERMUX ONLY)
TODO: Step by step instructions assuming a fully working environment with proper build tools and the like already installed

## Tech Stack
- Java Programming Language (Adoptium OpenJDK 21.0.9)
- Lanterna Library (3.1.3)

## Development Environment
Built on Termux Neovim on Android, because I found it more convenient than my laptop (Ability to work on the go). Plus, Lanterna seems to fair well under Unix-like environments unlike Windows, so...

## Why I Built This
After 1.5 years of endless architecturing the 'next best' architecture for the project, I realize my honeymoon phase had to end. It doesn't ship. It only promises.

Tetrue Lite is the v7, with all the unnecessary abstractions / over-engineering gutted or removed, with the focus of delivering an MVP, e.g., an actual playable game.

It was a brutal slap in reality when I realized this.
