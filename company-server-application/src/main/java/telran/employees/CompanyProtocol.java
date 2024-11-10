package telran.employees;

import java.util.Arrays;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;

import telran.net.Protocol;
import telran.net.Request;
import telran.net.Response;
import telran.net.ResponseCode;

public class CompanyProtocol implements Protocol {
    Company company;

    public CompanyProtocol(Company company) {
        this.company = company;
    }

    @Override
    public Response getResponse(Request req) {
        String type = req.requestType();
        String data = req.requestData();
        Response resp = switch (type) {
            case "addEmployee" -> addEmployee(data);
            case "getDepartmentBudget" -> getDepartmentBudget(data);
            case "getDepartments" -> getDepartments();
            case "getEmployee" -> getEmployee(data);
            case "getManagersWithMostFactor" -> getManagersWithMostFactor();
            case "removeEmployee" -> removeEmployee(data);
            default -> new Response(ResponseCode.WRONG_TYPE, "The type doesn't exist");
        };
        return resp;
    }

    private Response removeEmployee(String data) {
        Response resp = null;
        Long id = Long.parseLong(data);
        try {
            company.removeEmployee(id);
            resp = new Response(ResponseCode.OK, "");
        } catch (NoSuchElementException e) {
            resp = new Response(ResponseCode.WRONG_DATA, "Employee doesn't exist with this id");
        }
        return resp;
    }

    private Response getManagersWithMostFactor() {
        Response resp = null;
        try {
            Manager[] managers = company.getManagersWithMostFactor();
            String[] managersStrings = Arrays.stream(managers).map(Manager::toString).toArray(String[]::new);
            JSONArray managersJSON = new JSONArray(managersStrings);
            resp = new Response(ResponseCode.OK, managersJSON.toString());
        } catch (JSONException e) {
            resp = new Response(ResponseCode.WRONG_DATA, "There's no managers in the company");
        }
        return resp;
    }

    private Response getEmployee(String data) {
        Long id = Long.parseLong(data);
        Employee empl = company.getEmployee(id);
        return empl != null
                ? new Response(ResponseCode.OK, empl.toString())
                : new Response(ResponseCode.WRONG_DATA, "Employee doesn't exist with this id");
    }

    private Response getDepartments() {
        Response resp = null;
        try {
            String[] departments = company.getDepartments();
            JSONArray jsonArray = new JSONArray(departments);
            resp = new Response(ResponseCode.OK, jsonArray.toString());
            
        } catch (NoSuchElementException e) {
            resp = new Response(ResponseCode.WRONG_DATA, "There's no departments in the company");
        }
        return resp;
    }

    private Response getDepartmentBudget(String data) {
        Response resp = null;
        try {
            int budget = company.getDepartmentBudget(data);
            resp =  new Response(ResponseCode.OK, String.valueOf(budget));
        } catch (Exception e) {
            resp =  new Response(ResponseCode.WRONG_DATA, "There's no department budget in the company");
        }
        return resp;
    }

    private Response addEmployee(String data) {
        Response resp = null;
        Employee empl = Employee.getEmployeeFromJSON(data);
        try {
            company.addEmployee(empl);
            resp = new Response(ResponseCode.OK, "");
        } catch (IllegalStateException e) {
            resp = new Response(ResponseCode.WRONG_DATA, "Employee with this id already exist in the company");
        }
        return resp;
    }

}
