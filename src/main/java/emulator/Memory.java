package emulator;

public class Memory {

	private byte[] ram;
	private int[] stack;

	private final int font[] = { 0xF0, 0x90, 0x90, 0x90, 0xF0, // 0
			0x20, 0x60, 0x20, 0x20, 0x70, // 1
			0xF0, 0x10, 0xF0, 0x80, 0xF0, // 2
			0xF0, 0x10, 0xF0, 0x10, 0xF0, // 3
			0x90, 0x90, 0xF0, 0x10, 0x10, // 4
			0xF0, 0x80, 0xF0, 0x10, 0xF0, // 5
			0xF0, 0x80, 0xF0, 0x90, 0xF0, // 6
			0xF0, 0x10, 0x20, 0x40, 0x40, // 7
			0xF0, 0x90, 0xF0, 0x90, 0xF0, // 8
			0xF0, 0x90, 0xF0, 0x10, 0xF0, // 9
			0xF0, 0x90, 0xF0, 0x90, 0x90, // A
			0xE0, 0x90, 0xE0, 0x90, 0xE0, // B
			0xF0, 0x80, 0x80, 0x80, 0xF0, // C
			0xE0, 0x90, 0x90, 0x90, 0xE0, // D
			0xF0, 0x80, 0xF0, 0x80, 0xF0, // E
			0xF0, 0x80, 0xF0, 0x80, 0x80 // F
	};

	public void init() {

		ram = new byte[4096];
		stack = new int[16];

		for (int i = 0; i < font.length; i++) {
			ram[i] = (byte) font[i];
		}
	}

	public void writeRam(int addr, int value) {
		ram[addr] = (byte) (value & 0xFF);
	}
	
	public void writeRam(byte[] data,int addr, int length) {
		for (int i = 0; i < length; i++, addr++) 
			ram[addr] = data[i];		
	}

	public int readRam(int addr) {
		return ram[addr] & 0xFF;
	}
	
	public int readOpcode(int addr) {
		return ((ram[addr] & 0xFF) << 8) | (ram[addr + 1] & 0xFF);
	}
	
	public void writeStack(int sp, int value) {
		stack[sp] = value & 0xFFFF;
	}

	public int readStack(int sp) {
		return stack[sp] & 0xFFFF;
	}

}
