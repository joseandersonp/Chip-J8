package emulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Chip8 {

	public static final int STOP = 0;
	public static final int START = 1;
	public static final int RUNNING = 2;
	public static final int STARTING = 3;
	public static final int STOPPING = 4;
	public static final int PAUSE = 4;

	private int status;
	private Display display;
	private Keyboard keyboard;
	private Speaker speaker;
	private float step;
	private Cpu cpu;
	private Memory memory;

	public Chip8() {
		this.display = new Display();
		this.keyboard = new Keyboard();
		this.speaker = new Speaker();
		this.memory = new Memory();
		this.cpu = new Cpu(memory, display, keyboard, speaker);
	}

	public void start() {

		cpu.init();
		status = STARTING;
		new Thread(new Runnable() {
			public void run() {
				clock();
			}
		}).start();
	}

	public void stop() {

		if (status != STOP) {
			status = STOPPING;
			while (status != STOP) {
				try {
					Thread.sleep(1);
				} catch (Exception e) {
				}
			}
			
			speaker.stop();
			display.clear();
		}
	}

	public void clock() {
		setSpeed(1);
		long lastTime = System.nanoTime();
		status = RUNNING;
		while (status == RUNNING) {
			long time = System.nanoTime() - lastTime;
			if (time >= step) {
				cpu.tick();
				cpu.updateTimers();
				lastTime = System.nanoTime();
			}
		}
		status = STOP;
	}

	public void loadRom(File romFile) throws IOException {

		FileInputStream inStream = new FileInputStream(romFile);
		byte[] data = new byte[inStream.available()];
		inStream.read(data);
		inStream.close();
		
		memory.init();
		memory.writeRam(data, 0x200, data.length);

	}
	
	public void setSpeed(float speed) {
		this.step = 1000000000 / (int)(60 * speed);
	}
	
	public Display getDisplay() {
		return display;
	}

	public Keyboard getKeyboard() {
		return keyboard;
	}

}
