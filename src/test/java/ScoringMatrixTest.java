import muchsin.ScoringMatrix;
import org.junit.Test;

import java.io.IOException;

public class ScoringMatrixTest {

    @Test
    public void testBuild() {
        ScoringMatrix sm = new ScoringMatrix();

        for (int[] x : sm.getSubsMat())
        {
            for (int y : x)
            {
                System.out.print(y + "\t");
            }
            System.out.println();
        }

    }

    @Test
    public void testBuildFromFile() throws IOException {
        String matrixPath = "src/main/progfiles/matrices/blosum/BLOSUM50";
        ScoringMatrix sm = ScoringMatrix.parseFromFile(matrixPath);

        for (int[] x : sm.getSubsMat())
        {
            for (int y : x)
            {
                System.out.print(y + "\t");
            }
            System.out.println();
        }
    }

}
