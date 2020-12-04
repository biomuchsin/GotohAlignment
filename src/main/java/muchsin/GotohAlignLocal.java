package muchsin;

import java.util.HashMap;

public class GotohAlignLocal implements PairwiseAlignment {

    int score;
    int[][] insertionScoreMatrix;
    int[][] deletionScoreMatrix;
    int[][] substitutionScoreMatrix;
    String[] alignment;
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


    public void backtracking(){

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
