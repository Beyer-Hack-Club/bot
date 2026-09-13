package xyz.pbsi.listener;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.modals.Modal;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Text;
import xyz.pbsi.utils.Assets;
import xyz.pbsi.utils.JSON;
import xyz.pbsi.utils.Member;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.HashMap;
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
            case "log":
                log(event);
                break;
            case "add-member":
                addMember(event);
                break;
            case "get-member":
                getMember(event);
                break;
            case "reset-and-stop":
                resetAndStop(event);
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
        embedBuilder.setFooter("Beyer Hack Club", Assets.getLogo());
        embedBuilder.setThumbnail(Assets.getLogo());
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
        TextDisplay meetingsInfo = TextDisplay.of("Meetings will be every Tuesday after school in the robotics shop! ");
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

    private void addMember(SlashCommandInteractionEvent event)
    {
        if(!permissionCheck(event, "1488731960053469337", false)) return;
        TextInput json = TextInput.create("json", TextInputStyle.PARAGRAPH)
                .setRequired(true)
                .build();
        Modal modal = Modal.create("create-user", "Create a user").addComponents(
                Label.of("json", json)
        ).build();
        event.replyModal(modal).queue();
    }
    private void getMember(SlashCommandInteractionEvent event){
        if(!permissionCheck(event, "1488731960053469337", false)) return;
        String memberID = event.getOption("id").getAsString();
        File file = new File("/bhc/members/" + memberID + ".json");
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            Member member = new Gson().fromJson(reader, Member.class);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Member - " + member.getFirstName());
            embedBuilder.setDescription("**First Name**: " + member.getFirstName()
            +"\n**Last Name**: " + member.getLastName() +
                    "\n**Preferred Name**: " + member.getPreferredName() +
                    "\n**Pronouns**: " + member.getPronouns()+
                    "\n**Email**: " + member.getEmail() +
                    "\n**Phone Number**: " + member.getEmail() +
                    "\n**Equipment Contract**: " + member.getEquipmentContract()+
                    "\n**Permission Slip** " + member.getPermissionSlip());
            embedBuilder.setColor(Color.blue);
            embedBuilder.setFooter("Beyer Hack Club", Assets.getLogo());
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        } catch (FileNotFoundException e) {
            event.reply("Error, member not found").setEphemeral(true).queue();
        }
    }
    private void updateWebsite(SlashCommandInteractionEvent event)
    {
        event.deferReply().setEphemeral(true).queue();
        HttpClient client = HttpClient.newHttpClient();
        String requiredRole = "1488731960053469337";
        if(!permissionCheck(event, requiredRole, true)) return;
        String value = event.getOption("value").getAsString().toLowerCase();
        String text = event.getOption("text").getAsString();
        HashMap<String, String> values = new HashMap<>();
        values.put(value, text);
        String json = JSON.hashMapToJSON(values);
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.beyerhack.club/website/update"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorization)
                    .method("POST", HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200)
            {
                logger.warn(response.body());
            }
            if(response.statusCode() == 403)
            {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Error");
                eb.setDescription("The api key is not valid!");
                eb.setColor(Color.red);
                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                return;
            } else if (response.statusCode() != 200) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Error");
                eb.setDescription("An error has occurred! Status Code: " + response.statusCode());
                eb.setColor(Color.red);
                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                return;
            }
            EmbedBuilder eb = new EmbedBuilder();
            String embedMessage = "";
            if(value.equals("announcement"))
            {
                embedMessage = "latest announcement to `";
            }
            if(value.equals("meeting"))
            {
                embedMessage = "next meetings's info to `";
            }
            eb.setTitle("Updated Website!").setColor(Color.BLUE).setDescription("Set the " + embedMessage + text + "`.").setFooter("Beyer Hack Club").setThumbnail(Assets.getLogo());
            event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
        }catch (URISyntaxException | IOException | InterruptedException e )
        {
            event.getHook().sendMessage("An error has occurred: " + e.getMessage()).setEphemeral(true).queue();
        }
    }

    private void log(SlashCommandInteractionEvent event)
    {
        if(!permissionCheck(event, "1488731960053469337", false)) return;
        TextInput dateAndDuration = TextInput.create("date", TextInputStyle.SHORT)
                .setPlaceholder("mm/dd/yy|hh:mm:ss")
                .setMinLength(3)
                .setMaxLength(60)
                .setRequired(true)
                .build();
        TextInput objective = TextInput.create("objective", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Objective")
                .setMinLength(3)
                .setRequired(true)
                .build();
        TextInput activities = TextInput.create("activities", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Please describe the activities")
                .setMinLength(3)
                .setRequired(true)
                .build();
        TextInput oldNews = TextInput.create("old", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Old News")
                .setMinLength(3)
                .setMaxLength(60)
                .setRequired(true)
                .build();
        TextInput newNews = TextInput.create("new", TextInputStyle.PARAGRAPH)
                .setPlaceholder("New News")
                .setMinLength(3)
                .setRequired(true)
                .build();
        Modal modal = Modal.create("logs", "Submit a log")
                .addComponents(
                        Label.of("Date and Duration", dateAndDuration),
                        Label.of("Objective", objective),
                        Label.of("Activities", activities),
                        Label.of("New News", newNews),
                        Label.of("Old News", oldNews)
                ).build();
        event.replyModal(modal).queue();
    }
    private void resetAndStop(SlashCommandInteractionEvent event){
        String requiredRole = "1488731960053469337";
        if(!permissionCheck(event, requiredRole, true)) return;
        event.getGuild().retrieveCommands().queue(commands -> {
            for (Command command: commands){
                command.delete().queue();
            }
            System.exit(0);
        });

    }

    /**
     *
     * @param event The slash command used.
     * @param role The role to check whether the member has.
     * @return Whether the member has the role.
     */
    public boolean permissionCheck(SlashCommandInteractionEvent event, String role, boolean deferred)
    {
        if(!Objects.requireNonNull(event.getMember()).getRoles().contains(Objects.requireNonNull(event.getGuild()).getRoleById(role)))
        {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("No Permission!").setColor(Color.RED).setDescription("You must have the <@&"+role+"> role to use this command!").setFooter("Beyer Hack Club");

            if(deferred)
            {
                event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();
            }
            else{
                event.replyEmbeds(eb.build()).setEphemeral(true).queue();

            }
            return false;
        }
        else {

            return true;
        }
    }

}
