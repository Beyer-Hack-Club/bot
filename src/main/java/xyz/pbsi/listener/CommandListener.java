package xyz.pbsi.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.*;

public class CommandListener extends ListenerAdapter {
    Long startTime = System.currentTimeMillis();


    private static Logger logger = LoggerFactory.getLogger(CommandListener.class);
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        switch (event.getName())
        {
            case "uptime":
                uptime(event);
                break;
            case "info":
                info(event);
                break;
        }
    }
    private void uptime(SlashCommandInteractionEvent event)
    {
        long time = System.currentTimeMillis() - startTime;
        time = time / 1000;
        long hours = time / 3600;
        long min = (time % 3600)/ 60;
        long seconds = time % 60;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Uptime");
        embedBuilder.setDescription("Current uptime: " + hours + " hours, " + min + " minutes, and " + seconds + " seconds.\nOnline since <t:" + (startTime/1000) +":F>.");
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setFooter("Beyer Hack Club", event.getGuild().getIconUrl());
        embedBuilder.setThumbnail(event.getGuild().getIconUrl());
        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }
    private void info(SlashCommandInteractionEvent event)
    {
        Separator separator = Separator.create(true, Separator.Spacing.SMALL);
        TextDisplay title = TextDisplay.of("### Beyer Hack Club Info");
        TextDisplay baseInfo = TextDisplay.of("Beyer Hack Club is a programming club with the goal of sharing knowledge and collaborating on projects! People will work both club activities as well as personal/group projects!");
        TextDisplay hackClub = TextDisplay.of("**What is a Hack Club?**");
        TextDisplay hackClubInfo = TextDisplay.of("Hack Club is a non profit organizations which provides resources for programming clubs, such as fundraising tools, activities, and platforms to share your code! Beyer Hack Club will be a Hack Club chapter.\nMore info can be found [here](https://hack.club/clubs)!");
        TextDisplay meetings = TextDisplay.of("**When will meetings be?**");
        TextDisplay meetingsInfo = TextDisplay.of("Currently the dates are not set in stone. The goal is 1-2 meetings per week of a length of 60 - 90 minutes. We'll have a better idea of dates when the club gets closer to being created, which will be the start of the next school year!");
        TextDisplay surveyText = TextDisplay.of("Want to help decide dates and activities?");
        Section section = Section.of(Button.primary("open-survey", "Open Survey"), surveyText);
        Container container = Container.of(title, separator, baseInfo, separator, hackClub, hackClubInfo, separator, meetings, meetingsInfo, separator, section).withAccentColor(Color.GREEN);
        MessageCreateData messageData = new MessageCreateBuilder()
                .useComponentsV2(true)
                .setComponents(container)
                .build();
        event.reply(messageData).setEphemeral(true).queue();
    }

}
