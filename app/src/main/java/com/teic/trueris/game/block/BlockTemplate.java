package com.teic.trueris.game.block;

import com.teic.trueris.game.cell.CellType;
import org.jspecify.annotations.Nullable;

public record BlockTemplate(int size, @Nullable CellType[] cells) {}
