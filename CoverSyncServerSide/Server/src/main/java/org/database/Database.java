import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.*;
import java.util.*;



public class Database {

    private Connection conn;

    public Connection getConnection() { return this.conn; }


    private HashMap<Integer, Company> companyCache;
    private HashMap<Integer, InsuranceType> insuranceCache;
    private HashMap<Integer, Customer> customerCache;

    public Database(String url, String user, String password)  throws SQLException {
        this.conn = DriverManager.getConnection(url, user, password);
        this.conn.setAutoCommit(true);
        this.customerCache = new HashMap<>();
        this.companyCache = new HashMap<>();
        this.insuranceCache = new HashMap<>();

    }

    public void addCustomer(Customer customer, Company company, InsuranceType insurancetype) throws SQLException {
        // Add the customer to the customers table

        //if customer alerady exisit
        try(PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM customers WHERE id = ?")) {
            checkStmt.setInt(1, customer.getId());
            ResultSet rs = checkStmt.executeQuery();

            if(rs.next()) {
                //customer is present then update
                System.out.println("Customer already exists. Updating customer details..." + customer.getId());
                updateCustomer(customer, insurancetype, company);
            } else {
                String insertSql = "INSERT INTO customers (id, first_name, last_name, ssn, phone, email, address, dob, company, type) VALUES (?, ?, ?, ?, ?, ?, ?, ? , ?, ?)";
                try(PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setInt(1, customer.getId());
                    insertStmt.setString(2, customer.getfirstName());
                    insertStmt.setString(3, customer.getlastName());
                    insertStmt.setString(4, customer.getSsn());
                    insertStmt.setString(5, customer.getPhone());
                    insertStmt.setString(6, customer.getEmail());
                    insertStmt.setString(7, customer.getAddress());
                    insertStmt.setString(8, customer.getDateOfBirth());
                    insertStmt.setString(9, company.getName());
                    insertStmt.setString(10, insurancetype.getType());
                    insertStmt.executeUpdate();


                    try( ResultSet rsInsert = insertStmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int customerId = rsInsert.getInt(1);//
                            addCompanyTag(customerId, customer.getCompanyTag());
                            addInsuranceType(customerId, customer.getInsuranceType());
                            // Add customer to the cache
                            customerCache.put(customerId, customer);
                        }
                    }
                 }
             }
         }

    }



    public void addInsuranceType(int id, ArrayList<String> type) throws SQLException {

        if(type == null || type.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO insurances (id, type) VALUES (?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            for(String insuranceType : type) {
                stmt.setInt(1, id);
                stmt.setString(2, insuranceType);
                stmt.executeUpdate();
                //for Testing
                System.out.println("Inserted insurance type: " + insuranceType + " for customer ID: " + id);
            }
        }

    }

    public void addInsurance(int id, String type) throws SQLException {

        String sql = "INSERT INTO insurance_type (id, type) VALUES (?, ?) ON DUPLICATE KEY UPDATE type = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, type);
            stmt.setString(3, type);
            stmt.executeUpdate();
        }
    }

    public void addCompanyTag(int id, ArrayList<String> Companytag) throws SQLException {

        String sql = "INSERT INTO company_tags (id, tag) VALUES (?, ?)";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            for(String tag : Companytag) {
                stmt.setInt(1, id);
                stmt.setString(2, tag);
                System.out.println("Execuing sql: " + stmt.toString());
                int affectedRows = stmt.executeUpdate();
                System.out.println("Inserted company tag: " + tag + " for customer ID: " + id + " - affected rows: " + affectedRows);
            }
        }

    }



        // display all customer regardless of specs
    public Customer createCustomerFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String firstName = rs.getString("first_name");
        String lastName = rs.getString("last_name");
        String ssn = rs.getString("ssn");
        String phone = rs.getString("phone");
        String email = rs.getString("email");
        String address = rs.getString("address");
        String dob = rs.getString("dob");

        ArrayList<String> companyTags =  getCompanyTagForCustomer(id);
        ArrayList<String> insuranceTypes = getInsuranceTypeForCustomer(id);

        return new Customer(id, firstName, lastName, ssn, phone, email, address, dob, insuranceTypes, companyTags);
    }




    private ArrayList<String> getCompanyTagForCustomer(int customerId) throws SQLException {
        ArrayList<String> companyTags = new ArrayList<>();
        String sql = "SELECT tag FROM company_tags WHERE customer_id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                companyTags.add(rs.getString("tag"));
            }

        }
        return companyTags;

    }

    private ArrayList<String> getInsuranceTypeForCustomer(int customerId) throws SQLException {
        ArrayList<String> insuranceTypes = new ArrayList<>();
        String sql = "SELECT type FROM insurance_type WHERE id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                insuranceTypes.add(rs.getString("type"));
            }

        }
        return insuranceTypes;

    }


    public Company createCompanyFromResultset(ResultSet rs) throws SQLException {
        return new Company(rs.getInt("id"), rs.getString("name"), rs.getString("tag"));
    }

    public InsuranceType createInsuranceFromResultset(ResultSet rs) throws SQLException {
        return new InsuranceType(rs.getInt("id"), rs.getString("type"));
    }

    public Customer getCustomerById(int id) throws SQLException {

        if (customerCache.containsKey(id)) {
            return customerCache.get(id);
        }
            String sql = "SELECT * FROM customers WHERE id = ?";
            try(PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Customer customer = createCustomerFromResultSet(rs);
                        customerCache.put(id, customer);
                        return customer;
                    } else {
                        return null;
                    }
                }
            }
        }


    //Finding Customer by Company
    public List<Customer> getCustomerByCompany(String companyTag) throws SQLException
    {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT customers.* FROM customers JOIN company_tags ON customers.id = company_tags.customer_id WHERE company_tags.tag = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, companyTag);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()){
            Customer customer = createCustomerFromResultSet(rs);
            customers.add(customer);
        }

        return customers;
    }

    public List<Company> getCompanyForCustomer(int customerId) throws SQLException
    {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT tag FROM company_tags WHERE customer_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, customerId);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()){
            Company company = createCompanyFromResultset(rs);
            companies.add(company);
        }

        return companies;
    }

    //Finding Customer by Insurance Type
    public List<Customer> getCustomerByInsuranceType(String insuranceType) throws SQLException
    {
        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT customers.* FROM customers JOIN insurances ON customers.id = insurances.id WHERE insurances.id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, insuranceType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = createCustomerFromResultSet(rs);
                    customers.add(customer);
                }

            }
        }
        return customers;
    }

    public List<InsuranceType> getInsuranceForCustomer(int customerId) throws SQLException {
        List<InsuranceType> insurances = new ArrayList<>();
        String sql = "SELECT insurance_type.id, insurance_type.type FROM insurance_type INNER JOIN insurances ON insurance_type.id = insurances.id WHERE insurances.id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    insurances.add(createInsuranceFromResultset(rs));
                }
            }
        }
        return insurances;
    }

    public void updateCustomer(Customer customer, InsuranceType insuranceType, Company company) throws SQLException {
            // Update the customer details in the customers table
        String sql = "UPDATE customers SET first_name = ?, last_name = ?, ssn = ?, phone = ?, email = ?, address = ?, dob = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getfirstName());
            stmt.setString(2, customer.getlastName());
            stmt.setString(3, customer.getSsn());
            stmt.setString(4, customer.getPhone());
            stmt.setString(5, customer.getEmail());
            stmt.setString(6, customer.getAddress());
            stmt.setString(7, customer.getDateOfBirth());
            stmt.setInt(8, customer.getId());
            stmt.executeUpdate();
        }


            addCompanyTag(company.getId(), company.getTag());
            addInsurance(insuranceType.getId(), insuranceType.getType());
            customerCache.put(customer.getId(), customer);
    }

        public void deleteCustomer(int id) throws SQLException {
            String sql = "DELETE FROM customers WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
                customerCache.remove(id);
            }
            //deleteCustomerTags(id);
            deleteInsuranceType(id);

        }

        public void deleteCustomerTags(int id) throws SQLException {
            String sql = "DELETE FROM customers WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        }

        public void deleteInsuranceType(int id) throws SQLException {
            String sql = "DELETE FROM insurances WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        }


}


