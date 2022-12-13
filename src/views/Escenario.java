package views;

import com.jogamp.opengl.util.FPSAnimator;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 *
 * @author Honorio Acosta Ruiz
 */
public class Escenario extends JFrame implements Runnable, WindowListener {

	@Override
	public void run() {
		setLocationRelativeTo(null);
		setFocusable(true);
		setResizable(false);
		setVisible(true);

		animator.start();
	}

	public Escenario() {
		initComponents();
	}

	private void initComponents() {

		setTitle("Escenario");
		setSize(1100, 700);

		canvas = new CanvasEscenario();

		animator = new FPSAnimator(canvas, CanvasEscenario.FPS, true);

		addWindowListener(this);

		getContentPane().add(canvas);
	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		new Thread() {
			@Override
			public void run() {
				if (animator.isStarted()) {
					animator.stop();
				}

				System.exit(0);
			}
		}.start();
	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {

	}

	// Variable declaration
	private CanvasEscenario canvas;
	private FPSAnimator animator;
}
