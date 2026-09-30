package xyz.pbsi.Interfaces;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface DiscordCommand {
    void commandExecutor(SlashCommandInteractionEvent event);
}
