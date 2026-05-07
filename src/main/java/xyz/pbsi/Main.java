package xyz.pbsi;

import org.apache.commons.cli.*;

public class Main {

    private static Main INSTANCE;
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(new Option("t", "token", true, "Provide the token during startup."));
        CommandLineParser parser = new DefaultParser();
    try{
        CommandLine cmd = parser.parse(options, args);
        String token = cmd.hasOption("token") ? cmd.getOptionValue("token") : null;
        if(token == null)
        {
            System.out.println("No token provided!");
            System.exit(0);

        }
        HCBot.selfBot = new HCBot(token);
    } catch (ParseException e)
    {
        System.exit(0);
    }





    }
    public static Main getInstance()
    {
        return INSTANCE;
    }

}