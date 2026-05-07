package xyz.pbsi.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class ModalListener extends ListenerAdapter {
    private static Logger logger = LoggerFactory.getLogger(CommandListener.class);

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getCustomId().equals("survey")) {
            String discordUsername = event.getUser().getName();
            String learn = event.getValue("learn").getAsString();
            String available = event.getValue("availability").getAsString();
            String experience = event.getValue("experience").getAsString();
            String comments = event.getValue("comments").getAsString();
            TextChannel channel = event.getGuild().getTextChannelById("1501836882936332308");
            assert channel != null;

            if (comments.isEmpty()) {
                comments = "None provided!";
            }
            if (experience.isEmpty()) {
                experience = "None shared!";
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(new Color(35, 255, 0));
            eb.setTitle("Survey Input!");
            eb.setDescription("**Discord Username: **" + discordUsername +
                    "\n**Wants to learn: ** " + learn +
                    "\n**Available during: **" + available +
                    "\n**Experience: **" + experience +
                    "\n**Comments: **" + comments);

            channel.sendMessageEmbeds(eb.build()).queue();
            event.reply("Thanks for submitting a response!").setEphemeral(true).queue();
        }
    }
}
