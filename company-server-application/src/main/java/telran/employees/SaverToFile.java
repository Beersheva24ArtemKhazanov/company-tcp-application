package telran.employees;

import java.time.LocalTime;

import telran.io.Persistable;

public class SaverToFile extends Thread {

    private Company company;
    private static final int DEFAULT_SECONDS_DELAY = 30;
    private static int secondsDelay = DEFAULT_SECONDS_DELAY;

    public SaverToFile(Company company) {
        this.company = company;
    }

    public static void setDelay(int secondsDelay) {
        SaverToFile.secondsDelay = secondsDelay;
    } 

    @Override
    public void run() {
        while (true) {
            if (company instanceof Persistable persistable) {
            persistable.saveToFile("employees.data");
            System.out.printf("Data is saved in %s \n", LocalTime.now());
        }
        try {
            sleep(secondsDelay * 1000);
        } catch (InterruptedException e) {
            
        }
        }
    }

}
