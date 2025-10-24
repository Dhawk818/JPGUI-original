import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.Font;
/**
 * Graphical user interface for decision support application.
 *
 * @author Dr. Jody Paul
 * @version 20241114.3
 */
public class JPGUI extends UserInterface {
    /** Default point size for JOptionPane dialog text. */
    private static final int DEFAULT_FONT_SIZE = 12;

    static { // Establish a common font for JOptionPane dialogs.
      Font font = new Font("Monospaced", Font.PLAIN, DEFAULT_FONT_SIZE);
      UIManager.put("OptionPane.messageFont", font);
    }

    @Override
    public void showIntroduction() {
        JOptionPane.showMessageDialog(null,
                                      INTRODUCTION,
                                      "Decision Support Aid",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public List<Alternative> getAlternatives() {
        List<Alternative> alternativeList = new ArrayList<Alternative>();
        String alternative = JOptionPane.showInputDialog(
            FIRST_ALT_PROMPT + " (Cancel to quit.)");
        while (alternative != null && !("".equals(alternative))) {
            alternativeList.add(new Alternative(alternative));
            alternative = JOptionPane.showInputDialog(ADDITIONAL_ALT_PROMT);
        }
        if (alternativeList.size() < 1) {
            System.err.println("No alternatives entered.");
            System.exit(2);
        }
        return alternativeList;
    }

    @Override
    public List<Factor> getFactors() {
        List<Factor> factorList = new ArrayList<Factor>();
        String factor = JOptionPane.showInputDialog(FIRST_FACTOR_PROMPT);
        while (factor != null && !("".equals(factor))) {
            factorList.add(new Factor(factor));
            factor = JOptionPane.showInputDialog(ADDITIONAL_FACTOR_PROMPT);
        }
        if (factorList.size() < 1) {
            System.err.println("No factors entered.");
            System.exit(2);
        }
        return factorList;
    }

    @Override
    public void getFactorRankings(final List<Factor> factorList,
                                  final int standard) {
        int lastFactor = factorList.size() - 1;
        for (int i = 0; i < lastFactor; i++) {
            String importance = JOptionPane.showInputDialog(
                "<HTML>If <B><SIZE=+1>"
                + factorList.get(lastFactor).getName()
                + "</SIZE></B> has an <I>importance</I> of <B>"
                + standard
                + "</B>,<BR>"
                + "how important is <B><SIZE=+1>"
                + factorList.get(i).getName()
                + "</SIZE></B>?</HTML>");
             if (importance == null || "".equals(importance)) {
                 factorList.get(i).setRank(standard);
             } else {
                 factorList.get(i).setRank(Integer.valueOf(importance));
             }
        }
        factorList.get(lastFactor).setRank(standard);
    }

    @Override
    public double[][] getCrossRankings(final List<Alternative> alternatives,
                                       final List<Factor> factors,
                                       final int standard) {
        double[][] crossRankings = new double[alternatives.size()]
                                             [factors.size()];
        for (int i = 0; i < factors.size(); i++) {
            int firstAlternative = 0;
            crossRankings[firstAlternative][i] = standard;
            for (int j = firstAlternative + 1; j < alternatives.size(); j++) {
                String rank = JOptionPane.showInputDialog(
                    "<HTML>Considering <B><SIZE=+1>"
                    + factors.get(i).getName()
                    + "</SIZE></B> only,<BR>"
                    + "if <B><SIZE=+1>"
                    + alternatives.get(firstAlternative).getDescriptor()
                    + "</SIZE></B> has a value of <B><SIZE=+1>"
                    + standard
                    + "</SIZE></B> "
                    + ",<BR>"
                    + "what value would you associate with <B><SIZE=+1>"
                    + alternatives.get(j).getDescriptor()
                    + "</SIZE></B><BR>"
                    + "(a higher value is <I>more desirable</I>)?</HTML>");
                if (rank == null || "".equals(rank)) {
                    crossRankings[j][i] = standard;
                } else {
                    crossRankings[j][i] = Integer.valueOf(rank);
                }
            }
        }
        return crossRankings;
    }

    @Override
    public void showResults(final List<Alternative> alternatives) {
        Alternative preferredAlternative = alternatives.get(0);
        String s = "<HTML>Preferred choice: <B>";
        s += preferredAlternative.getDescriptor();
        s += "</B>\n-----\n";
        for (Alternative a : alternatives) {
            s += a + "\n";
        }
        JOptionPane.showMessageDialog(null,
                                      s,
                                     "Decider Results",
                                     JOptionPane.INFORMATION_MESSAGE);
    }
}
