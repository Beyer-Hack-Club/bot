package xyz.pbsi.Commands;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.pbsi.Interfaces.DiscordCommand;
import xyz.pbsi.Listeners.CommandListener;
import xyz.pbsi.Utils.Assets;
import xyz.pbsi.Utils.JSON;
import xyz.pbsi.Utils.Roles;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Objects;

import static xyz.pbsi.Utils.Roles.permissionCheck;

public class UpdateWebsite implements DiscordCommand {
    Logger logger = LoggerFactory.getLogger(UpdateWebsite.class);
    Dotenv dotenv = Dotenv.load();
    String authorization = dotenv.get("SECRET");
    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {

        //Disables Discord's auto timeout (Allows the bot to respond)
        event.deferReply().setEphemeral(true).queue();

        //Creates a client to make web requests
        HttpClient client = HttpClient.newHttpClient();
        if(!permissionCheck(event, Roles.getRoleID(Roles.RolesList.LEADERSHIP), true)) return;

        //Gets the values from the command arguments and verifies that they aren't null
        String value = Objects.requireNonNull(event.getOption("value")).getAsString().toLowerCase();
        String text = Objects.requireNonNull(event.getOption("text")).getAsString();

        //Puts the values in a Hash Map
        HashMap<String, String> values = new HashMap<>();
        values.put(value, text);

        //Converts the Hash Map to JSON for the browser
        String json = JSON.hashMapToJSON(values);
        try{

            //Builds the web request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.beyerhack.club/website/update"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorization)
                    .method("POST", HttpRequest.BodyPublishers.ofString(json))
                    .build();
            //Sends and gets the response from the http request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            client.close();

            //Logs any errors
            if(response.statusCode() != 200)
            {
                logger.warn(response.body());
            }

            //Handles errors
            //Checks if the API Key is invalid
            if(response.statusCode() == 403)
            {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Error");
                eb.setDescription("The api key is not valid!");
                eb.setColor(Color.red);
                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                return;
            } else if (response.statusCode() != 200) {
                //Handles all other errors
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Error");
                eb.setDescription("An error has occurred! Status Code: " + response.statusCode());
                eb.setColor(Color.red);
                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                return;
            }

            //Sends the success message if the website was updated successfully
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
            //Catches errors
            event.getHook().sendMessage("An error has occurred: " + e.getMessage()).setEphemeral(true).queue();
        }

    }
}
