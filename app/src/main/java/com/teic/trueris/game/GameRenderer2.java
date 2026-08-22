package com.teic.trueris.game;

import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.cell.Color;
import com.teic.trueris.game.component.Position;
import com.teic.trueris.game.component.Rotation;
import com.teic.trueris.game.component.Shape;
import com.teic.trueris.game.system.RotationSystem;
import io.github.bfur64.terminal.Terminal;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

import static com.teic.trueris.game.utils.CellGrid.getCell;

@NullMarked
public class GameRenderer2 {
    private final Terminal terminal;
    private final World world;

    public GameRenderer2(Terminal terminal, World world) {
        this.terminal = terminal;
        this.world = world;
    }

    public void update(long delta) {
        terminal.clear();

        List<Integer> blocks = world.query(Position.class, Rotation.class, Shape.class);

        for (Integer block : blocks) {
            Position position = world.get(block, Position.class);
            Rotation rotation = world.get(block, Rotation.class);
            Shape shape = world.get(block, Shape.class);

            int direction = rotation.direction().ordinal();

            List<@Nullable CellType> rotatedCells = RotationSystem.rotateBlockNTimes(direction, shape.blockTemplate());

            writeBlock(position.x(), position.y(), shape.blockTemplate().size(), rotatedCells, "█");
        }
    }

    private void writeBlock(int colStart, int rowStart, int width, List<@Nullable CellType> cells, String out) {
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < width; col++) {
                CellType cell = getCell(cells, width, row, col);
                if (cell != null) {
                    putCell(col + colStart, row + rowStart, out, cell.color());
                }
            }
        }
    }

    private void putCell(int col, int row, String out, Color color) {
        int colOffset = col * 2;

        int[] textColor = getTextColor(color);
        terminal.setFg(textColor[0], textColor[1], textColor[2]);

        terminal.put(colOffset, row, out);
        terminal.put(colOffset + 1, row, out);

        terminal.reset();
    }

    private int[] getTextColor(Color color) {
        return switch (color) {
            case DEFAULT -> new int[]{0, 0, 0};
            case GREY -> new int[]{96, 96, 96};
            case YELLOW -> new int[]{205, 205, 0};
            case BLUE -> new int[]{0, 0, 205};
            case ORANGE -> new int[]{205, 102, 0};
            case GREEN -> new int[]{0, 205, 0};
            case RED -> new int[]{205, 0, 0};
            case PURPLE -> new int[]{154, 0, 205};
            case CYAN -> new int[]{0, 205, 205};
            case WHITE -> new int[]{255, 255, 255};
        };
    }
}