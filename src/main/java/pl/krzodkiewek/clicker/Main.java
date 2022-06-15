package pl.krzodkiewek.clicker;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import pl.krzodkiewek.clicker.event.Bot;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static String name = "FluffyClicker", version = "b1", author = "KrzodkieweK";
    public static String token, guildid, channelid;
    public static JDA jda;
    public static String appDataDir = System.getenv("APPDATA");
    public static boolean ready = false;

    public static void main(String[] args) {

        try {

            File theDir = new File(appDataDir + "/" + name);
            File settingsFile = new File(appDataDir + "/" + name + "/" + "settings.txt");

            if (!theDir.exists()){
                theDir.mkdirs();
            }

            if (settingsFile.createNewFile()) {

                System.out.println("This is your first time using " + name + ". Creating " + settingsFile.getName() + " file.");

                Scanner inputInfo = new Scanner(System.in);

                System.out.println("Enter bot token: ");
                token = inputInfo.nextLine();

                System.out.println("Enter channel id: ");
                channelid = inputInfo.nextLine();

                System.out.println("Enter guild id: ");
                guildid = inputInfo.nextLine();

                FileWriter myWriter = new FileWriter(appDataDir + "/" + name + "/" + "settings.txt");
                myWriter.write(token + "\n");
                myWriter.write(channelid + "\n");
                myWriter.write(guildid);
                myWriter.close();

            } else {
                System.out.println("Reading data from " + settingsFile.getName());

                Scanner myReader = new Scanner(settingsFile);

                while (myReader.hasNextLine()) {
                    token = myReader.nextLine();
                    channelid = myReader.nextLine();
                    guildid = myReader.nextLine();

                }

                ready = true;

            }
        } catch (IOException e){
            System.out.println("IOException detected. Code " + e.getMessage());
        } catch (Exception e){
            System.out.println("Unkonow error. Code " + e.getMessage());
        }

        try {

            jda = JDABuilder.createDefault(token)
                    .setActivity(Activity.streaming("FluffyStudios", "https://www.twitch.tv/krzodkiewek96"))
                    .build();

        } catch (LoginException e) {
            System.out.println("Wrong token!");
        }

        jda.addEventListener(new Bot());

    }

    public static JDA getJda(){
        return jda;
    }

}
