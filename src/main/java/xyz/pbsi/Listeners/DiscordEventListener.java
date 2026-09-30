package xyz.pbsi.Listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.pbsi.Commands.*;
import xyz.pbsi.HCBot;

import java.awt.*;
import java.io.File;

import static xyz.pbsi.Utils.EventManager.commandDataList;
import static xyz.pbsi.Utils.EventManager.registerCommand;

public class DiscordEventListener extends ListenerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DiscordEventListener.class);

    public HCBot bot;

    public DiscordEventListener(HCBot bot)
    {
        this.bot = bot;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        registerCommands(bot.getShardManager());
    }

    public void registerCommands(ShardManager jda)
    {
        Guild g = jda.getGuildById("1488731919037366482");
    if(g != null)
    {
        registerCommand(Commands.slash("info", "Some basic information about the club!"), new Info());
        registerCommand(Commands.slash("uptime", "Bot uptime"), new Uptime());
        registerCommand(Commands.slash("log", "Adds a new log to the website"), new Log());
        registerCommand(Commands.slash("donate", "Provides the link to donate to the club!"),new Donate());
        registerCommand(Commands.slash("update-website", "Updates either the latest announcement or the next meeting date on the website!").addOptions(
                new OptionData(OptionType.STRING, "value", "The value to update", true)
                        .addChoice("Meeting", "Meeting")
                        .addChoice("Announcement", "Announcement"),
                new OptionData(OptionType.STRING, "text", "The new value", true)
        ), new UpdateWebsite());
        registerCommand(Commands.slash("add-member", "Adds a member"), new AddMember());
        registerCommand(Commands.slash("get-member", "Gets a members info"), new GetMember());
        registerCommand(Commands.slash("reset-and-stop", "Deletes all commands and stops the bot."), new ResetAndStop());
        registerCommand(Commands.slash("edit-member", "Edits a member."), new EditMember());
        registerCommand(Commands.slash("keychain", "Gets logins for accounts").addOptions(
                new OptionData(OptionType.STRING, "account", "The account to get.", true)
                        .addChoice("Instagram", "instagram")
                        .addChoice("Email", "email")
                        .addChoice("Google", "google")
                        .addChoice("List Monk", "listmonk")
                        .addChoice("Socials Emails", "socials")
        ), new Keychain());
        CommandListUpdateAction commands = g.updateCommands();
        commands.addCommands(commandDataList).queue();
        File folder = new File("/var/lib/bhc");
        if(!folder.exists()) {
            if (!folder.mkdirs()) {
                logger.error("Failed to make directory!");
            }
        }
    }





}}
