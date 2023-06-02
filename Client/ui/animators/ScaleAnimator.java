package Client.ui.animators;

import java.awt.Graphics2D;

import javax.swing.AbstractButton;

public class ScaleAnimator implements Animator {
    private final float scale;
    private final AbstractButton target;

    public ScaleAnimator(AbstractButton target, float scaleFactor) {
        this.scale = scaleFactor;
        this.target = target;
    }

    @Override
    public void animateBorder(Graphics2D g) {
        if(target.getModel().isPressed()) {
            g.translate(-target.getWidth() * (scale - 1) / 2, -target.getHeight() * (scale - 1) / 2);
            g.scale(scale, scale);
        }
    }
}
