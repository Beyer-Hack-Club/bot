package xyz.pbsi.Utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.Objects;

public class Roles {
    public enum RolesList {
        ADMIN,
        LEADERSHIP,
        SOCIALMEDIA,
        MEMBER,
    }
    public static String getRoleID(RolesList role)
    {
        return switch (role) {
            case ADMIN -> "1488741240353722500";
            case LEADERSHIP -> "1488731960053469337";
            case SOCIALMEDIA -> "1549091886361215028";
            case MEMBER -> "1488732014982074408";
        };
    }

    /**
     *
     * @param event The slash command used.
     * @param role The role to check whether the member has.
     * @return Whether the member has the role.
     */
    public static boolean permissionCheck(SlashCommandInteractionEvent event, String role, boolean deferred)
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
