package com.teic.trueris.game;

import com.teic.trueris.game.block.BlockFactory;
import com.teic.trueris.game.event.BlockSpawnEvent;
import com.teic.trueris.game.grid.GridData2;
import com.teic.trueris.game.system.*;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GameManager2 {
    private final World world;
    private final EventBus eventBus;

    private final BlockFactory blockFactory;

    private final GravitySystem gravitySystem;
    private final BlockMovementSystem blockMovementSystem;
    private final CollisionSystem collisionSystem;
    private final BlockPlaceSystem gridSystem;
    private final BlockSpawnSystem blockSpawnSystem;

    private Integer activeBlockId;

    public GameManager2(World world, GridData2 gridData) {
        this.world = world;
        this.eventBus = new EventBus();
        this.blockFactory = new BlockFactory(world);

        this.gravitySystem = new GravitySystem(world, eventBus);
        this.blockMovementSystem = new BlockMovementSystem(world, eventBus);
        this.collisionSystem = new CollisionSystem(world, eventBus);
        this.gridSystem = new BlockPlaceSystem(gridData, world, eventBus);
        this.blockSpawnSystem = new BlockSpawnSystem(blockFactory, world, eventBus);

        eventBus.subscribe(BlockSpawnEvent.class, event -> {
            activeBlockId = event.entityId();
        });

        this.activeBlockId = blockSpawnSystem.spawnBlock();
    }

    public void update(long delta) {
        gravitySystem.update(delta);
    }

    public void moveBlockDown() {
        blockMovementSystem.moveBlockDown(activeBlockId);
    }

    public void moveBlockLeft() {
        blockMovementSystem.moveBlockLeft(activeBlockId);
    }

    public void moveBlockRight() {
        blockMovementSystem.moveBlockRight(activeBlockId);
    }
}
