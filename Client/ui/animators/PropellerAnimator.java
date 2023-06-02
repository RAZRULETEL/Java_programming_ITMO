package Client.ui.animators;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractButton;
import javax.swing.Timer;

public class PropellerAnimator  extends Timer implements Animator {
    private static final String AudioFile = "propeller_animator.wav";
    private static final int MAX_SPEED = 100, START_SPEED = 10, TRIES_COUNT = 2;
    private static final double PHASE_STEP = 0.03;
    private static volatile Clip audio;
    private static final AtomicInteger usingAudio = new AtomicInteger(0);
    private boolean reservedAudio = false;
    private double speed = START_SPEED, tryPhase = 0;
    private int tries = 0;
    private AbstractButton target;

    static {
        loadAudio();
    }

    public PropellerAnimator(AbstractButton target, int delay) {
        super(delay, null);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!reservedAudio) {
                    startAudio();
                    reservedAudio = true;
                }
                if (tries < TRIES_COUNT) {
                    tryPhase += PHASE_STEP;
                    if (tryPhase > 1) {
                        tries++;
                        tryPhase = 0;
                        counter = 0;
                    }
                    if (!target.getModel().isPressed() && tryPhase < 0.5) {
                        tries = 0;
                        tryPhase = 0;
                        speed = START_SPEED;
                        if (reservedAudio) {
                            reservedAudio = false;
                            stopAudio();
                        }
                        ((Timer) e.getSource()).stop();
                    }
                } else {
                    if (target.getModel().isPressed())
                        speed += 1;
                    else
                        speed -= 0.5;
                    if (speed <= 0) {
                        tries = 0;
                        tryPhase = 0;
                        speed = START_SPEED;
                        if (reservedAudio) {
                            reservedAudio = false;
                            stopAudio();
                        }
                        ((Timer) e.getSource()).stop();
                    }
                }
                if (speed > MAX_SPEED)
                    speed = MAX_SPEED;
                target.repaint();
            }
        });
        this.target = target;
    }

    private double counter;

    private double getAngle() {
        counter += speed;
        return tries != TRIES_COUNT ? Math.PI - (tryPhase < 0.5 ? Math.sqrt(Math.abs(tryPhase - 0.5) * 2) : Math.abs(tryPhase - 0.5) * 2) * Math.PI : counter / MAX_SPEED;
    }

    synchronized private static void startAudio() {
        if (audio != null && audio.isOpen()) {
            audio.loop(Clip.LOOP_CONTINUOUSLY);
            usingAudio.incrementAndGet();
        }
    }

    synchronized private static void stopAudio() {
        if (audio != null && usingAudio.decrementAndGet() == 0) {
            audio.stop();
            audio.setFramePosition(0);
        }
    }

    private static void loadAudio() {
        try {
            ClassLoader classLoader = PropellerAnimator.class.getClassLoader();
            URL resource = classLoader.getResource(AudioFile);
            File audioFile = new File(resource.getFile());
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            audio = AudioSystem.getClip();
            audio.open(audioStream);
            audio.setLoopPoints((int) (audio.getFrameLength()*0.2), audio.getFrameLength()-1);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ignored) {
        }
    }

    @Override
    public void animate(Graphics2D g) {
        if(target.getModel().isPressed()) {
            if (!isRunning()) start();
        }
        if(isRunning())
            g.rotate(getAngle(), target.getWidth() / 2.0, target.getHeight() / 2.0);
    }
}