package org.example;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class KeyboardUtil {

    private static final Map<Character, Integer[]> specialKeyMap = new HashMap<>();

    static {
        specialKeyMap.put(':', new Integer[]{KeyEvent.VK_SHIFT, KeyEvent.VK_PERIOD});
        specialKeyMap.put(';', new Integer[]{KeyEvent.VK_SHIFT, KeyEvent.VK_COMMA});
        specialKeyMap.put(',', new Integer[]{KeyEvent.VK_COMMA});
        specialKeyMap.put('.', new Integer[]{KeyEvent.VK_PERIOD});
        specialKeyMap.put('ó', new Integer[]{KeyEvent.VK_O});
        specialKeyMap.put('á', new Integer[]{KeyEvent.VK_A});
        specialKeyMap.put('é', new Integer[]{KeyEvent.VK_E});
        specialKeyMap.put('í', new Integer[]{KeyEvent.VK_I});
        specialKeyMap.put('ú', new Integer[]{KeyEvent.VK_U});
        specialKeyMap.put('\n', new Integer[]{KeyEvent.VK_ENTER});
        specialKeyMap.put('(', new Integer[]{KeyEvent.VK_SHIFT, KeyEvent.VK_8});
        specialKeyMap.put(')', new Integer[]{KeyEvent.VK_SHIFT, KeyEvent.VK_9});
        specialKeyMap.put('ñ', new Integer[]{KeyEvent.VK_ALT_GRAPH, KeyEvent.VK_SHIFT, KeyEvent.VK_N});
    }

    public static void typeString(String text, Robot robot) throws Exception {
        for (char character : text.toCharArray()) {
            typeCharacter(character, robot);
        }
    }

    public static void typeCharacter(char character, Robot robot) throws Exception {
        Integer[] keyEvents = specialKeyMap.get(character);
        if (keyEvents != null) {
            if (keyEvents.length > 1 && keyEvents[0] == KeyEvent.VK_SHIFT) {  // Handle Shift modifier for characters
                robot.keyPress(KeyEvent.VK_SHIFT);
            }
            robot.keyPress(keyEvents[keyEvents.length - 1]);
            robot.keyRelease(keyEvents[keyEvents.length - 1]);
            if (keyEvents.length > 1 && keyEvents[0] == KeyEvent.VK_SHIFT) {
                robot.keyRelease(KeyEvent.VK_SHIFT);
            }
        } else {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(character);
            if (keyCode == KeyEvent.VK_UNDEFINED) {
                throw new IllegalArgumentException("Undefined key code for character: " + character);
            } else {
                if (Character.isUpperCase(character)) {
                    robot.keyPress(KeyEvent.VK_SHIFT);
                }
                robot.keyPress(keyCode);
                robot.keyRelease(keyCode);
                if (Character.isUpperCase(character)) {
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                }
            }
        }
        robot.delay(1);
    }

    public static void main(String[] args) {
        try {
            Thread.sleep(5000);
            Robot robot = new Robot();
            robot.setAutoDelay(1);

            String text = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(KeyboardUtil.class.getClassLoader().getResourceAsStream("write.txt"))
            )).lines().collect(Collectors.joining("\n"));

            typeString(text, robot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
