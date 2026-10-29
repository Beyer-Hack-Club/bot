package xyz.pbsi.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import software.amazon.awssdk.services.s3.model.S3Object;
import xyz.pbsi.Interfaces.DiscordCommand;
import xyz.pbsi.Utils.Assets;
import xyz.pbsi.Utils.CloudflareR2Client;

import java.awt.*;
import java.util.List;
import java.util.Objects;

import static xyz.pbsi.Main.config;

public class MyUploads implements DiscordCommand {

    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {

        //Creates a client for connecting to the R2 bucket
        CloudflareR2Client client = new CloudflareR2Client(config);

        //Gets a list of objects inside the bucket
        List<S3Object> objects =  client.listObjects("bhc-user-cdn");

        StringBuilder uploads = new StringBuilder();
        long size = 0;

        //Iterates through all the objects in the database and checks if the path contains the user id and if so counts adds it to the list and adds it's size to the member's storage total.
        for(S3Object object : objects)
        {
            if(object.key().contains(Objects.requireNonNull(event.getMember()).getId())){
                //Converts the size of the object to megabytes from bytes
                size = (long) (size + (object.size() * 10e-7));

                //Adds the object name and link to the embed
                uploads.append("- [").append(object.key().replace("upload/" + event.getMember().getId() + "/", "")).append("](https://uploads.beyerhack.club/").append(object.key()).append(")\n");
            }
        }
        if(uploads.isEmpty())
        {
            event.reply("You have no uploads! You should try uploading something </upload:1559048033788756001> !").setEphemeral(true).queue();
            return;
        }

        //Responds with the list of objects that a member has uploaded
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Your Uploads");
        eb.setDescription("**Uploads**: \n" + uploads + "\nTotal storage used: " + size + "mb ");
        eb.setColor(Color.blue);
        eb.setFooter("Beyer Hack Club", Assets.getLogo());
        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }
}
