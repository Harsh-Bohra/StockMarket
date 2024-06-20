## Stock Market Simulator with News Reader 

Welcome to your one-stop shop for simulating stock trading and staying informed with the latest news! This Java application provides a user-friendly interface to manage your portfolio, buy and sell stocks, and access real-time news articles.

**Key Features:**

* **User Authentication:** Sign up and sign in to manage your personal portfolio and keep your finances secure. (Database integration included!)
* **Stock Trading:** Browse through a list of simulated stocks, view their dynamic prices, and buy or sell shares based on your investment strategy.
* **Real-Time News:** Access top headlines from the US using a free News API integration. 
* **Search Functionality:** Search for specific news articles to stay updated on market trends and company news.
* **Intuitive Interface:** Enjoy a clean and user-friendly interface with clear buttons, informative labels, and well-structured layouts.

**Getting Started:**

1. **Prerequisites:** 
    * Java Runtime Environment (JRE) 8 or later ([https://www.java.com/download/ie_manual.jsp](https://www.java.com/download/ie_manual.jsp))
2. **Download the Code:**
    * Clone this repository using Git (`git clone https://github.com/your-username/stock-market-simulator.git`).
    * Alternatively, download the ZIP file and extract the contents.
3. **Compile and Run:**
    * Open a terminal or command prompt and navigate to the project directory.
    * Compile the code using `javac MainApp.java Sign_Up.java Sign_In.java Portfolio.java News.java`.
    * Run the application using `java MainApp`.

**Database Setup (Optional):**

This application can be configured to connect to a MySQL database for user authentication. To enable this feature:

1. Update the database connection details (`DB_URL`, `USER`, and `PASS`) in the `Sign_Up.java` and `Sign_In.java` files with your MySQL credentials.
2. Ensure you have a MySQL server running and a database named `trademill` created.

**Customization:**

Feel free to customize the following aspects of the application:

* **Stock List:** Modify the list of simulated stocks and their initial prices in the `Portfolio.java` file.
* **News API Key:** Replace the placeholder API key (`API_KEY`) in `News.java` with your own key obtained from [https://newsapi.org/](https://newsapi.org/).

**Additional Notes:**

* This application is for educational purposes only and does not simulate real-world market conditions.
* Error handling and user input validation are implemented for a more robust experience.

We hope you enjoy using this Stock Market Simulator with News Reader!
# StockMarket
