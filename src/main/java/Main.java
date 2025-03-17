import java.nio.CharBuffer;
import java.util.Random;

public class Main {

    public static String chiffre = "00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000";

    public static void main(String[] args) {
        // Sample to test from exercise
        var spn = new SPN("00010001001010001000110000000000");
        var yExpected = 0b1010_1110_1011_0100;
        var yActual = spn.encrypt(0b0001_0010_1000_1111);
        System.out.println(yActual == yExpected);

        // Check if encrypting and decrypting leads to the same content
        System.out.println(Encryption.decrypt(Encryption.encrypt("hello")).equals("hello"));

        // Chiffre from exercise
        var expected = "Gut gemacht!";
        var actual = Encryption.decrypt(chiffre);
        System.out.println("-----");
        System.out.println(">>> Expected: " + expected);
        System.out.println(">>> Actual: " + actual);
        System.out.println(">>> Compare decrypted to encrypted: " + actual.equals(expected));
    }

    private static String toBitString (int in) {
        return String.format("%16s", Integer.toBinaryString(in)).replace(' ', '0');
    }

    public static class Encryption {
        private static final int chunkLength = 16;

        static String encrypt(String decrypted) {
            var buffer = new StringBuilder();
            // Add padding 1000..
            var paddedInput = convertToPaddedBitString(decrypted);
            var spn = new SPN();
            var initialY = generateRandomKey();
            System.out.println(toBitString(initialY) + " RANDOM");

            // Add random key at the beginning
            buffer.append(toBitString(initialY));

            for(int i = 0; i< paddedInput.length()/chunkLength; i++) {
                System.out.println(">> Chunk " + i);
                // Calculate (y_-1 + i) mod 2^l
                var contentToEncrypt = (initialY + i) % (1<<chunkLength);
                var startOfSection = i*chunkLength;
                var x = paddedInput.substring(startOfSection,startOfSection+chunkLength);
                // E(.., k) xor x_i to encrypt
                var encrypted = spn.encrypt(contentToEncrypt) ^ Integer.parseInt(x,2);
                buffer.append(toBitString(encrypted));
            }

            return buffer.toString();
        }

        static String decrypt(String encrypted) {
            if(encrypted.length()%chunkLength != 0) throw new IllegalArgumentException("Has to be %16");
            var spn = new SPN();
            var buffer = new StringBuilder();
            // Remove first chunk which represents y_-1
            var initialY = Integer.parseInt(encrypted.substring(0,chunkLength),2);
            System.out.println(toBitString(initialY) + " RANDOM");
            var encryptedRaw = encrypted.substring(chunkLength);
            for(int i = 0; i<encryptedRaw.length()/chunkLength; i++) {
                System.out.println(">> Chunk "+i);
                // Calculate (y_-1 + i) mod 2^l
                var contentToEncrypt = (initialY + i) % (1<<chunkLength);
                var startOfSection = i*chunkLength;
                var y = Integer.parseInt(encryptedRaw.substring(startOfSection,startOfSection+chunkLength),2);
                // E(.., k) xor x_i to decrypt
                var x = spn.encrypt(contentToEncrypt)  ^ y;
                buffer.append(toBitString(x));
            }

            // Remove padding 10000.. etc
            return convertFromPaddedBitString(buffer.toString());
        }

        private static int generateRandomKey() {
            // Random key with max value of 0b1111_1111_1111_1111 -> 16 bits
            Random random = new Random();
            return random.nextInt(65536);
        }

        /*
        This function has been generated with AI
        */
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


        /*
            This function has been generated with AI
         */
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