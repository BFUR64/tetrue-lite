package com.teic.trueris.game.block;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.World;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.component.*;
import com.teic.trueris.game.event.GravityChangeEvent;
import com.teic.trueris.game.event.timer.GravityTimer;
import com.teic.trueris.game.event.timer.LockTimer;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

@NullMarked
public class BlockFactory {
    private final World world;

    private int nextBlockId = 0;
    private int gravity = Config.gravity.get();

    private final int BLOCK_X_OFFSET = (Config.gridWidth.get() / 2) - 2;

    @SuppressFBWarnings(
        value = "EI2",
        justification = "World is intentionally shared between systems."
    )
    public BlockFactory(World world, EventBus eventBus) {
        this.world = world;

        eventBus.subscribe(GravityChangeEvent.class, event -> gravity = event.gravity());
    }

    public Integer createBagBlock(CellType cell) {
        int id = nextBlockId++;

        world.put(id, new Shape(BlockRegistry.getBlock(cell)));

        return id;
    }

    public Integer createGhostBlock(Integer parentId) {
        int id = nextBlockId++;

        if (world.has(parentId, Position.class, Rotation.class, Shape.class, HasGhost.class)) {
            Rotation rotation = world.get(parentId, Rotation.class);
            Shape shape = world.get(parentId, Shape.class);

            world.put(id, new Position(BLOCK_X_OFFSET, 0));
            world.put(id, new Rotation(rotation.direction()));
            world.put(id, new Shape(BlockRegistry.getBlock(shape.blockTemplate().cellType())));
            world.put(id, new IsGhost(parentId));
        }

        return id;
    }

    public void convertBagBlockToBlock(Integer entityId) {
        convertToBlock(entityId);
    }

    public void convertBlockToHeldBlock(Integer entityId) {
        world.remove(entityId, Position.class);
        world.remove(entityId, Rotation.class);
        world.remove(entityId, OnGround.class);
        world.remove(entityId, HasGhost.class);
        world.remove(entityId, GravityTimer.class);
        world.remove(entityId, LockTimer.class);

        world.put(entityId, new Held());
    }

    public void convertHeldBlockToBlock(Integer entityId) {
        convertToBlock(entityId);

        world.remove(entityId, Held.class);
    }

    public void convertToBlock(Integer entityId) {
        world.put(entityId, new Position(BLOCK_X_OFFSET, 0));
        world.put(entityId, new Rotation(Direction.UP));
        world.put(entityId, new OnGround(false));
        world.put(entityId, new HasGhost(null));
        world.put(entityId, new GravityTimer(Duration.ofMillis(gravity).toNanos()));
        world.put(entityId, new LockTimer(Duration.ofMillis(Config.lockTimer.get()).toNanos()));
    }
}
