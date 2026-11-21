
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandProductMaker extends JFrame {
    private JTextField idfield = new JTextField();
    private JTextField namefield = new JTextField();
    private JTextField descfield = new JTextField();
    private JTextField costfield = new JTextField();
    private JTextField countfield = new JTextField();
    private int recordCount =0;

    public RandProductMaker(){
        setTitle("RandProductMaker");
        setSize(500,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(5,2,5,5));
        JLabel title = new JLabel("Rand Product Maker");
        JPanel buttonPanel = new JPanel();
        JButton quit = new JButton("Quit");
        JButton add = new JButton("Add");

        form.add(new JLabel("ID (6 characters):"));
        form.add(idfield);

        form.add(new JLabel("Name (35 characters):"));
        form.add(namefield);

        form.add(new JLabel("Description (75 characters):"));
        form.add(descfield);

        form.add(new JLabel("Cost:"));
        form.add(costfield);

        form.add(new JLabel("Count:"));
        form.add(countfield);

        buttonPanel.add(quit);
        buttonPanel.add(add);


        add(form, BorderLayout.CENTER);
        add(title, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        countfield.setEditable(false);
        countfield.setText("0");

        //Action Listener
        quit.addActionListener(e -> System.exit(0));
        add.addActionListener(e -> addRecord());



    }
    private void addRecord(){
        String id = idfield.getText().trim();
        String name = namefield.getText().trim();
        String desc = descfield.getText().trim();
        String costText = costfield.getText().trim();

        if (id.isEmpty() || name.isEmpty() || desc.isEmpty() || costText.isEmpty())
        {
            JOptionPane.showMessageDialog(this, "Please fill all the fields!");
            return;
        }
        double cost;

        try {
             cost = Double.parseDouble(costText);
        }catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "Please enter a number!");
            return;

        }
        Product p = new Product(id,name, desc, cost);
        try(RandomAccessFile raf = new RandomAccessFile("ProductData.bin", "rw")){
            raf.seek(raf.length());
            p.writeTo(raf);

        }catch(IOException ex){
            JOptionPane.showMessageDialog(this, "Something went wrong!" + ex.getMessage());
            return;
        }
        recordCount++;
        countfield.setText(String.valueOf(recordCount));

        idfield.setText("");
        namefield.setText("");
        descfield.setText("");
        costfield.setText("");



    }





}