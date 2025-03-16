import java.nio.CharBuffer;
import java.util.Random;

public class Main {

    String chiffre = "00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000";

    public static void main(String[] args) {

//        var spn = new SPN("00010001001010001000110000000000");
//        var res = spn.encrypt(0b0001_0010_1000_1111);
//        System.out.println(">>>>>>>>>>>>>>>>");
//        var resy = spn.decrypt(res);
//        var y = 0b1101_1110_1011_0100;
//        System.out.println(res == y);

        var spn = new SPN("00010001001010001000110000000000");
        var original = 0b0001_0010_1000_1111;
        var y = 0b1010_1110_1011_0100;
        spn.encrypt(original);
//        var encrypted = spn.encrypt(original);
//        System.out.println(">>>>>>>>>>>>>>>>");
//        var decrypted = spn.decrypt(encrypted);
//        System.out.println(original == decrypted);

//        System.out.println(Encryption.convertFromPaddedBitString(Encryption.convertToPaddedBitString("hel")).equals("hel"));

//        var content = "00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000";
//          System.out.println(Encryption.decrypt(content));

        System.out.println(Encryption.decrypt(Encryption.encrypt("hello")).equals("hello"));

//        var y = Encryption.encrypt("hello");
//        System.out.println(y);
//        var x = Encryption.decrypt(y);
//        System.out.println("--- end ---");
//        System.out.println(x);
    }

    private static String toBitString (int in) {
        return String.format("%16s", Integer.toBinaryString(in)).replace(' ', '0');
    }

    public static class Encryption {
        private static final int chunkLength = 16;

        static String encrypt(String decrypted) {
            var buffer = new StringBuilder();
            var paddedInput = convertToPaddedBitString(decrypted);
            var spn = new SPN();
            var initialY = generateRandomKey();
            System.out.println(toBitString(initialY) + " RANDOM");

            // Add random key at the beginning
            buffer.append(toBitString(initialY));

            for(int i = 0; i< paddedInput.length()/chunkLength; i++) {
                var contentToEncrypt = (initialY + i) % 2^chunkLength;
                var startOfSection = i*chunkLength;
                var x = paddedInput.substring(startOfSection,startOfSection+chunkLength);
                var encrypted = spn.encrypt(contentToEncrypt) ^ Integer.parseInt(x,2);
                buffer.append(toBitString(encrypted));
            }

            return buffer.toString();
        }

        static String decrypt(String encrypted) {
            if(encrypted.length()%chunkLength != 0) throw new IllegalArgumentException("Has to be %16");
            var spn = new SPN();
            var buffer = new StringBuilder();
            var initialY = Integer.parseInt(encrypted.substring(0,chunkLength),2);
            System.out.println(toBitString(initialY) + " RANDOM");
            var encryptedRaw = encrypted.substring(chunkLength);
            for(int i = 0; i<encryptedRaw.length()/chunkLength; i++) {
                var contentToEncrypt = (initialY + i) % 2^chunkLength;
                var startOfSection = i*chunkLength;
                var y = Integer.parseInt(encryptedRaw.substring(startOfSection,startOfSection+chunkLength),2);
                var x = spn.encrypt(contentToEncrypt)  ^ y;
                buffer.append(toBitString(x));
            }

            return convertFromPaddedBitString(buffer.toString());
        }

        private static int generateRandomKey() {
            Random random = new Random();
            return random.nextInt(65536);
        }

        private static String convertFromPaddedBitString(String bitString) {
            // Remove trailing '0's until the first '1' is encountered
            int lastOneIndex = bitString.lastIndexOf('1');
            if (lastOneIndex == -1) {
                throw new IllegalArgumentException("Invalid padded bit string: no '1' found.");
            }

            String trimmedBitString = bitString.substring(0, lastOneIndex);

            // Convert the bit string back to characters (8 bits per character)
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < trimmedBitString.length(); i += 8) {
                String byteString = trimmedBitString.substring(i, Math.min(i + 8, trimmedBitString.length()));
                char c = (char) Integer.parseInt(byteString, 2);
                text.append(c);
            }

            return text.toString();
        }


        private static String convertToPaddedBitString(String text) {
            StringBuilder bitString = new StringBuilder();

            // Convert each character to an 8-bit binary string
            for (char c : text.toCharArray()) {
                String binary = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
                bitString.append(binary);
            }

            // Append a '1' at the end
            bitString.append('1');

            // Calculate padding length to make the total length a multiple of 16
            int paddingNeeded = (16 - (bitString.length() % 16)) % 16;

            // Append the necessary '0's
            for (int i = 0; i < paddingNeeded; i++) {
                bitString.append('0');
            }

            return bitString.toString();
        }
    }
}