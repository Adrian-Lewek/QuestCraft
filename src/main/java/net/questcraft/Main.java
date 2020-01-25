package net.questcraft;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.security.auth.login.AccountException;
import java.sql.SQLException;

import static com.fasterxml.jackson.databind.SerializationFeature.WRAP_ROOT_VALUE;
import static spark.Spark.*;

public class Main {

    public static void main (String [] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(WRAP_ROOT_VALUE, true);
        AccountSessions accountSessions = AccountSessions.getInstance();
        AccountUtil accountUtil = AccountUtil.getInstance();
        staticFiles.location("/public");
        get("/hello", (request, response) -> "HamBurger Test");
        get("/signup", (request, response) -> {
            System.out.println("made a account");
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            String email = request.queryParams("email");
            String mcUser = request.queryParams("mcUser");
            try {
                accountUtil.createAccount(username, password, email, mcUser);
                System.out.println("created account");
                String uuid = accountSessions.getNewUUID(username);
                return objectMapper.writeValueAsString(uuid);
            } catch (SQLException ex) {
                return new ErrorClass("DataBase Malfunction, Please try again later", 1);
            }
        });
        get("/logIn", (request, response) -> {
            String username = request.queryParams("username");
            String password = request.queryParams("password");

            if (accountUtil.verifyAccount(username, password)) {
                String uuid = accountSessions.getNewUUID(username);
                return objectMapper.writeValueAsString(uuid);
            } else {
                System.out.println("sending errorclass");
                ErrorClass errorClass = new ErrorClass("Could not Verify User and Password", 3);
                 System.out.println(objectMapper.writeValueAsString(errorClass));
                return objectMapper.writeValueAsString(errorClass);
            }
        });
        get("/getInfo", (request, response) -> {
            String uuid = request.queryParams("UUID");

                try {
                    if (accountSessions.checkUUID(uuid)) {
                        return accountSessions.getUserInfo(uuid);
                    } else {
                        return new ErrorClass( "Could not Find Account UUID in Lists, Please Try Again",3);
                    }
                } catch (SQLException e) {
                    return new ErrorClass("DataBase malfunction, Please Try Again Later", 1);
                } catch (AccountException e) {
                    return new ErrorClass("Could not Find Account UUID in Lists, Please Try Again", 2);
                }
        });
        get("/verify", (request, response) -> accountSessions.checkUUID(request.queryParams("UUID")));


        options("/*",
                (request, response) -> {

                    String accessControlRequestHeaders = request
                            .headers("Access-Control-Request-Headers");
                    if (accessControlRequestHeaders != null) {
                        response.header("Access-Control-Allow-Headers",
                                accessControlRequestHeaders);
                    }

                    String accessControlRequestMethod = request
                            .headers("Access-Control-Request-Method");
                    if (accessControlRequestMethod != null) {
                        response.header("Access-Control-Allow-Methods",
                                accessControlRequestMethod);
                    }

                    return "OK";
                });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }

}
