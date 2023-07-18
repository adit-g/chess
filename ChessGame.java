import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChessGame extends JFrame{

    private ChessBoard board;
    private JToggleButton newGame;
    private JToggleButton settings;
    private JToggleButton startGame;

    private boolean isMultiplayer;
    private boolean isWhiteAtBottom;
    
    public ChessGame() {
        super();

        getContentPane().removeAll();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("CHEZZ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialScreen();
    }

    public ChessBoard getBoard() {return board;}
    public JToggleButton getNewGameButton() {return newGame;}

    public boolean getIsWhiteAtBottom() {return isWhiteAtBottom;}
    public void setIsWhiteAtBottom(boolean what) {isWhiteAtBottom = what;}

    public boolean getIsMultiplayer() {return isMultiplayer;}
    public void setIsMultiplayer(boolean what) {isMultiplayer = what;}

    public void initialScreen() {
        getContentPane().removeAll();
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        JLabel l1 = new JLabel("game settings");
        add(l1, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        JRadioButton b1 = new JRadioButton("White");
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setIsWhiteAtBottom(true);
            }
        });
        b1.setPreferredSize(new Dimension(100, 100));
        add(b1, c);

        c.gridx = 1;
        c.gridy = 1;
        JRadioButton b2 = new JRadioButton("Black");
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setIsWhiteAtBottom(false);
            }
        });
        b2.setPreferredSize(new Dimension(100, 100));
        add(b2, c);

        ButtonGroup bg = new ButtonGroup();
        bg.add(b1);
        bg.add(b2);

        c.gridx = 0;
        c.gridy = 2;
        JRadioButton b3 = new JRadioButton("Single Player");
        b3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setIsMultiplayer(false);
            }
        });
        b3.setPreferredSize(new Dimension(150, 100));
        add(b3, c);

        c.gridx = 1;
        c.gridy = 2;
        JRadioButton b4 = new JRadioButton("Multiplayer");
        b4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setIsMultiplayer(true);
            }
        });
        b4.setPreferredSize(new Dimension(150, 100));
        add(b4, c);

        ButtonGroup bg2 = new ButtonGroup();
        bg2.add(b3);
        bg2.add(b4);

        c.gridx = 0;
		c.gridy = 3;
        c.gridwidth = 2;
        startGame = new JToggleButton();
        startGame.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                newGame();
            }

        });
        startGame.setText("Start Game");
		startGame.setPreferredSize(new Dimension(150, 50));
        add(startGame, c);

        setVisible(true);
        validate();
    }

    public void newGame() {
        board = new ChessBoard(isMultiplayer, isWhiteAtBottom);
        redraw();
    }

    public void redraw() {
        getContentPane().removeAll();
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(board, c);

        c.gridx = 0;
		c.gridy = 1;
        c.gridwidth = 1;
        newGame = new JToggleButton();
        newGame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                board.resetBoard();
                newGame.setSelected(false);
            }
        });
		newGame.setText("Restart Game");
		newGame.setPreferredSize(new Dimension(300, 50));
		add(newGame, c);

        c.gridx = 1;
        c.gridy = 1;
        settings = new JToggleButton();
        settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initialScreen();
            }
        });
        settings.setText("Settings");
        settings.setPreferredSize(new Dimension(300, 50));
        add(settings, c);

        setVisible(true);
        validate();
    }
}
