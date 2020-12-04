package muchsin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GotohAlignLocal implements PairwiseAlignment {

    int score;
    int[][] insertionScoreMatrix;
    int[][] deletionScoreMatrix;
    int[][] substitutionScoreMatrix;
    String[][] alignments;
    String querySequence;
    String referenceSequence;

    public void computeScoreMatrix(String querySeq, String refSeq, ScoringMatrix subsMat,
                                               int gapOpen, int gapExtend) {

        int[][] substitutionScore = new int[querySeq.length()+1][refSeq.length()+1];
        int[][] insertionScore = new int[querySeq.length()+1][refSeq.length()+1];
        int[][] deletionScore = new int[querySeq.length()+1][refSeq.length()+1];

        int[][] substitutionMatrix = subsMat.getSubsMat();
        HashMap<Character, Integer> alphabetIndex = subsMat.getAlphabetIndex();

        for(int idx = 1; idx < querySeq.length()+1; idx++) {
            deletionScore[idx][0] = Integer.MIN_VALUE/2;
        }

        for(int idx = 1; idx < refSeq.length()+1; idx++) {
            insertionScore[0][idx] = Integer.MIN_VALUE/2;
        }

        for(int idx_i = 1; idx_i < querySeq.length()+1; idx_i++) {
            for(int idx_j = 1; idx_j < refSeq.length()+1; idx_j++) {

                int matchScore = substitutionMatrix[alphabetIndex.get(querySeq.charAt(idx_i-1))][alphabetIndex.get(refSeq.charAt(idx_j-1))];

                int[] insertCand = {substitutionScore[idx_i-1][idx_j]+gapOpen+gapExtend,
                        insertionScore[idx_i-1][idx_j]+gapExtend};
                insertionScore[idx_i][idx_j] = maxIntArray(insertCand);

                int[] deleteCand = {substitutionScore[idx_i][idx_j-1]+gapOpen+gapExtend,
                        deletionScore[idx_i][idx_j-1]+gapExtend};
                deletionScore[idx_i][idx_j] = maxIntArray(deleteCand);

                int[] subsCand = {substitutionScore[idx_i-1][idx_j-1]+matchScore,
                        insertionScore[idx_i][idx_j], deletionScore[idx_i][idx_j], 0};
                substitutionScore[idx_i][idx_j] = maxIntArray(subsCand);
            }
        }

        int maxScore = maxIntArray2D(substitutionScore);

        this.querySequence = querySeq;
        this.referenceSequence = refSeq;
        this.score = maxScore;
        this.insertionScoreMatrix = insertionScore;
        this.deletionScoreMatrix = deletionScore;
        this.substitutionScoreMatrix = substitutionScore;

    }

    public String getQuerySeq() {
        return this.querySequence;
    }

    public String getReferenceSeq() {
        return this.referenceSequence;
    }

    public String[][] getAlignments() {
        return this.alignments;
    }

    private static int maxIntArray(int[] arr) {
        int maxVal = arr[0];
        for(int val: arr) {
            if(val > maxVal) {
                maxVal = val;
            }
        }
        return maxVal;
    }

    private static int maxIntArray2D(int[][] arr) {
        int maxVal = arr[0][0];
        for(int[] vals: arr) {
            for(int val: vals) {
                if(val > maxVal) {
                    maxVal = val;
                }
            }
        }

        return maxVal;
    }

    private static int[][] findValue2D(int[][] arr, int searchVal) {

        ArrayList<Integer> valIdx1 = new ArrayList<>();
        ArrayList<Integer> valIdx2 = new ArrayList<>();

        for(int idx_i=0; idx_i<arr.length; idx_i++) {
            for(int idx_j=0; idx_j<arr[idx_i].length; idx_j++) {
                if(arr[idx_i][idx_j] == searchVal) {
                    valIdx1.add(idx_i);
                    valIdx2.add(idx_j);
                }
            }
        }

        int[][] valIdx = new int[2][valIdx1.size()];
        for(int idx = 0; idx < valIdx1.size(); idx++) {
            valIdx[0][idx] =valIdx1.get(idx);
            valIdx[1][idx] =valIdx2.get(idx);
        }

        return valIdx;
    }

    public ArrayList<int[][]> backtracking(){

        int[][] traceIdxs = findValue2D(this.substitutionScoreMatrix, this.score);

        ArrayList<int[][]> backtrackPath = new ArrayList<>();
        for(int idx=0; idx<traceIdxs[0].length; idx++) {

            int idx_i = traceIdxs[0][idx];
            int idx_j = traceIdxs[1][idx];

            ArrayList<Integer> path = backTrack(idx_i, idx_j);

            int[][] backPath = new int[2][path.size()/2];
            for(int pos = path.size(); pos > 0; pos=pos-2){
                backPath[0][(pos/2)-1] = path.get(path.size() - pos);
                backPath[1][(pos/2)-1] = path.get(path.size() - pos + 1);
            }

            backtrackPath.add(backPath);

        }

        return backtrackPath;

    }

    private ArrayList<Integer> backTrack(int idx_i, int idx_j) {

        ArrayList<Integer> tempPath = new ArrayList<>();

        while(this.substitutionScoreMatrix[idx_i][idx_j] > 0) {

            tempPath.add(idx_i);
            tempPath.add(idx_j);

            if(this.substitutionScoreMatrix[idx_i][idx_j] == this.insertionScoreMatrix[idx_i][idx_j]) {
                idx_i--;
            } else if(this.substitutionScoreMatrix[idx_i][idx_j] == this.deletionScoreMatrix[idx_i][idx_j]) {
                idx_j--;
            } else {

                if(idx_i==0) {
                    idx_j--;
                } else if (idx_j==0) {
                    idx_i--;
                } else {
                    idx_i--;
                    idx_j--;
                }
            }
        }
        tempPath.add(idx_i);
        tempPath.add(idx_j);

        return tempPath;
    }

    public int[][] getScoringMatrix() {
        return this.substitutionScoreMatrix;
    }

    public int getScore() {
        return this.score;
    }

    public void align(String querySeq, String refSeq, ScoringMatrix subsMat, int gapOpen, int gapExtend) {

        computeScoreMatrix(querySeq, refSeq, subsMat, gapOpen, gapExtend);
        ArrayList<int[][]> backtrackPath = backtracking();

        String[][] alignments = new String[backtrackPath.size()][2];

        for(int idx=0; idx < backtrackPath.size(); idx++) {
            String[] alignSeq = buildAlignment(backtrackPath.get(idx));
            alignments[idx] = alignSeq;
        }

        this.alignments = alignments;
    }
}
