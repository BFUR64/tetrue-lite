package com.teic.trueris.game;

import com.teic.trueris.game.block.BlockFactory;
import com.teic.trueris.game.event.BlockSpawnEvent;
import com.teic.trueris.game.event.BlockSwitchEvent;
import com.teic.trueris.game.grid.GridData2;
import com.teic.trueris.game.system.*;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GameManager2 {
    private final GravitySystem gravitySystem;
    private final OnGroundSystem onGroundSystem;
    private final LockTimerSystem lockTimerSystem;
    private final BlockMovementSystem blockMovementSystem;
    private final BlockRotationSystem blockRotationSystem;
    private final BlockHoldSystem blockHoldSystem;
    private final GhostBlockSystem ghostBlockSystem;

    private Integer activeBlockId;

    public GameManager2(World world, EventBus eventBus, GridData2 gridData) {

        this.gravitySystem = new GravitySystem(world, eventBus);
        this.onGroundSystem = new OnGroundSystem(world, eventBus);
        this.lockTimerSystem = new LockTimerSystem(world, eventBus);

        this.blockMovementSystem = new BlockMovementSystem(world, eventBus);
        this.blockRotationSystem = new BlockRotationSystem(world, eventBus);

        new CollisionSystem(gridData, world, eventBus);
        new BlockPlaceSystem(gridData, world, eventBus);

        BlockFactory blockFactory = new BlockFactory(world);
        SevenBagSystem sevenBagSystem = new SevenBagSystem(blockFactory);
        BlockSpawnSystem blockSpawnSystem = new BlockSpawnSystem(sevenBagSystem, world, eventBus);
        this.blockHoldSystem = new BlockHoldSystem(blockFactory, world, eventBus);

        this.ghostBlockSystem = new GhostBlockSystem(blockFactory, world, eventBus);

        new LineClearSystem(gridData, eventBus);
        new ScoreTrackerSystem(eventBus);

        new GameOverSystem(eventBus);

        eventBus.subscribe(BlockSpawnEvent.class, event -> activeBlockId = event.entityId());
        eventBus.subscribe(BlockSwitchEvent.class, event -> activeBlockId = event.entityId());

        this.activeBlockId = blockSpawnSystem.spawnBlock();
    }

    public void update(long delta) {
        onGroundSystem.update();
        gravitySystem.update(delta);
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

    public void holdBlock() {
        blockHoldSystem.holdBlock(activeBlockId);
    }
}
