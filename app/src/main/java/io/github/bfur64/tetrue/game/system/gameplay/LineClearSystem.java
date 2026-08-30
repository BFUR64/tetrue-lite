package io.github.bfur64.tetrue.game.system.gameplay;

import io.github.bfur64.tetrue.Config;
import io.github.bfur64.tetrue.game.EventBus;
import io.github.bfur64.tetrue.game.cell.CellType;
import io.github.bfur64.tetrue.game.event.BlockPlaceEvent;
import io.github.bfur64.tetrue.game.event.LineClearEvent;
import io.github.bfur64.tetrue.game.grid.GridData;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class LineClearSystem {
    private final GridData gridData;
    private final EventBus eventBus;

    @SuppressFBWarnings(
        value = "EI2",
        justification = "World and EventBus is intentionally shared between systems."
    )
    public LineClearSystem(GridData gridData, EventBus eventBus) {
        this.gridData = gridData;
        this.eventBus = eventBus;

        eventBus.subscribe(BlockPlaceEvent.class, event -> clearFilledRows());
    }

    public void clearFilledRows() {
        clearFilledRows(returnFilledRows());
    }

    private void clearFilledRows(boolean[] filledRows) {
        boolean hasFilled = false;
        for (int row = 0; row < filledRows.length; row++) {
            if (!filledRows[row]) continue;

            hasFilled = true;
            shiftSolidGridRowFrom(row);
        }

        if (hasFilled) {
            clearFirstRow();
            eventBus.publish(new LineClearEvent(filledRows));
        }
    }

    private boolean[] returnFilledRows() {
        int totalGridRow = Config.gridHeight.get();
        boolean[] filledRows = new boolean[totalGridRow];

        for (int row = 0; row < totalGridRow; row++) {
            boolean isEmpty = false;
            for (int col = 0; col < Config.gridWidth.get(); col++) {
                CellType cell = gridData.getCell(col, row);

                if (cell != null) continue;

                isEmpty = true;
                break;
            }

            if (isEmpty) continue;

            filledRows[row] = true;
        }

        return filledRows;
    }

    private void shiftSolidGridRowFrom(int rowStart) {
        for (int row = rowStart; row > 0; row--) {
            for (int col = 0; col < Config.gridWidth.get(); col++) {
                CellType cell = gridData.getCell(col, row - 1);
                gridData.setCell(col, row, cell);
            }
        }
    }

    private void clearFirstRow() {
        for (int col = 0; col < Config.gridWidth.get(); col++) {
            gridData.setCell(col, 0, null);
        }
    }
}
