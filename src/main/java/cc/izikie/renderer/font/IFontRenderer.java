package cc.izikie.renderer.font;

import java.awt.*;

public interface IFontRenderer {
    void drawText(String text, int x, int y, Color color);
    void drawText(String text, int x, int y, int color);

    void drawTextShadow(String text, int x, int y, Color color);
    void drawTextShadow(String text, int x, int y, int color);

    int getWidth(String text);
    int getHeight(String text);
}