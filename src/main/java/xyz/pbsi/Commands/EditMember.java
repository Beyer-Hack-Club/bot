package xyz.pbsi.Commands;

import com.google.gson.Gson;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.modals.Modal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.pbsi.Interfaces.DiscordCommand;
import xyz.pbsi.Listeners.CommandListener;
import xyz.pbsi.Utils.Member;
import xyz.pbsi.Utils.MemberUtils;
import xyz.pbsi.Utils.Roles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static xyz.pbsi.Utils.Roles.permissionCheck;

public class EditMember implements DiscordCommand {
    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {
        if(!permissionCheck(event, Roles.getRoleID(Roles.RolesList.LEADERSHIP), false)) return;


        //Creates a select menu with the editable values
        StringSelectMenu editValue = StringSelectMenu.create("edit-value")
                .addOption("First Name", "first-name")
                .addOption("Last Name", "last-name")
                .addOption("Preferred Name", "preferred-name")
                .addOption("Pronouns", "pronouns")
                .addOption("Phone Number", "phone-number")
                .addOption("Email", "email")
                .addOption("Permission Slip", "permission-slip")
                .addOption("Contract", "equipment-contract")
                .setRequiredRange(1,1)
                .build();

        //Creates a text input to update the value
        TextInput value = TextInput.create("new-value",  TextInputStyle.SHORT).build();
        //Builds the modal
        Modal modal = Modal.create("edit-member", "Edit a member").addComponents(
                //Member selection menu is gotten from util class
                Label.of("Member", MemberUtils.getMembersSelectMenu()),
                Label.of("Value to edit", editValue),
                Label.of("New Value", value)
        ).build();
        event.replyModal(modal).queue();
    }
}
