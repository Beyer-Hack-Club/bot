package xyz.pbsi.Commands;

import com.google.gson.Gson;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.modals.Modal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.pbsi.Interfaces.DiscordCommand;
import xyz.pbsi.Utils.MemberUtils;
import xyz.pbsi.Utils.Roles;


import static xyz.pbsi.Utils.Roles.permissionCheck;

public class GetMember implements DiscordCommand {
    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {
        Logger logger = LoggerFactory.getLogger(GetMember.class);
        if(!permissionCheck(event, Roles.getRoleID(Roles.RolesList.LEADERSHIP), false)) return;
        Modal modal = Modal.create("get-member", "Get a member's info").addComponents(
                Label.of("Member", MemberUtils.getMembersSelectMenu())
        ).build();
        event.replyModal(modal).queue();
    }
}
