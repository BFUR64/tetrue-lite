package com.teic.trueris.display;

import java.io.Closeable;
import java.io.IOException;

public interface Renderer extends Closeable {
    void clearScreen();
    void putString(int x, int y, String out);
    void flush();

    void setForegroundColor(int r, int g, int b);
    void setBackgroundColor(int r, int g, int b);
    void resetColorAndStyle();

    @Override
    void close() throws IOException;
}
