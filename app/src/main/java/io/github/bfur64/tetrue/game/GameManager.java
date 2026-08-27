package io.github.bfur64.tetrue.game;

import io.github.bfur64.tetrue.game.block.BlockFactory;
import io.github.bfur64.tetrue.game.event.BlockSpawnEvent;
import io.github.bfur64.tetrue.game.event.BlockSwitchEvent;
import io.github.bfur64.tetrue.game.grid.GridData;
import io.github.bfur64.tetrue.game.system.lifecycle.BlockHoldSystem;
import io.github.bfur64.tetrue.game.system.lifecycle.BlockSpawnSystem;
import io.github.bfur64.tetrue.game.system.gameplay.GameOverSystem;
import io.github.bfur64.tetrue.game.system.gameplay.LineClearSystem;
import io.github.bfur64.tetrue.game.system.gameplay.ScoreTrackerSystem;
import io.github.bfur64.tetrue.game.system.movement.*;
import io.github.bfur64.tetrue.game.system.lifecycle.BlockPlaceSystem;
import io.github.bfur64.tetrue.game.system.lifecycle.BlockQueueSystem;
import io.github.bfur64.tetrue.game.system.presentation.GhostBlockSystem;
import io.github.bfur64.tetrue.game.system.presentation.SoundSystem;
import io.github.bfur64.tetrue.game.system.timing.GravityTimerSystem;
import io.github.bfur64.tetrue.game.system.timing.LockTimerSystem;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GameManager {
    private final GravityTimerSystem gravityTimerSystem;
    private final OnGroundSystem onGroundSystem;
    private final LockTimerSystem lockTimerSystem;
    private final BlockMovementSystem blockMovementSystem;
    private final BlockRotationSystem blockRotationSystem;
    private final BlockHoldSystem blockHoldSystem;
    private final GhostBlockSystem ghostBlockSystem;

    private int activeBlockId;

    public GameManager(World world, EventBus eventBus, GridData gridData) {
        // Initialize Movement Systems
        this.gravityTimerSystem = new GravityTimerSystem(world, eventBus);
        this.onGroundSystem = new OnGroundSystem(world, eventBus);
        this.lockTimerSystem = new LockTimerSystem(world, eventBus);

        this.blockMovementSystem = new BlockMovementSystem(world, eventBus);
        this.blockRotationSystem = new BlockRotationSystem(world, eventBus);

        // Initialize Block Systems
        BlockFactory blockFactory = new BlockFactory(world, eventBus);

        new CollisionSystem(gridData, world, eventBus);
        new BlockPlaceSystem(gridData, world, eventBus);

        BlockQueueSystem blockQueueSystem = new BlockQueueSystem(blockFactory, eventBus);

        this.blockHoldSystem = new BlockHoldSystem(blockFactory, eventBus);

        this.ghostBlockSystem = new GhostBlockSystem(blockFactory, world, eventBus);

        BlockSpawnSystem blockSpawnSystem = new BlockSpawnSystem(blockQueueSystem, world, eventBus);

        // Initialize Game Systems
        new LineClearSystem(gridData, eventBus);
        new ScoreTrackerSystem(eventBus);
        new GameOverSystem(eventBus);

        // Register Events
        eventBus.subscribe(BlockSpawnEvent.class, event -> activeBlockId = event.entityId());
        eventBus.subscribe(BlockSwitchEvent.class, event -> activeBlockId = event.entityId());

        // Start Game
        this.activeBlockId = blockSpawnSystem.spawnBlock();

        new SoundSystem(eventBus);
    }

    public void update(long delta) {
        onGroundSystem.update();
        gravityTimerSystem.update(delta);
        lockTimerSystem.update(delta);
        ghostBlockSystem.update();
    }

    public void moveBlockDown() {
        blockMovementSystem.moveBlockDown(activeBlockId);
    }

    public void dropBlock() {
        blockMovementSystem.dropBlock(activeBlockId);
    }

    public void moveBlockLeft() {
        blockMovementSystem.moveBlockLeft(activeBlockId);
    }

    public void moveBlockRight() {
        blockMovementSystem.moveBlockRight(activeBlockId);
    }

    public void rotateBlockLeft() {
        blockRotationSystem.rotateLeft(activeBlockId);
    }

    public void rotateBlockRight() {
        blockRotationSystem.rotateRight(activeBlockId);
    }

    public void rotate180() {
        blockRotationSystem.rotate180(activeBlockId);
    }

    public void holdBlock() {
        blockHoldSystem.holdBlock(activeBlockId);
    }
}
