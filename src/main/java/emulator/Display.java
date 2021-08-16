package emulator;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Display extends JPanel {

	private int cols;
	private int rows;
	private byte pixels[];

	public Display() {
		init(64, 32);
	}

	public Display(int cols, int rows) {
		init(cols, rows);
	}

	public void init(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
		clear();
	}

	public void clear() {
		pixels = new byte[cols * rows];
	}
	
	public boolean setPixel(int x, int y) {

		x = x & 63;
		y = y & 31;
		

		int i = x + y * cols;

		boolean col = pixels[i] == 1;
		pixels[i] = (byte) (pixels[i] ^ 1);

		return col;

	}

	public byte[] getPixels() {
		return pixels;
	}

	public int getCols() {
		return cols;
	}

	public int getRows() {
		return rows;
	}

}
