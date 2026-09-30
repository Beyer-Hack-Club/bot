package xyz.pbsi.Commands;

import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.container.Container;
import net.dv8tion.jda.api.components.section.Section;
import net.dv8tion.jda.api.components.separator.Separator;
import net.dv8tion.jda.api.components.textdisplay.TextDisplay;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import xyz.pbsi.Interfaces.DiscordCommand;

import java.awt.*;

public class Info implements DiscordCommand {
    @Override
    public void commandExecutor(SlashCommandInteractionEvent event) {
        Separator separator = Separator.create(true, Separator.Spacing.SMALL);
        TextDisplay title = TextDisplay.of("### Beyer Hack Club Info");
        TextDisplay baseInfo = TextDisplay.of("Beyer Hack Club is a programming club with the goal of sharing knowledge and collaborating on projects! People will work both club activities as well as personal/group projects!");
        TextDisplay hackClub = TextDisplay.of("**What is a Hack Club?**");
        TextDisplay hackClubInfo = TextDisplay.of("Hack Club is a non profit organizations which provides resources for programming clubs, such as fundraising tools, activities, and platforms to share your code! Beyer Hack Club is Hack Club chapter.\nMore info can be found [here](https://hack.club/clubs)!");
        TextDisplay meetings = TextDisplay.of("**When will meetings be?**");
        TextDisplay meetingsInfo = TextDisplay.of("Meetings will be every Tuesday after school in the robotics shop! ");
        TextDisplay surveyText = TextDisplay.of("Want to help decide dates and activities?");
        TextDisplay fiscalSponsorship = TextDisplay.of("-# Beyer Hack Club is fiscally sponsored by The Hack Foundation (d.b.a. Hack Club), a 501(c)(3) nonprofit (EIN: 81-2908499). [Learn more](https://hackclub.com/fiscal-sponsorship) | [View our financials](https://hcb.hackclub.com/beyer-hack-club)");
        Section section = Section.of(Button.primary("open-survey", "Open Survey"), surveyText);
        Container container = Container.of(title, separator, baseInfo, separator, hackClub, hackClubInfo, separator, meetings, meetingsInfo, separator, section, fiscalSponsorship).withAccentColor(Color.GREEN);
        MessageCreateData messageData = new MessageCreateBuilder()
                .useComponentsV2(true)
                .setComponents(container)
                .build();
        event.reply(messageData).setEphemeral(true).queue();
    }
}
