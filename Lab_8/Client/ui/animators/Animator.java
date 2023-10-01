package Client.ui.animators;

import java.awt.Graphics2D;

public interface Animator {
    default void animate(Graphics2D g){};
    default void animateBorder(Graphics2D g){};
}
