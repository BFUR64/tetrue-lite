package io.github.bfur64.tetrue.game.block;

import io.github.bfur64.tetrue.Config;
import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.World;
import io.github.bfur64.tetrue.game.cell.CellType;
import io.github.bfur64.tetrue.game.component.*;
import io.github.bfur64.tetrue.game.event.GravityChangeEvent;
import io.github.bfur64.tetrue.game.timer.GravityTimer;
import io.github.bfur64.tetrue.game.timer.LockTimer;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class BlockFactory {
    private final World world;

    private int nextBlockId = 0;
    private double gravity = Config.gravityMs.get() / 1000.0;

    private final int BLOCK_X_OFFSET = (Config.gridWidth.get() / 2) - 2;

    @SuppressFBWarnings(
        value = "EI2",
        justification = "World is intentionally shared between systems."
    )
    public BlockFactory(World world, EventBus eventBus) {
        this.world = world;

        eventBus.subscribe(GravityChangeEvent.class, event -> gravity = event.gravity());
    }

    public int createBagBlock(CellType cell) {
        int id = nextBlockId++;

        world.put(id, new Shape(BlockRegistry.getBlock(cell)));

        return id;
    }

    public int createGhostBlock(int parentId) {
        int id = nextBlockId++;

        Rotation rotation = world.get(parentId, Rotation.class);
        Shape shape = world.get(parentId, Shape.class);

        world.put(id, new Position(0, 0));
        world.put(id, new Rotation(rotation.direction()));
        world.put(id, new Shape(BlockRegistry.getBlock(shape.blockTemplate().cellType())));
        world.put(id, new IsGhost(parentId));

        return id;
    }

    public void convertBagBlockToBlock(int entityId) {
        convertToBlock(entityId);
    }

    public void convertBlockToHeldBlock(int entityId) {
        world.remove(entityId, Position.class);
        world.remove(entityId, Rotation.class);
        world.remove(entityId, OnGround.class);
        world.remove(entityId, HasGhost.class);
        world.remove(entityId, GravityTimer.class);
        world.remove(entityId, LockTimer.class);

        world.put(entityId, new Held());
    }

    public void convertHeldBlockToBlock(int entityId) {
        convertToBlock(entityId);

        world.remove(entityId, Held.class);
    }

    public void convertToBlock(int entityId) {
        world.put(entityId, new Position(BLOCK_X_OFFSET, 0));
        world.put(entityId, new Rotation(Direction.UP));
        world.put(entityId, new OnGround(false));
        world.put(entityId, new HasGhost(null));
        world.put(entityId, new GravityTimer(gravity));
        world.put(entityId, new LockTimer(gravity));
    }
}
