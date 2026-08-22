package com.teic.trueris.game;

import com.teic.trueris.game.block.BlockFactory;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.system.BlockMovementSystem;
import com.teic.trueris.game.system.CollisionSystem;
import com.teic.trueris.game.system.GravitySystem;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class GameManager2 {
    private final World world;
    private final EventBus eventBus;

    private final BlockFactory blockFactory;

    private final GravitySystem gravitySystem;
    private final BlockMovementSystem blockMovementSystem;
    private final CollisionSystem collisionSystem;

    private Integer activeBlockId;

    public GameManager2(World world) {
        this.world = world;
        this.eventBus = new EventBus();
        this.blockFactory = new BlockFactory(world);

        this.gravitySystem = new GravitySystem(world, eventBus);
        this.blockMovementSystem = new BlockMovementSystem(world, eventBus);
        this.collisionSystem = new CollisionSystem(world, eventBus);

        activeBlockId = blockFactory.createBlock(CellType.I);
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
