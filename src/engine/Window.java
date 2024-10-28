package engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window {

    private JFrame frame;
    public JFrame getFrame() {
        return frame;
    }

    private BufferedImage image;
    public BufferedImage getImage() {
        return image;
    }

    private Canvas canvas;
    public Canvas getCanvas() {
        return canvas;
    }

    private BufferStrategy bs;
    private Graphics g;

    public Window(Engine engine) {

        image = new BufferedImage(engine.getWidth(), engine.getHeight(), BufferedImage.TYPE_INT_RGB);
        canvas = new Canvas();

        Dimension size = new Dimension((int)(engine.getWidth() * engine.getScale()), engine.getHeight() * (int)(engine.getScale()));

        canvas.setPreferredSize(size);
        canvas.setMaximumSize(size);
        canvas.setMinimumSize(size);

        frame = new JFrame(engine.getTitle());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(2);
        bs = canvas.getBufferStrategy();
        g = bs.getDrawGraphics();
    }

    public void update() {
        g.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        bs.show();
    }

}
