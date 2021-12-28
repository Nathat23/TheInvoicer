package com.invoicer.main;

import com.invoicer.main.data.Customer;
import com.invoicer.sql.Attribute;
import com.invoicer.sql.SqlHandler;
import com.invoicer.sql.SqlTable;

import java.util.concurrent.ThreadLocalRandom;

public class TheInvoicer {

    public static void main(String[] args) {
        SqlHandler sqlHandler = new SqlHandler("jdbc:mariadb://localhost:3306/invoicer", "new_user" , "new_password");
        sqlHandler.initialise();
        SqlTable<Customer> sqlTable = new SqlTable<>(sqlHandler, Customer.class);
        Customer customer = sqlTable.getObject(1);
        customer.getAttributes().forEach(attribute -> System.out.println(attribute.getName() + "," + attribute.getValue()));
        for (Attribute<String> attribute : customer.getAttributes()) {
            if (attribute.getName().equals("lastname")) {
                attribute.setValue(ThreadLocalRandom.current().nextInt(5, 400) + "");
            }
        }
        sqlTable.updateObject(customer);
        Customer customer1 = sqlTable.getObject(1);
        customer1.getAttributes().forEach(attribute -> System.out.println(attribute.getName() + "," + attribute.getValue()));
        MainWindow.main(args);
    }
}
