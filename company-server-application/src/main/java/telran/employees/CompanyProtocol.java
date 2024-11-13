package telran.employees;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;

import org.json.JSONArray;

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
        Response resp = null;
        try {
            Method method = this.getClass().getDeclaredMethod(type, String.class);
            resp = (Response) method.invoke(this, data);
        } catch (NoSuchMethodException e) {
            resp = new Response(ResponseCode.WRONG_TYPE, "The type doesn't exist");
        } catch (IllegalAccessException | InvocationTargetException e) {
            resp = new Response(ResponseCode.WRONG_DATA, e.getMessage());
        }
        return resp;
    }

    Response getOkResponse(String responseData) {
        return new Response(ResponseCode.OK, responseData);
    }

    Response removeEmployee(String requestData) {
        long id = Long.parseLong(requestData);
        Employee empl = company.removeEmployee(id);
        return getOkResponse(empl.toString());
    }

    Response getManagersWithMostFactor(String requestData) {
        Manager[] managers = company.getManagersWithMostFactor();
        JSONArray jsonArray = new JSONArray(Arrays.stream(managers).map(Manager::toString).toList());
        return getOkResponse(jsonArray.toString());
    }

    Response addEmployee(String requestData) {
        Employee empl = Employee.getEmployeeFromJSON(requestData);
        company.addEmployee(empl);
        return getOkResponse("");
    }

    Response getDepartments(String requestData) {
        String[] departments = company.getDepartments();
        JSONArray jsonArray = new JSONArray(departments);
        return getOkResponse(jsonArray.toString());
    }

    Response getDepartmentBudget(String requestData) {
        int budget = company.getDepartmentBudget(requestData);
        return getOkResponse(budget + "");
    }

    Response getEmployee(String requestData) {
        long id = Long.parseLong(requestData);
        Employee empl = company.getEmployee(id);
        if (empl == null) {
            throw new NoSuchElementException(String.format("Employee %d not found", id));
        }
        return getOkResponse(empl.toString());
    }

}
