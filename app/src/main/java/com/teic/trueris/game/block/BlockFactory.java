package com.teic.trueris.game.block;

import com.teic.trueris.game.World;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.component.Rotation;
import com.teic.trueris.game.component.Shape;

public class BlockFactory {
    private final World world;

    private int nextBlockId = 0;

    public BlockFactory(World world) {
        this.world = world;
    }

    public Integer createBlock(CellType cell) {
        int id = nextBlockId++;

        world.add(id, new Position(0, 0));
        world.add(id, new Rotation(Direction.UP));
        world.add(id, new Shape(BlockRegistry2.getBlock(cell)));

        return id;
    }
}
