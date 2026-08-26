package com.teic.trueris.game;

import com.teic.trueris.game.block.BlockFactory;
import com.teic.trueris.game.event.BlockSpawnEvent;
import com.teic.trueris.game.event.BlockSwitchEvent;
import com.teic.trueris.game.grid.GridData;
import com.teic.trueris.game.system.lifecycle.BlockHoldSystem;
import com.teic.trueris.game.system.lifecycle.BlockSpawnSystem;
import com.teic.trueris.game.system.gameplay.GameOverSystem;
import com.teic.trueris.game.system.gameplay.LineClearSystem;
import com.teic.trueris.game.system.gameplay.ScoreTrackerSystem;
import com.teic.trueris.game.system.movement.*;
import com.teic.trueris.game.system.lifecycle.BlockPlaceSystem;
import com.teic.trueris.game.system.lifecycle.BlockQueueSystem;
import com.teic.trueris.game.system.presentation.GhostBlockSystem;
import com.teic.trueris.game.system.presentation.SoundSystem;
import com.teic.trueris.game.system.timing.GravityTimerSystem;
import com.teic.trueris.game.system.timing.LockTimerSystem;
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

    private Integer activeBlockId;

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
