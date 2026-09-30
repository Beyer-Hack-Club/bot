package xyz.pbsi.Commands;

import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.modals.Modal;
import xyz.pbsi.Interfaces.DiscordCommand;

import static xyz.pbsi.Utils.Roles.permissionCheck;

public class Log implements DiscordCommand {
    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {
        if(!permissionCheck(event, "1488731960053469337", false)) return;
        TextInput dateAndDuration = TextInput.create("date", TextInputStyle.SHORT)
                .setPlaceholder("mm/dd/yy|hh:mm:ss")
                .setMinLength(3)
                .setMaxLength(60)
                .setRequired(true)
                .build();
        TextInput objective = TextInput.create("objective", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Objective")
                .setMinLength(3)
                .setRequired(true)
                .build();
        TextInput activities = TextInput.create("activities", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Please describe the activities")
                .setMinLength(3)
                .setRequired(true)
                .build();
        TextInput oldNews = TextInput.create("old", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Old News")
                .setMinLength(3)
                .setMaxLength(60)
                .setRequired(true)
                .build();
        TextInput newNews = TextInput.create("new", TextInputStyle.PARAGRAPH)
                .setPlaceholder("New News")
                .setMinLength(3)
                .setRequired(true)
                .build();
        Modal modal = Modal.create("logs", "Submit a log")
                .addComponents(
                        Label.of("Date and Duration", dateAndDuration),
                        Label.of("Objective", objective),
                        Label.of("Activities", activities),
                        Label.of("New News", newNews),
                        Label.of("Old News", oldNews)
                ).build();
        event.replyModal(modal).queue();
    }
}
