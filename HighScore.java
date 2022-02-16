/**
 * @author ftwanlin & nhankk21
 *
 *
 * */

package Sudoku;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class HighScore {
    private static String easy, medium, hard;
    private static void writeToFile() {
        try {
            FileWriter fw = new FileWriter("highScore.txt");
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(easy);
            bw.newLine();
            bw.write(medium);
            bw.newLine();
            bw.write(hard);

            bw.close();
            fw.close();
        } catch (Exception ignored) {
        }
    }
    public String getScore(int LV){
        readFromFile();
        if(LV == 1) return easy;
        else if(LV == 2) return medium;
        else if(LV == 3) return hard;
        else return "00:00:00";
    }
    public void setEasy(String score, int LV){
        readFromFile();
        if(LV == 1 && isLessThan(score, easy)) easy = score;
        else if(LV == 2 && isLessThan(score, medium)) medium = score;
        else if(LV == 3 && isLessThan(score, hard)) hard = score;
        writeToFile();
    }
    private static void readFromFile() {
        try {
            FileReader fr = new FileReader("highScore.txt");
            BufferedReader br = new BufferedReader(fr);
            easy = br.readLine();
            medium = br.readLine();
            hard = br.readLine();
            fr.close();
            br.close();
        }
        catch (Exception ignored) {
        }
    }
    private static boolean isLessThan(String new_score, String high_score) {

        String[] t1 = new_score.split(":");
        String[] t2 = high_score.split(":");
        if (Integer.parseInt(t1[0]) < Integer.parseInt(t2[0])) return true;
        else if (Integer.parseInt(t1[0]) == Integer.parseInt(t2[0])) {
            if (Integer.parseInt(t1[1]) < Integer.parseInt(t2[1])) return true;
            else if (Integer.parseInt(t1[1]) == Integer.parseInt(t2[1])) {
                return Integer.parseInt(t1[2]) < Integer.parseInt(t2[2]);
            }
        }
        return false;
    }

}
