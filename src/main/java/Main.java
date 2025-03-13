import java.util.HashMap;
import java.util.Map;

public class Main {

    String chiffre = "00000100110100100000101110111000000000101000111110001110011111110110000001010001010000111010000000010011011001110010101110110000";

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    class SPN {
        private final int rounds = 4;
        private final String key = "00111010100101001101011000111111";
        private final int keyLength = 16;
        private static final int[][] sBox = {
                {0x0, 0xE}, {0x1, 0x4}, {0x2, 0xD}, {0x3, 0x1}, {0x4, 0x2}, {0x5, 0xF}, {0x6, 0xB}, {0x7, 0x8}, {0x8, 0x3}, {0x9, 0xA}, {0xA, 0x6}, {0xB, 0xC}, {0xC, 0x5}, {0xD, 0x9}, {0xE, 0x0}, {0xF, 0x7}
        };
        private static final int[][] permutation = {
                {0,0},{1,4},{2,8},{3,12},{4,1},{5,5},{6,9},{7,13},{8,2},{9,6},{10,10},{11,14},{12,3},{13,7},{14,11},{15,15}
        };

        public int encrypt(int raw) {
            // Weissrunde
            var k0Applied = raw ^ getRoundKey(0);



            return 0;
        }

        private int runRound(int input, int i) {
            var sBoxParse = applySBox(input);
            return 0;
        }

        private int applyPermutation(int input) {
            var output = new String[16];
            var inputBitString = String.format("%16s", Integer.toBinaryString(input)).replace(' ', '0');
            for(int i = 0; i < 16; i++) {
                var permutPos = getPermutationValue(i);
                output[permutPos] = String.valueOf(inputBitString.charAt(i));
            }
            var outputBitString = String.join("", output);
            return Integer.parseInt(outputBitString, 2);
        }

        private int applySBox(int input) {
            var mask1 = input & 0b1111_0000_0000_0000;
            var shift1 = getSBoxValue(mask1 >> 12) << 12;

            var mask2 = input & 0b0000_1111_0000_0000;
            var shift2 = getSBoxValue(mask2 >> 8) << 8;

            var mask3 = input & 0b1111_0000_1111_0000;
            var shift3 = getSBoxValue(mask3 >> 4) << 4;

            var mask4 = input & 0b1111_0000_0000_1111;
            var shift4 = getSBoxValue(mask4);

            return shift1 | shift2 | shift3 | shift4;
        }

        private int getRoundKey(int i) {
            var k = key.substring(4*i, i+keyLength);
            return Integer.parseInt(k, 2);
        }

        private int getSBoxValue(int searchValue) {
            for (int[] box : sBox) {
                if (box[0] == searchValue) return box[1];
            }
            throw new IllegalArgumentException("Value not found");
        }

        private int getPermutationValue(int searchValue) {
            for (int[] box : permutation) {
                if (box[0] == searchValue) return box[1];
            }
            throw new IllegalArgumentException("Value not found");
        }
    }

    static class Encryption {
        private static Map<Integer, Integer> encryptions = new HashMap<>();


    }

    static class SBox {
        private static final int[][] sBox = {
                {0x0, 0xE},
                {0x1, 0x4},
                {0x2, 0xD},
                {0x3, 0x1},
                {0x4, 0x2},
                {0x5, 0xF},
                {0x6, 0xB},
                {0x7, 0x8},
                {0x8, 0x3},
                {0x9, 0xA},
                {0xA, 0x6},
                {0xB, 0xC},
                {0xC, 0x5},
                {0xD, 0x9},
                {0xE, 0x0},
                {0xF, 0x7}
        };

        public static int get(int searchValue) {
            for (int[] box : sBox) {
                if (box[0] == searchValue) return box[1];
            }
            throw new IllegalArgumentException("Value not found");
        }

        public static int getInversed(int searchValue) {
            for (int[] box : sBox) {
                if (box[1] == searchValue) return box[0];
            }
            throw new IllegalArgumentException("Value not found");
        }
    }
}