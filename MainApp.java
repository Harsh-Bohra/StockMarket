import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {
    private JPanel signInPanel;
    private JPanel signUpPanel;
    private JFrame newsFrame;

    public MainApp() {
        setTitle("Main Menu");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        signInPanel = new Sign_In(this);

        signUpPanel = new Sign_Up(this, this);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton signInButton = new JButton("Sign In");
        JButton signUpButton = new JButton("Sign Up");
        JButton newsButton = new JButton("News");

        signInButton.addActionListener(e -> {
            setContentPane(signInPanel);
            revalidate();
            repaint();
        });

        signUpButton.addActionListener(e -> {
            setContentPane(signUpPanel);
            revalidate();
            repaint();
        });

        newsButton.addActionListener(e -> {
            newsFrame = new News();
            newsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newsFrame.setVisible(true);
        });

        panel.add(signInButton);
        panel.add(signUpButton);
panel.add(newsButton);

        add(panel, BorderLayout.CENTER);
    }

    public void showPortfolioPanel() {
        Portfolio portfolioPanel = new Portfolio();
        JFrame portfolioFrame = new JFrame("Portfolio");
        portfolioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        portfolioFrame.setContentPane(portfolioPanel); 
        portfolioFrame.pack();
        portfolioFrame.setLocationRelativeTo(null); 
        portfolioFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApp mainApp = new MainApp();
            mainApp.setVisible(true);
        });
    }
}
