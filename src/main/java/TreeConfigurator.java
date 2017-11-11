import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TreeConfigurator extends JFrame{
    private JSpinner kSpinner;
    private JSpinner countSpinner;
    private JCheckBox isRuleACheckBox;
    private JSpinner rSpinner;
    private JButton generateButton;
    private JButton cancelButton;
    private JPanel mainPanel;

    private boolean isRuleA;

    public TreeConfigurator() {
        isRuleA = false;


        kSpinner.setModel(new SpinnerNumberModel(5, 2, 10, 1));
        countSpinner.setModel(new SpinnerNumberModel(150, 3, 1000, 1));
        rSpinner.setModel(new SpinnerNumberModel(200, 1, 500, 1));

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateGraphView();
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setContentPane(mainPanel);
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void generateGraphView() {
        int k = (int)this.kSpinner.getValue();
        int count = (int)this.countSpinner.getValue();
        boolean isRuleA = this.isRuleACheckBox.isSelected();
        int r = (int)this.rSpinner.getValue();

        new GraphView(k, count, isRuleA, r);
    }
}
