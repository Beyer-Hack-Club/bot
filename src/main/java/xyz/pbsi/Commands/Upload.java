package xyz.pbsi.Commands;

import net.dv8tion.jda.api.components.attachmentupload.AttachmentUpload;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.modals.Modal;
import xyz.pbsi.Interfaces.DiscordCommand;

public class Upload implements DiscordCommand {
    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {
        //Creates the modal for uploading files
        AttachmentUpload upload = AttachmentUpload.create("file").setRequired(true).setMaxValues(1).build();
        event.replyModal(Modal.create("file-upload", "Upload a file").addComponents(Label.of("Upload", upload)).build()).queue();
    }
}
