package pl.edu.agh.lab5;

import pl.edu.agh.util.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Mandelbrot extends JFrame {

    private final int MAX_ITER = 10000;
    private final double ZOOM = 150;
    private BufferedImage I;
    private ExecutorService executor;

    public Mandelbrot(int numberOfThreads) {
        super("Mandelbrot Set");
        setBounds(100, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        executor = Executors.newFixedThreadPool(numberOfThreads);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    public void scheduleDrawing(int numberOfTasks) {
        int linesPerTask = getHeight() / numberOfTasks;
        List<Future<Boolean>> isDone = new ArrayList<>(numberOfTasks);
        for (int i = 0; i < numberOfTasks; i++) {
            final int finalI = i;
            isDone.add(executor.submit(() -> drawMandelbrot(finalI * linesPerTask,
                    finalI == numberOfTasks - 1 ? getHeight() : (finalI + 1) * linesPerTask)));
        }
        for (Future<Boolean> done : isDone) {
            try {
                done.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        setVisible(true);
    }

    public boolean drawMandelbrot(int lineFrom, int lineTo) {
        for (int y = lineFrom; y < lineTo; y++) {
            for (int x = 0; x < getWidth(); x++) {
                setPixel(x, y);
            }
        }
        return true;
    }

    private void setPixel(int x, int y) {
        double zx = 0;
        double zy = 0;
        double cX = (x - 400) / ZOOM;
        double cY = (y - 300) / ZOOM;
        int iter = MAX_ITER;
        double tmp;
        while (zx * zx + zy * zy < 4 && iter > 0) {
            tmp = zx * zx - zy * zy + cX;
            zy = 2.0 * zx * zy + cY;
            zx = tmp;
            iter--;
        }
        I.setRGB(x, y, iter | (iter << 8));
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(I, 0, 0, this);
    }

    public static void main(String[] args) {
        Utils.measureExecutionTime(() -> new Mandelbrot(8).scheduleDrawing(799));
    }
}
