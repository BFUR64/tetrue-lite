package com.teic.trueris.game.event;

public record MoveBlockCommand(Integer entityId, int dx, int dy) {}
