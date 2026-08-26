package com.teic.trueris.game.query.rotation;

import com.teic.trueris.game.block.Direction;
import com.teic.trueris.game.utils.Offset;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public record MoveRotationQuery(Integer entityId, Direction direction, List<Offset> offsets) {}
