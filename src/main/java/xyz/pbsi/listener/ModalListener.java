package xyz.pbsi.listener;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.pbsi.utils.Assets;
import xyz.pbsi.utils.JSON;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ModalListener extends ListenerAdapter {
    private static Logger logger = LoggerFactory.getLogger(CommandListener.class);
    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("APIKEY");


    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getCustomId().equals("survey")) {
            survey(event);
        }
        if(event.getCustomId().equals("git-signup"))
        {
            if(apiKey == null){
                logger.error("Apikey is null");
                return;
            }
            gitSignup(event);
        }
    }

    private void gitSignup(ModalInteractionEvent event)
    {
        event.deferReply().queue();
        
        String username = getString(event, "username");
        String email = getString(event, "email");
        String fullName = getString(event, "full-name");
        StringBuilder tempPasswordGenerator = new StringBuilder(UUID.randomUUID().toString());
        tempPasswordGenerator.setLength(8);

        String tempPassword = tempPasswordGenerator.toString();
        String apiURL = "https://git.beyerhack.club/api/v1/admin/users";

        HttpClient client = HttpClient.newHttpClient();
        try {
            HashMap<String, String> webBody = new HashMap<>();
            webBody.put("username", username);
            webBody.put("full_name", fullName);
            webBody.put("password", tempPassword);
            webBody.put("email", email);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiURL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", apiKey)
                    .method("POST", HttpRequest.BodyPublishers.ofString(JSON.hashMapToJSON(webBody)
                    ))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            EmbedBuilder embedBuilder = new EmbedBuilder();
            if(response.statusCode() != 201)
            {
                Gson gson = new Gson();
                HashMap<?, ?> hashMap = gson.fromJson(response.body(), HashMap.class);
                String error = (String) hashMap.get("message");
                embedBuilder.setTitle("Error");
                embedBuilder.setDescription("An error has occurred! " + error);
                embedBuilder.setColor(Color.red);
                embedBuilder.setFooter("Beyer Hack Club", "https://s3.beyerhack.club/logos/raster/logo.png");
                event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();

            }
            else {
                embedBuilder.setTitle("Created Account! :white_check_mark:");
                embedBuilder.setDescription("Username: `" + username + "`\nPassword: `" +tempPassword + "`\n*You will have to update your password upon signing in.*");
                embedBuilder.setColor(new Color(23, 223, 62));
                embedBuilder.setFooter("Beyer Hack Club", "https://s3.beyerhack.club/logos/raster/logo.png");
                event.getHook().sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();
                event.getUser().openPrivateChannel().flatMap(
                        channel -> channel.sendMessageEmbeds(embedBuilder.build())
                ).queue();
            }

        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }



    private void survey(ModalInteractionEvent event)
    {
        String discordUsername = event.getUser().getName();
        String name = getString(event, "name");
        String learn = getString(event, "learn");
        String available = getString(event, "availability");
        String experience = getString(event, "experience");
        String comments = getString(event, "comments");
        TextChannel channel = Objects.requireNonNull(event.getGuild()).getTextChannelById("1501836882936332308");
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
        eb.setThumbnail(event.getUser().getAvatarUrl());
        eb.setFooter("Beyer Hack Club", Assets.getLogo());
        eb.setDescription("**Discord Username: **" + discordUsername +
                "\n**Name: ** " + name +
                "\n**Wants to learn: ** " + learn +
                "\n**Availability: **" + available +
                "\n**Experience: **" + experience +
                "\n**Comments: **" + comments);

        channel.sendMessageEmbeds(eb.build()).queue();
        event.reply("Thanks for submitting a response!").setEphemeral(true).queue();
    }

    /**
     *
     * @param event The modal event, used for the context of getting the customId.
     * @param customId The id to identify
     * @return The string, not null.
     */
    public static String getString(ModalInteractionEvent event, String customId)
    {
        return Objects.requireNonNull(event.getValue(customId)).getAsString();
    }
}
