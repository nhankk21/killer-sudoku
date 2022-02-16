/**
 * @author ftwanlin
 *
 *
 * */

package Sudoku;

import java.awt.*;
import java.util.Random;

public class Cage {
    private static int[][] sudoku = new int[9][10];
    private static final boolean[][] vis = new boolean[9][9];
    private static final int[][] color = new int[9][9];
    private static final int[] total = new int[50];
    private static final Random generator = new Random();
    private static int cnt = 0;
    public Cage() {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                color[i][j] = 0;
                vis[i][j] = false;
            }
        }
        for (int i = 0; i < 50; ++i) total[i] = 0;
        paint();
    }
    private static final int[] dx = {0, 0, 1, -1};
    private static final int[] dy = {1, -1, 0, 0};
    private static void dfs(int x, int y, int tot, int _color) {
        if (x < 0 || y < 0 || x >= 9 || y >= 9 || vis[x][y]) return;
        if (cnt == tot) return;
        cnt++;
        color[x][y] = _color;
        vis[x][y] = true;
        for (int ii = 0; ii < 81; ++ii) { // Thich thi cho 81 luon
            int i = generator.nextInt(3);
            int xx = dx[i] + x;
            int yy = dy[i] + y;
            dfs(xx, yy, tot, _color);
        }
    }
    private static void paint() {
        int clr = 1;
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                if (!vis[i][j]) {
                    cnt = 0;
                    int tot = generator.nextInt(3) + 2;
                    dfs(i, j, tot, clr);
                    clr++;
                }
            }
        }
    }
    private static void countSum() {
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) {
                total[color[i][j]] += sudoku[i][j];
            }
        }
    }

    public void setSudoku(int[][] sudoku) {
        Cage.sudoku = sudoku;
        countSum();
    }

    public Color getCellColor(int color){
        switch (color % 15){
            case 1:
                return new Color(255, 255, 255);
            case 2:
                return new Color(229, 0, 248);
            case 3:
                return new Color(151, 255, 255);
            case 4:
                return new Color(66, 225, 13);
            case 5:
                return new Color(255, 239, 21);
            case 6:
                return new Color(236, 188, 153);
            case 7:
                return new Color(224, 155, 223);
            case 8:
                return new Color(220, 119, 119);
            case 9:
                return new Color(150, 229, 138);
            case 10:
                return new Color(206, 138, 90);
            case 11:
                return new Color(48, 159, 239);
            case 12:
                return new Color(177, 185, 255);
            case 13:
                return new Color(161, 0, 255);
            case 14:
                return new Color(6, 248, 212);
            default:
                return new Color(210, 162, 252);
        }
    }
    public int getTotal(int i, int j){
        return total[color[i][j]];
    }
    public int[][] getColor(){
        return color;
    }
}
