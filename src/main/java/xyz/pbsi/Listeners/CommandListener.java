package xyz.pbsi.Listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.pbsi.Interfaces.DiscordCommand;
import xyz.pbsi.Utils.*;

import java.awt.*;
import java.io.*;

public class CommandListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CommandListener.class);
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        DiscordCommand command = EventManager.commands.get(event.getName());
        if(command == null){
            event.reply("No executor defined for " + event.getName() + "!").setEphemeral(true).queue();
            logger.error("Missing executor for {}!", event.getName());
            return;
        }
        command.commandExecutor(event);
    }
}
