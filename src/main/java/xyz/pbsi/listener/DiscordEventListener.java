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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.pbsi.HCBot;

import java.awt.*;
import java.io.File;

public class DiscordEventListener extends ListenerAdapter {
    private static Logger logger = LoggerFactory.getLogger(CommandListener.class);

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
    }


    public void setupAction(ShardManager jda)
    {
        Guild g = jda.getGuildById("1488731919037366482");
        assert g != null;
        TextChannel channel = g.getTextChannelById("1537685243425984562");
        assert channel != null;
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(0, 150, 255));
        embed.setTitle("Git Registration!");
        embed.setDescription("Sign up for git.beyerhack.club! We'll be using this to store our code!");
        embed.setFooter("Beyer Hack Club");
        Button openSurvey = Button.success("git-signup", "Sign Up");
        channel.sendMessageEmbeds(embed.build()).addComponents(
                ActionRow.of(openSurvey)
        ).queue();
    }
    public void registerCommands(ShardManager jda)
    {
        Guild g = jda.getGuildById("1488731919037366482");
        //setupAction(jda);
    if(g != null)
    {
        CommandListUpdateAction commands = g.updateCommands();
        commands.addCommands(
                Commands.slash("uptime", "Bot uptime"),
                Commands.slash("log", "Adds a new log to the website"),
                Commands.slash("info", "Some basic information about the club!"),
                Commands.slash("donate", "Provides the link to donate to the club!"),
                Commands.slash("update-website", "Updates either the latest announcement or the next meeting date on the website!").addOptions(
                        new OptionData(OptionType.STRING, "value", "The value to update", true)
                                .addChoice("Meeting", "Meeting")
                                .addChoice("Announcement", "Announcement"),
                        new OptionData(OptionType.STRING, "text", "The new value", true)
                ),
                Commands.slash("add-member", "Adds a member"),
                Commands.slash("get-member", "Gets a members info").addOptions(new OptionData(
                        OptionType.STRING, "id", "The Student ID of the member", true
                )),
                Commands.slash("reset-and-stop", "Deletes all commands and stops the bot.")
        ).queue();
        File folder = new File("./bhc/members/");
        if(!folder.exists()) {
            if (!folder.mkdirs()) {
                logger.error("Failed to make directory!");
            }
        }

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
