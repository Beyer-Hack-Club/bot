package xyz.pbsi.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import xyz.pbsi.Interfaces.DiscordCommand;
import xyz.pbsi.Main;
import xyz.pbsi.Utils.Assets;

import java.awt.*;

public class Uptime implements DiscordCommand {
    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {
        long time = System.currentTimeMillis() - Main.startTime;
        time = time / 1000;
        long hours = time / 3600;
        long min = (time % 3600)/ 60;
        long seconds = time % 60;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Uptime");
        embedBuilder.setDescription("Current uptime: " + hours + " hours, " + min + " minutes, and " + seconds + " seconds.\nOnline since <t:" + (Main.startTime/1000) +":F>.");
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setFooter("Beyer Hack Club", Assets.getLogo());
        embedBuilder.setThumbnail(Assets.getLogo());
        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }
}
