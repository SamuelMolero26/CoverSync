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
        this.customerCache = new HashMap<>();
        this.companyCache = new HashMap<>();
        this.insuranceCache = new HashMap<>();

    }

    public void addCustomer(Customer customer, Company company, InsuranceType insurancetype) throws SQLException {
        // Add the customer to the customers table

        //if customer alerady exisit

        try(PreparedStatement checkStmt = conn.prepareStatement("SELECT * FROM customers WHERE id = ?")) {
            checkStmt.setInt(1, Integer.parseInt(customer.getId()));
            ResultSet rs = checkStmt.executeQuery();
            if(rs.next()) {
                String updateSql = "UPDATE customers SET name = ?, ssn = ?, phone = ?, email = ?, address = ?, dob = ?, company = ?, type = ? WHERE id = ?";
                try(PreparedStatement updateStmt = conn.prepareCall(updateSql)) {
                    updateStmt.setString(1, customer.getName());
                    updateStmt.setString(1, customer.getId());
                    updateStmt.setString(2, customer.getName());
                    updateStmt.setString(3, customer.getSsn());
                    updateStmt.setString(4, customer.getPhone());
                    updateStmt.setString(5, customer.getEmail());
                    updateStmt.setString(6, customer.getAddress());
                    updateStmt.setString(7, customer.getDateOfBirth());
                    updateStmt.setString(8, insurancetype.getType());
                    updateStmt.setString(9, company.getName());
                }
            } else {
                String insertSql = "INSERT INTO customers (id, name, ssn, phone, email, address, dob, company, type) VALUES (?, ?, ?, ?, ?, ?, ?, ? , ?)";
                try(PreparedStatement insertStmt = conn.prepareCall(insertSql)) {
                    insertStmt.setString(1, customer.getId());
                    insertStmt.setString(2, customer.getName());
                    insertStmt.setString(3, customer.getSsn());
                    insertStmt.setString(4, customer.getPhone());
                    insertStmt.setString(5, customer.getEmail());
                    insertStmt.setString(6, customer.getAddress());
                    insertStmt.setString(7, customer.getDateOfBirth());
                    insertStmt.setString(8, insurancetype.getType());
                    insertStmt.setString(9, company.getName());
                    insertStmt.executeUpdate();

                    insertStmt.executeUpdate();

                    // Get the ID of the newly inserted customer
                    //still need some work
                    ResultSet rsInsert = insertStmt.getGeneratedKeys();
                    if(rs.next()) {
                        int customerId = rsInsert.getInt(1);//


                        int insuranceTypeId = insurancetype.getId();
                        int companyId = company.getId();

                        addCustomerTag(customerId, customer.getCompanyTag(), insuranceTypeId);
                        addInsuranceTypes(customerId, customer.getInsuranceType(), companyId);


                        // Add customer to the cache
                        customerCache.put(customerId, customer);
                     }
                 }
             }
         }

    }


    //handle the insertion of data (client_company_insurance) since customers can have multiple tags
    // (companies) and types of insurance ---> might need some more work tweak
    private void addCustomerTag(int customerId, ArrayList<String> companyTags, int insuranceTypeId) throws SQLException {
        String tagSql = "INSERT INTO client_company_insurance (client_id, company_id, insurance_type_id) VALUES (?, ?, ?)";
        try(PreparedStatement tagStmt = conn.prepareCall(tagSql)) {
            for (String tag : companyTags) {
                tagStmt.setInt(1, customerId);
                tagStmt.setInt(2, Integer.parseInt(tag));
                tagStmt.setInt(3, insuranceTypeId);
                tagStmt.addBatch();
            }
            tagStmt.executeBatch();

        }
    }

    private void addInsuranceTypes(int customerId, ArrayList<String> insuranceTypes, int companyId) throws SQLException {
        String typeSql = "INSERT INTO client_company_insurance (client_id, company_id, insurance_type_id) VALUES (?, ?, ?)";
        try(PreparedStatement typeStmt = conn.prepareCall(typeSql)) {
            for (String type : insuranceTypes) {
                typeStmt.setInt(1, customerId);
                typeStmt.setInt(2, companyId);
                typeStmt.setInt(3, Integer.parseInt(type));
                typeStmt.addBatch();
            }
            typeStmt.executeBatch();
        }
    }

        // display all customer regardless of specs
    public Customer createCustomerFromResultSet(ResultSet rs) throws SQLException {
        String  type = rs.getString("type");
        String company = rs.getString("company");

        ArrayList<String> companyTags = (company != null) ? new ArrayList<>(Arrays.asList(company.split(","))) : new ArrayList<>();
        ArrayList<String> InsuranceTypes = (type != null) ? new ArrayList<>(Arrays.asList(type.split(","))) : new ArrayList<>();
        Customer customer = new Customer(rs.getString("id"),
                rs.getString("name"),
                rs.getString("ssn"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("address"),
                rs.getString("dob"), InsuranceTypes, companyTags);
        loadCompanyTags(customer);
        loadInsuranceTypes(customer);
        return customer;
    }

    private void loadCompanyTags(Customer customer) throws SQLException {
        String tagSql = "SELECT company_id FROM company_tags WHERE customer_id = ?";
        try(PreparedStatement tagStmt = conn.prepareCall(tagSql)) {
            tagStmt.setInt(1, Integer.parseInt(customer.getId()));
            ResultSet tagRs = tagStmt.executeQuery();
            while(tagRs.next()) {
                int companyId = tagRs.getInt("company_id");
                customer.addCompanyTag(getCompanyName(companyId));
            }
        }
    }

    private void loadInsuranceTypes(Customer customer) throws SQLException {
        String sql = "SELECT insurance_type_id FROM client_company_insurance WHERE client_id = ?";
        try(PreparedStatement stmt = conn.prepareCall(sql)) {
            stmt.setInt(1, Integer.parseInt(customer.getId()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int insuranceTypeId = rs.getInt("insurance_type_id");
                customer.addInsuranceType(getInsuranceTypeName(insuranceTypeId));
            }
        }
    }

    private String getCompanyName(int companyId) throws SQLException{
        if(companyCache.containsKey(companyId)) {
            return companyCache.get(companyId).getName();
        }


        String sql = "SELECT company_name FROM companies WHERE company_id = ?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                String companyName = rs.getString("company_name");
                companyCache.put(companyId, new Company(companyId, companyName, null));
                return companyName;
            }
        }

        throw new SQLException("company not Found: " + companyId);
    }

    private String getInsuranceTypeName(int insuranceTypeId) throws SQLException {
        if(customerCache.containsKey(insuranceTypeId)) {
            return insuranceCache.get(insuranceTypeId).getType();
        }

        String sql = "SELECT insurance_type FROM insurance_types WHERE insurance_type_id =?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, insuranceTypeId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                String insuranceTypeName = rs.getString("insurance_type_name");
                insuranceCache.put(insuranceTypeId, new InsuranceType(insuranceTypeId, insuranceTypeName));
                return insuranceTypeName;
            }
        }
        throw new SQLException("Insurance type not found: " + insuranceTypeId);
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
            try(PreparedStatement stmt = conn.prepareCall(sql)) {
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
        PreparedStatement stmt = conn.prepareCall(sql);
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
        String sql = "SELECT insurances.* FROM insurances JOIN insurance_types ON insurances.id = insurance_types.type WHERE insurance_types.customer_id = ?";
        PreparedStatement stmt = conn.prepareCall(sql);
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
        String sql = "SELECT customers.* FROM customers JOIN insurance_types ON customers.id = insurance_types.customer_id WHERE insurance_types.type = ?";
        PreparedStatement stmt = conn.prepareCall(sql);
        stmt.setString(1, insuranceType);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()){
            Customer customer = createCustomerFromResultSet(rs);
            customers.add(customer);
        }
        return customers;
    }

    public List<InsuranceType> getInsuranceForCustomer(int customerId) throws SQLException
    {
        List<InsuranceType> insurances = new ArrayList<>();
        String sql = "SELECT insurances.* FROM insurances JOIN insurance_types ON insurances.id = insurance_types.insurance_id WHERE insurance_types.customer_id = ?";
        PreparedStatement stmt = conn.prepareCall(sql);
        stmt.setInt(1, customerId);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()){
            InsuranceType insurance = createInsuranceFromResultset(rs);
            insurances.add(insurance);
        }

        return insurances;
    }

    public void updateCustomer(Customer customer, InsuranceType insuranceType, Company company) throws SQLException {
            // Update the customer details in the customers table
            String sql = "UPDATE customers SET name = ?, ssn = ?, phone = ?, email = ?, address = ?, dob = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getSsn());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getAddress());
            stmt.setString(6, customer.getDateOfBirth());
            stmt.setInt(7, Integer.parseInt(customer.getId()));
            stmt.executeUpdate();
        }

            //delete existing - easy on memory
            deleteCustomerTags(Integer.parseInt(customer.getId()));
            deleteInsuranceType(Integer.parseInt(customer.getId()));
            deleteCustomer(Integer.parseInt(customer.getId()));
            //delete porevious


            addCustomerTag(Integer.parseInt(customer.getId()), customer.getCompanyTag(), insuranceType.getId());
            addInsuranceTypes(Integer.parseInt(customer.getId()), customer.getInsuranceType(), company.getId());

            customerCache.put(Integer.parseInt(customer.getId()), customer);
    }

        public void deleteCustomer(int id) throws SQLException {
            String sql = "DELETE FROM customers WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
                customerCache.remove(id);
            }
            deleteCustomerTags(id);
            deleteInsuranceType(id);
        }

        public void deleteCustomerTags(int id) throws SQLException {
            String sql = "DELETE FROM client_company_insurance WHERE client_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        }

        public void deleteInsuranceType(int id) throws SQLException {
            String sql = "DELETE FROM insurance_types WHERE customer_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        }
}


