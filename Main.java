import java.util.Scanner;

import command_processing.CommandProcessor;
import command_processing.YamlTools;

public class Main {
    private static final String filename = "data";
    public static void main(String[] args){
        Scanner consoleReader = new Scanner(System.in);
        CommandProcessor mainProcessor = new CommandProcessor(YamlTools.load(filename));
        if(args.length > 0)
        while(true){
            if (consoleReader.hasNextLine()) {
                String consoleString = consoleReader.nextLine();
                mainProcessor.process(consoleString);
                //consoleReader.next();
            }
        }
        //        Route[] coll = new Route[]{new Route(235323, "wvrtrge")};
//        if(YamlTools.save(coll,"data"))
//            System.out.println("saved");
//        else
//            System.out.println("not saved");
//        System.out.println(Arrays.toString(YamlTools.load("data")));
    }
}
