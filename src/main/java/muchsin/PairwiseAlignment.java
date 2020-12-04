package muchsin;

public interface PairwiseAlignment {

    PairwiseAlignment align(String querySeq, String refSeq, ScoringMatrix subsMat, int gapOpen, int gapExtend);

    default String[] getAlignment(String querySeq, String refSeq, int[][] backtrackPath) {

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
    void backtracking();
    void computeScoreMatrix(String querySeq, String refSeq, ScoringMatrix subsMat, int gapOpen, int gapExtend);

}
