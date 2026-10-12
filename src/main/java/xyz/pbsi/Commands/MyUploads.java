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
        CloudflareR2Client client = new CloudflareR2Client(config);
        List<S3Object> objects =  client.listObjects("bhc-user-cdn");
        StringBuilder uploads = new StringBuilder();
        for(S3Object object : objects)
        {
            if(object.key().contains(Objects.requireNonNull(event.getMember()).getId())){
                uploads.append("- https://uploads.beyerhack.club/").append(object.key()).append("\n");
            }
        }
        if(uploads.isEmpty())
        {
            event.reply("You have no uploads! You should try uploading something </upload:1559048033788756001> !").setEphemeral(true).queue();
            return;
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Your Uploads");
        eb.setDescription("**Uploads**: \n" + uploads);
        eb.setColor(Color.blue);
        eb.setFooter("Beyer Hack Club", Assets.getLogo());
        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
    }
}
