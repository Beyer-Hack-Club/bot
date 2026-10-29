package xyz.pbsi.Listeners;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.pbsi.Main;
import xyz.pbsi.Utils.*;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ModalListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CommandListener.class);
    Dotenv dotenv = Dotenv.load();
    String authorization = dotenv.get("SECRET");
    String apiKey = dotenv.get("APIKEY");


    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        //Maps the event to the executor function, passes the interaction event so that the functions can reply to it.
        switch (event.getCustomId()){
            case "survey":
                survey(event);
                break;
            case "git-signup":
                if(apiKey == null){
                    logger.error("Apikey is null");
                    return;
                }
                gitSignup(event);
                break;
            case "logs":
                submitLog(event);
                break;
            case "create-user":
                createUser(event);
                break;
            case "edit-member":
                editUser(event);
                break;
            case "get-member":
                getMember(event);
                break;
            case "file-upload":
                fileUpload(event);
                break;
            default:
                event.reply("This modal is misconfigured (Err. Lacking function!)").setEphemeral(true).queue();
                break;
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
                embedBuilder.setFooter("Beyer Hack Club", Assets.getLogo());
                event.getHook().sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();

            }
            else {
                embedBuilder.setTitle("Created Account! :white_check_mark:");
                embedBuilder.setDescription("Username: `" + username + "`\nPassword: `" +tempPassword + "`\n*You will have to update your password upon signing in.*");
                embedBuilder.setColor(new Color(23, 223, 62));
                embedBuilder.setFooter("Beyer Hack Club", Assets.getLogo());
                event.getHook().sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();
                event.getUser().openPrivateChannel().flatMap(
                        channel -> channel.sendMessageEmbeds(embedBuilder.build())
                ).queue();
            }

        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void submitLog(ModalInteractionEvent event)
    {
        //Disables Discord's auto timeout
        event.deferReply(true).queue();

        //Gets the fields from the modal
        String dateAndDuration = getString(event, "date");
        String objective = getString(event, "objective");
        String activities = getString(event, "activities");
        String oldNews = getString(event, "old");
        String newNews = getString(event, "new");
        String date  = "";
        String duration = "";
        if(!dateAndDuration.contains("|"))
        {
            event.getHook().sendMessage("You need to format the date / duration correctly!").setEphemeral(true).queue();
            return;
        }
        boolean settingDate = true;
        //Separates the date and duration (Modals were too small to fit all the information)
        for (int i = 0; i < dateAndDuration.length(); i++) {
            if(dateAndDuration.charAt(i) == '|')
            {
                settingDate = false;
                i++;
            }
            if(settingDate)
            {
                date = date.concat(String.valueOf(dateAndDuration.charAt(i)));
            }
            else{
                duration = duration.concat(String.valueOf(dateAndDuration.charAt(i)));
            }
        }

        String apiURL = "https://api.beyerhack.club/logs/meeting";

        //Creates the post request
        HttpClient client = HttpClient.newHttpClient();
        try {
            HashMap<String, String> webBody = new HashMap<>();
            webBody.put("date", date);
            webBody.put("duration", duration);
            webBody.put("objective", objective);
            webBody.put("activities", activities);
            webBody.put("old", oldNews);
            webBody.put("new", newNews);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiURL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", authorization)
                    .method("POST", HttpRequest.BodyPublishers.ofString(JSON.hashMapToJSON(webBody)
                    ))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            EmbedBuilder embedBuilder = new EmbedBuilder();
            //Logs errors if the server responds with the wrong status code.
            if(response.statusCode() != 200)
            {
                logger.error("{}{}", response.statusCode(), response.body());

            }
            else {
                embedBuilder.setTitle("Submitted Meeting log! :white_check_mark:");
                embedBuilder.setDescription("Successfully submitted meeting log!");
                embedBuilder.setColor(new Color(23, 223, 62));
            }
            embedBuilder.setFooter("Beyer Hack Club", Assets.getLogo());
            event.getHook().sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();

        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void survey(ModalInteractionEvent event)
    {
        String discordUsername = event.getUser().getName();
        //Gets the fields from the modals
        String name = getString(event, "name");
        String learn = getString(event, "learn");
        String available = getString(event, "availability");
        String experience = getString(event, "experience");
        String comments = getString(event, "comments");

        //Channel for storing survey responses
        TextChannel channel = Objects.requireNonNull(event.getGuild()).getTextChannelById("1501836882936332308");
        assert channel != null;

        //Handles empty fields
        if (comments.isEmpty()) {
            comments = "None provided!";
        }
        if (experience.isEmpty()) {
            experience = "None shared!";
        }

        //Builds the embed to store the survey information in
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

    private void editUser(ModalInteractionEvent event)
    {
        try{
            Gson gson = new Gson();
            //Gets the member from the dropdown (It returns a list, but it will *always* have a length of one, so use .getFirst())
            String memberID =  Objects.requireNonNull(event.getValue("member-select")).getAsStringList().getFirst();

            //Created the reader to read the content from inside the file
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/var/lib/bhc/" + memberID + ".json"));
            //Converts the JSON into the design specified by the Member class
            Member member = gson.fromJson(bufferedReader, Member.class);

            //Gets the values from the modal to modify
            String valueEditing = Objects.requireNonNull(event.getValue("edit-value")).getAsStringList().getFirst();
            String newValue = Objects.requireNonNull(event.getValue("new-value")).getAsString();

            //Finds (and edits) the value specified in the modal
            switch (valueEditing){
                case "first-name":
                    member.setFirstName(newValue);
                    break;
                case "last-name":
                    member.setLastName(newValue);
                    break;
                case "preferred-name":
                    member.setPreferredName(newValue);
                    break;
                case "pronouns":
                    member.setPronouns(newValue);
                case "phone-number":
                    member.setPhoneNumber(newValue);
                    break;
                case "email":
                    member.setEmail(newValue);
                    break;
                case "permission-slip":
                    member.setPermissionSlip(newValue.equalsIgnoreCase("true"));
                    break;
                case "equipment-contract":
                    member.setEquipmentContract(newValue.equalsIgnoreCase("true"));
                    break;
            }
            try{
                //Converts the Member object to JSON and writes it to the pre-existing file
                FileWriter fileWriter = new FileWriter("/var/lib/bhc/" + memberID + ".json");
                fileWriter.write(gson.toJson(member));
                fileWriter.close();
                event.reply("Successfully edited " + valueEditing + " to " + newValue + "!").setEphemeral(true).queue();
            } catch (IOException e) {
                //Logs the exception if the file couldn't be written.
                logger.error(e.getMessage());
                event.reply("An error has occurred: " + e.getMessage()).setEphemeral(true).queue();
            }
        }catch (IOException e){
            logger.error(e.getMessage());
            event.reply("Couldn't edit value, does the member exist?").setEphemeral(true).queue();
        }

    }

    private void getMember(ModalInteractionEvent event)
    {
        //Gets the member from the drop-down (Length of 1 always)
        String memberID = Objects.requireNonNull(event.getValue("member-select")).getAsStringList().getFirst();
        //Gets the file containing the member information
        File file = new File("/var/lib/bhc/" + memberID + ".json");
        try{
            //Reads the contents of the file and converts it into a Member object.
            BufferedReader reader = new BufferedReader(new FileReader(file));
            Member member = new Gson().fromJson(reader, Member.class);

            //Creates the message containing the member information.
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Member - " + member.getFirstName());
            embedBuilder.setDescription("**First Name**: " + member.getFirstName()
                    +"\n**Last Name**: " + member.getLastName() +
                    "\n**Preferred Name**: " + member.getPreferredName() +
                    "\n**Pronouns**: " + member.getPronouns()+
                    "\n**Email**: " + member.getEmail() +
                    "\n**Phone Number**: " + member.getPhoneNumber() +
                    "\n**Equipment Contract**: " + member.getEquipmentContract()+
                    "\n**Permission Slip** " + member.getPermissionSlip());
            embedBuilder.setColor(Color.blue);
            embedBuilder.setFooter("Beyer Hack Club", Assets.getLogo());
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        } catch (IOException e) {
            //Handles any error with reading and writing files
            event.reply("Error, member not found").setEphemeral(true).queue();
        }
    }

    private void createUser(ModalInteractionEvent event)
    {
        //Gets the raw JSON
        String json = getString(event, "json");
        Gson gson = new Gson();
        Member member = gson.fromJson(json, Member.class);
        //Gets the student ID to be used as the file name
        String id = member.getStudentID();
        File folder = new File("/var/lib/bhc");

        //Creates the data folder and handles any errors with such
        if(!folder.exists()) {
            event.reply("Making folder").setEphemeral(true).queue();
            if (!folder.mkdirs()) {
                event.reply("Something went wrong, see logs.").setEphemeral(true).queue();
                return;
            }
        }
        //Creates the file object to be written
        File file = new File(folder + "/" + id+".json");

        //Checks if the member already exists
        if(file.exists())
        {
            event.reply("err (exists)").setEphemeral(true).queue();
            return;
        }
        try{
            //Writes the file
            FileWriter fileWriter = new FileWriter(folder + "/" + id + ".json");
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException e) {
            //Handles errors with reading and writing files
            logger.error(e.getMessage());
            event.reply(e.getMessage()).setEphemeral(true).queue();
            return;
        }
         event.reply("Successfully created user").setEphemeral(true).queue();
    }

    private void fileUpload(ModalInteractionEvent event)  {
        //Disables Discord's auto timeout (Gives the bot more time to respond)
        event.deferReply().setEphemeral(true).queue();

        //Gets the file as an attachment, list is always one so get first
        Message.Attachment attachment = Objects.requireNonNull(event.getValue("file")).getAsAttachmentList().getFirst();

        //Gets the URL of the attachment uploaded to Discord (Attachments in modals are links as they are uploaded to Discord's CDN)
        String url =  attachment.getUrl();

        //Creates a temp folder to download the files before re-uploading them
        File dir = new File("/temp");
        dir.mkdir();
        try{
            //Downloads the file
            InputStream in = new URL(url).openStream();
            Files.copy(in, Paths.get("/temp/", attachment.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e)
        {
         event.getHook().sendMessage("An error has occurred").setEphemeral(true).queue();
         logger.error(e.getMessage());
        }

        //Uploads the file to Cloudflare R2 Object Storage
        new CloudflareR2Client(Main.config).putObject("bhc-user-cdn", "upload/" + Objects.requireNonNull(event.getMember()).getId() + "/" + attachment.getFileName(), new File("/temp/" + attachment.getFileName()));

        //Responds with the link
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("File Upload");
        eb.setColor(Color.blue);
        eb.setDescription("Your file has been uploaded! https://uploads.beyerhack.club/upload/" + Objects.requireNonNull(event.getMember()).getId() + "/" + attachment.getFileName() + " .");
        eb.setFooter("Beyer Hack Club", Assets.getLogo());
        event.getHook().sendMessageEmbeds(eb.build()).setEphemeral(true).queue();

        //Deletes the temp file
        new File("/temp/" + attachment.getFileName()).delete();
    }

    /**
     * Gets the string from a modal without allowing null values.
     * @param event The modal event, used for the context of getting the customId.
     * @param customId The id to identify
     * @return The string, not null.
     */
    public static String getString(ModalInteractionEvent event, String customId)
    {
        return Objects.requireNonNull(event.getValue(customId)).getAsString();
    }
}
