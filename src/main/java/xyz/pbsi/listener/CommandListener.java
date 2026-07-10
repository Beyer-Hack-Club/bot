package xyz.pbsi.listener;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.Objects;

public class CommandListener extends ListenerAdapter {
    Dotenv dotenv = Dotenv.load();
    String authorization = dotenv.get("SECRET");
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
            case "donate":
                donate(event);
                break;
            case "update-website":
                updateWebsite(event);
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
        TextDisplay hackClubInfo = TextDisplay.of("Hack Club is a non profit organizations which provides resources for programming clubs, such as fundraising tools, activities, and platforms to share your code! Beyer Hack Club is Hack Club chapter.\nMore info can be found [here](https://hack.club/clubs)!");
        TextDisplay meetings = TextDisplay.of("**When will meetings be?**");
        TextDisplay meetingsInfo = TextDisplay.of("Currently the dates are not set in stone. The goal is 1-2 meetings per week of a length of 60 - 90 minutes. We'll have a better idea of dates when the club gets closer to being created, which will be the start of the next school year!");
        TextDisplay surveyText = TextDisplay.of("Want to help decide dates and activities?");
        TextDisplay fiscalSponsorship = TextDisplay.of("-# Beyer Hack Club is fiscally sponsored by The Hack Foundation (d.b.a. Hack Club), a 501(c)(3) nonprofit (EIN: 81-2908499). [Learn more](https://hackclub.com/fiscal-sponsorship) | [View our financials](https://hcb.hackclub.com/beyer-hack-club)");
        Section section = Section.of(Button.primary("open-survey", "Open Survey"), surveyText);
        Container container = Container.of(title, separator, baseInfo, separator, hackClub, hackClubInfo, separator, meetings, meetingsInfo, separator, section, fiscalSponsorship).withAccentColor(Color.GREEN);
        MessageCreateData messageData = new MessageCreateBuilder()
                .useComponentsV2(true)
                .setComponents(container)
                .build();
        event.reply(messageData).setEphemeral(true).queue();
    }

    private void donate(SlashCommandInteractionEvent event)
    {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Donate!");
        embedBuilder.setDescription("Donations to the Beyer Hack Club will be spent on goods for the club! These donations are vital for the club to operate, and we're appreciative of all donations!\n\n-# Beyer Hack Club is fiscally sponsored by The Hack Foundation (d.b.a. Hack Club), a 501(c)(3) nonprofit (EIN: 81-2908499).");
        embedBuilder.setColor(Color.GREEN);
        Button donate = Button.link("https://hcb.hackclub.com/donations/start/beyer-hack-club", "Donate!");
        event.replyEmbeds(embedBuilder.build()).addComponents(ActionRow.of(donate)).setEphemeral(true).queue();
    }
    private void updateWebsite(SlashCommandInteractionEvent event)
    {
        if(!Objects.requireNonNull(event.getMember()).getRoles().contains(Objects.requireNonNull(event.getGuild()).getRoleById("1488731960053469337")))
        {
            event.reply("You do not have permission to use this!").setEphemeral(true).queue();
            return;
        }
        String value = event.getOption("value").getAsString().toLowerCase();
        String text = event.getOption("text").getAsString();
        System.out.println("{\"" + value + "\":\"" + text + "\"}");
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.beyerhack.club/website/update"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorization)
                    .method("POST", HttpRequest.BodyPublishers.ofString("{\"" + value + "\":\"" + text + "\"}"))
                    .build();
            HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            event.reply("Updated value successfully").setEphemeral(true).queue();
        }catch (URISyntaxException | IOException | InterruptedException e )
        {
            event.reply("An error has occurred: " + e.getMessage()).setEphemeral(true).queue();
        }

    }

}
