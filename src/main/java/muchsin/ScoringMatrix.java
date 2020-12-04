package muchsin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScoringMatrix {

    int[][] subsMat;
    HashMap<Character, Integer> alphabetIndex;

    public ScoringMatrix(){

        char[] standarDNA = {'A', 'C', 'G', 'T'};

        int[][] subsMat = new int[4][4];
        HashMap<Character, Integer> alphabetIndex = new HashMap<>();
        for(int idx1 = 0; idx1 < 4; idx1++) {
            alphabetIndex.put(standarDNA[idx1], idx1);
            for(int idx2=0; idx2 < 4; idx2++) {
                if(idx1 == idx2) {
                    subsMat[idx1][idx2] = 4;
                } else {
                    subsMat[idx1][idx2] = -1;
                }
            }
        }

        this.subsMat = subsMat;
        this.alphabetIndex = alphabetIndex;
    }

    public int[][] getSubsMat() {
        return subsMat;
    }

    private void setSubsMat(int[][] subsMat) {
        this.subsMat = subsMat;
    }

    public HashMap<Character, Integer> getAlphabetIndex() {
        return alphabetIndex;
    }

    private void setAlphabetIndex(HashMap<Character, Integer> alphabetIndex) {
        this.alphabetIndex = alphabetIndex;
    }

    public static ScoringMatrix parseFromFile(String filePath) throws IOException {

        int[][] subsMat = new int[0][];
        HashMap<Character, Integer> alphabetIndex = new HashMap<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

        String line;
        boolean header = true;

        while ((line=br.readLine()) != null) {

            if(line.startsWith("#")) {
                continue;
            }

            List<String> allMatches = new ArrayList<String>();
            Matcher m = Pattern.compile("\\S+").matcher(line);
            while (m.find()) {
                allMatches.add(m.group());
            }

            if(header) {

                subsMat = new int[allMatches.size()][allMatches.size()];
                for(int idx = 0; idx < allMatches.size(); idx++) {
                    alphabetIndex.put(allMatches.get(idx).charAt(0), idx);
                }

                header = false;
                continue;

            }

            int idxFirst = alphabetIndex.get(allMatches.get(0).charAt(0));
            for(int idx=1; idx < allMatches.size(); idx++) {
                int idxSecond = idx - 1;
                subsMat[idxFirst][idxSecond] = Integer.parseInt(allMatches.get(idx));
            }

        }

        br.close();

        ScoringMatrix sm = new ScoringMatrix();
        sm.setSubsMat(subsMat);
        sm.setAlphabetIndex(alphabetIndex);

        return sm;
    }
}
