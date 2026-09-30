package xyz.pbsi.Utils;

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.pbsi.Interfaces.DiscordCommand;

import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {
    private static final Logger log = LoggerFactory.getLogger(EventManager.class);
    public static HashMap<String, DiscordCommand> commands = new HashMap<>();
    public static ArrayList<SlashCommandData> commandDataList = new ArrayList<>();
    /**
     *
     * @param command The Discord Slash Command
     * @param executor The command executor.
     */
    public static void registerCommand(SlashCommandData command, DiscordCommand executor){
        commands.put(command.getName(), executor);
        commandDataList.add(command);
        log.info("Registered command {}.", command.getName());
    }
}
