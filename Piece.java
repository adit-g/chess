import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;


import javax.imageio.ImageIO;
public class Piece {

    private Pieces type;

    public Piece(Pieces piece) {
        type = piece;
    }

    public Pieces getType() {
        return type;
    }

    public boolean notBlank() {
        return type != Pieces.BLANK;
    }

    public boolean isWhite() {
        return (getType() == Pieces.BISHOP_WHITE || getType() == Pieces.HORSE_WHITE || getType() == Pieces.KING_WHITE || getType() == Pieces.PAWN_WHITE || getType() == Pieces.QUEEN_WHITE || getType() == Pieces.ROOK_WHITE);
    }

    public static boolean isSameColor(Piece piece1, Piece piece2) {
        if ((piece1.getType() == Pieces.BISHOP_BLACK || piece1.getType() == Pieces.HORSE_BLACK || piece1.getType() == Pieces.KING_BLACK || piece1.getType() == Pieces.PAWN_BLACK || piece1.getType() == Pieces.QUEEN_BLACK || piece1.getType() == Pieces.ROOK_BLACK)
            && (piece2.getType() == Pieces.BISHOP_BLACK || piece2.getType() == Pieces.HORSE_BLACK || piece2.getType() == Pieces.KING_BLACK || piece2.getType() == Pieces.PAWN_BLACK || piece2.getType() == Pieces.QUEEN_BLACK || piece2.getType() == Pieces.ROOK_BLACK)) {
                return true;
        }
        if ((piece1.getType() == Pieces.BISHOP_WHITE || piece1.getType() == Pieces.HORSE_WHITE || piece1.getType() == Pieces.KING_WHITE || piece1.getType() == Pieces.PAWN_WHITE || piece1.getType() == Pieces.QUEEN_WHITE || piece1.getType() == Pieces.ROOK_WHITE)
            && (piece2.getType() == Pieces.BISHOP_WHITE || piece2.getType() == Pieces.HORSE_WHITE || piece2.getType() == Pieces.KING_WHITE || piece2.getType() == Pieces.PAWN_WHITE || piece2.getType() == Pieces.QUEEN_WHITE || piece2.getType() == Pieces.ROOK_WHITE)) {
                return true;
        }
        return false;
    }

    public BufferedImage drawPiece() {
        BufferedImage thing = null;
        try {
            switch (type) {
                case BISHOP_BLACK: thing = ImageIO.read(new File("Images/Chess_bdt60.png"));
                    break;
                case BISHOP_WHITE: thing = ImageIO.read(new File("Images/Chess_blt60.png"));
                    break;
                case HORSE_BLACK: thing = ImageIO.read(new File("Images/Chess_ndt60.png"));
                    break;
                case HORSE_WHITE: thing = ImageIO.read(new File("Images/Chess_nlt60.png"));
                    break;
                case KING_BLACK: thing = ImageIO.read(new File("Images/Chess_kdt60.png"));
                    break;
                case KING_WHITE: thing = ImageIO.read(new File("Images/Chess_klt60.png"));
                    break;
                case PAWN_BLACK: thing = ImageIO.read(new File("Images/Chess_pdt60.png"));
                    break;
                case PAWN_WHITE: thing = ImageIO.read(new File("Images/Chess_plt60.png"));
                    break;
                case QUEEN_BLACK: thing = ImageIO.read(new File("Images/Chess_qdt60.png"));
                    break;
                case QUEEN_WHITE: thing = ImageIO.read(new File("Images/Chess_qlt60.png"));
                    break;
                case ROOK_BLACK: thing = ImageIO.read(new File("Images/Chess_rdt60.png"));
                    break;
                case ROOK_WHITE: thing = ImageIO.read(new File("Images/Chess_rlt60.png"));
                    break;
                case BLANK:
                default:
                    break;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return thing;
    }
}
