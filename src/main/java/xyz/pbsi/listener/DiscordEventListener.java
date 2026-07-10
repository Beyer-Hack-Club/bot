package xyz.pbsi.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import xyz.pbsi.HCBot;

import java.awt.*;

public class DiscordEventListener extends ListenerAdapter {

    public HCBot bot;
    private static DiscordEventListener INSTANCE;


    public DiscordEventListener(HCBot bot)
    {
        this.bot = bot;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        registerCommands(bot.getShardManager());
        //setupAction(bot.getShardManager());

    }
    public void setupAction(ShardManager jda)
    {
        Guild g = jda.getGuildById("1488731919037366482");
        assert g != null;
        TextChannel channel = g.getTextChannelById("1501831917425655878");
        assert channel != null;
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(35, 255, 0));
        embed.setTitle("Planning Survey!");
        embed.setDescription("Have a second? Please fill out a quick survey to provide your input so that we can plan accordingly!");
        embed.setFooter("Beyer Hack Club");
        Button openSurvey = Button.primary("open-survey", "Open Survey");
        channel.sendMessageEmbeds(embed.build()).addComponents(
                ActionRow.of(openSurvey)
        ).queue();
    }
    public void registerCommands(ShardManager jda)
    {
        Guild g = jda.getGuildById("1488731919037366482");
    if(g != null)
    {
        CommandListUpdateAction commands = g.updateCommands();
        commands.addCommands(
                Commands.slash("uptime", "Bot uptime"),
                Commands.slash("info", "Some basic information about the club!"),
                Commands.slash("donate", "Provides the link to donate to the club!"),
                Commands.slash("update-website", "Updates either the latest announcement or the next meeting date on the website!").addOptions(
                        new OptionData(OptionType.STRING, "value", "The value to update", true)
                                .addChoice("Meeting", "Meeting")
                                .addChoice("Announcement", "Announcement"),
                        new OptionData(OptionType.STRING, "text", "The new value", true)
                )
        ).queue();

/*
        commands.addCommands(Commands.slash("example", "Example Command")
                .addOption(OptionType.STRING, "String Option", "An Example String option",true),
                Commands.slash("anotherexample", "We love examples")
                        .addOptions(
                                new OptionData(OptionType.STRING, "categories", "The category of this command option", true)
                                        .addChoice("key", "value")
                        )
        ).queue();

    }
 */
    }





}}
