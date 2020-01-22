package com.example.assignments;

import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieServiceResponse;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Main {

    private static final String URL = "http://localhost:8180/kie-server/services/rest/server";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    private static final MarshallingFormat FORMAT = MarshallingFormat.JSON;

    private static KieServicesConfiguration conf;
    private static KieServicesClient kieServicesClient;

    public static void main(String[] args) {
        conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
        conf.setMarshallingFormat(FORMAT);
        kieServicesClient = KieServicesFactory.newKieServicesClient(conf);

        String containerId = "Assignments_1.0.4-SNAPSHOT";
        System.out.println("== Sending commands to the server ==");
        RuleServicesClient rulesClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
        KieCommands commandsFactory = KieServices.Factory.get().getCommands();

        int count = 100;
        List<Person> personList = new ArrayList<Person>(count);
        int i = 0;
        while (i < count) {
            personList.add(PersonFactory.newPerson());
            i++;
        }

//        Command<?> insert = commandsFactory.newInsertElements(personList, "insert-person");
        Command<?> insert = commandsFactory.newInsert(PersonFactory.newPerson(), "insert-person");
        Command<?> fireAllRules = commandsFactory.newFireAllRules();
        Command<?> batchCommand = commandsFactory.newBatchExecution(Arrays.asList(insert, fireAllRules));

        ServiceResponse<ExecutionResults> executeResponse = rulesClient.executeCommandsWithResults(containerId, batchCommand);

        if (executeResponse.getType() == KieServiceResponse.ResponseType.SUCCESS) {
            System.out.println("Commands executed with success! Response: ");
            System.out.println(executeResponse.getResult());
        } else {
            System.out.println("Error executing rules. Message: ");
            System.out.println(executeResponse.getMsg());
        }

        Command<?> queryAssignments = commandsFactory.newQuery("query-result", "get-assignments");
        executeResponse = rulesClient.executeCommandsWithResults(containerId, queryAssignments);
        ExecutionResults resultData = executeResponse.getResult();
        QueryResults queryResult = (QueryResults) resultData.getValue("query-result");
        Iterator<QueryResultsRow> rowIt = queryResult.iterator();
        System.out.println("results: " + queryResult.size());
        while (rowIt.hasNext()) {
            QueryResultsRow row = rowIt.next();
            System.out.println(row.get("$assignment"));
        }


    }

}
