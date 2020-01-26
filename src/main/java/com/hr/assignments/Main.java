package com.hr.assignments;

import com.google.common.collect.Lists;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.command.Setter;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieServiceResponse;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Main {

    private static final String URL = "http://localhost:8090/rest/server";
    private static final String USER = "kieserver";
    private static final String PASSWORD = "kieserver1!";

    private static final MarshallingFormat FORMAT = MarshallingFormat.JSON;

    private static KieServicesConfiguration conf;
    private static KieServicesClient kieServicesClient;
    private static RuleServicesClient rulesClient;
    private static KieCommands commandsFactory;
    private static String containerId = "assignments_1.0.0-SNAPSHOT";

    public static void main(String[] args) {
        conf = KieServicesFactory.newRestConfiguration(URL, USER, PASSWORD);
        conf.setMarshallingFormat(FORMAT);
        Set<Class<?>> extraClassList = new HashSet<Class<?>>();
        extraClassList.add(Person.class);
        extraClassList.add(Assignment.class);
        conf.addExtraClasses(extraClassList);
        kieServicesClient = KieServicesFactory.newKieServicesClient(conf);

        System.out.println("== Sending commands to the server ==");

        rulesClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
        commandsFactory = KieServices.Factory.get().getCommands();

//        dispose();
        FactHandle factHandle = sendPerson();
        getAssignments();

        sleep(500);

        modify(factHandle);

        sleep(500);

        getAssignments();

    }

    public static void sleep(long seconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void modify(FactHandle factHandle) {
        Setter setter = commandsFactory.newSetter("grade", "1");
        Command<?> modify = commandsFactory.newModify(factHandle, Lists.newArrayList(setter));
        Command<?> fireAllRules = commandsFactory.newFireAllRules();
        Command<?> batchCommand = commandsFactory.newBatchExecution(Arrays.asList(modify, fireAllRules));
        rulesClient.executeCommandsWithResults(containerId, batchCommand);
    }
    public static void dispose() {
        Command<?> dispose =  commandsFactory.newDispose();
        rulesClient.executeCommandsWithResults(containerId, dispose);
    }

    public static FactHandle sendPerson() {
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
        FactHandle factHandle = (FactHandle) executeResponse.getResult().getFactHandle("insert-person");
        return factHandle;

    }

    public static void getAssignments() {
        Command<?> queryAssignments = commandsFactory.newQuery("query-result", "get-assignments");
        ServiceResponse<ExecutionResults> executeResponse = rulesClient.executeCommandsWithResults(containerId, queryAssignments);
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
