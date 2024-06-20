import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class News extends JFrame {
    private static final String API_KEY = "93210547177545be9983432ffe4f5d72";
    private static final String NEWS_API_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_KEY;

    private final JTextArea newsTextArea;
    private final JTextArea searchTextArea;

    private final File newsFile;

    public News() {
        setTitle("News App");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));

        // Upper panel for displaying news
        JPanel upperPanel = new JPanel(new BorderLayout());
        JButton printNewsButton = new JButton("Print News");
        newsTextArea = new JTextArea();
        newsTextArea.setEditable(false);
        upperPanel.add(printNewsButton, BorderLayout.NORTH);
        upperPanel.add(new JScrollPane(newsTextArea), BorderLayout.CENTER);

        // Lower panel for searching news
        JPanel lowerPanel = new JPanel(new BorderLayout());
        JButton searchNewsButton = new JButton("Search News");
        searchTextArea = new JTextArea();
        lowerPanel.add(searchNewsButton, BorderLayout.NORTH);
        lowerPanel.add(new JScrollPane(searchTextArea), BorderLayout.CENTER);

        mainPanel.add(upperPanel);
        mainPanel.add(lowerPanel);
        add(mainPanel);

        // Initialize news file
        newsFile = new File("news.txt");
        if (!newsFile.exists()) {
            try {
                newsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Action Listeners
        printNewsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayNews();
            }
        });

        searchNewsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String searchTerm = JOptionPane.showInputDialog(News.this, "Enter search term:");
                if (searchTerm != null && !searchTerm.isEmpty()) {
                    searchNews(searchTerm);
                }
            }
        });
    }

    private void displayNews() {
        try {
            Set<String> uniqueNews = new HashSet<>();
            String currentNews = readNewsFromFile();
            if (!currentNews.isEmpty()) {
                newsTextArea.setText(currentNews);
            } else {
                // Fetch news from API
                URL url = new URL(NEWS_API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Extract titles from response
                String responseData = response.toString();
                int index = responseData.indexOf("\"title\":\"");
                while (index != -1) {
                    int startIndex = index + "\"title\":\"".length();
                    int endIndex = responseData.indexOf("\"", startIndex);
                    String title = responseData.substring(startIndex, endIndex);
                    if (uniqueNews.add(title)) {
                        newsTextArea.append(title + "\n");
                        writeNewsToFile(title);
                    }
                    index = responseData.indexOf("\"title\":\"", endIndex);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void searchNews(String searchTerm) {
        try {
            StringBuilder matchingNews = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(newsFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains(searchTerm.toLowerCase())) {
                    matchingNews.append(line).append("\n");
                }
            }
            reader.close();
            if (matchingNews.length() == 0) {
                searchTextArea.setText("No news found matching the keyword.");
            } else {
                searchTextArea.setText(matchingNews.toString());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String readNewsFromFile() {
        StringBuilder news = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(newsFile));
            String line;
            while ((line = reader.readLine()) != null) {
                news.append(line).append("\n");
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return news.toString();
    }

    private void writeNewsToFile(String news) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(newsFile, true));
            writer.write(news);
            writer.newLine();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new News().setVisible(true);
            }
        });
    }
}
