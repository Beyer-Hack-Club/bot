package xyz.pbsi.listener;


import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.modals.Modal;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if(event.getChannelId().equals("1501831917425655878"))
        {
            if(event.getComponentId().equals("open-survey"))
            {
                TextInput learn = TextInput.create("learn", TextInputStyle.PARAGRAPH)
                        .setMinLength(2)
                        .setRequired(true)
                        .build();
                TextInput available = TextInput.create("availability", TextInputStyle.PARAGRAPH)
                        .setMinLength(3)
                        .setRequired(true)
                        .build();
                TextInput experience = TextInput.create("experience", TextInputStyle.PARAGRAPH)
                        .setMinLength(1)
                        .setRequired(false)
                        .build();
                TextInput comments = TextInput.create("comments", TextInputStyle.PARAGRAPH)
                        .setMaxLength(300)
                        .setRequired(false)
                        .build();
                Modal modal = Modal.create("appeals", "Punishment Appeals")
                        .addComponents(
                                net.dv8tion.jda.api.components.label.Label.of("What do you want to learn next year?", learn),
                                net.dv8tion.jda.api.components.label.Label.of("What days & times will you be available!", available),
                                net.dv8tion.jda.api.components.label.Label.of("Do you have any experience in programming?", experience),
                                net.dv8tion.jda.api.components.label.Label.of("Any additional comments?", comments)

                        )
                        .build();

                event.replyModal(modal).queue();
            }
        }
    }
}
