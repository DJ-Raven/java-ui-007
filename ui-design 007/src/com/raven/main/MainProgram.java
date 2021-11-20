package com.raven.main;

import com.raven.swing.slideshow.Slideshow;

public class MainProgram {

    private static MainProgram instance;

    private Slideshow slideShow;

    public static MainProgram getInstance() {
        if (instance == null) {
            instance = new MainProgram();
        }
        return instance;
    }

    private MainProgram() {
    }

    public Slideshow getSlideShow() {
        return slideShow;
    }

    public void setSlideShow(Slideshow slideShow) {
        this.slideShow = slideShow;
    }
}
