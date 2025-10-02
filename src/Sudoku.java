import java.awt.*;
import java.awt.event.*;
import javax.swing.*;/* for UI */

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

    String[] puzzle = {
        "--74916-5",
        "2---6-3-9",
        "-----7-1-",
        "-586----4",
        "--3----9-",
        "--62--187",
        "9-4-7---2",
        "67-83----",
        "81--45---"
    };

    String[] solution = {
        "387491625",
        "241568379",
        "569327418",
        "758619234",
        "123784596",
        "496253187",
        "934176852",
        "675832941",
        "812945763"
    };

    JFrame window = new JFrame("JavaSudoku by ic3dust");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();

    JPanel board = new JPanel();

    JPanel buttons = new JPanel();
    JButton selectedBtn = null;
    int errors = 0;


    Sudoku(){
        window.setSize(width, height);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);/*Exit button closes the window */
        window.setLocationRelativeTo(null);/*centred location */
        window.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Sudoku: 0");

        textPanel.add(textLabel);/*adding label to panel */
        textPanel.setBackground(Color.decode("#817DE5"));
        window.add(textPanel, BorderLayout.NORTH/*north side of the window*/);/*adding panel to window */

        board.setLayout(new GridLayout(9, 9));
        setupTiles();
        window.add(board, BorderLayout.CENTER);
        
        buttons.setLayout(new GridLayout(1,9));
        setupButtons();
        window.add(buttons, BorderLayout.SOUTH);

        window.setVisible(true);
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
        textLabel.setText("You made 5 errors :( Try again!");

        for (Component comp : board.getComponents()) {
            if (comp instanceof Tile) {
                Tile tile = (Tile) comp;
                tile.setEnabled(false);
                tile.setBackground(Color.LIGHT_GRAY);
            }
        }
    }


}
