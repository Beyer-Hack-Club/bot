package xyz.pbsi.Utils;

import com.google.gson.Gson;
import net.dv8tion.jda.api.components.selections.SelectMenu;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class MemberUtils {

    private static final Logger log = LoggerFactory.getLogger(MemberUtils.class);

    public static SelectMenu getMembersSelectMenu(){

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
            log.error(e.getMessage());
        }
        return menuBuilder.build();
    }
}
