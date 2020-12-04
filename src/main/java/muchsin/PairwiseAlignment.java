package muchsin;

import java.util.ArrayList;

public interface PairwiseAlignment {

    void align(String querySeq, String refSeq, ScoringMatrix subsMat, int gapOpen, int gapExtend);

    default String[] buildAlignment(int[][] backtrackPath) {

        String querySeq = getQuerySeq();
        String refSeq = getReferenceSeq();

        char[] queryChar = new char[backtrackPath[0].length -1];
        char[] refChar = new char[backtrackPath[0].length -1];

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

        return new String[]{String.valueOf(queryChar), String.valueOf(refChar)};
    }

    int[][] getScoringMatrix();
    int getScore();
    ArrayList<int[][]> backtracking();
    void computeScoreMatrix(String querySeq, String refSeq, ScoringMatrix subsMat, int gapOpen, int gapExtend);
    String getQuerySeq();
    String getReferenceSeq();
    String[][] getAlignments();

    default int[][] countMutation() {

        int[][] mutationCounter = new int[getAlignments().length][3];
        for(int idx = 0; idx < getAlignments().length; idx++) {

            int synonimous = 0;
            int nonsynonimous = 0;
            int numGap = 0;

            for(int idx_char=0; idx_char < getAlignments()[idx][0].length(); idx_char++) {
               if(getAlignments()[idx][0].charAt(idx_char) == getAlignments()[idx][1].charAt(idx_char)) {
                   synonimous++;
               } else {

                   if((getAlignments()[idx][0].charAt(idx_char) == '-') || (getAlignments()[idx][1].charAt(idx_char) == '-')) {
                       numGap++;
                   } else {
                       nonsynonimous++;
                   }
               }
            }
            mutationCounter[idx] = new int[]{synonimous,nonsynonimous, numGap};
        }

        return mutationCounter;

    }

}
