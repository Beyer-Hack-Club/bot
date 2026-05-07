package xyz.pbsi;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import xyz.pbsi.listener.ButtonListener;
import xyz.pbsi.listener.CommandListener;
import xyz.pbsi.listener.DiscordEventListener;
import xyz.pbsi.listener.ModalListener;

import javax.security.auth.login.LoginException;

public class HCBot {
    protected static HCBot selfBot;
    private ShardManager shardManager = null;

    public HCBot(String token)
    {
        try{
            shardManager = buildShardManager(token);
        } catch (LoginException e) {
            System.out.println("Failed to start the bot!");
            System.exit(0);
        }
    }


    private ShardManager buildShardManager(String token) throws LoginException
    {
        DefaultShardManagerBuilder builder =
                DefaultShardManagerBuilder.createDefault(token)
                        .setActivity(Activity.playing("Hacking"))
                        .addEventListeners(new DiscordEventListener(this), new CommandListener(), new ModalListener(), new ButtonListener());
        return builder.build();

    }

    public ShardManager getShardManager()
    {
        return shardManager;
    }
}
