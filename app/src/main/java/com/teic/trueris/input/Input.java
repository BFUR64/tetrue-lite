package com.teic.trueris.input;

import org.jspecify.annotations.NonNull;

import java.io.Closeable;

public interface Input extends Closeable {

    @NonNull
    Key readInput();

    @NonNull
    Key pollInput();
}
