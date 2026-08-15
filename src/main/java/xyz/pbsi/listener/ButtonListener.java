package xyz.pbsi.listener;


import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.modals.Modal;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
            if(event.getComponentId().equals("open-survey"))
            {
                survey(event);
            }
            if(event.getComponentId().equals("git-signup"))
            {
                gitSignup(event);
            }
    }


    private void gitSignup(ButtonInteractionEvent event)
    {
       TextInput username = TextInput.create("username", TextInputStyle.SHORT)
               .setPlaceholder("Please type in a username!")
               .setMinLength(3)
               .setMaxLength(16)
               .setRequired(true)
               .build();
        TextInput fullName = TextInput.create("full-name", TextInputStyle.SHORT)
                .setPlaceholder("Please type in a display name!")
                .setMinLength(1)
                .setMaxLength(32)
                .setRequired(true)
                .build();
        TextInput email = TextInput.create("email", TextInputStyle.SHORT)
                .setPlaceholder("Please type in your email! (Personal / School)")
                .setMinLength(3)
                .setMaxLength(64)
                .setRequired(true)
                .build();
       Modal modal = Modal.create("git-signup", "Signup")
               .addComponents(
                       Label.of("Username", username),
                       Label.of("Display Name", fullName),
                       Label.of("Email", email)
               ).build();
       event.replyModal(modal).queue();
    }
    private void survey(ButtonInteractionEvent event)
    {
        TextInput name = TextInput.create("name", TextInputStyle.SHORT)
                .setMinLength(1)
                .setMaxLength(75)
                .setPlaceholder("Please put the name you go by and your last name!")
                .setRequired(true)
                .build();
        TextInput learn = TextInput.create("learn", TextInputStyle.PARAGRAPH)
                .setMinLength(2)
                .setPlaceholder("Any skills you want to learn or any projects you want to do?")
                .setRequired(false)
                .build();
        TextInput available = TextInput.create("availability", TextInputStyle.PARAGRAPH)
                .setMinLength(3)
                .setPlaceholder("Days of the week and times are ideal!")
                .setRequired(true)
                .build();
        TextInput experience = TextInput.create("experience", TextInputStyle.PARAGRAPH)
                .setMinLength(1)
                .setPlaceholder("Don't worry if you don't! This club is for all skill levels.")
                .setRequired(false)
                .build();
        TextInput comments = TextInput.create("comments", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Suggestions, ideas, or comments? Share them here!")
                .setRequired(false)
                .build();
        Modal modal = Modal.create("survey", "Welcome Survey")
                .addComponents(
                        Label.of("What's your name?", name),
                        net.dv8tion.jda.api.components.label.Label.of("What do you want to learn next year?", learn),
                        net.dv8tion.jda.api.components.label.Label.of("What days & times will you be available?", available),
                        net.dv8tion.jda.api.components.label.Label.of("Do you have any experience in programming?", experience),
                        net.dv8tion.jda.api.components.label.Label.of("Any additional comments?", comments)

                )
                .build();

        event.replyModal(modal).queue();
    }
}
