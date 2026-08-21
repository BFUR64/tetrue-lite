package com.teic.trueris.game.block;

import com.teic.trueris.game.cell.CellType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@NullMarked
public class BlockRegistry2 {
    private static final Map<CellType, BlockTemplate> BLOCK_MAP = new HashMap<>();

    static {
        BLOCK_MAP.put(
            CellType.O,
            new BlockTemplate(
                2,
                new CellType[]{
                    CellType.O, CellType.O,
                    CellType.O, CellType.O
                }
            )
        );

        BLOCK_MAP.put(
            CellType.J,
            new BlockTemplate(
            3,
                new @Nullable CellType[]{
                    CellType.J, null,       null,
                    CellType.J, CellType.J, CellType.J,
                    null,       null,       null
                }
            )
        );

        BLOCK_MAP.put(
            CellType.L,
            new BlockTemplate(
            3,
                new @Nullable CellType[]{
                    null,       null,       CellType.L,
                    CellType.L, CellType.L, CellType.L,
                    null,       null,       null
                }
            )
        );

        BLOCK_MAP.put(
            CellType.S,
            new BlockTemplate(
                3,
                new @Nullable CellType[]{
                    null,       CellType.S, CellType.S,
                    CellType.S, CellType.S, null,
                    null,       null,       null
                }
            )
        );

        BLOCK_MAP.put(
            CellType.Z,
            new BlockTemplate(
                3,
                new @Nullable CellType[]{
                    CellType.Z, CellType.Z, null,
                    null,       CellType.Z, CellType.Z,
                    null,       null,       null
                }
            )
        );

        BLOCK_MAP.put(
            CellType.T,
            new BlockTemplate(
                3,
                new @Nullable CellType[]{
                    null,       CellType.T, null,
                    CellType.T, CellType.T, CellType.T,
                    null,       null,       null
                }
            )
        );

        BLOCK_MAP.put(
            CellType.I,
            new BlockTemplate(
                3,
                new @Nullable CellType[]{
                    null,       null,       null,       null,
                    CellType.I, CellType.I, CellType.I, CellType.I,
                    null,       null,       null,       null,
                    null,       null,       null,       null
                }
            )
        );
    }

    public BlockTemplate getBlock(CellType cellType) {
        return BLOCK_MAP.get(cellType);
    }
}
