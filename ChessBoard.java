import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ChessBoard extends JPanel {

    private PieceButton[][] board;
    private boolean[][] available;
    private Piece selectedPiece;
    private String currentFEN;
    private String oldFEN;
    private String uselessFENPart;
    private boolean isMultiplayer;
    private boolean isWhiteAtBottom;

    public static final int SQUARE_SIZE = 60;
    public static final String START_FEN_WHITE = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final String START_FEN_BLACK = "RNBKQBNR/PPPPPPPP/8/8/8/8/pppppppp/rnbkqbnr w KQkq - 0 1";
    public static final String RANDOM_FEN = "8/5k2/3p4/1p1Pp2p/pP2Pp1P/P4P1K/8/8 b - - 99 50";

    public ChessBoard(boolean isMultiplayer, boolean isWhiteAtBottom) {
        super();
        board = new PieceButton[8][8];
        available = new boolean[8][8];
        selectedPiece = null;
        setLayout(new GridLayout(8, 8));
        if (isWhiteAtBottom) {
            drawBoard(START_FEN_WHITE);
            currentFEN = START_FEN_WHITE;
        } else {
            drawBoard(START_FEN_BLACK);
            currentFEN = START_FEN_BLACK;
        }
        Shmoove.setBoardOrientation(isWhiteAtBottom);
        this.isMultiplayer = isMultiplayer;
        this.isWhiteAtBottom = isWhiteAtBottom;
    }

    public void resetBoard() {
        board = new PieceButton[8][8];
        available = new boolean[8][8];
        selectedPiece = null;
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if (isWhiteAtBottom) {
            drawBoard(START_FEN_WHITE);
            currentFEN = START_FEN_WHITE;
        } else {
            drawBoard(START_FEN_BLACK);
            currentFEN = START_FEN_BLACK;
        }
        oldFEN = null;
    }

    private void drawBoard(String fen) {
        removeAll();
        renderFEN(fen);
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                JPanel panel = new JPanel();
                panel.setSize(new Dimension(SQUARE_SIZE, SQUARE_SIZE));
                boolean isLight = (file + rank) % 2 == 0;
                if (isLight) {
                    panel.setBackground((available[file][rank]) ? new Color(247, 252, 172) : new Color(227, 202, 172));
                } else {
                    panel.setBackground((available[file][rank]) ? new Color(137, 113, 45) : new Color(117, 83, 45));
                }
                panel.add(board[file][rank]);
                add(panel);
            }
        }
        updateUI();
    }

    private String findFENFromBoard() {
        String fen = "";
        int blankCount;
        for (int i = 0; i < 8; i++) {
            blankCount = 0;
            for (int j = 0; j < 8; j++) {
                PieceButton current = board[i][j];

                if (!current.getPiece().notBlank()) {
                    blankCount++;
                    if (j == 7) {
                        fen += blankCount;
                    }
                    continue;
                }

                if (blankCount != 0) {
                    fen += blankCount;
                }

                switch (current.getPiece().getType()) {
                    case BISHOP_BLACK:
                        fen += "b";
                        break;
                    case BISHOP_WHITE:
                        fen += "B";
                        break;
                    case HORSE_BLACK:
                        fen += "n";
                        break;
                    case HORSE_WHITE:
                        fen += "N";
                        break;
                    case KING_BLACK:
                        fen += "k";
                        break;
                    case KING_WHITE:
                        fen += "K";
                        break;
                    case PAWN_BLACK:
                        fen += "p";
                        break;
                    case PAWN_WHITE:
                        fen += "P";
                        break;
                    case QUEEN_BLACK:
                        fen += "q";
                        break;
                    case QUEEN_WHITE:
                        fen += "Q";
                        break;
                    case ROOK_BLACK:
                        fen += "r";
                        break;
                    case ROOK_WHITE:
                        fen += "R";
                        break;
                    case BLANK:
                    default:
                        break;
                }
                blankCount = 0;
            }

            fen += (i < 7) ? "/" : "";
        }
        fen += uselessFENPart;
        return fen;
    }

    private void renderFEN(String fen) {
        String boardPart = fen.substring(0, fen.indexOf(" "));
        uselessFENPart = fen.substring(fen.indexOf(" "));
        int colCount;

        for (int i = 0; i < 8; i++) {
            String row = (i < 7) ? boardPart.substring(0, boardPart.indexOf("/")) : boardPart;
            boardPart = (i < 7) ? boardPart.substring(boardPart.indexOf("/") + 1) : "";
            colCount = 0;
            for (int j = 0; j < row.length(); j++) {
                char current = row.charAt(j);

                if (current > 48 && current < 58) {
                    int num = current - 48;

                    for (int k = 0; k < num; k++) {
                        board[i][colCount] = new PieceButton(new Piece(Pieces.BLANK));
                        int x = i;
                        int y = colCount;
                        board[i][colCount].addMouseListener(new MouseListener() {

                            @Override
                            public void mousePressed(java.awt.event.MouseEvent e) {
                                if (selectedPiece != null) {
                                    dropPiece(selectedPiece, x, y);
                                }
                            }

                            @Override
                            public void mouseReleased(java.awt.event.MouseEvent e) {
                                /* */ }

                            @Override
                            public void mouseEntered(java.awt.event.MouseEvent e) {
                                /* */ }

                            @Override
                            public void mouseExited(java.awt.event.MouseEvent e) {
                                /* */ }

                            @Override
                            public void mouseClicked(MouseEvent e) {
                                /* */ }

                        });
                        colCount++;
                    }
                    continue;
                }

                switch (current) {
                    case 'p':
                        board[i][colCount] = new PieceButton(new Piece(Pieces.PAWN_BLACK));
                        break;
                    case 'P':
                        board[i][colCount] = new PieceButton(new Piece(Pieces.PAWN_WHITE));
                        break;
                    case 'b':
                        board[i][colCount] = new PieceButton(new Piece(Pieces.BISHOP_BLACK));
                        break;
                    case 'B':
                        board[i][colCount] = new PieceButton(new Piece(Pieces.BISHOP_WHITE));
                        break;
                    case 'n':
                        board[i][colCount] = new PieceButton(new Piece(Pieces.HORSE_BLACK));
                        break;
                    case 'N':
                        board[i][colCount] = new PieceButton(new Piece(Pieces.HORSE_WHITE));
                        break;
                    case 'k':
                        board[i][colCount] = new PieceButton(new Piece(Pieces.KING_BLACK));
                        break;
                    case 'K':
                        board[i][colCount] = new PieceButton(new Piece(Pieces.KING_WHITE));
                        break;
                    case 'q':
                        board[i][colCount] = new PieceButton(new Piece(Pieces.QUEEN_BLACK));
                        break;
                    case 'Q':
                        board[i][colCount] = new PieceButton(new Piece(Pieces.QUEEN_WHITE));
                        break;
                    case 'r':
                        board[i][colCount] = new PieceButton(new Piece(Pieces.ROOK_BLACK));
                        break;
                    case 'R':
                        board[i][colCount] = new PieceButton(new Piece(Pieces.ROOK_WHITE));
                        break;
                    default:
                        board[i][colCount] = new PieceButton(new Piece(Pieces.BLANK));
                        break;
                }

                PieceButton edit = board[i][colCount];
                int x = i;
                int y = colCount;

                edit.addMouseListener(new MouseListener() {
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        if (selectedPiece == null) {
                            selectPiece(edit.getPiece(), x, y);
                        } else {
                            dropPiece(selectedPiece, x, y);
                        }
                    }

                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent e) {
                        /* */ }

                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        /* */ }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        /* */ }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        /* */ }

                });

                colCount++;
            }
        }

        Shmoove.setIsWhiteMove(uselessFENPart.charAt(1) == 'w');
    }

    private void selectPiece(Piece piece, int file, int rank) {
        oldFEN = currentFEN;
        selectedPiece = piece;

        Shmoove.loadBoard(board);
        available = Shmoove.generateLegalSquares(file, rank);

        board[file][rank] = new PieceButton(new Piece(Pieces.BLANK));
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(selectedPiece.drawPiece(), new Point(30, 30),
                "custom cursor"));
        drawBoard(findFENFromBoard());
    }

    private void dropPiece(Piece piece, int file, int rank) {
        selectedPiece = null;
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        if (available[file][rank]) {
            board[file][rank] = new PieceButton(piece);
            currentFEN = findFENFromBoard();
            flipMove();

            available = new boolean[8][8];
            drawBoard(currentFEN);

            Shmoove.loadBoard(board);
            if (Shmoove.inCheckmate()) {
                winScreen(piece.isWhite());
            }
        } else {
            currentFEN = oldFEN;
            available = new boolean[8][8];
            drawBoard(currentFEN);
        }
    }

    private void flipMove() {
        currentFEN = (currentFEN.charAt(currentFEN.indexOf(" ") + 1) == 'w') ? currentFEN.replaceFirst(" w ", " b ")
                : currentFEN.replaceFirst(" b ", " w ");
    }

    // true for white, false for black
    public void winScreen(boolean whiteOrBlack) {
        // removeAll();
        JLabel l1 = new JLabel((whiteOrBlack) ? "White Wins!!!!!!!" : "Black Wins!!!!!!!");
        add(l1);

        updateUI();
    }
}
