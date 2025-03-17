import java.util.function.Function;

public class SPN {
    private final int rounds = 5;
    private final int keyLength = 16;

    // Key from exercise
    private String key = "00111010100101001101011000111111";

    public SPN(String key) {
        this.key = key;
    }

    public SPN() {}

    private static final int[][] sBox = {
            {0x0, 0xE}, {0x1, 0x4}, {0x2, 0xD}, {0x3, 0x1}, {0x4, 0x2}, {0x5, 0xF}, {0x6, 0xB}, {0x7, 0x8}, {0x8, 0x3}, {0x9, 0xA}, {0xA, 0x6}, {0xB, 0xC}, {0xC, 0x5}, {0xD, 0x9}, {0xE, 0x0}, {0xF, 0x7}
    };
    private static final int[][] permutation = {
            {0,0},{1,4},{2,8},{3,12},{4,1},{5,5},{6,9},{7,13},{8,2},{9,6},{10,10},{11,14},{12,3},{13,7},{14,11},{15,15}
    };

    private void print(int in, String identifier) {
        String binaryString = String.format("%16s", Integer.toBinaryString(in)).replace(' ', '0');
        StringBuilder formattedBinary = new StringBuilder();
        for (int i = 0; i < binaryString.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                formattedBinary.append(' ');
            }
            formattedBinary.append(binaryString.charAt(i));
        }
        System.out.println(formattedBinary + " " + identifier);
    }

    int encrypt(int decrypted) {
        // 0. Runde, Weissrunde
        print(decrypted, "input");
        print(getRoundKey(0), "+");
        var firstKeyApplied = decrypted ^ getRoundKey(0);
        print(firstKeyApplied, "after k0");

        // 1. to r-1 Runden
        var output = firstKeyApplied;
        for(int i = 1; i <rounds-1; i++) {
            System.out.println("---------");
            System.out.println("Round " + i);
            var sBoxIterApplied = applySBox(output, this::getSBoxValue);
            print(sBoxIterApplied, "sBox");
            var betaIterApplied = applyPermutation(sBoxIterApplied);
            print(betaIterApplied, "beta");
            print(getRoundKey(i), "k"+i);
            output = getRoundKey(i) ^ betaIterApplied;
            print(output, "+ k"+i);
        }

        System.out.println("-----");
        // Verkürzte r-1. Runde
        var sBoxApplied = applySBox(output, this::getSBoxValue);
        print(sBoxApplied, "sBox");
        print(getRoundKey(rounds-1), "k"+(rounds-1));
        var lastKeyApplied = sBoxApplied ^ getRoundKey(rounds-1);
        print(lastKeyApplied, "after k"+(rounds-1));
        return lastKeyApplied;
    }

    int decrypt(int encrypted) {
        // r-1. Runde, Weissrunde
        var firstKeyApplied = encrypted ^ getRoundKey(rounds-1);
        print(encrypted, "input");
        print(getRoundKey(rounds-1), "k"+(rounds-1));
        print(applyPermutation(getRoundKey(rounds-1)), "+"+(rounds-1)+"'");
        print(firstKeyApplied, "after k"+(rounds-1));

        //r-1. to 1 Runden
        var output = firstKeyApplied;
        for(int i = rounds-2; i > 0; i--) {
            System.out.println("---------");
            System.out.println("Round " + i);
            var sBoxIterApplied = applySBox(output, this::getSBoxInversed);
            print(sBoxIterApplied, "sBox");
            var betaIterApplied = applyPermutation(sBoxIterApplied);
            print(betaIterApplied, "beta");
            print(getRoundKey(i), "k"+i);
            output = applyPermutation(getRoundKey(i)) ^ betaIterApplied;
            print(output, "+ k"+i);
        }

        System.out.println("-----");
        // Verkürzte 0.round
        var sBoxApplied = applySBox(output, this::getSBoxInversed);
        print(sBoxApplied, "sBox");
        print(getRoundKey(0), "k"+(0));
        var lastKeyApplied = sBoxApplied ^ getRoundKey(0);
        print(lastKeyApplied, "after k"+(0));
        return lastKeyApplied;
    }

    private int applyPermutation(int input) {
        var output = new String[keyLength];
        var inputBitString = String.format("%16s", Integer.toBinaryString(input)).replace(' ', '0');
        for(int i = 0; i < keyLength; i++) {
            var permutPos = getPermutationValue(i);
            output[permutPos] = String.valueOf(inputBitString.charAt(i));
        }
        var outputBitString = String.join("", output);
        return Integer.parseInt(outputBitString, 2);
    }

    private int applySBox(int input, Function<Integer,Integer> getMappingValue) {
        var mask1 = input & 0b1111_0000_0000_0000;
        var shift1 = getMappingValue.apply(mask1 >> 12) << 12;

        var mask2 = input & 0b1111_0000_0000;
        var shift2 = getMappingValue.apply(mask2 >> 8) << 8;

        var mask3 = input & 0b0000_1111_0000;
        var shift3 = getMappingValue.apply(mask3 >> 4) << 4;

        var mask4 = input & 0b0000_0000_1111;
        var shift4 = getMappingValue.apply(mask4);

        return shift1 | shift2 | shift3 | shift4;
    }

    private int getRoundKey(int i) {
        var k = key.substring(4*i, 4*i+keyLength);
        return Integer.parseInt(k, 2);
    }

    private int getSBoxValue(int searchValue) {
        for (int[] box : sBox) {
            if (box[0] == searchValue) return box[1];
        }
        throw new IllegalArgumentException("Value not found");
    }

    private int getSBoxInversed(int searchValue) {
        for (int[] box : sBox) {
            if (box[1] == searchValue) return box[0];
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