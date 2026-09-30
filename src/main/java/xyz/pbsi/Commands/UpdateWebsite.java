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
    Logger logger = LoggerFactory.getLogger(CommandListener.class);
    Dotenv dotenv = Dotenv.load();
    String authorization = dotenv.get("SECRET");
    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {
        event.deferReply().setEphemeral(true).queue();
        HttpClient client = HttpClient.newHttpClient();
        String requiredRole = "1488731960053469337";
        if(!permissionCheck(event, requiredRole, true)) return;
        String value = Objects.requireNonNull(event.getOption("value")).getAsString().toLowerCase();
        String text = Objects.requireNonNull(event.getOption("text")).getAsString();
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
}
