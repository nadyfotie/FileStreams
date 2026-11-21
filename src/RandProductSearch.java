import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandProductSearch extends JFrame {
    private JTextField searchField = new JTextField(20);
    private JTextArea result = new JTextArea();
    private JButton searchButton = new JButton("Search");


    public RandProductSearch() {
        setTitle("Product Search");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel upperPanel = new JPanel();
        JPanel lowerPanel = new JPanel();
        JButton quit = new JButton("Quit");


        upperPanel.add(new JLabel(" Product Name:"));
        upperPanel.add(searchField);
        upperPanel.add(searchButton);

        lowerPanel.add(quit);

        add(upperPanel, BorderLayout.NORTH);
        add(new JScrollPane(result), BorderLayout.CENTER);
        add(lowerPanel, BorderLayout.SOUTH);

        //Action listeners
        quit.addActionListener(e -> System.exit(0));
        searchButton.addActionListener(e -> searchRecords());
        result.setEditable(false);













    }
    private void searchRecords() {
        String target = searchField.getText().trim();
        if (target.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter part of a product name.");
            return;
        }

        StringBuilder out = new StringBuilder();
        int matches = 0;

        try (RandomAccessFile raf = new RandomAccessFile("ProductData.bin", "r")) {
            long numRecords = raf.length() / Product.RECORD_SIZE;

            for (int i = 0; i < numRecords; i++) {
                raf.seek(i * Product.RECORD_SIZE);      // jump to record i
                Product p = Product.readFrom(raf);      // read its 240 bytes

                if (p.getName().toLowerCase().contains(target.toLowerCase())) {
                    out.append(String.format("%-6s  %-15s  %-28s %10.2f%n",
                            p.getID(), p.getName(), p.getDescription(), p.getCost()));
                    matches++;
                }
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "No ProductData.bin found. Run RandProductMaker first.");
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
            return;
        }

        if (matches == 0) {
            result.setText("No products match \"" + target + "\".");
        } else {
            result.setText(out.toString());
        }
    }
}
