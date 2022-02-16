/**
 * @author nhankk21
 *
 *
 * */

package Sudoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class KillerSudoku extends JFrame implements ActionListener, KeyListener {
	private final Matrix matrix = new Matrix();
	private final Cage cage = new Cage();
	private int currentX, currentY, mistake, hint, rightCell;
	private final int LV;
	private Timer timer;
	private final HighScore highScore = new HighScore();
	private boolean isPlaying;
	private final int[] hide = {81, 45, 55, 64};
	private JLabel time_label;
	private JLabel mistake_label;
	private final JComboBox<String> level = new JComboBox<>();
	private JButton newGame_bt, hint_bt, erase_bt, pause_bt;
	private final JButton[][] displayButton = new JButton[9][9];
	private final int[][] currentMatrix = new int[9][9];
	private final int[][] answerMatrix = new int[9][9];
	private final int[][] initMatrix = new int[9][9];
	private int[][] colorMatrix = new int[9][9];
	private final int[] x = new int[81];
	private final int[] y = new int[81];
	private final int[] temp = new int[81];

	public KillerSudoku(int level) {
		super("PTITHCM/Java - KillerSudoku");
		LV = level;
		isPlaying = LV != 0;
		genSudoku();
	}
	private void genSudoku() {

		colorMatrix = cage.getColor();
		mistake = 3;
		hint = 3;
		rightCell = 81 - hide[LV];

		Container container = this.getContentPane();
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout());
		// thêm các nhãn thời gian, số lỗi đã mắc, điểm cao nhất
		time_label = new JLabel("00:00:00");
		mistake_label = new JLabel("Mistake: " + mistake);
		JLabel highScore_label = new JLabel("High score: " + highScore.getScore(LV));
		// khởi tạo và thêm phương thức lắng nghe chuột các nút: New game, Hint, Pause, Erase
		newGame_bt = new JButton("New Game");
		newGame_bt.addActionListener(this);

		hint_bt = new JButton("Hint: " + hint);
		hint_bt.addActionListener(this);

		pause_bt = new JButton("Pause");
		pause_bt.addActionListener(this);

		erase_bt = new JButton("Erase");
		erase_bt.addActionListener(this);

		// Thêm 3 level: Dễ, Vừa, Khó
		level.addItem("None");
		level.addItem("Easy");
		level.addItem("Medium");
		level.addItem("Hard");
		level.setSelectedIndex(LV);

		// thêm các nút theo thứ tự vào panel top bar
		panel1.add(newGame_bt);
		panel1.add(level);
		panel1.add(time_label);
		panel1.add(mistake_label);
		panel1.add(hint_bt);
		panel1.add(erase_bt);
		panel1.add(pause_bt);
		panel1.add(highScore_label);
		container.add(panel1, "North");

		// thêm ma trận 9x9
		JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayout(9, 9));
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++) {


				displayButton[i][j] = new JButton();
				displayButton[i][j].addActionListener(this);
				displayButton[i][j].addKeyListener(this);
				displayButton[i][j].setActionCommand(i + "" + j);

				if(LV == 0) displayButton[i][j].setBackground(Color.white);
				else displayButton[i][j].setBackground(cage.getCellColor(colorMatrix[i][j]));

				displayButton[i][j].setFont(new Font("UTM Micra", 1, 30));
				displayButton[i][j].setForeground(Color.black);
				panel2.add(displayButton[i][j]);

			}
		createMatrix(); // khởi tạo ma trận

		// tạo các đường viền, màu nền, tính tổng mỗi khối
		displayButton[1][1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(1, 1) + "", 0, 2, new Font("UTM Micra", 1, 15)));
		displayButton[1][4].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(1, 4) + "", 0, 2, new Font("UTM Micra", 1, 15)));
		displayButton[1][7].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(1, 7) + "", 0, 2, new Font("UTM Micra", 1, 15)));
		displayButton[4][1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(4, 1) + "", 0, 2, new Font("UTM Micra", 1, 15)));
		displayButton[4][4].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(4, 4) + "", 0, 2, new Font("UTM Micra", 1, 15)));
		displayButton[4][7].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(4, 7) + "", 0, 2, new Font("UTM Micra", 1, 15)));
		displayButton[7][1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(7, 1) + "", 0, 2, new Font("UTM Micra", 1, 15)));
		displayButton[7][4].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(7, 4) + "", 0, 2, new Font("UTM Micra", 1, 15)));
		displayButton[7][7].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(7, 7) + "", 0, 2, new Font("UTM Micra", 1, 15)));

		for (int i = 0; i < 9; i += 3)
			for (int j = 0; j < 9; j += 3) {
				displayButton[i][j].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(3,3,1,1, Color.black), cage.getTotal(i, j) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[i][j + 2].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(3,1,1,3, Color.black), cage.getTotal(i, j + 2) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[i + 2][j + 2].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,3,3, Color.black), cage.getTotal(i + 2, j + 2) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[i + 2][j].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,3,3,1, Color.black), cage.getTotal(i + 2, j) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[i][j + 1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(3,1,1,1, Color.black), cage.getTotal(i, j + 1) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[i + 1][j + 2].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,3, Color.black), cage.getTotal(i + 1, j + 2) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[i + 2][j + 1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,3,1, Color.black), cage.getTotal(i + 2, j + 1) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[i + 1][j].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,3,1,1, Color.black), cage.getTotal(i + 1, j) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[i + 1][j +1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(i + 1, j + 1) + "", 0, 2, new Font("UTM Micra", 1, 15)));
			}
		
		container.add(panel2);
		this.setVisible(true);
		this.setSize(850, 850);
		this.setLocationRelativeTo(null);
		this.setResizable(false);

		// xử lý đồng hồ
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isPlaying) time_label.setText(nextSecond(time_label.getText()));
			}
		});
	}
	
	private String nextSecond(String text) {

		// tính cấu hình đồng hồ của giây tiếp theo
		String[] str = text.split(":");
		int s = Integer.parseInt(str[2]);
		int m = Integer.parseInt(str[1]);
		int h = Integer.parseInt(str[0]);
		++s;
		if(s > 59){
			++m;
			s = 0;
		}
		if(m > 59){
			++h;
			m = 0;
		}
		return (h <= 9 ? "0" : "") + h + ":" + (m <= 9 ? "0" : "") + m + ":" + (s <= 9 ? "0" : "") + s;
	}
	
	private void createMatrix() {

		// khởi tạo ma trận
		/*
		initMatrix: ma trận khởi tạo
		currentMatrix: ma trận hiện tại
		answerMatrix: ma trận hiện tại
		colorMatrix: ma trận màu
		* */

		if(LV == 0) return;
		String str = matrix.generateMatrix();  // nhận 1 lời giải nhẫu nhiên của màn chơi
        int N = 0;
        for (int i = 0; i < 9; i++)
        	for (int j = 0; j < 9; j++){
        		//khởi tạo giá trị cho từng matrix
				initMatrix[i][j] = currentMatrix[i][j] = answerMatrix[i][j] = str.charAt(i * 9 + j) - 48;
				x[N] = i;
				y[N] = j;
				temp[N++] = (int) (10000 * Math.random());
				//khởi tạo màu
				displayButton[i][j].setBackground(cage.getCellColor(colorMatrix[i][j]));
			}

		for (int i = 0; i < N - 1; i ++)
			for (int j = i + 1; j < N; j++)
				if (temp[i] > temp[j]) {
					int t = x[i];
					x[i] = x[j];
					x[j] = t;
					
					t = y[i];
					y[i] = y[j];
					y[j] = t;
					
					t = temp[i];
					temp[i] = temp[j];
					temp[j] = t;
				}

		// ẩn đi 1 số ô tuỳ theo từng level
		for (int i = 0; i < hide[LV]; i++) {
			initMatrix[x[i]][y[i]] = currentMatrix[x[i]][y[i]] = 0;
		}
		for (int i = 0; i < 9; i++)
        	for (int j = 0; j < 9; j++){
				if (initMatrix[i][j] > 0) displayButton[i][j].setText(initMatrix[i][j] + "");
			}
		cage.setSudoku(answerMatrix);
    }
	public void dispose() {
		timer.stop();
		super.dispose();
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals(newGame_bt.getText())) {

			// bắt đầu 1 game mới
			String mess = (level.getSelectedIndex() == 1) ? "New easy game?" : ((level.getSelectedIndex() == 2) ? "New medium game?" : "New hard game?");
			if(level.getSelectedIndex() != 0) JOptionPane.showMessageDialog(null, mess);
			new KillerSudoku(level.getSelectedIndex()).timer.start();
			this.dispose();
		}
		if (e.getActionCommand().equals(pause_bt.getText())) {
			if(LV == 0) return;
			isPlaying = !isPlaying;
			if(!isPlaying){
				for (int i = 0; i < 9; i++)
					for (int j = 0; j < 9; j++){
						displayButton[i][j].setBackground(Color.white);
						displayButton[i][j].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), "", 0, 2, new Font("UTM Micra", 1, 15)));
						displayButton[i][j].setText("");
					}
				for (int i = 0; i < 9; i += 3)
					for (int j = 0; j < 9; j += 3) {
						displayButton[i][j].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(3,3,1,1, Color.black), "", 0, 2, new Font("UTM Micra", 1, 15)));
						//displayButton[i][j].setBorder(BorderFactory.createMatteBorder(3,3,1,1, Color.black));
						displayButton[i][j + 2].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(3,1,1,3, Color.black), "", 0, 2, new Font("UTM Micra", 1, 15)));
						//displayButton[i][j + 2].setBorder(BorderFactory.createMatteBorder(3,1,1,3, Color.black));
						displayButton[i + 2][j + 2].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,3,3, Color.black), "", 0, 2, new Font("UTM Micra", 1, 15)));
						//displayButton[i + 2][j + 2].setBorder(BorderFactory.createMatteBorder(1,1,3,3, Color.black));
						displayButton[i + 2][j].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,3,3,1, Color.black), "", 0, 2, new Font("UTM Micra", 1, 15)));
						//displayButton[i + 2][j].setBorder(BorderFactory.createMatteBorder(1,3,3,1, Color.black));
						displayButton[i][j + 1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(3,1,1,1, Color.black), "", 0, 2, new Font("UTM Micra", 1, 15)));
						//displayButton[i][j + 1].setBorder(BorderFactory.createMatteBorder(3,1,1,1, Color.black));
						displayButton[i + 1][j + 2].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,3, Color.black), "", 0, 2, new Font("UTM Micra", 1, 15)));
						//displayButton[i + 1][j + 2].setBorder(BorderFactory.createMatteBorder(1,1,1,3, Color.black));
						displayButton[i + 2][j + 1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,3,1, Color.black), "", 0, 2, new Font("UTM Micra", 1, 15)));
						//displayButton[i + 2][j + 1].setBorder(BorderFactory.createMatteBorder(1,1,3,1, Color.black));
						displayButton[i + 1][j].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,3,1,1, Color.black), "", 0, 2, new Font("UTM Micra", 1, 15)));
						//displayButton[i + 1][j].setBorder(BorderFactory.createMatteBorder(1,3,1,1, Color.black));
						displayButton[i + 1][j + 1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), "", 0, 2, new Font("UTM Micra", 1, 15)));
						//displayButton[i + 1][j + 1].setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black));
					}
			} else {
				displayButton[1][1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(1, 1) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[1][4].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(1, 4) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[1][7].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(1, 7) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[4][1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(4, 1) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[4][4].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(4, 4) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[4][7].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(4, 7) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[7][1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(7, 1) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[7][4].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(7, 4) + "", 0, 2, new Font("UTM Micra", 1, 15)));
				displayButton[7][7].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(7, 7) + "", 0, 2, new Font("UTM Micra", 1, 15)));

				for (int i = 0; i < 9; i += 3)
					for (int j = 0; j < 9; j += 3) {
						displayButton[i][j].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(3,3,1,1, Color.black), cage.getTotal(i, j) + "", 0, 2, new Font("UTM Micra", 1, 15)));
						displayButton[i][j + 2].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(3,1,1,3, Color.black), cage.getTotal(i, j + 2) + "", 0, 2, new Font("UTM Micra", 1, 15)));
						displayButton[i + 2][j + 2].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,3,3, Color.black), cage.getTotal(i + 2, j + 2) + "", 0, 2, new Font("UTM Micra", 1, 15)));
						displayButton[i + 2][j].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,3,3,1, Color.black), cage.getTotal(i + 2, j) + "", 0, 2, new Font("UTM Micra", 1, 15)));
						displayButton[i][j + 1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(3,1,1,1, Color.black), cage.getTotal(i, j + 1) + "", 0, 2, new Font("UTM Micra", 1, 15)));
						displayButton[i + 1][j + 2].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,3, Color.black), cage.getTotal(i + 1, j + 2) + "", 0, 2, new Font("UTM Micra", 1, 15)));
						displayButton[i + 2][j + 1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,3,1, Color.black), cage.getTotal(i + 2, j + 1) + "", 0, 2, new Font("UTM Micra", 1, 15)));
						displayButton[i + 1][j].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,3,1,1, Color.black), cage.getTotal(i + 1, j) + "", 0, 2, new Font("UTM Micra", 1, 15)));
						displayButton[i + 1][j +1].setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.black), cage.getTotal(i + 1, j + 1) + "", 0, 2, new Font("UTM Micra", 1, 15)));
					}
				for (int i = 0; i < 9; i++)
					for (int j = 0; j < 9; j++){
						displayButton[i][j].setBackground(cage.getCellColor(colorMatrix[i][j]));
						displayButton[i][j].setText((currentMatrix[i][j] != 0) ? currentMatrix[i][j] + "" : "");
					}
			}
		} else if (e.getActionCommand().equals(hint_bt.getText())) {
			if(LV == 0) return;
			if(!isPlaying) return;
			if(hint > 0 && initMatrix[currentX][currentY] == 0){
				--hint;

				displayButton[currentX][currentY].setText(answerMatrix[currentX][currentY] + "");
				currentMatrix[currentX][currentY] = answerMatrix[currentX][currentY];
				displayButton[currentX][currentY].setForeground(Color.BLUE);
				hint_bt.setText("Hint: " + hint);
				++rightCell;
				if(rightCell == 81){
					highScore.setEasy(time_label.getText(), LV);
					JOptionPane.showMessageDialog(null, "You win!");
					new KillerSudoku(0).timer.start();
					this.dispose();
				}
			}
		} else if (e.getActionCommand().equals(erase_bt.getText())) {
			if(LV == 0) return;
			if(!isPlaying) return;
			if(initMatrix[currentX][currentY] == 0){

				displayButton[currentX][currentY].setText("");
				currentMatrix[currentX][currentY] = 0;
			}
			for (int i = 0; i < 9; i++)
				for (int j = 0; j < 9; j++){
					displayButton[i][j].setBackground(cage.getCellColor(colorMatrix[i][j]));
				}
		} else {
			if(!isPlaying) return;
			String s = e.getActionCommand();
			currentX = s.charAt(0) - '0';
			currentY = s.charAt(1) - '0';
			for (int i = 0; i < 9; i++)
				for (int j = 0; j < 9; j++){
					displayButton[i][j].setBackground(cage.getCellColor(colorMatrix[i][j]));
				}
			try{
				if (currentMatrix[currentX][currentY] > 0) {
					for (int i = 0; i < 9; i++)
						for (int j = 0; j < 9; j++)
							if (currentMatrix[i][j] == currentMatrix[currentX][currentY])
								displayButton[i][j].setBackground(Color.gray);
				} else{
					displayButton[currentX][currentY].setBackground(Color.gray);
				}
			}catch (Exception ignored){

			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(!isPlaying) return;
		if ((keyCode >= 49 && keyCode <= 57) || (keyCode >= 97 && keyCode <= 105)) {
			if (keyCode <= 57) keyCode -= 48;
			if (keyCode >= 97) keyCode -= 96;
			if (currentMatrix[currentX][currentY] == 0 || currentMatrix[currentX][currentY] != answerMatrix[currentX][currentY]) {
				displayButton[currentX][currentY].setText(keyCode + "");
				currentMatrix[currentX][currentY] = keyCode;
				if (keyCode == answerMatrix[currentX][currentY]) {
					displayButton[currentX][currentY].setForeground(Color.BLUE);
					++rightCell;
					if(rightCell == 81){
						highScore.setEasy(time_label.getText(), LV);
						JOptionPane.showMessageDialog(null, "You win!");
						new KillerSudoku(LV).timer.start();
						this.dispose();
					}
				}
				else {
					mistake--;
					displayButton[currentX][currentY].setForeground(Color.red);
					mistake_label.setText("Mistake: " + mistake);
					if (mistake == 0) {
						JOptionPane.showMessageDialog(null, "Game over!");
						mistake_label.setText("Game over!");
						new KillerSudoku(LV).timer.start();
						this.dispose();
					}
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
	public static void main(String[] args) {
		new KillerSudoku(0).timer.start();
	}
}