package muchsin;

import java.util.ArrayList;
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
            deletionScore[idx][0] = Integer.MIN_VALUE;
        }

        for(int idx = 1; idx < refSeq.length()+1; idx++) {
            insertionScore[0][idx] = Integer.MIN_VALUE;
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

    private int maxIntArray(int[] arr) {
        int maxVal = arr[0];
        for(int val: arr) {
            if(val > maxVal) {
                maxVal = val;
            }
        }
        return maxVal;
    }

    private int maxIntArray2D(int[][] arr) {
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

    private int[][] findValue2D(int[][] arr, int searchVal) {

        ArrayList<Integer> valIdx1 = new ArrayList<Integer>();
        ArrayList<Integer> valIdx2 = new ArrayList<Integer>();

        for(int idx_i=0; idx_i<arr.length; idx_i++) {
            for(int idx_j=0; idx_j<arr[idx_i].length; idx_j++) {
                if(arr[idx_i][idx_j] == searchVal) {
                    valIdx1.add(idx_i);
                    valIdx2.add(idx_j)
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

            int traceScore = this.score;

            int idx_i = traceIdxs[0][idx];
            int idx_j = traceIdxs[1][idx];

            ArrayList<ArrayList<Integer>> altPaths = backTrack(new ArrayList<>(), new ArrayList<>(), idx_i, idx_j);

            
        }

    }

    private ArrayList<ArrayList<Integer>> backTrack(ArrayList<ArrayList<Integer>> tempResult,
                                                    ArrayList<Integer> tempPath,
                                                    int idx_i, int idx_j) {

        if(this.substitutionScoreMatrix[idx_i][idx_j] == 0) {
            tempResult.add(tempPath);
        }

        while(this.substitutionScoreMatrix[idx_i][idx_j] > 0) {

            tempPath.add(idx_i);
            tempPath.add(idx_j);

            if(this.substitutionScoreMatrix[idx_i][idx_j] == this.insertionScoreMatrix[idx_i][idx_j]) {
                backTrack(tempResult, tempPath, idx_i-1, idx_j);
            }

            if(this.substitutionScoreMatrix[idx_i][idx_j] == this.deletionScoreMatrix[idx_i][idx_j]) {
                backTrack(tempResult, tempPath, idx_i, idx_j-1);
            }

            if((this.substitutionScoreMatrix[idx_i][idx_j] != this.insertionScoreMatrix[idx_i][idx_j]) &&
                    (this.substitutionScoreMatrix[idx_i][idx_j] != this.deletionScoreMatrix[idx_i][idx_j])) {

                if(idx_i==0) {
                    backTrack(tempResult, tempPath, idx_i, idx_j-1);
                } else if (idx_j==0) {
                    backTrack(tempResult, tempPath, idx_i-1, idx_j);
                } else {
                    backTrack(tempResult, tempPath, idx_i-1, idx_j-1);
                }

            }

        }

        return tempResult;

    }

    public int[][] getScoringMatrix() {
        return new int[0][];
    }

    public int getScore() {
        return 0;
    }

    public PairwiseAlignment align(String querySeq, String refSeq, ScoringMatrix subsMat, int gapOpen, int gapExtend) {
        return null;
    }
}
