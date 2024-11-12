package telran.employees;

import java.util.Arrays;
import java.util.Iterator;

import org.json.JSONArray;

import telran.net.TCPClient;

public class CompanyTCPProxy implements Company{
    TCPClient tcpClient;
    public CompanyTCPProxy (TCPClient tcpClient) {
        this.tcpClient = tcpClient;
    }
    @Override
    public Iterator<Employee> iterator() {
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public void addEmployee(Employee empl) {
        tcpClient.sendAndReceive("addEmployee", empl.toString());
    }

    @Override
    public int getDepartmentBudget(String department) {
        return Integer.parseInt(tcpClient.sendAndReceive("getDepartmentBudget", department));
    }

    @Override
    public String[] getDepartments() {
        String jsonStr = tcpClient.sendAndReceive("getDepartments", "");
        JSONArray jsonArray = new JSONArray(jsonStr) ;
        String[]res = jsonArray.toList().toArray(String[]::new);
        return res;
    }

    @Override
    public Employee getEmployee(long id) {
        String emplJSON = tcpClient.sendAndReceive("getEmployee", String.valueOf(id));
        Employee empl = Employee.getEmployeeFromJSON(emplJSON);
        return empl;
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        String managersJSON = tcpClient.sendAndReceive("getManagersWithMostFactor", "");
        JSONArray jsonArray = new JSONArray(managersJSON);
        String[] managersArray = jsonArray.toList().toArray(String[]::new);
        return Arrays.stream(managersArray).map(Employee::getEmployeeFromJSON).toArray(Manager[]::new);
    }

    @Override
    public Employee removeEmployee(long id) {
        String emplJSON = tcpClient.sendAndReceive("removeEmployee", String.valueOf(id));
        Employee empl = Employee.getEmployeeFromJSON(emplJSON);
        return empl;

    }

}