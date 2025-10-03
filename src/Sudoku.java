import java.awt.*;
import java.awt.event.*;
import javax.swing.*;/* for UI */
import java.io.*;/*fileReader, bufferedReader, IOexceptions */
import java.util.List;/*List<> */
import java.util.ArrayList;/*ArrayList<> */
import java.util.HashSet;
import java.util.Random;
import java.util.Set;// include sets(unique elements lists)

public class Sudoku {

    class Tile extends JButton{
        int row;
        int col;
        Tile(int row, int col){
            this.row = row;
            this.col = col;
        }
    }

    int width = 600;
    int height = 650;

    String[] puzzle;
    String[] solution;

    List<String[]> puzzleList = new ArrayList<>();/*lists to store puzzles and solutions */
    List<String[]> solutionList = new ArrayList<>();

    void loadPuzzles(String filename){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));/*open file for reading */
            String line;
            int lineNum = 0;
            String[] tempPuz = null;

            while((line = bufferedReader.readLine())!= null/*read solution, puzzle lines while they contain */){
                if (lineNum % 2 == 0){/* if even */
                    tempPuz = line.split(",");/*split row into an array of 9 strings */
                }
                else{
                    String[] tempSol = line.split(",");
                    puzzleList.add(tempPuz);
                    solutionList.add(tempSol);
                }
                lineNum ++;
            }
            bufferedReader.close(); /*close file */

        }
        catch(IOException e){/*catch file-related exceptions */
            e.printStackTrace();/*print catched error */
        }
    }

    
    JFrame window = new JFrame("JavaSudoku by ic3dust");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();

    JPanel board = new JPanel();

    JPanel buttons = new JPanel();
    JButton selectedBtn = null;
    int errors = 0;

    JButton restart = new JButton("Restart");

    Set<Integer> completedPuzzles = new HashSet<>();// int elements in set; completedPuzzles = hashset. unordered, only checks if the number exists there. faster
    int lastPuzzleIndex = -1; // last puzzle solved(it wont appear after a victory)


    Sudoku(){

        loadPuzzles("puzzles.txt");
        pickRandomPuzzle();
        window.setSize(width, height);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);/*Exit button closes the window */
        window.setLocationRelativeTo(null);/*centred location */
        window.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Sudoku: 0");

        restart.setFont(new Font("Arial", Font.BOLD, 20));
        restart.setBackground(Color.decode("#C8C2F4"));
        restart.setFocusable(false);
        restart.setVisible(false);

        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));/*stack label and restart vertically */
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);/*centred alignment */
        restart.setAlignmentX(Component.CENTER_ALIGNMENT);

        textPanel.add(textLabel);/*adding label to panel */
        textPanel.add(Box.createRigidArea(new Dimension(0, 10)));/*space 10px between label and restart */
        textPanel.add(restart);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));/*space 5px after restart in lable */
        textPanel.setBackground(Color.decode("#817DE5"));

        window.add(textPanel, BorderLayout.NORTH/*north side of the window*/);/*adding panel to window */

        board.setLayout(new GridLayout(9, 9));
        setupTiles();
        window.add(board, BorderLayout.CENTER);
        
        buttons.setLayout(new GridLayout(1,9));
        setupButtons();
        window.add(buttons, BorderLayout.SOUTH);

        restart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                restart.setVisible(false);
                board.removeAll();
                buttons.removeAll();
                errors =0;
                textLabel.setText("Sudoku: 0");
                selectedBtn=null;
                
                if (completedPuzzles.contains(lastPuzzleIndex) && completedPuzzles.size() < puzzleList.size()) {
                    pickRandomPuzzle();// go to next puzzle from uncompleted after one win
                }
                
                setupTiles();
                setupButtons();
                board.revalidate();/*refresh board layout */
                board.repaint();/*draw board again */
                buttons.revalidate();
                buttons.repaint();
            }
        });

        window.setVisible(true);
    }
    void pickRandomPuzzle() {
        if (completedPuzzles.size() == puzzleList.size()) {
        masterWin();
        return;
    }
     Random randIndex = new Random();
            int index;
            do {
                index = randIndex.nextInt(puzzleList.size());//take random index
            } while (completedPuzzles.contains(index));

            lastPuzzleIndex = index;// save index
            puzzle = puzzleList.get(index);/*gets puzzle and gets solution for that index in puzzles.txt rows */
            solution = solutionList.get(index);
}

    void setupTiles(){
        for (int row = 0; row <9; row++){
            for (int col = 0; col< 9; col ++){
                Tile tile = new Tile(row, col);
                char tileChar = puzzle[row].charAt(col);

                if (tileChar != '-'){
                    tile.setFont(new Font("Arial", Font.BOLD, 20));
                    tile.setText(String.valueOf(tileChar));
                    tile.setBackground(Color.decode("#C4C2FF"));
                }
                else{
                    tile.setFont(new Font("Arial", Font.PLAIN, 20));
                    tile.setBackground(Color.decode("#E9E8FF"));
                }
                if ((row == 2 && col ==2) || (row==2 && col ==5) || (row ==5&&col==2)||(row==5&&col==5)){
                    tile.setBorder(BorderFactory.createMatteBorder(1,1,3,3, Color.black));
                }
                else if (row==2 || row == 5){
                    tile.setBorder(BorderFactory.createMatteBorder(1,1,3,1, Color.black));
                }
                else if(col == 2 || col ==5){
                    tile.setBorder(BorderFactory.createMatteBorder(1,1,1,3, Color.black));
                }
                else{
                    tile.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));
                }

                tile.setFocusable(false);/*rect around number revoved */
                board.add(tile);

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        Tile tileClicked = (Tile) e.getSource();
                        int row = tile.row;
                        int col = tile.col;

                        if(selectedBtn != null){
                            if (tile.getText() != ""){/*if a user clicks on tile with number already */
                                return;
                            }
                            String selectedBtnText = selectedBtn.getText();
                            String tileSolution = String.valueOf(solution[row].charAt(col));
                            if (tileSolution.equals(selectedBtnText)/*compare values, not the same object, values have to refer to */){
                                tile.setText(selectedBtnText);
                                gameWon();
                            }
                            else{/*wrong tile */
                                errors +=1;
                                textLabel.setText("Sudoku: " + String.valueOf(errors));
                                if (errors>4){
                                    endGame();
                                }
                            }

                        }
                    }
                });
            }
        }
    }
    void setupButtons(){
        for (int i = 0; i<10; i++){
            JButton button = new JButton();
            button.setFont(new Font("Arial", Font.BOLD, 30));
            button.setText(String .valueOf(i));
            button.setFocusable(false);
            button.setBackground(Color.decode("#817DE5"));
            buttons.add(button);/*add these buttons to buttons panel */

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    JButton buttonClicked = (JButton) e.getSource();

                    if(selectedBtn == buttonClicked){
                        selectedBtn.setBackground(Color.decode("#817DE5"));
                        selectedBtn=null;
                        return;
                    }
                    else{
                        if(selectedBtn != null){
                            selectedBtn.setBackground(Color.decode("#817DE5"));
                        }
                    }
                    selectedBtn = buttonClicked;
                    selectedBtn.setBackground(Color.decode("#C2AEF2"));
                }
            });
        }
    }
    void endGame() {
        textLabel.setText("You made 5 errors :( Game over!");

        for (Component comp : board.getComponents()) {
            if (comp instanceof Tile) {
                Tile tile = (Tile) comp;
                tile.setEnabled(false);
                tile.setBackground(Color.LIGHT_GRAY);
            }
        }
        restart.setVisible(true);

        window.revalidate();/*refresh layout for tiles */
        window.repaint();/*repaint board */
        }
        void gameWon(){

            boolean won = true;
            
            for (int row = 0; row<9; row++){
                for (int col = 0; col<9; col++){
                    Tile tile = (Tile) board.getComponent(row*9 + col);
                    String current = tile.getText();
                    String correct = String.valueOf(solution[row].charAt(col));
                    
                    if (!current.equals(correct)){
                        won = false;
                        break;
                    }

                }
                if(!won) break;
            }

            if(won){
                textLabel.setText("You won! Errors count: " + errors);
                for (Component comp : board.getComponents()) {
                    if (comp instanceof Tile) {
                    Tile tile = (Tile) comp;
                    tile.setEnabled(false);
                    tile.setBackground(Color.decode("#ADE0C7"));
                    }
                }

                completedPuzzles.add(lastPuzzleIndex);//"puzzle completed" mark
                restart.setVisible(true);
                if(completedPuzzles.size()==puzzleList.size()){
                    masterWin();
                }
            }
            window.revalidate();
            window.repaint();
            
        }
        void masterWin(){
            textLabel.setText("<html><center>You've completed all 4 puzzles! Congrats! :)</center></html>");
            for (Component comp : board.getComponents()) {
                if (comp instanceof Tile) {
                    Tile tile = (Tile) comp;
                    tile.setEnabled(false);
                    tile.setBackground(Color.decode("#ADE0C7"));
                }
            }
            restart.setVisible(true);
            window.revalidate();
            window.repaint();

    }
}

