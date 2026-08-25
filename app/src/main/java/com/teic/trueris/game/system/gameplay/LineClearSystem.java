package com.teic.trueris.game.system.gameplay;

import com.teic.trueris.Config;
import com.teic.trueris.game.EventBus;
import com.teic.trueris.game.cell.CellType;
import com.teic.trueris.game.event.BlockPlaceEvent;
import com.teic.trueris.game.event.LineClearEvent;
import com.teic.trueris.game.grid.GridData;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class LineClearSystem {
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
