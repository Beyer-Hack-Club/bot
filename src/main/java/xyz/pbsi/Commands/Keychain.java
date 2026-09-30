package xyz.pbsi.Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.pbsi.Interfaces.DiscordCommand;
import xyz.pbsi.Listeners.CommandListener;
import xyz.pbsi.Utils.Assets;
import xyz.pbsi.Utils.Files;
import xyz.pbsi.Utils.JSON;
import xyz.pbsi.Utils.Roles;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import static xyz.pbsi.Utils.Roles.permissionCheck;

public class Keychain implements DiscordCommand {
    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {
        Logger logger = LoggerFactory.getLogger(CommandListener.class);
        try{
            String search = Objects.requireNonNull(event.getOption("account")).getAsString();
            if(search.equals("instagram") || search.equals("listmonk") || search.equals("socials")){
                if(!permissionCheck(event, Roles.getRoleID(Roles.RolesList.SOCIALMEDIA), false)) return;

            }else {
                if(!permissionCheck(event, Roles.getRoleID(Roles.RolesList.LEADERSHIP), false)) return;
            }
            HashMap<?, ?> keyChain =  JSON.JSONFileToHashmap(new File(Files.getMainDirectory() + "/data/" + "/keychain.json"));
            String username = (String) keyChain.get(search +  "-username");
            String password = (String) keyChain.get(search + "-password");
            if (username == null || password == null){
                event.reply("Couldn't find " + search).setEphemeral(true).queue();
                return;
            }
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Account Login ("+search+").");
            eb.setColor(Color.blue);
            eb.setDescription("Username: " + username + "\nPassword: ``" + password + "``");
            eb.setFooter("Beyer Hack Club", Assets.getLogo());
            event.replyEmbeds(eb.build()).setEphemeral(true).queue();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
