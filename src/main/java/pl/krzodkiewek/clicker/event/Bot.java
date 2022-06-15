package pl.krzodkiewek.clicker.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.krzodkiewek.clicker.Main;
import pl.krzodkiewek.clicker.clicker.Clicker;

import java.awt.*;
import java.util.Scanner;

import static pl.krzodkiewek.clicker.utils.Password.generateRandomPassword;
import static pl.krzodkiewek.clicker.utils.System.getComputerName;

public class Bot extends ListenerAdapter {

    public static boolean verified = false;
    public static EmbedBuilder embed = new EmbedBuilder();
    public static TextChannel textChannel;
    public static Clicker clicker = new Clicker();

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        super.onReady(event);

        while (true) {

            if (Main.ready) {

                textChannel = Main.getJda().getTextChannelById(Main.channelid);

                String code = generateRandomPassword(8);

                Scanner scanner = new Scanner(System.in);

                embed.setAuthor("Opened!", "https://discord.gg/bHhPqQebJ5");
                embed.setDescription("**" + Main.name + "** opened on **" + getComputerName() + "**");
                embed.addField("**Code:**", code, true);
                embed.setFooter("Dev: " + Main.author, "https://media.discordapp.net/attachments/935598249019052123/960194293228785724/F.png");
                embed.setColor(Color.CYAN);
                embed.build();

                textChannel.sendMessageEmbeds(embed.build()).queue();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                embed.clear();

                while (!verified) {

                    System.out.print("Key: ");
                    String keyTyped = scanner.nextLine();

                    if (keyTyped.equals(code)) {

                        embed.setAuthor("Verified!", "https://discord.gg/bHhPqQebJ5");
                        embed.setDescription("**" + Main.name + "** has been successfully **verified** on **" + getComputerName() + "**");
                        embed.setFooter("Dev: " + Main.author, "https://media.discordapp.net/attachments/935598249019052123/960194293228785724/F.png");
                        embed.setColor(Color.GREEN);
                        embed.build();

                        textChannel.sendMessageEmbeds(embed.build()).queue();

                        verified = true;
                    } else {
                        System.out.println("Wrong key!");
                    }

                }
                break;
            }
        }

        System.out.println("Verified");

        //Help Embed (all commands)
        embed.setColor(Color.YELLOW); //Color
        embed.setThumbnail("https://cdn.discordapp.com/attachments/935598249019052123/960194293228785724/F.png"); //Thumbnail
        //embed.setTimestamp("123"); //Time
        embed.setTitle("Commands"); //Title
        embed.setDescription("*" + Main.name + "*"); //Description
        embed.addField("**Start Clicker:**", "*c!start*",false); //Field
        embed.addField("**Help:**", "*c!help*",false); //Field
        embed.addField("**Set Cps:**", "*c!setcps <value>*",false); //Field
        embed.addField("**Extra Randomization:**", "*c!extrarandomization <Y/N>*",false); //Field
        embed.addField("**Bind:**", "*c!bind <key>*",false); //Field
        embed.setFooter("Dev: " + Main.author); //Footer
        embed.build(); //End

        //Sending Help Embed
        textChannel.sendMessageEmbeds(embed.build()).queue();

    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if(event.getAuthor().isBot()) {
            return;
        }

        embed.clear();

        String arguments[] = event.getMessage().getContentRaw().split(" ");

        if(verified){

            if(arguments[0].equalsIgnoreCase("c!start") && !(arguments.length > 1)){

                if(clicker.getCps() != 0 && clicker.getKey() != null) {

                    embed.setColor(Color.GREEN); //Color
                    embed.setThumbnail("https://cdn.discordapp.com/attachments/935598249019052123/960194293228785724/F.png"); //Thumbnail
                    //embed.setTimestamp("123"); //Time
                    embed.setTitle("Launched " + Main.name); //Title
                    embed.setDescription(Main.name + " has started. Now you can't set the clicker **(CPS, RANDOMIZATION, ETC)**. If you want to do this, restart the **clicker**"); //Description
                    embed.setFooter("Dev: " + Main.author); //Footer
                    embed.build(); //End

                    textChannel.sendMessageEmbeds(embed.build()).queue();

                    clicker.autoclicker();
                    return;

                } else {

                    embed.setColor(Color.RED); //Color
                    embed.setThumbnail("https://cdn.discordapp.com/attachments/935598249019052123/960194293228785724/F.png"); //Thumbnail
                    //embed.setTimestamp("123"); //Time
                    embed.setTitle("Error!"); //Title
                    embed.setDescription("You need to set up **BIND** and **CPS**"); //Description
                    embed.setFooter("Dev: " + Main.author); //Footer
                    embed.build(); //End

                    textChannel.sendMessageEmbeds(embed.build()).queue();

                }
            } else if (arguments[0].equalsIgnoreCase("c!setcps") && !(arguments.length > 2)){
                if(arguments.length == 1){
                    embed.setColor(Color.RED); //Color
                    embed.setThumbnail("https://cdn.discordapp.com/attachments/935598249019052123/960194293228785724/F.png"); //Thumbnail
                    //embed.setTimestamp("123"); //Time
                    embed.setTitle("Error!"); //Title
                    embed.setDescription("Correct use: c!setcps (value)"); //Description
                    embed.setFooter("Dev: " + Main.author); //Footer
                    embed.build(); //End

                    textChannel.sendMessageEmbeds(embed.build()).queue();
                } else {
                    embed.setColor(Color.GREEN); //Color
                    embed.setThumbnail("https://cdn.discordapp.com/attachments/935598249019052123/960194293228785724/F.png"); //Thumbnail
                    //embed.setTimestamp("123"); //Time
                    embed.setTitle("Error!"); //Title
                    embed.setDescription("Changed **CPS** to **" + arguments[1] + "**"); //Description
                    embed.setFooter("Dev: " + Main.author); //Footer
                    embed.build(); //End

                    clicker.setCps(Integer.parseInt(arguments[1]));

                    textChannel.sendMessageEmbeds(embed.build()).queue();
                }

            } else if (arguments[0].equalsIgnoreCase("c!bind") && !(arguments.length > 2)){
                if(arguments.length == 1){
                    embed.setColor(Color.RED); //Color
                    embed.setThumbnail("https://cdn.discordapp.com/attachments/935598249019052123/960194293228785724/F.png"); //Thumbnail
                    //embed.setTimestamp("123"); //Time
                    embed.setTitle("Error!"); //Title
                    embed.setDescription("Correct use: c!bind (value)"); //Description
                    embed.setFooter("Dev: " + Main.author); //Footer
                    embed.build(); //End

                    textChannel.sendMessageEmbeds(embed.build()).queue();
                } else {
                    embed.setColor(Color.GREEN); //Color
                    embed.setThumbnail("https://cdn.discordapp.com/attachments/935598249019052123/960194293228785724/F.png"); //Thumbnail
                    //embed.setTimestamp("123"); //Time
                    embed.setTitle("Error!"); //TiEddqvS7Qtle
                    embed.setDescription("Changed **BIND** to **" + arguments[1] + "**"); //Description
                    embed.setFooter("Dev: " + Main.author); //Footer
                    embed.build(); //End

                    clicker.setKey(arguments[1]);

                    textChannel.sendMessageEmbeds(embed.build()).queue();
                }
            }

        }

        super.onMessageReceived(event);
    }

}
