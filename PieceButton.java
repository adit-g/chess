import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

public class PieceButton extends JToggleButton {
    
    private Piece piece;

    public PieceButton(Piece aPiece) {
        super();
        piece = aPiece;

        //setOpaque(false);
        //setContentAreaFilled(false);
        setBorderPainted(false);
        if (piece.notBlank()) {
            setIcon(new ImageIcon(piece.drawPiece()));
        }
        setPreferredSize(new Dimension(ChessBoard.SQUARE_SIZE, ChessBoard.SQUARE_SIZE));
    }

    public Piece getPiece() {return piece;}

}
