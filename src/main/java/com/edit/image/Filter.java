package com.edit.image;

import java.util.function.Function;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Filter implements Function<Image, Image>{

    String name;
    private Function<Color, Color> colorMap;

    Filter(String name, Function<Color, Color> colorMap) {
        this.name = name;
        this.colorMap = colorMap;
    }

    @Override
    public Image apply(Image source) {
        int w = (int) source.getWidth();
        int h = (int) source.getHeight();

        WritableImage image = new WritableImage(w, h);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Color c1 = source.getPixelReader().getColor(x, y);
                Color c2 = colorMap.apply(c1);

                image.getPixelWriter().setColor(x, y, c2);
            }
        }

        return image;
    }
}
