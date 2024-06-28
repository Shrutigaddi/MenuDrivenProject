package storemanaging;
import java.sql.*;
import java.util.Scanner;

public class storemanaging {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/store";
    private static final String USER = "root";
    private static final String PASS = "Root";
    private static Connection conn = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            boolean exit = false;
            while (!exit) {
                System.out.println("Store Management System");
                System.out.println("1. Manage Products");
                System.out.println("2. Manage Customers");
                System.out.println("3. Manage Orders");
                System.out.println("4. Manage Employees");
                System.out.println("5. Manage Categories");
                System.out.println("6. Exit");
                System.out.print("Select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        manageProducts();
                        break;
                    case 2:
                        manageCustomers();
                        break;
                    case 3:
                        manageOrders();
                        break;
                    case 4:
                        manageEmployees();
                        break;
                    case 5:
                        manageCategories();
                        break;
                    case 6:
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void manageProducts() throws SQLException {
        boolean back = false;
        while (!back) {
            System.out.println("Manage Products");
            System.out.println("1. Add Product");
            System.out.println("2. View Products");
            System.out.println("3. Back");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    viewProducts();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addProduct() {
        try {
            System.out.print("Enter product name: ");
            String name = scanner.nextLine();
            System.out.print("Enter product price: ");
            double price = scanner.nextDouble();
            scanner.nextLine(); // consume newline
            System.out.print("Enter category ID: ");
            int categoryId = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.print("Enter product quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // consume newline

            // Check if the category_id exists in Categories table
            if (!isCategoryExists(categoryId)) {
                System.out.println("Error: Category with ID " + categoryId + " does not exist.");
                return;
            }

            String query = "INSERT INTO Products (name, price, category_id, quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, categoryId);
            pstmt.setInt(4, quantity);
            pstmt.executeUpdate();
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    private static void viewProducts() throws SQLException {
        String query = "SELECT * FROM Products";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("Products List:");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + ", Price: " + rs.getDouble("price") +
                               ", Category ID: " + rs.getInt("category_id") + ", Quantity: " + rs.getInt("quantity"));
        }
    }

    private static boolean isCategoryExists(int categoryId) throws SQLException {
        String query = "SELECT id FROM Categories WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, categoryId);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();  // Returns true if category with categoryId exists
    }

    private static void manageCustomers() throws SQLException {
        boolean back = false;
        while (!back) {
            System.out.println("Manage Customers");
            System.out.println("1. Add Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Back");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    viewCustomers();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addCustomer() throws SQLException {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();

        String query = "INSERT INTO Customers (name, email) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.setString(2, email);
        pstmt.executeUpdate();
        System.out.println("Customer added successfully.");
    }

    private static void viewCustomers() throws SQLException {
        String query = "SELECT * FROM Customers";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("Customers List:");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + ", Email: " + rs.getString("email"));
        }
    }

    private static void manageOrders() throws SQLException {
        boolean back = false;
        while (!back) {
            System.out.println("Manage Orders");
            System.out.println("1. Add Order");
            System.out.println("2. View Orders");
            System.out.println("3. Back");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addOrder();
                    break;
                case 2:
                    viewOrders();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addOrder() throws SQLException {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        // Check if the customer_id exists in Customers table
        if (!isCustomerExists(customerId)) {
            System.out.println("Error: Customer with ID " + customerId + " does not exist.");
            return;
        }

        // Check if the product_id exists in Products table
        if (!isProductExists(productId)) {
            System.out.println("Error: Product with ID " + productId + " does not exist.");
            return;
        }

        String query = "INSERT INTO Orders (customer_id, product_id) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, customerId);
        pstmt.setInt(2, productId);
        pstmt.executeUpdate();
        System.out.println("Order added successfully.");
    }

    private static void viewOrders() throws SQLException {
        String query = "SELECT Orders.id, Customers.name AS customer_name, Products.name AS product_name FROM Orders " +
                       "JOIN Customers ON Orders.customer_id = Customers.id " +
                       "JOIN Products ON Orders.product_id = Products.id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("Orders List:");
        while (rs.next()) {
            System.out.println("Order ID: " + rs.getInt("id") + ", Customer: " + rs.getString("customer_name") + ", Product: " + rs.getString("product_name"));
        }
    }

    private static boolean isCustomerExists(int customerId) throws SQLException {
        String query = "SELECT id FROM Customers WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, customerId);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();  // Returns true if customer with customerId exists
    }

    private static boolean isProductExists(int productId) throws SQLException {
        String query = "SELECT id FROM Products WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, productId);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();  // Returns true if product with productId exists
    }

    private static void manageEmployees() throws SQLException {
        boolean back = false;
        while (!back) {
            System.out.println("Manage Employees");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Back");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewEmployees();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addEmployee() throws SQLException {
        System.out.print("Enter employee name: ");
        String name = scanner.nextLine();
        System.out.print("Enter employee position: ");
        String position = scanner.nextLine();

        String query = "INSERT INTO Employees (name, position) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.setString(2, position);
        pstmt.executeUpdate();
        System.out.println("Employee added successfully.");
    }

    private static void viewEmployees() throws SQLException {
        String query = "SELECT * FROM Employees";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("Employees List:");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + ", Position: " + rs.getString("position"));
        }
    }

    private static void manageCategories() throws SQLException {
        boolean back = false;
        while (!back) {
            System.out.println("Manage Categories");
            System.out.println("1. Add Category");
            System.out.println("2. View Categories");
            System.out.println("3. Back");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addCategory();
                    break;
                case 2:
                    viewCategories();
                    break;
                case 3:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addCategory() throws SQLException {
        System.out.print("Enter category name: ");
        String name = scanner.nextLine();

        String query = "INSERT INTO Categories (name) VALUES (?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.executeUpdate();
        System.out.println("Category added successfully.");
    }

    private static void viewCategories() throws SQLException {
        String query = "SELECT * FROM Categories";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        System.out.println("Categories List:");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
        }
    }
}
