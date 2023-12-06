import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper extends JFrame
{
    JTextField urlTextField;
    DefaultTableModel tableModel;
    JTable jTable;
    JComboBox<String> regexComboBox;
    JButton btn;
    JButton resetButton;
    HashSet<String> matches = new HashSet<String>();


    public Scraper() 
    {
        super("Scrape the Application");

        setLayout(new BorderLayout());


        urlTextField = new JTextField("Enter URL...");
        add(urlTextField, BorderLayout.NORTH);


        // String columns[] = {"ID", "NAME", "SALARY"};
        // String data[][] = {{"1", "Jake", "55000"}, {"2", "Tom", "30000"}, {"3", "Jack", "80000"}};

        tableModel = new DefaultTableModel();
        jTable = new JTable(tableModel);

        tableModel.addColumn("Line #");
        tableModel.addColumn("Result");

        JScrollPane scrollPane = new JScrollPane(jTable);
        add(scrollPane);


        JPanel southJpanel = new JPanel();


        //regexTextField = new JTextField("Enter a Regex", 25);

        regexComboBox = new JComboBox<String>();
        regexComboBox.addItem("\\d{3}\\-\\d{3}\\-\\d{4}");
        regexComboBox.addItem("[0-9]");
        regexComboBox.addItem("[A-Za-z0-9\\.]+\\@[A-Za-z0-9]+\\.[A-Za-z0-9]+");
        
        southJpanel.add(regexComboBox);
        


        btn = new JButton("Click Me!");
        btn.addActionListener(this::searchButton); //adds the action event to the button. same as e -> searchButton(e)
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this::Reset);
        southJpanel.add(btn);
        southJpanel.add(resetButton);



        add(southJpanel, BorderLayout.SOUTH);
        

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(1500, 200);
        setSize(700, 700);
        setVisible(true);
    }

    public void searchButton(ActionEvent e)
    {
        tableModel.setRowCount(0);
        try
        {
            URL url = new URL(urlTextField.getText());
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = null;


            while((line = bufferedReader.readLine()) != null)
            {
                //regex pattern matching
                Pattern pattern = Pattern.compile(regexComboBox.getSelectedItem().toString());
                Matcher matcher = pattern.matcher(line);
                //add to our table

                if(matcher.find())
                {
                    if(matches.contains(matcher.group()))
                    {

                    }
                    else 
                    {
                        matches.add(matcher.group());
                        tableModel.addRow(new Object[]{String.valueOf(tableModel.getRowCount() + 1), matcher.group()});
                    }
                }
            }



        }
        catch (Exception exception)
        {

        }
    }

    public void Reset(ActionEvent e) //reset button
    {
        tableModel.setRowCount(0);
        matches.clear(); //clears HashSet so you can rerun the query
    }




}
