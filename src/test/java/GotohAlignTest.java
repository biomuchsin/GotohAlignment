import muchsin.GotohAlignLocal;
import muchsin.ScoringMatrix;
import org.junit.Test;

public class GotohAlignTest {

    @Test
    public void testAlignSimple(){

        String querySeq = "TTTTAAAAGGCCTTTTTTT";
        String refSeq = "AAAAAACCGGCCTTGGGGG";
        int gapOpen = -11;
        int gapExtend = -1;
        ScoringMatrix scoringMatrix = new ScoringMatrix();

        GotohAlignLocal gotohAlign = new GotohAlignLocal();
        gotohAlign.align(querySeq, refSeq, scoringMatrix, gapOpen, gapExtend);

        for(String[] alignSeq: gotohAlign.getAlignments()) {
            System.out.println(alignSeq[0]);
            System.out.println(alignSeq[1]);
            System.out.println("###############################################");
        }

    }

}
