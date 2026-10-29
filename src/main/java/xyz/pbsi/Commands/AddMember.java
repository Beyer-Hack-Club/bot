package xyz.pbsi.Commands;

import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.modals.Modal;
import xyz.pbsi.Interfaces.DiscordCommand;
import xyz.pbsi.Utils.Roles;

import static xyz.pbsi.Utils.Roles.permissionCheck;

public class AddMember implements DiscordCommand {
    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {
        if(!permissionCheck(event, Roles.getRoleID(Roles.RolesList.LEADERSHIP), false)) return;
        //Builds a modal for adding members
        TextInput json = TextInput.create("json", TextInputStyle.PARAGRAPH)
                .setRequired(true)
                .build();
        Modal modal = Modal.create("create-user", "Create a user").addComponents(
                Label.of("json", json)
        ).build();
        event.replyModal(modal).queue();
    }
}
