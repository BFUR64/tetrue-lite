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

    public void convertBagBlockToBlock(Integer id) {
        world.put(id, new Position(0, 0));
        world.put(id, new Rotation(Direction.UP));
        world.put(id, new OnGround(false));
        world.put(id, new GravityTimer(Duration.ofMillis(Config.gravity.get()).toNanos()));
        world.put(id, new LockTimer(Duration.ofMillis(Config.lockTimer.get()).toNanos()));
    }
}
