package emulator;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Keyboard extends KeyAdapter {

	private Map<Integer, Integer> keyMap;

	public Keyboard() {

		keyMap = new HashMap<>();

		keyMap.put(49, 0x1); // 1
		keyMap.put(50, 0x2); // 2
		keyMap.put(51, 0x3); // 3
		keyMap.put(52, 0xC); // 4

		keyMap.put(81, 0x4); // Q
		keyMap.put(87, 0x5); // W
		keyMap.put(69, 0x6); // E
		keyMap.put(82, 0xD); // R

		keyMap.put(65, 0x7); // A
		keyMap.put(83, 0x8); // S
		keyMap.put(68, 0x9); // D
		keyMap.put(70, 0xE); // F

		keyMap.put(90, 0xA); // Z
		keyMap.put(88, 0x0); // X
		keyMap.put(67, 0xB); // C
		keyMap.put(86, 0xF); // V
	}

	private int keyPressed = -1;

	public int getKeyPressed() {
		return keyPressed;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Integer key = keyMap.get(e.getKeyCode());
		if (key != null)
			keyPressed = key;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyPressed = -1;
	}

	public boolean isKeyPressed(int k) {
		return keyPressed == k;
	}
}
