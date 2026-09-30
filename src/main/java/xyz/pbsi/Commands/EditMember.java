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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static xyz.pbsi.Utils.Roles.permissionCheck;

public class EditMember implements DiscordCommand {
    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {
        Logger logger = LoggerFactory.getLogger(CommandListener.class);
        if(!permissionCheck(event, "1488731960053469337", false)) return;
        Gson gson = new Gson();
        StringSelectMenu.Builder menuBuilder = StringSelectMenu.create("member-select")
                .setPlaceholder("Select a member")
                .setRequiredRange(1,1)
                .setRequired(true);

        File folder = new File("/var/lib/bhc");
        String[] folderList = folder.list();
        try{
            if(folderList != null)
            {
                for (String s : folderList) {
                    if(s.equals("data")){continue;}
                    BufferedReader bufferedReader = new BufferedReader(new FileReader("/var/lib/bhc/" + s));
                    String formattedArg = s.replace(".json", "");
                    Member member = gson.fromJson(bufferedReader, Member.class);
                    menuBuilder = menuBuilder.addOption(formattedArg + " | " + member.getFirstName(), formattedArg);
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            event.reply("An error has occurred: " + e.getMessage()).setEphemeral(true).queue();
        }
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
        TextInput value = TextInput.create("new-value",  TextInputStyle.SHORT).build();
        Modal modal = Modal.create("edit-member", "Edit a member").addComponents(
                Label.of("Member", menuBuilder.build()),
                Label.of("Value to edit", editValue),
                Label.of("New Value", value)
        ).build();
        event.replyModal(modal).queue();
    }
}
