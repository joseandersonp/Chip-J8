package emulator;

public class Cpu {

	private Keyboard keyboard;
	private Display monitor;
	private Speaker speaker;
	private Memory memory;
	
	// 16 General purpose 8-bit registers
	private int v[];
	// Index register
	private int i;

	// Delay Timer;
	private int dt;
	// Sound Timer
	private int st;

	// Program Counter
	private int pc;
	// Stack Pointer
	private int sp;

	public Cpu(Memory memory, Display monitor, Keyboard keyboard, Speaker speaker) {
		this.monitor = monitor;
		this.keyboard = keyboard;
		this.speaker = speaker;
		this.memory = memory;
	}

	public void init() {
		
		v = new int[16];

		i = 0;
		dt = 0;
		st = 0;
		pc = 0x200;
		sp = 0;
		
	}

	public void execute(int op) {

		switch (op >> 12) {

		case 0x0:
			// 00E0 - CLS
			// Clear the display.
			if (op == 0xE0)
				monitor.clear();

			// 00EE - RET
			// Return from a subroutine.
			if (op == 0xEE) {
				pc = memory.readStack(sp);
				sp--;
			}
			// 0nnn - SYS addr
			// not implemented;

			break;

		case 0x1:
			// 1nnn - JP addr
			// Jump to location nnn.
			pc = (op & 0xFFF) - 2;
			break;

		case 0x2:
			// 2nnn - CALL addr
			// Call subroutine at nnn.
			sp++;
			memory.writeStack(sp,pc);
			pc = (op & 0xFFF) - 2;
			break;

		case 0x3:
			// 3xkk - SE Vx, byte
			// Skip next instruction if Vx = kk.
			int x = (op >> 8) & 0xF;
			int kk = op & 0xFF;
			if (v[x] == kk)
				pc += 2;
			break;

		case 0x4:
			// 4xkk - SNE Vx, byte
			// Skip next instruction if Vx != kk.
			x = (op >> 8) & 0xF;
			kk = op & 0xFF;
			if (v[x] != kk)
				pc += 2;
			break;

		case 0x5:
			// 5xy0 - SE Vx, Vy
			// Skip next instruction if Vx = Vy.
			x = (op >> 8) & 0xF;
			int y = (op >> 4) & 0xF;
			if (v[x] == v[y])
				pc += 2;
			break;

		case 0x6:
			// 6xkk - LD Vx, byte
			// Set Vx = kk.
			x = (op >> 8) & 0xF;
			kk = op & 0xFF;
			v[x] = kk;
			break;

		case 0x7:

			// 7xkk - ADD Vx, byte
			// Set Vx = Vx + kk.
			x = (op >> 8) & 0xF;
			kk = op & 0xFF;
			v[x] = (kk + v[x]) & 0xFF;
			break;

		case 0x8:

			x = (op >> 8) & 0xF;
			y = (op >> 4) & 0xF;

			switch (op & 0xF) {
			case 0x0:
				// 8xy0 - LD Vx, Vy
				// Set Vx = Vy.
				v[x] = v[y];
				break;
			case 0x1:
				// 8xy1 - OR Vx, Vy
				// Set Vx = Vx OR Vy.
				v[x] = v[x] | v[y];
				break;
			case 0x2:
				// 8xy2 - AND Vx, Vy
				// Set Vx = Vx AND Vy.
				v[x] = v[x] & v[y];
				break;
			case 0x3:
				// 8xy3 - XOR Vx, Vy
				// Set Vx = Vx XOR Vy.
				v[x] = v[x] ^ v[y];
				break;
			case 0x4:
				// 8xy4 - ADD Vx, Vy
				// Set Vx = Vx + Vy, set VF = carry.
				v[x] = v[x] + v[y];
				v[0xF] = v[x] > 255 ? 1 : 0;
				v[x] &= 0xFF;
				break;
			case 0x5:
				// 8xy5 - SUB Vx, Vy
				// Set Vx = Vx - Vy, set VF = NOT borrow.
				v[0xF] = v[x] >= v[y] ? 1 : 0;
				v[x] = (v[x] - v[y]) & 0xFF;
				break;
			case 0x6:
				// 8xy6 - SHR Vx {, Vy}
				// Set Vx = Vx SHR 1.
				v[0xF] = v[x] & 1;
				v[x] = v[x] >> 1;
				break;
			case 0x7:
				// 8xy7 - SUBN Vx, Vy
				// Set Vx = Vy - Vx, set VF = NOT borrow.
				v[0xF] = v[y] >= v[x] ? 1 : 0;
				v[x] = (v[y] - v[x]) & 0xFF;
				break;
			case 0xE:
				// 8xyE - SHL Vx {, Vy}
				// Set Vx = Vx SHL 1.
				v[0xF] = (v[x] >> 7) & 1;
				v[x] = (v[x] << 1) & 0xFF;
				break;
			}

			break;

		case 0x9:
			// 9xy0 - SNE Vx, Vy
			// Skip next instruction if Vx != Vy.
			x = (op >> 8) & 0xF;
			y = (op >> 4) & 0xF;
			if (v[x] != v[y])
				pc += 2;
			break;

		case 0xA:
			// Annn - LD I, addr
			// Set I = nnn.
			i = op & 0xFFF;
			break;

		case 0xB:
			// Bnnn - JP V0, addr
			// Jump to location nnn + V0.
			pc = v[0] + (op & 0xFFF);
			break;

		case 0xC:
			// Cxkk - RND Vx, byte
			// Set Vx = random byte AND kk.
			x = (op >> 8) & 0xF;
			kk = op & 0xFF;
			v[x] = kk & (int) (Math.random() * 0xFF);
			break;

		case 0xD:

			// Dxyn - DRW Vx, Vy, nibble
			// Display n-byte sprite starting at memory
			// location I at (Vx, Vy), set VF = collision.

			v[0xF] = 0;
			int n = op & 0xF;
			x = (op >> 8) & 0xF;
			y = (op >> 4) & 0xF;

			for (int r = 0; r < n; r++) {
				int b = memory.readRam(i + r);
				for (int c = 0; c < 8; c++) {
					if ((b & (0x80 >> c)) != 0) {
						boolean col = monitor.setPixel(v[x] + c, v[y] + r);
						if (col)
							v[0xF] = 1;
					}
				}
			}

			break;

		case 0xE:

			// Ex9E - SKP Vx
			// Skip next instruction if key with the
			// value of Vx is pressed.
			x = (op >> 8) & 0xF;
			if ((op & 0xFF) == 0x9E) {
				if (keyboard.isKeyPressed(v[x]))
					pc += 2;
				break;
			}
			// ExA1 - SKNP Vx
			// Skip next instruction if key with the
			// value of Vx is not pressed.
			if ((op & 0xFF) == 0xA1) {
				if (!keyboard.isKeyPressed(v[x]))
					pc += 2;
			}
			break;

		case 0xF:

			x = (op >> 8) & 0xF;

			switch (op & 0xFF) {
			case 0x07:
				// Fx07 - LD Vx, DT
				// Set Vx = delay timer value.
				v[x] = dt;
				break;
			case 0x0A:
				// Fx0A - LD Vx, K
				// Wait for a key press, store the value
				// of the key in Vx.
				int k = keyboard.getKeyPressed();
				if (k != -1) {
					v[x] = k;
				} else
					pc -= 2;

				break;
			case 0x15:
				// Fx15 - LD DT, Vx
				// Set delay timer = Vx.
				dt = v[x];
				break;
			case 0x18:
				// Fx18 - LD ST, Vx
				// Set sound timer = Vx.
				st = v[x];
				break;
			case 0x1E:
				// Fx1E - ADD I, Vx
				// Set I = I + Vx.
				i += v[x];
				break;
			case 0x29:
				// Fx29 - LD F, Vx
				// Set I = location of sprite for digit Vx.
				i = 5 * v[x];
				break;
			case 0x33:
				// Fx33 - LD B, Vx
				// Store BCD representation of Vx in
				// memory locations I, I+1, and I+2.
				memory.writeRam(i , v[x] / 100);
				memory.writeRam(i + 1 , (v[x] % 100) / 10);
				memory.writeRam(i + 2 , v[x] % 10);
				break;
			case 0x55:
				// Fx55 - LD [I], Vx
				// Store registers V0 through Vx in
				// memory starting at location I.
				for (int c = 0; c <= x; c++)
					memory.writeRam(i + c, v[c]);
				break;
			case 0x65:
				// Fx65 - LD Vx, [I]
				// Read registers V0 through Vx from
				// memory starting at location I.
				for (int c = 0; c <= x; c++)
					v[c] = memory.readRam(i + c);
				break;
			}
		}
	}

	public void tick() {

		for (int i = 0; i < 10; i++) {
			int opcode = memory.readOpcode(pc);
			execute(opcode);
			pc += 2;
		}

	}

	public void updateTimers() {
		if (dt > 0)
			dt--;
		
		if (st > 0) {
			speaker.play();	
			st--;
		} else
			speaker.stop();		
	}
}
