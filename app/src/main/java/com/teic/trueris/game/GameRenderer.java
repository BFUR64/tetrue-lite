package com.teic.trueris.game;

import com.teic.trueris.Config;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.cell.Color;
import com.teic.trueris.game.component.*;
import com.teic.trueris.game.event.BlockQueueChangeEvent;
import com.teic.trueris.game.event.ScoreChangeEvent;
import com.teic.trueris.game.grid.GridReader;
import com.teic.trueris.game.system.RotationSystem;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.output.TextColor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

import static com.teic.trueris.game.utils.CellGrid.getCell;

@NullMarked
public class GameRenderer {
    private static final String SOLID = "█";
    private static final String GHOST = "░";

    private static final int BORDER_OFFSET = 1;
    private static final int BORDER_PADDING = 2;

    private static final int GAME_BORDER_WIDTH = Config.gridWidth.get() + BORDER_PADDING;
    private static final int GAME_BORDER_HEIGHT = Config.gridHeight.get() + BORDER_PADDING;

    private final Terminal terminal;
    private final World world;
    private final GridReader gridReader;

    private int score;
    private List<Integer> blockQueueIds = new LinkedList<>();

    @SuppressFBWarnings(
        value = "EI2",
        justification = "Terminal and World is intentionally shared between systems."
    )
    public GameRenderer(Terminal terminal, World world, GridReader gridReader, EventBus eventBus) {
        this.terminal = terminal;
        this.world = world;
        this.gridReader = gridReader;

        eventBus.subscribe(ScoreChangeEvent.class, event -> score = event.score());
        eventBus.subscribe(BlockQueueChangeEvent.class, event -> blockQueueIds = event.entityIds());
    }

    public void update(long delta) {
        terminal.clear();

        writeBorder(GAME_BORDER_WIDTH, GAME_BORDER_HEIGHT);

        writeGhostBlocks();
        writeActiveBlocks();
        writeLockedCells();

        int leftPadding = GAME_BORDER_WIDTH + 1;
        putString(leftPadding, 1, "Score: " + score);

        int heldBlockY = 5;
        writeHeldBlock(leftPadding, heldBlockY);

        int textQueueY = 8;
        putString(leftPadding, textQueueY, "Next");
        writeBlockQueue(leftPadding + 2, textQueueY + 2);

        // Optional
        if (Config.showDebug.get()) {
            showDebug(delta);
        }

        terminal.flush();
    }

    private void showDebug(long delta) {
        int offset = 23;
        terminal.put(0, offset, "FPS: " + Math.round(1_000_000_000.0d / delta));
        terminal.put(0, ++offset, "Entities: " + world.query());
    }

    private void writeGhostBlocks() {
        List<Integer> ghostIds = world.query(Position.class, Rotation.class, Shape.class, IsGhost.class);

        for (Integer ghostId : ghostIds) {
            Position position = world.get(ghostId, Position.class);
            Rotation rotation = world.get(ghostId, Rotation.class);
            Shape shape = world.get(ghostId, Shape.class);

            int direction = rotation.direction().ordinal();

            List<@Nullable CellType> rotatedCells = RotationSystem.rotateBlockNTimes(direction, shape.blockTemplate());

            writeBlock(
                position.x() + BORDER_OFFSET,
                position.y() + BORDER_OFFSET,
                shape.blockTemplate().size(),
                rotatedCells,
                GHOST
            );
        }
    }

    private void writeActiveBlocks() {
        List<Integer> blockIds = world.query(Position.class, Rotation.class, Shape.class);

        for (Integer blockId : blockIds) {
            if (world.has(blockId, IsGhost.class)) continue;

            Position position = world.get(blockId, Position.class);
            Rotation rotation = world.get(blockId, Rotation.class);
            Shape shape = world.get(blockId, Shape.class);

            int direction = rotation.direction().ordinal();

            List<@Nullable CellType> rotatedCells = RotationSystem.rotateBlockNTimes(direction, shape.blockTemplate());

            writeBlock(
                position.x() + BORDER_OFFSET,
                position.y() + BORDER_OFFSET,
                shape.blockTemplate().size(),
                rotatedCells,
                SOLID
            );
        }
    }

    private void writeLockedCells() {
        for (int row = 0; row < Config.gridHeight.get(); row++) {
            for (int col = 0; col < Config.gridWidth.get(); col++) {
                CellType cell = gridReader.getCell(col, row);
                int rowOffset = row + BORDER_OFFSET;
                int colOffset = col + BORDER_OFFSET;

                if (cell != null) {
                    putCell(colOffset, rowOffset, SOLID, cell.color());
                }
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void writeBorder(int xSize, int ySize) {
        for (int row = 0; row < ySize; row++) {
            for (int col = 0; col < xSize; col++) {
                if (
                    row == 0 || row == ySize - 1 ||
                    col == 0 || col == xSize - 1
                ) {
                    putCell(col, row, SOLID, Color.GREY);
                }
            }
        }
    }

    private void writeHeldBlock(int x, int y) {
        String holdName = "Held";
        putString(x, y, holdName);

        List<Integer> entityIds = world.query(Shape.class, Held.class);
        if (entityIds.isEmpty()) return;

        Integer heldBlockId = entityIds.getFirst();

        Shape shape = world.get(heldBlockId, Shape.class);

        writeBlock(
            x + holdName.length() + 1,
            y,
            shape.blockTemplate().size(),
            shape.blockTemplate().cells(),
            SOLID
        );

    }
    private void writeBlockQueue(int x, int rowPointer) {
        int blocksShown = 3;

        int counter = 0;
        for (Integer blockQueueId : blockQueueIds) {
            if (world.has(blockQueueId, Shape.class) && counter < blocksShown) {
                Shape shape = world.get(blockQueueId, Shape.class);

                int size = shape.blockTemplate().size();

                writeBlock(
                    x,
                    rowPointer,
                    size,
                    shape.blockTemplate().cells(),
                    SOLID
                );

                int topPadding = 1;
                rowPointer += size + topPadding;
            }

            counter++;
        }
    }

    // =====================
    // Screen Drawing
    // =====================
    private void putCell(int col, int row, String out, Color color) {
        int colOffset = col * 2;

        int[] textColor = getTextColor(color);
        terminal.setFg(textColor[0], textColor[1], textColor[2]);

        terminal.put(colOffset, row, out);
        terminal.put(colOffset + 1, row, out);

        terminal.reset();
    }

    private void putString(int col, int row, String out) {
        int colOffset = col * 2;

        char[] charArray = out.toCharArray();

        terminal.setFg(TextColor.WHITE);

        for (int pointer = 0; pointer < charArray.length; pointer++) {
            terminal.put(colOffset + pointer * 2, row, String.valueOf(charArray[pointer]));
        }

        terminal.reset();
    }

    // =====================
    // Helper Methods
    // =====================
    private void writeBlock(int colStart, int rowStart, int width, List<@Nullable CellType> cells, String out) {
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < width; col++) {
                CellType cell = getCell(cells, width, col, row);
                if (cell != null) {
                    putCell(col + colStart, row + rowStart, out, cell.color());
                }
            }
        }
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