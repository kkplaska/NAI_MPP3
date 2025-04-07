import javax.swing.*;

public class Gui extends JFrame {
    private JPanel mainPanel;
    private JTextArea textArea1;
    private JLabel recognisedAnswerLabel;
    private JLabel availableLangsLabel;
    private JButton recogniseButton;
    private JButton clearTextAreaButton;
    private JLabel recognisedTextLabel;


    public Gui(double alpha, int epochs) {
        this.setVisible(true);
        this.setTitle("NAI MPP3 S30213");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(mainPanel);
        this.setSize(800, 600);

        this.textArea1.setLineWrap(true);

        this.recognisedAnswerLabel.setVisible(false);
        this.recognisedTextLabel.setVisible(false);

        Operator operator = new Operator(alpha, epochs);

        this.availableLangsLabel.setText(operator.getLanguages().toString());

        this.recogniseButton.addActionListener(e -> {
            MyVector recogniseVector = new MyVector(
                    Operator.getCharMap(this.textArea1.getText()).values().stream().mapToDouble(val -> val).toArray()
            );
            recogniseVector.setNormalizeVector();
            operator.recognizeAndSetGroup(recogniseVector);

            this.recognisedAnswerLabel.setText(recogniseVector.getGroup());

            this.recognisedAnswerLabel.setVisible(true);
            this.recognisedTextLabel.setVisible(true);
        });

        this.clearTextAreaButton.addActionListener(e -> {
            this.textArea1.setText("");
            this.recognisedAnswerLabel.setText("[__]");
            this.recognisedAnswerLabel.setVisible(false);
            this.recognisedTextLabel.setVisible(false);
        });
    }
}
