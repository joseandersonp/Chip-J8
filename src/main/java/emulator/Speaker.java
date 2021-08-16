package emulator;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class Speaker {

	private MidiChannel midiChannel;
	private int bank = 128;
	private int program = 80;
	private int note = 66;
	private int velocity = 64;
	private int reverb = 0;
	private boolean playing = false;

	public Speaker() {

		Synthesizer synthesizer;
		try {
			synthesizer = MidiSystem.getSynthesizer();

			synthesizer.open();
			midiChannel = synthesizer.getChannels()[0];
			midiChannel.programChange(bank, program);
			midiChannel.controlChange(91, reverb);

		} catch (MidiUnavailableException e) {
			throw new RuntimeException(e);
		}
	}

	public void play() {
		if (!playing) {
			midiChannel.noteOn(note, velocity);
			playing = true;
		}
	}

	public void stop() {
		if (playing) {
			midiChannel.noteOff(note, velocity);
			playing = false;
		}
	}

}
