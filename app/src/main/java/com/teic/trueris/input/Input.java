package com.teic.trueris.input;

import java.io.Closeable;

public interface Input extends Closeable {
    Key readInput();
    Key pollInput();
}
