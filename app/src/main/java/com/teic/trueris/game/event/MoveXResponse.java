package com.teic.trueris.game.event;

public record MoveXResponse(Integer entityId, boolean isValid, int dx) {}
