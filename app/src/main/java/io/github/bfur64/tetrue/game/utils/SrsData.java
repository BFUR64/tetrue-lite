package io.github.bfur64.tetrue.game.utils;

import io.github.bfur64.tetrue.game.block.Direction;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Map;

@NullMarked
public final class SrsData {
    private static final Map<RotationPair, List<Offset>> threeTable = Map.of(
        new RotationPair(Direction.UP, Direction.RIGHT),
        List.of(
            new Offset(0, 0),
            new Offset(-1, 0),
            new Offset(-1, -1),
            new Offset(0, 2),
            new Offset(-1, 2)
        ),

        new RotationPair(Direction.RIGHT, Direction.UP),
        List.of(
            new Offset(0, 0),
            new Offset(1, 0),
            new Offset(1, 1),
            new Offset(0, -2),
            new Offset(1, -2)
        ),

        new RotationPair(Direction.RIGHT, Direction.DOWN),
        List.of(
            new Offset(0, 0),
            new Offset(1, 0),
            new Offset(1, 1),
            new Offset(0, -2),
            new Offset(1, -2)
        ),

        new RotationPair(Direction.DOWN, Direction.RIGHT),
        List.of(
            new Offset(0, 0),
            new Offset(-1, 0),
            new Offset(-1, -1),
            new Offset(0, 2),
            new Offset(-1, 2)
        ),

        new RotationPair(Direction.DOWN, Direction.LEFT),
        List.of(
            new Offset(0, 0),
            new Offset(1, 0),
            new Offset(1, -1),
            new Offset(0, 2),
            new Offset(1, 2)
        ),

        new RotationPair(Direction.LEFT, Direction.DOWN),
        List.of(
            new Offset(0, 0),
            new Offset(-1, 0),
            new Offset(-1, 1),
            new Offset(0, -2),
            new Offset(-1, -2)
        ),

        new RotationPair(Direction.LEFT, Direction.UP),
        List.of(
            new Offset(0, 0),
            new Offset(-1, 0),
            new Offset(-1, 1),
            new Offset(0, -2),
            new Offset(-1, -2)
        ),

        new RotationPair(Direction.UP, Direction.LEFT),
        List.of(
            new Offset(0, 0),
            new Offset(1, 0),
            new Offset(1, -1),
            new Offset(0, 2),
            new Offset(1, 2)
        )
    );

    private static final Map<RotationPair, List<Offset>> iTable = Map.of(
        new RotationPair(Direction.UP, Direction.RIGHT),
        List.of(
            new Offset(0, 0),
            new Offset(-2, 0),
            new Offset(1, 0),
            new Offset(-2, 1),
            new Offset(1, -2)
        ),

        new RotationPair(Direction.RIGHT, Direction.UP),
        List.of(
            new Offset(0, 0),
            new Offset(2, 0),
            new Offset(-1, 0),
            new Offset(2, -1),
            new Offset(-1, 2)
        ),

        new RotationPair(Direction.RIGHT, Direction.DOWN),
        List.of(
            new Offset(0, 0),
            new Offset(-1, 0),
            new Offset(2, 0),
            new Offset(-1, -2),
            new Offset(2, 1)
        ),

        new RotationPair(Direction.DOWN, Direction.RIGHT),
        List.of(
            new Offset(0, 0),
            new Offset(1, 0),
            new Offset(-2, 0),
            new Offset(1, 2),
            new Offset(-2, -1)
        ),

        new RotationPair(Direction.DOWN, Direction.LEFT),
        List.of(
            new Offset(0, 0),
            new Offset(2, 0),
            new Offset(-1, 0),
            new Offset(2, -1),
            new Offset(-1, 2)
        ),

        new RotationPair(Direction.LEFT, Direction.DOWN),
        List.of(
            new Offset(0, 0),
            new Offset(-2, 0),
            new Offset(1, 0),
            new Offset(-2, 1),
            new Offset(1, -2)
        ),

        new RotationPair(Direction.LEFT, Direction.UP),
        List.of(
            new Offset(0, 0),
            new Offset(1, 0),
            new Offset(-2, 0),
            new Offset(1, 2),
            new Offset(-2, -1)
        ),

        new RotationPair(Direction.UP, Direction.LEFT),
        List.of(
            new Offset(0, 0),
            new Offset(-1, 0),
            new Offset(2, 0),
            new Offset(-1, -2),
            new Offset(2, 1)
        )
    );

    public static Map<RotationPair, List<Offset>> getThreeTable() {
        return threeTable;
    }

    public static Map<RotationPair, List<Offset>> getITable() {
        return iTable;
    }
}