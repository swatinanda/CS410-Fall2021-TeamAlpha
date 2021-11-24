package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


@Component
@Configuration
public class AlertChatBotCmdRunner implements CommandLineRunner {

    @Autowired
    private Environment env;
    ObjectMapper mapper = new ObjectMapper();
    public void run(String... args) throws Exception {
        try {

            String username = env.getProperty("org.neo4j.driver.authentication.username","neo4j");
            String password = env.getProperty("org.neo4j.driver.authentication.password","abcd");
            String uri = env.getProperty("org.neo4j.driver.uri", "bolt://localhost:7687");
            Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));

            Thread.sleep(2000);

            Scanner sc= new Scanner(System.in);
            String choice = "";
            do {
                System.out.println("****************************************************************");
                System.out.println("* Options:                                                     *");
                System.out.println("*     1) Diagnose an alert");
                System.out.println("*     2) Get Alert Counts in a given duration");
                System.out.println("*     3) Get Impacted Devices due to an Alert in a given duration");
                System.out.println("*     4) Unhealthy services in a given duration");
                System.out.println("*****************************************************************");



                System.out.println("Enter your option (1 | 2 | 3 | 4   or q to quit)");
                choice = sc.nextLine();
                if (choice.equalsIgnoreCase("1")) {
                    getAlerts(sc, driver);
                }
                if (choice.equalsIgnoreCase("2")) {
                    getAlertCount(sc, driver);
                }
            } while (!choice.equals("q"));




            System.exit(0);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAlerts(Scanner sc, Driver driver) throws JsonProcessingException {
        String choice = "";
        do {
            String message = "";
            String device = "";
            System.out.println("Enter alert message:");
            choice = sc.nextLine();

            message = choice;
            if(!message.isEmpty()) {
                if (!choice.equals("q")) {
                    //System.out.println("Enter device:");
                    //choice = sc.nextLine();
                    //device = choice;
                    //if (!device.isEmpty()) {
                    if (!choice.equals("q")) {
                        AlertsController ctrller = new AlertsController(driver);
                        List<Map<String, Object>> response = ctrller.getAlerts(message, device);
                        Comparator<Map<String,Object>> sortByScore = Comparator.comparing(x -> ((Double)x.get("mutual_information")));

                        response.sort(sortByScore);
                        if (response.size() > 0) {
                            System.out.println("These are the correlated Alerts:");
                            for (int i = response.size() - 1; i >= 0; i--)
                                System.out.println(mapper.writeValueAsString(response.get(i)));

                        } else {
                            System.out.println("No substantially correlated alerts found.");
                        }
                        System.out.println("Want to diagnose more alerts? Y/N");
                        choice = sc.nextLine();


                    }
                    //}
                    //}

                }
            }
        } while (!choice.equals("N"));
    }

    private void getAlertCount(Scanner sc, Driver driver) throws JsonProcessingException {
        String choice = "";
        do {
            String start = "";
            String end = "";
            System.out.println("Enter start date time in format MM-DD-YYYY hh:mm:");
            choice = sc.nextLine();

            start = choice;
            if(!start.isEmpty()) {
                if (!choice.equals("q")) {
                    System.out.println("Enter end date time in format MM-DD-YYYY hh:mm:");
                    choice = sc.nextLine();
                    end = choice;
                    if (!end.isEmpty()) {
                        if (!choice.equals("q")) {
                            AlertsController ctrller = new AlertsController(driver);
                            String response = ctrller.getAlertsCount(start, end);
                            System.out.println(response);

                        }
                        //}
                        //}

                    }

                    System.out.println("Want to get alert counts again? Y/N");
                    choice = sc.nextLine();
                }
            }
        } while (!choice.equals("N"));
    }
}
