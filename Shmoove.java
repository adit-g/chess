
public abstract class Shmoove {

    private static Piece[][] board;

    // TODO: implement castling and enpassant rules
    private static boolean _whiteAtBottom;
    private static boolean isWhiteMove;
    // private boolean[] castlingRights;
    // private String enPassantTarget;
    // private int halfmoveCount;
    // private int fullmoveCount;

    private static final int[] FILE_OFFSETS = { -1, 1, 0, 0, -1, 1, -1, 1 };
    private static final int[] RANK_OFFSETS = { 0, 0, -1, 1, -1, 1, 1, -1 };

    public static boolean inCheckmate() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!board[i][j].notBlank()) {
                    continue;
                }
                if (!isEmpty(generateLegalSquares(i, j))) {
                    return false;
                }
            }
        }

        return true;
    }

    // true is white king in check, false is black king in check
    public static boolean inCheck() {
        boolean[][] attackedSquares = generateAttackedSquares(!isWhiteMove);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (attackedSquares[i][j] && ((board[i][j].getType() == Pieces.KING_BLACK && !isWhiteMove)
                        || (board[i][j].getType() == Pieces.KING_WHITE && isWhiteMove))) {
                    return true;
                }
            }
        }

        return false;
    }

    // generates valid moves for a piece if its king is in check
    public static boolean[][] generateLegalSquares(int startFile, int startRank) {
        boolean[][] squares = generateFeasibleSquares(startFile, startRank, false);
        Piece piece = board[startFile][startRank];
        Pieces pieceType = piece.getType();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!squares[i][j]) {
                    continue;
                }

                board[startFile][startRank] = new Piece(Pieces.BLANK);
                board[i][j] = new Piece(pieceType);

                if (inCheck()) {
                    squares[i][j] = false;
                }

                board[startFile][startRank] = new Piece(pieceType);
                board[i][j] = new Piece(Pieces.BLANK);
            }
        }

        return squares;
    }

    // finds which squares are controlled by a color
    // true is white, false is black
    public static boolean[][] generateAttackedSquares(boolean whiteOrBlack) {
        boolean[][] squares = new boolean[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!board[i][j].notBlank()) {
                    continue;
                }
                if (board[i][j].isWhite() ^ whiteOrBlack) {
                    continue;
                }

                addArrays(squares, generateFeasibleSquares(i, j, true));
            }
        }

        return squares;
    }

    public static void loadBoard(PieceButton[][] chonky) {
        board = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = chonky[i][j].getPiece();
            }
        }
    }

    public static boolean[][] generateFeasibleSquares(int startFile, int startRank, boolean attackingSquaresMode) {
        boolean[][] squares = new boolean[8][8];
        Piece piece = board[startFile][startRank];
        Pieces pieceType = piece.getType();
        int numUp = startFile;
        int numDown = 7 - startFile;
        int numLeft = startRank;
        int numRight = 7 - startRank;
        int[] slidingMoveData = { numUp, numDown, numLeft, numRight, Math.min(numUp, numLeft),
                Math.min(numDown, numRight), Math.min(numUp, numRight), Math.min(numDown, numLeft) };

        if (!piece.notBlank()) {
            return squares;
        }

        if ((piece.isWhite() ^ isWhiteMove) && !attackingSquaresMode) {
            return squares;
        }

        if (pieceType == Pieces.HORSE_BLACK || pieceType == Pieces.HORSE_WHITE) {
            for (int i = 0; i < 4; i++) {
                int targetFile = startFile + FILE_OFFSETS[i] * 2;
                int targetRank = startRank + RANK_OFFSETS[i] * 2;

                if (targetFile == startFile) {
                    targetFile++;
                    if (targetFile < 8 && targetRank < 8 && targetFile > -1 && targetRank > -1
                            && (!Piece.isSameColor(board[targetFile][targetRank], piece) || attackingSquaresMode))
                        squares[targetFile][targetRank] = true;
                    targetFile = targetFile - 2;
                    if (targetFile < 8 && targetRank < 8 && targetFile > -1 && targetRank > -1
                            && (!Piece.isSameColor(board[targetFile][targetRank], piece) || attackingSquaresMode))
                        squares[targetFile][targetRank] = true;
                }
                if (targetRank == startRank) {
                    targetRank++;
                    if (targetFile < 8 && targetRank < 8 && targetFile > -1 && targetRank > -1
                            && (!Piece.isSameColor(board[targetFile][targetRank], piece) || attackingSquaresMode))
                        squares[targetFile][targetRank] = true;
                    targetRank = targetRank - 2;
                    if (targetFile < 8 && targetRank < 8 && targetFile > -1 && targetRank > -1
                            && (!Piece.isSameColor(board[targetFile][targetRank], piece) || attackingSquaresMode))
                        squares[targetFile][targetRank] = true;
                }
            }

            return squares;
        }

        if (pieceType == Pieces.KING_BLACK || pieceType == Pieces.KING_WHITE) {
            for (int directionIndex = 0; directionIndex < 8; directionIndex++) {
                int targetFile = startFile + FILE_OFFSETS[directionIndex];
                int targetRank = startRank + RANK_OFFSETS[directionIndex];
                if (targetFile < 0 || targetFile > 7 || targetRank < 0 || targetRank > 7) {
                    continue;
                }
                Piece squarePiece = board[targetFile][targetRank];

                if (squarePiece.notBlank() && Piece.isSameColor(piece, squarePiece) && !attackingSquaresMode) {
                    continue;
                }

                squares[targetFile][targetRank] = true;
            }

            return squares;
        }

        if (pieceType == Pieces.PAWN_BLACK || pieceType == Pieces.PAWN_WHITE) {
            int direction;
            if (pieceType == Pieces.PAWN_WHITE ^ _whiteAtBottom) {
                direction = 1;
            } else {
                direction = 0;
            }

            int startIndex = direction + 4;
            for (int directionIndex = startIndex; directionIndex < 8; directionIndex = directionIndex + 2) {
                int targetFile = startFile + FILE_OFFSETS[directionIndex];
                int targetRank = startRank + RANK_OFFSETS[directionIndex];

                if (targetFile < 0 || targetFile > 7 || targetRank < 0 || targetRank > 7) {
                    continue;
                }
                Piece squarePiece = board[targetFile][targetRank];

                if ((squarePiece.notBlank() && !Piece.isSameColor(piece, squarePiece)) || attackingSquaresMode) {
                    squares[targetFile][targetRank] = true;
                }
            }

            if (attackingSquaresMode) {
                return squares;
            }

            int targetFile = startFile + FILE_OFFSETS[direction];
            int targetRank = startRank + RANK_OFFSETS[direction];
            if (targetFile < 0 || targetFile > 7) {
                return squares;
            }
            Piece squarePiece = board[targetFile][targetRank];
            if (squarePiece.notBlank()) {
                return squares;
            }
            squares[targetFile][targetRank] = true;

            int checkFile = -5 * direction + 6;
            targetFile = targetFile + FILE_OFFSETS[direction];
            if (startFile == checkFile && !board[targetFile][targetRank].notBlank()) {
                squares[targetFile][targetRank] = true;
            }

            return squares;
        }

        int startIndex = (pieceType == Pieces.BISHOP_BLACK || pieceType == Pieces.BISHOP_WHITE) ? 4 : 0;
        int endIndex = (pieceType == Pieces.ROOK_BLACK || pieceType == Pieces.ROOK_WHITE) ? 4 : 8;

        for (int directionIndex = startIndex; directionIndex < endIndex; directionIndex++) {
            for (int i = 0; i < slidingMoveData[directionIndex]; i++) {
                int targetFile = startFile + FILE_OFFSETS[directionIndex] * (i + 1);
                int targetRank = startRank + RANK_OFFSETS[directionIndex] * (i + 1);
                Piece squarePiece = board[targetFile][targetRank];

                if (squarePiece.notBlank() && Piece.isSameColor(piece, squarePiece) && !attackingSquaresMode) {
                    break;
                }

                squares[targetFile][targetRank] = true;

                if (squarePiece.notBlank() && (!Piece.isSameColor(piece, squarePiece) || attackingSquaresMode)) {
                    break;
                }
            }
        }

        return squares;
    }

    public static void setIsWhiteMove(boolean isIt) {
        isWhiteMove = isIt;
    }

    public static void setBoardOrientation(boolean isWhiteBottom) {
        _whiteAtBottom = isWhiteBottom;
    }

    private static boolean[][] addArrays(boolean[][] array1, boolean[][] array2) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                array1[i][j] = array1[i][j] || array2[i][j];
            }
        }
        return array1;
    }

    private static boolean isEmpty(boolean[][] array1) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (array1[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

}
