package xyz.pbsi.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import xyz.pbsi.Interfaces.DiscordCommand;

import java.awt.*;

public class Donate implements DiscordCommand {
    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Donate!");
        embedBuilder.setDescription("Donations to the Beyer Hack Club will be spent on goods for the club! These donations are vital for the club to operate, and we're appreciative of all donations!\n\n-# Beyer Hack Club is fiscally sponsored by The Hack Foundation (d.b.a. Hack Club), a 501(c)(3) nonprofit (EIN: 81-2908499).");
        embedBuilder.setColor(Color.GREEN);
        net.dv8tion.jda.api.components.buttons.Button donate = Button.link("https://hcb.hackclub.com/donations/start/beyer-hack-club", "Donate!");
        event.replyEmbeds(embedBuilder.build()).addComponents(ActionRow.of(donate)).setEphemeral(true).queue();
    }
}
