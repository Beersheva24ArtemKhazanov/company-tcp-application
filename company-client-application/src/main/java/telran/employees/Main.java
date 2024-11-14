package telran.employees;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;

import telran.net.NetworkClient;
import telran.net.TCPClient;
import telran.view.InputOutput;
import telran.view.Item;
import telran.view.Menu;
import telran.view.StandardInputOutput;

public class Main {
    private static final String HOST = "localhost";
    private static final int PORT = 4000;
    public static void main(String[] args) {
        InputOutput io = new StandardInputOutput();
        NetworkClient netClient = new TCPClient(HOST, PORT);
        Company company = new CompanyNetProxy(netClient);
        Item[] items = CompanyItems.getItems(company);
        items = addExitItem(items, netClient);
        Menu menu = new Menu("Company Network Application", items);
        menu.perform(io);
        io.writeLine("Application is finished");
    }

    private static Item[] addExitItem(Item[] items, NetworkClient netClient) {
       Item[] res = Arrays.copyOf(items, items.length + 1);
       res[items.length] = Item.of("Exit", io -> {
        try {
            if (netClient instanceof Closeable closable) {
                closable.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }, true);
    return res;
    }
}