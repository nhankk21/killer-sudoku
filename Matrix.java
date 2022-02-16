/**
 * @author ftwanlin
 *
 *
 * */

package Sudoku;

public class Matrix {
    private final int[][] answer;
    Matrix() {
        answer = new int[9][9];
    }
    private void fillValues()
    {
        // Fill the diagonal of SRN x SRN matrices
        fillDiagonal();

        // Fill remaining blocks
        fillRemaining(0, 3);

        // Remove Randomly K digits to make game
        //removeKDigits();
    }

    // Fill the diagonal SRN number of SRN x SRN matrices
    private void fillDiagonal()
    {

        for (int i = 0; i<9; i=i+3)

            // for diagonal box, start coordinates->i==j
            fillBox(i, i);
    }

    // Returns false if given 3 x 3 block contains num.
    private boolean unUsedInBox(int rowStart, int colStart, int num)
    {
        for (int i = 0; i<3; i++)
            for (int j = 0; j<3; j++)
                if (answer[rowStart+i][colStart+j]==num)
                    return false;

        return true;
    }

    // Fill a 3 x 3 matrix.
    private void fillBox(int row,int col)
    {
        int num;
        for (int i=0; i<3; i++)
        {
            for (int j=0; j<3; j++)
            {
                do
                {
                    num = randomGenerator();
                }
                while (!unUsedInBox(row, col, num));

                answer[row+i][col+j] = num;
            }
        }
    }

    // Random generator
    private int randomGenerator()
    {
        return (int) Math.floor((Math.random()* 9 +1));
    }

    // Check if safe to put in cell
    private boolean CheckIfSafe(int i,int j,int num)
    {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i-i%3, j-j%3, num));
    }

    // check in the row for existence
    private boolean unUsedInRow(int i,int num)
    {
        for (int j = 0; j<9; j++)
            if (answer[i][j] == num)
                return false;
        return true;
    }

    // check in the row for existence
    private boolean unUsedInCol(int j,int num)
    {
        for (int i = 0; i<9; i++)
            if (answer[i][j] == num)
                return false;
        return true;
    }

    // A recursive function to fill remaining
    // matrix
    private boolean fillRemaining(int i, int j)
    {
        if (j>=9 && i<9-1)
        {
            i = i + 1;
            j = 0;
        }
        if (i>=9 && j>=9)
            return true;

        if (i < 3)
        {
            if (j < 3)
                j = 3;
        }
        else if (i < 9-3)
        {
            if (j==(int)(i/3)*3)
                j = j + 3;
        }
        else
        {
            if (j == 9-3)
            {
                i = i + 1;
                j = 0;
                if (i>=9)
                    return true;
            }
        }

        for (int num = 1; num<=9; num++)
        {
            if (CheckIfSafe(i, j, num))
            {
                answer[i][j] = num;
                if (fillRemaining(i, j+1))
                    return true;

                answer[i][j] = 0;
            }
        }
        return false;
    }
    public String generateMatrix() {
        fillValues();
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 9; ++i) {
            for (int j = 0; j < 9; ++j) res.append(answer[i][j]);
        }
        return res.toString();
    }
}
