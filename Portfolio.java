import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Portfolio extends JFrame {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private final Map<String, Double> stocks = new HashMap<>();
    private final Map<String, Integer> stockQuantities = new HashMap<>();
    private double walletBalance = 10000.00; // Initial wallet balance
    private final JLabel walletLabel = new JLabel("Wallet: $" + DECIMAL_FORMAT.format(walletBalance));
    {
        walletLabel.setFont(new Font("Arial", Font.PLAIN, 40)); // Set wallet label font size to 24
    }
    private final JTextArea stocksTextArea = new JTextArea();

    public Portfolio() {
        setTitle("Stock Trading App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(1, 1));
        stocksTextArea.setEditable(false);
        centerPanel.add(new JScrollPane(stocksTextArea));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton buyButton = createButton("Buy", Color.GREEN);
        JButton sellButton = createButton("Sell", Color.RED);
        bottomPanel.add(buyButton);
        bottomPanel.add(sellButton);

        add(walletLabel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        generateStocks();

        buyButton.addActionListener(e -> buyStock());
        sellButton.addActionListener(e -> sellStock());

        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(color);
        button.setFont(new Font("Arial", Font.PLAIN, 40)); // Set button font size to 20
        return button;
    }

    private void generateStocks() {
        String[] stockNames = {"AAPL", "MSFT", "GOOGL", "AMZN", "TSLA", "JPM", "JNJ", "NVDA", "V", "PG",
                "MA", "HD", "UNH", "BAC", "DIS", "PYPL", "ADBE", "CMCSA", "NFLX", "INTC",
                "ASML", "CSCO", "PEP", "ABNB", "T"};
        Random random = new Random();
        for (String stockName : stockNames) {
            double price = 10 + random.nextDouble() * 90; // Random price between 10 and 100
            stocks.put(stockName, price);
            stockQuantities.put(stockName, 0); // Initialize the quantity of each stock to 0
        }
        displayStocks();
    }

    private void displayStocks() {
        JPanel stocksPanel = new JPanel(new GridLayout(0, 5, 10, 10)); // 5 columns, variable rows, gap of 10 pixels
        stocksPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        for (Map.Entry<String, Double> entry : stocks.entrySet()) {
            JPanel stockPanel = new JPanel(new BorderLayout());
            stockPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to each stock panel

            JLabel nameLabel = new JLabel(entry.getKey());
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            stockPanel.add(nameLabel, BorderLayout.NORTH);

            JLabel priceLabel = new JLabel("$" + DECIMAL_FORMAT.format(entry.getValue()));
            priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
            stockPanel.add(priceLabel, BorderLayout.CENTER);

            stocksPanel.add(stockPanel);
        }

        JScrollPane scrollPane = new JScrollPane(stocksPanel);
        stocksTextArea.setText(""); // Clear existing text
        stocksTextArea.setLayout(new BorderLayout());
        stocksTextArea.add(scrollPane, BorderLayout.CENTER);
        stocksTextArea.revalidate(); // Refresh the layout
    }

    private void buyStock() {
        String stockName = JOptionPane.showInputDialog(this, "Enter stock name to buy:");
        if (stockName != null && stocks.containsKey(stockName)) {
            int quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter quantity to buy:"));
            double price = stocks.get(stockName);
            double totalCost = price * quantity;
            if (totalCost <= walletBalance) {
                walletBalance -= totalCost;
                stockQuantities.put(stockName, stockQuantities.get(stockName) + quantity); // Update the quantity of the bought stock
                updateWalletBalance();
                JOptionPane.showMessageDialog(this, "Stock bought successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient funds to buy!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid stock name!");
        }
    }

    private void sellStock() {
        String stockName = JOptionPane.showInputDialog(this, "Enter stock name to sell:");
        if (stockName != null && stocks.containsKey(stockName)) {
            int quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter quantity to sell:"));
            int ownedQuantity = stockQuantities.get(stockName);
            if (quantity <= ownedQuantity) {
                double price = stocks.get(stockName);
                double totalSale = price * quantity;
                walletBalance += totalSale;
                stockQuantities.put(stockName, ownedQuantity - quantity); // Update the quantity of the sold stock
                updateWalletBalance();

                // Display another set of prices
                displayStocks();

                JOptionPane.showMessageDialog(this, "Stock sold successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "You don't own enough quantity to sell!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid stock name!");
        }
    }

    private void updateWalletBalance() {
        walletLabel.setText("Wallet: $" + DECIMAL_FORMAT.format(walletBalance));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Portfolio::new);
    }
}

