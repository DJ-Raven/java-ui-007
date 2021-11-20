package com.raven.swing.slideshow;

import java.awt.Component;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

public class Slideshow extends JLayeredPane {

    private final JPanel panel;
    private final Animator animator;
    private final MigLayout layout;
    private Component componentShow;
    private Component componentOut;
    private int currentIndex;
    private boolean next;

    public Slideshow() {
        setOpaque(false);
        layout = new MigLayout("inset 0");
        panel = new JPanel();
        panel.setOpaque(false);
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void begin() {
                componentShow.setVisible(true);
                componentOut.setVisible(true);
            }

            @Override
            public void timingEvent(float fraction) {
                double width = panel.getWidth();
                int location = (int) (width * fraction);
                int locationShow = (int) (width * (1f - fraction));
                if (next) {
                    layout.setComponentConstraints(componentShow, "pos " + locationShow + " 0 100% 100%, w 100%!");
                    layout.setComponentConstraints(componentOut, "pos -" + location + " 0 " + (width - location) + " 100%");
                } else {
                    layout.setComponentConstraints(componentShow, "pos -" + locationShow + " 0 " + (width - locationShow) + " 100%");
                    layout.setComponentConstraints(componentOut, "pos " + location + " 0 100% 100%, w 100%!");
                }
                panel.revalidate();
            }

            @Override
            public void end() {
                componentOut.setVisible(false);
                layout.setComponentConstraints(componentShow, "pos 0 0 100% 100%, width 100%");
            }
        };
        animator = new Animator(500, target);
        animator.setResolution(0);
        animator.setAcceleration(0.5f);
        animator.setDeceleration(0.5f);
        panel.setLayout(layout);
        setLayout(new MigLayout("fill, inset 0", "[fill, center]", "3[fill]3"));
        add(panel, "w 100%-6!");
    }

    public void initSlideshow(Component... coms) {
        if (coms.length >= 2) {
            for (Component com : coms) {
                com.setVisible(false);
                panel.add(com, "pos 0 0 0 0");
            }
            if (panel.getComponentCount() > 0) {
                componentShow = panel.getComponent(0);
                componentShow.setVisible(true);
                layout.setComponentConstraints(componentShow, "pos 0 0 100% 100%");
            }
        }
    }

    public void next() {
        if (!animator.isRunning()) {
            next = true;
            currentIndex = getNext(currentIndex);
            componentShow = panel.getComponent(currentIndex);
            componentOut = panel.getComponent(checkNext(currentIndex - 1));
            animator.start();
        }
    }

    public void slideTo(int index) {
        next = currentIndex < index;
        if (next) {
            componentOut = panel.getComponent(checkNext(currentIndex));
            currentIndex = getNext(index - 1);
            componentShow = panel.getComponent(currentIndex);
            animator.start();
        } else {
            componentOut = panel.getComponent(checkBack(currentIndex));
            currentIndex = getBack(index + 1);
            componentShow = panel.getComponent(currentIndex);
            animator.start();
        }
    }

    public void back() {
        if (!animator.isRunning()) {
            next = false;
            currentIndex = getBack(currentIndex);
            componentShow = panel.getComponent(currentIndex);
            componentOut = panel.getComponent(checkBack(currentIndex + 1));
            animator.start();
        }
    }

    private int getNext(int index) {
        if (index == panel.getComponentCount() - 1) {
            return 0;
        } else {
            return index + 1;
        }
    }

    private int checkNext(int index) {
        if (index == -1) {
            return panel.getComponentCount() - 1;
        } else {
            return index;
        }
    }

    private int getBack(int index) {
        if (index == 0) {
            return panel.getComponentCount() - 1;
        } else {
            return index - 1;
        }
    }

    private int checkBack(int index) {
        if (index == panel.getComponentCount()) {
            return 0;
        } else {
            return index;
        }
    }
}
