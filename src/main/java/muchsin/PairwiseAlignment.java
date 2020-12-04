package muchsin;

import java.util.ArrayList;

public interface PairwiseAlignment {

    void align(String querySeq, String refSeq, ScoringMatrix subsMat, int gapOpen, int gapExtend);

    default String[] buildAlignment(int[][] backtrackPath) {

        String querySeq = getQuerySeq();
        String refSeq = getReferenceSeq();

        char[] queryChar = new char[backtrackPath[0].length];
        char[] refChar = new char[backtrackPath[0].length];

        for(int idx = 0; idx < backtrackPath[0].length-1; idx++) {

            int queryIdx = backtrackPath[0][idx];
            int refIdx = backtrackPath[1][idx];
            int queryNext = backtrackPath[0][idx+1];
            int refNext = backtrackPath[1][idx+1];

            if(queryIdx == queryNext) {
                queryChar[idx] = '-';
                refChar[idx] = refSeq.charAt(refIdx);
            } else if (refIdx == refNext) {
                queryChar[idx] = querySeq.charAt(queryIdx);
                refChar[idx] = '-';
            } else {
                queryChar[idx] = querySeq.charAt(queryIdx);
                refChar[idx] = refSeq.charAt(refIdx);
            }
        }

        String[] alignment = {String.valueOf(queryChar), String.valueOf(refChar)};

        return alignment;
    }

    int[][] getScoringMatrix();
    int getScore();
    ArrayList<int[][]> backtracking();
    void computeScoreMatrix(String querySeq, String refSeq, ScoringMatrix subsMat, int gapOpen, int gapExtend);
    String getQuerySeq();
    String getReferenceSeq();
    String[][] getAlignments();

}
