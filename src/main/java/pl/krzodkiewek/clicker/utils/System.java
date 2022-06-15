package pl.krzodkiewek.clicker.utils;

import java.util.Map;

public class System {
    public static String getComputerName()
    {
        Map<String, String> env = java.lang.System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "Unknown Computer";
    }

}
