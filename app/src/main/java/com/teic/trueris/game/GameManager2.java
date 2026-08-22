package com.teic.trueris.game;

import com.teic.trueris.game.block.BlockFactory;
import com.teic.trueris.game.event.BlockSpawnEvent;
import com.teic.trueris.game.grid.GridData2;
import com.teic.trueris.game.system.*;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GameManager2 {
    private final GravitySystem gravitySystem;
    private final OnGroundSystem onGroundSystem;
    private final LockTimerSystem lockTimerSystem;
    private final BlockMovementSystem blockMovementSystem;

    private Integer activeBlockId;

    public GameManager2(World world, EventBus eventBus, GridData2 gridData) {
        BlockFactory blockFactory = new BlockFactory(world);

        this.gravitySystem = new GravitySystem(world, eventBus);
        this.onGroundSystem = new OnGroundSystem(world, eventBus);
        this.lockTimerSystem = new LockTimerSystem(world, eventBus);

        this.blockMovementSystem = new BlockMovementSystem(world, eventBus);
        new CollisionSystem(gridData, world, eventBus);
        new BlockPlaceSystem(gridData, world, eventBus);
        BlockSpawnSystem blockSpawnSystem = new BlockSpawnSystem(blockFactory, world, eventBus);

        new LineClearSystem(gridData, eventBus);

        new GameOverSystem(eventBus);

        eventBus.subscribe(BlockSpawnEvent.class, event -> activeBlockId = event.entityId());

        this.activeBlockId = blockSpawnSystem.spawnBlock();
    }

    public void update(long delta) {
        onGroundSystem.update();
        gravitySystem.update(delta);
        lockTimerSystem.update(delta);
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
}
