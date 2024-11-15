package telran.employees;

import telran.io.*;
import telran.net.*;

public class Main {
    private static final int PORT = 4000;
    public static void main(String[] args) {
        Company company = new CompanyImpl();
        Protocol protocol = new CompanyProtocol(company);
        TCPServer server = new TCPServer(protocol, PORT);
        Thread threadServer = new Thread(server);
        SaverToFile saver = new SaverToFile(company);
        if (company instanceof Persistable persistable) {
            persistable.restoreFromFile("employees.data");
        }
        threadServer.start();
        saver.start();
    }
}