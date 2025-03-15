import java.util.HashMap;
import java.util.Map;

public class Main {

    String chiffre = "00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000";

    public static void main(String[] args) {
        System.out.println("Hello World!");

//        var spn = new SPN("00010001001010001000110000000000");
//
//        var res = spn.encrypt(0b0001_0010_1000_1111);
//        System.out.println(">>>>>>>>>>>>>>>>");
//        var resy = spn.decrypt(res);
//        var y = 0b1101_1110_1011_0100;
//        System.out.println(res == y);
        var spn = new SPN();
        var original = 0b0111_1010_0101_1100;
        var encrypted = spn.encrypt(original);
        System.out.println(">>>>>>>>>>>>>>>>");
        var decrypted = spn.decrypt(encrypted);
        System.out.println(original == decrypted);
    }

    public static class Encryption {
        private static Map<Integer, Integer> encryptions = new HashMap<>();

        static String encrypt(String input) {
            var paddedInput = convertToPaddedBitString(input);
            var spn = new SPN();
            return "";
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

//    public class SPN {
//        private final int rounds = 4;
//        private final String key = "00111010100101001101011000111111";
//        private final int keyLength = 16;
//        private static final int[][] sBox = {
//                {0x0, 0xE}, {0x1, 0x4}, {0x2, 0xD}, {0x3, 0x1}, {0x4, 0x2}, {0x5, 0xF}, {0x6, 0xB}, {0x7, 0x8}, {0x8, 0x3}, {0x9, 0xA}, {0xA, 0x6}, {0xB, 0xC}, {0xC, 0x5}, {0xD, 0x9}, {0xE, 0x0}, {0xF, 0x7}
//        };
//        private static final int[][] permutation = {
//                {0,0},{1,4},{2,8},{3,12},{4,1},{5,5},{6,9},{7,13},{8,2},{9,6},{10,10},{11,14},{12,3},{13,7},{14,11},{15,15}
//        };
//
//        int encrypt(int decrypted) {
//            // 0. Runde, Weissrunde
//            var firstKeyApplied = decrypted ^ getRoundKey(0);
//
//            // 1. to r-1 Runden
//            var output = firstKeyApplied;
//            for(int i = 1; i <rounds-1; i++) {
//                var sBoxIterApplied = applySBox(output);
//                var betaIterApplied = applyPermutation(sBoxIterApplied);
//                output = getRoundKey(i) ^ betaIterApplied;
//            }
//
//            // Verkürzte Runde
//            var sBoxApplied = applySBox(output);
//            var lastKeyApplied = sBoxApplied ^ getRoundKey(rounds-1);
//            return lastKeyApplied;
//        }
//
//        private int runRound(int input, int i) {
//            var sBoxParse = applySBox(input);
//            return 0;
//        }
//
//        private int applyPermutation(int input) {
//            var output = new String[keyLength];
//            var inputBitString = String.format("%16s", Integer.toBinaryString(input)).replace(' ', '0');
//            for(int i = 0; i < keyLength; i++) {
//                var permutPos = getPermutationValue(i);
//                output[permutPos] = String.valueOf(inputBitString.charAt(i));
//            }
//            var outputBitString = String.join("", output);
//            return Integer.parseInt(outputBitString, 2);
//        }
//
//        private int applySBox(int input) {
//            var mask1 = input & 0b1111_0000_0000_0000;
//            var shift1 = getSBoxValue(mask1 >> 12) << 12;
//
//            var mask2 = input & 0b0000_1111_0000_0000;
//            var shift2 = getSBoxValue(mask2 >> 8) << 8;
//
//            var mask3 = input & 0b1111_0000_1111_0000;
//            var shift3 = getSBoxValue(mask3 >> 4) << 4;
//
//            var mask4 = input & 0b1111_0000_0000_1111;
//            var shift4 = getSBoxValue(mask4);
//
//            return shift1 | shift2 | shift3 | shift4;
//        }
//
//        private int getRoundKey(int i) {
//            var k = key.substring(4*i, i+keyLength);
//            return Integer.parseInt(k, 2);
//        }
//
//        private int getSBoxValue(int searchValue) {
//            for (int[] box : sBox) {
//                if (box[0] == searchValue) return box[1];
//            }
//            throw new IllegalArgumentException("Value not found");
//        }
//
//        private int getPermutationValue(int searchValue) {
//            for (int[] box : permutation) {
//                if (box[0] == searchValue) return box[1];
//            }
//            throw new IllegalArgumentException("Value not found");
//        }
//    }
}