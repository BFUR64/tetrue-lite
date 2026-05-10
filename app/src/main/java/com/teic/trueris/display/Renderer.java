package com.teic.trueris.display;

import java.io.Closeable;
import java.io.IOException;

public interface Renderer extends Closeable {
    void clearScreen();
    void putString(int x, int y, String out);
    void flush();

    @Override
    void close() throws IOException;
}
