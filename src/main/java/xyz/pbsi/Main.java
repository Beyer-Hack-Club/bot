package xyz.pbsi;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.cli.*;
import xyz.pbsi.Utils.CloudflareR2Client;

public class Main {
    public static long startTime = System.currentTimeMillis();
    static Dotenv dotenv = Dotenv.load();
    public static CloudflareR2Client.S3Config config = new CloudflareR2Client.S3Config(dotenv.get("CLOUDFLARE_ACCOUNT_ID"), dotenv.get("CLOUDFLARE_ACCESS_KEY"), dotenv.get("CLOUDFLARE_SECRET_KEY"));

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(new Option("t", "token", true, "Provide the token during startup."));
        CommandLineParser parser = new DefaultParser();
    try{
        CommandLine cmd = parser.parse(options, args);
        new CloudflareR2Client(config);
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
}