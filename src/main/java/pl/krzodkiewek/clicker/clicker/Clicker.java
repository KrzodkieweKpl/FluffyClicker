package pl.krzodkiewek.clicker.clicker;

import com.sun.jna.Native;
import com.sun.jna.PointerType;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Clicker {

    private int cps;
    private String key;
    private boolean toggled;

    Robot robot;
    Random random;

    public void autoclicker(){

        try {
            robot = new Robot();
            random = new Random();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        //Disable NativeKeyListener logs
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        //Key Listeners (NativeKeyListener)
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new NativeKeyListener() {

                @Override
                public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
                }

                @Override
                public void nativeKeyReleased(NativeKeyEvent nativeEvent) {

                    byte[] windowText = new byte[512];

                    PointerType hwnd = User32.INSTANCE.GetForegroundWindow(); // then you can call it!
                    User32.INSTANCE.GetWindowTextA(hwnd, windowText, 512);

                    String keyText = NativeKeyEvent.getKeyText(nativeEvent.getKeyCode());
                    if (keyText.equalsIgnoreCase(getKey())) {
                        if (Native.toString(windowText).endsWith("/master)") || Native.toString(windowText).contains("Minecraft") || Native.toString(windowText).contains("1.")) {
                            toggled = !toggled;
                            System.out.println(keyText);
                            System.out.println(toggled);
                        }
                    }
                }

                @Override
                public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
                }
            });
        } catch (NativeHookException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                byte[] windowText = new byte[512];

                PointerType hwnd = User32.INSTANCE.GetForegroundWindow(); // then you can call it!
                User32.INSTANCE.GetWindowTextA(hwnd, windowText, 512);

                if (Native.toString(windowText).endsWith("/master)") || Native.toString(windowText).contains("Minecraft") || Native.toString(windowText).contains("1.")) {
                    System.out.println(Native.toString(windowText));

                    if (toggled) {
                        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        Thread.sleep(1000 / cps);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getCps() {
        return cps;
    }

    public void setCps(int cps) {
        this.cps = cps;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public interface User32 extends StdCallLibrary {
        User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);

        WinDef.HWND GetForegroundWindow();  // add this

        int GetWindowTextA(PointerType hWnd, byte[] lpString, int nMaxCount);
    }

}
