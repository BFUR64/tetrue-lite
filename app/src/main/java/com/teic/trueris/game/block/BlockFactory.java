package com.teic.trueris.game.block;

import com.teic.trueris.Config;
import com.teic.trueris.game.World;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.component.*;
import com.teic.trueris.game.event.timer.GravityTimer;
import com.teic.trueris.game.event.timer.LockTimer;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

@NullMarked
public class BlockFactory {
    private final World world;

    private int nextBlockId = 0;

    public BlockFactory(World world) {
        this.world = world;
    }

    public Integer createBagBlock(CellType cell, int position) {
        int id = nextBlockId++;

        world.put(id, new Shape(BlockRegistry2.getBlock(cell)));
        world.put(id, new BagPosition(position));

        return id;
    }

    public Integer createGhostBlock(Integer parentId) {
        int id = nextBlockId++;

        if (world.has(parentId, Position.class, Rotation.class, Shape.class, HasGhost.class)) {
            Rotation rotation = world.get(parentId, Rotation.class);
            Shape shape = world.get(parentId, Shape.class);

            world.put(id, new Position(0, 0));
            world.put(id, new Rotation(rotation.direction()));
            world.put(id, new Shape(BlockRegistry2.getBlock(shape.blockTemplate().cellType())));
            world.put(id, new IsGhost(parentId));
        }

        return id;
    }

    public void convertBagBlockToBlock(Integer entityId) {
        convertToBlock(entityId);
        world.remove(entityId, BagPosition.class);
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
        world.put(entityId, new Position(0, 0));
        world.put(entityId, new Rotation(Direction.UP));
        world.put(entityId, new OnGround(false));
        world.put(entityId, new HasGhost(null));
        world.put(entityId, new GravityTimer(Duration.ofMillis(Config.gravity.get()).toNanos()));
        world.put(entityId, new LockTimer(Duration.ofMillis(Config.lockTimer.get()).toNanos()));
    }
}
