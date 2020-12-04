import muchsin.GotohAlignLocal;
import muchsin.ScoringMatrix;
import org.junit.Test;

import java.io.IOException;

public class GotohAlignTest {

    @Test
    public void testAlignSimple(){

        String querySeq = "GGGGGTTTTTAAAAAGGGGG";
        String refSeq = "AAAAATTTTTGGGGGAAAAATTTTT";
        int gapOpen = -2;
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

    @Test
    public void testAlignBLOSUM() throws IOException {

        String querySeq = "MEKRSSDESVGNKGSDGGSGGLSRYDNIFVLNMSSASKIERIVDRVKSLA" +
                "LKRFSRESLYKDWFRHMLDPCAGLVAPELGDDGSSEGGGNAAMIVGDREL" +
                "ARRPPFLPFSCLLITGTAGAGKTSSVQVLAANLDCVITGSTVISSQALSS" +
                "ALNRSRSAQIKTIFRTFGFNSRHVALADRVHLRRRDDVAFDGDVDPICQQ" +
                "QWRDLSTYWPVVSDIAIRALDGGKGRKDTDDLCRSNIIVIDECGVILRHM" +
                "LHVVVFFYYFYNALNDSELYRQRAAPCIVCVGSPTQSEALESRYDHRTQN" +
                "RDVQRGMDVLSALISDPVLSEYCDVAHNWVMFINNKRCLDLEFGDLLKHI" +
                "EFGLPLKSEHVEYLDRFVRPAGLIRDPAHAIDVTRLFISHAEVKRYFTAL" +
                "HDRVRIYSQHLIFEVPVYCVLNNSAFHEYCASMCTGEPTPRPETWFRKNL" +
                "ARISNYSQFTDHNLSEDIQVEELAQSCGGGGAGGDDDGFDLEEEMINETL" +
                "LTCRITFIRDSAVGVTAKTKACVVGYTGTFDDFAEILQKDLFIERTPCEQ" +
                "AVYAYSLISGLLFSAMYLFYSSPLTTPEILRDLSEIPLPDIPTLVIGANG" +
                "GDGARDSDDNDEYEEDLEGGGCFDGVSGGGSGNGGGEKYRRRLTSDDEDD" +
                "FYDLSYVDRGRQPEPPQLQPPPQPQPQPRLTMSAALPPRQIDEEISDVEM" +
                "LCYSDIYTDKFFLKYSIPPPVSSISFEEIVHIYTIFRDIFLARYRIMQKH" +
                "TKGAFGKTRLVTYNRRNVWRRKNCEIESQTGSFVGMLTFVSPSNNYVLEG" +
                "FTNHNVFIMDAERNRIHRRILEKGLPRLIVRDACGFLLILDYNVSKFSDV" +
                "IDGKSVHICTMVDYGVTSRMAMTIAKSQGIGLESVAIDFGDNPKNLKMSQ" +
                "IYVGISRVVDPDRLVLNTNPVRNTYENNTFITPFIRRALQNKDTTLVF*";
        String refSeq = "MSMTASSSTPRPTPKYDDALILNLSSAAKIERIVDKVKSLSRQRFAPEDF" +
                "SFQWFRSISRVERTTDNNPSAATTAAATTTVHSSASSSAAAAASSEAGGT" +
                "RVPCVDRWPFFPFRALLVTGTAGAGKTSSIQVLAANLDCVITGTTVIAAQ" +
                "NLSAILNRTRSAQVKTIYRVFGFVSKHVPLADSAVSHETLERYRVCEPHE" +
                "ETTIQRLQINDLLAYWPVIADIVDKCLNMWERKAASASAAAAAAACEDLS" +
                "ELCESNIIVIDECGLMLRYMLQVVVFFYYFYNALGDTRLYRERRVPCIIC" +
                "VGSPTQTEALESRYDHYTQNKSVRKGVDVLSALIQNEVLINYCDIADNWV" +
                "MFIHNKRCTDLDFGDLLKYMEFGIPLKEEHVAYVDRFVRPPSSIRNPSYA" +
                "AEMTRLFLSHVEVQAYFKRLHEQIRLSERHRLFDLPVYCVVNNRAYQELC" +
                "ELADPLGDSPQPVEFWFRQNLARIINYSQFVDHNLSSEITKEALRPAADV" +
                "VATNNSSVQAHGGGGSVIGSTGGNDETAFFQDDDTTTAPDSRETLLTLRI" +
                "TYIKGSSVGVNSKVRACVIGYQGTVERFVDILQKDTFIERTPCEQAAYAY" +
                "SLVSGLLFSAMYYFYVSPYTTEEMLRELARVELPDVSSLCAAAAATAAAP" +
                "AWSGGENPINNHVDADSSQGGQSVPVSQRMEHGQEETHDIPCLSNHHDDS" +
                "DAITDAELMDHTSLYADPFFLKYVKPPSLALLSFEETVHMYTTFRDIFLK" +
                "RYQLMQRLTGGRFATLPLVTYNRRNVVFKANCQISSQTGSFVGMLSHVSP" +
                "AQTYTLEGYTSDNVLSLPSDRHRIHPEVVQRGLSRLVLRDALGFLFVLDV" +
                "NVSRFVESAQGKSLHVCTTVDYGLTSRTAMTIAKSQGLSLEKVAVDFGDH" +
                "PKNLKMSHIYVAMSRVTDPEHLMMNVNPLRLPYEKNTAITPYICRALKDK" +
                "RTTLIF*";
        int gapOpen = -11;
        int gapExtend = -1;
        String matrixPath = "src/main/progfiles/matrices/blosum/BLOSUM50";
        ScoringMatrix sm = ScoringMatrix.parseFromFile(matrixPath);

        GotohAlignLocal gotohAlign = new GotohAlignLocal();
        gotohAlign.align(querySeq, refSeq, sm, gapOpen, gapExtend);
        int[][] mutationCount = gotohAlign.countMutation();

        for(int idx= 0; idx < gotohAlign.getAlignments().length; idx++) {
            System.out.print("Synonimous mutation: ");
            System.out.print(mutationCount[idx][0]);
            System.out.print("; Non-synonimous mutation: ");
            System.out.print(mutationCount[idx][1]);
            System.out.print("; Gap: ");
            System.out.println(mutationCount[idx][2]);
            System.out.println(gotohAlign.getAlignments()[idx][0]);
            System.out.println(gotohAlign.getAlignments()[idx][1]);
            System.out.println("###############################################");
        }

    }

}
