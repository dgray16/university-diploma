package utils;

import lombok.SneakyThrows;

import javax.sound.sampled.AudioInputStream;

public class ByteConverterUtils {

    @SneakyThrows
    public static byte[] convertAudioToBytes(AudioInputStream stream) {
        byte[] bytes;

        bytes = new byte[(int) (stream.getFrameLength() * stream.getFormat().getFrameSize())];
        stream.read(bytes);

        return bytes;
    }

}
