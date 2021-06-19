package start;

import operation.LOSProcess;
import static util.Utility.*;

public class Application {
    public static void main(String[] args) {
        LOSProcess losProcess = new LOSProcess();
        while (true){
        System.out.println("Do you have application Number or not?Press -1 to Exit()");
        int applicationNumber = scanner.nextInt();
        if (applicationNumber == -1){
            System.out.println("Thanks for using");
            System.exit(0);
        }
        if (applicationNumber==0){
            losProcess.sourcing();
        }
        else{
            losProcess.checkStage(applicationNumber);
        }

    }
    }
}
