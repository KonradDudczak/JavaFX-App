package sample;

import java.io.File;

public class Shutdown extends Thread {

    private static final String desktop = System.getProperty("user.home") + "\\Desktop";

    @Override
    public void run() {


        File file1 = new File(desktop+"\\deliver.txt");
        file1.delete();


    }
}
