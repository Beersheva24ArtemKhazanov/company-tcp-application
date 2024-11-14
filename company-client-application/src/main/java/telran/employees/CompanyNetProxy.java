package telran.employees;

import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONArray;

import telran.net.NetworkClient;
import telran.net.TCPClient;

public class CompanyNetProxy implements Company{
    NetworkClient netClient;
    public CompanyNetProxy (NetworkClient netClient) {
        this.netClient = netClient;
    }
    @Override
    public Iterator<Employee> iterator() {
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public void addEmployee(Employee empl) {
        netClient.sendAndReceive("addEmployee", empl.toString());
    }

    @Override
    public int getDepartmentBudget(String department) {
        return Integer.parseInt(netClient.sendAndReceive("getDepartmentBudget", department));
    }

    @Override
    public String[] getDepartments() {
        String jsonStr = netClient.sendAndReceive("getDepartments", "");
        JSONArray jsonArray = new JSONArray(jsonStr) ;
        String[]res = jsonArray.toList().toArray(String[]::new);
        return res;
    }

    @Override
    public Employee getEmployee(long id) {
        String emplJSON = netClient.sendAndReceive("getEmployee", String.valueOf(id));
        Employee empl = Employee.getEmployeeFromJSON(emplJSON);
        return empl;
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        String managersJSON = netClient.sendAndReceive("getManagersWithMostFactor", "");
        JSONArray jsonArray = new JSONArray(managersJSON);
        String[] managersArray = jsonArray.toList().toArray(String[]::new);
        return Arrays.stream(managersArray).map(Employee::getEmployeeFromJSON).toArray(Manager[]::new);
    }

    @Override
    public Employee removeEmployee(long id) {
        String emplJSON = netClient.sendAndReceive("removeEmployee", String.valueOf(id));
        Employee empl = Employee.getEmployeeFromJSON(emplJSON);
        return empl;

    }

}