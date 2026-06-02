package xyz.pbsi.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class MemberJoin implements EventListener {
    @Override
    public void onEvent(GenericEvent genericevent) {
        if(genericevent instanceof GuildMemberJoinEvent event)
        {
            User user = event.getUser();
            TextChannel channel = event.getGuild().getTextChannelById("1488744694749069323");
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Welcome!");
            embedBuilder.setDescription("Welcome to the server, " + user.getAsMention() +   "! You are member #" + event.getGuild().getMemberCount() + "!\nPlease fill out a [quick form](https://discord.com/channels/1488731919037366482/1501831917425655878/1501836684206276628) as it helps us plan!");
            embedBuilder.setThumbnail(user.getAvatarUrl());
            embedBuilder.setFooter("Beyer Hack Club", event.getGuild().getIconUrl());
            assert channel != null;
            channel.sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }
}
