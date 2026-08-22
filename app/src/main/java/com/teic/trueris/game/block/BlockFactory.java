package com.teic.trueris.game.block;

import com.teic.trueris.Config;
import com.teic.trueris.game.World;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.component.OnGround;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.component.Rotation;
import com.teic.trueris.game.component.Shape;
import com.teic.trueris.game.event.GravityTimer;
import com.teic.trueris.game.event.LockTimer;
import org.jspecify.annotations.NullMarked;

import java.time.Duration;

@NullMarked
public class BlockFactory {
    private final World world;

    private int nextBlockId = 0;

    public BlockFactory(World world) {
        this.world = world;
    }

    public Integer createBlock(CellType cell) {
        int id = nextBlockId++;

        world.put(id, new Position(0, 0));
        world.put(id, new Rotation(Direction.UP));
        world.put(id, new Shape(BlockRegistry2.getBlock(cell)));
        world.put(id, new OnGround(false));
        world.put(id, new GravityTimer(Duration.ofMillis(Config.gravity.get()).toNanos()));
        world.put(id, new LockTimer(Duration.ofMillis(Config.lockTimer.get()).toNanos()));

        return id;
    }
}
