import javax.swing.*;

public class PreRunGui extends JFrame {
    private JPanel mainPanel;
    private JButton confirmButton;
    private JTextField epochsField;
    private JTextField alfaField;

    public PreRunGui() {
        this.setVisible(true);
        this.setTitle("NAI MPP3 S30213");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(mainPanel);
        this.pack();

        confirmButton.addActionListener(e -> {
            new Gui(
                    Double.parseDouble(alfaField.getText()),
                    Integer.parseInt(epochsField.getText())
            );
            this.dispose();
        });


    }
}
