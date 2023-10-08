package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Knight extends Piece {
    //Imagine Knight is on tile d4:
    //
    //Chess board
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   |   |   |   |   |   |
    // 7|   |   |   |   |   |   |   |   |
    // 6|   |   | * |   | * |   |   |   |
    // 5|   | * |   |   |   | * |   |   |
    // 4|   |   |   | N |   |   |   |   |
    // 3|   | * |   |   |   | * |   |   |
    // 2|   |   | * |   | * |   |   |   |
    // 1|   |   |   |   |   |   |   |   |
    //
    // "N" represents the piece Knight
    // "n" represents the moved piece Knight
    // "*" represents it's possible moves

    //MOVE_UP_LEFT Nc6
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   |   |   |   |   |   |
    // 7|   |   |   |   |   |   |   |   |
    // 6|   |   | n<|-- | * |   |   |   |
    // 5|   | * |   | | |   | * |   |   |
    // 4|   |   |   | N |   |   |   |   |
    // 3|   | * |   |   |   | * |   |   |
    // 2|   |   | * |   | * |   |   |   |
    // 1|   |   |   |   |   |   |   |   |

    //MOVE_RIGHT_DOWN Nf3
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   |   |   |   |   |   |
    // 7|   |   |   |   |   |   |   |   |
    // 6|   |   | * |   | * |   |   |   |
    // 5|   | * |   |   |   | * |   |   |
    // 4|   |   |   | N-|---|-v |   |   |
    // 3|   | * |   |   |   | n |   |   |
    // 2|   |   | * |   | * |   |   |   |
    // 1|   |   |   |   |   |   |   |   |

    private final static int MOVE_UP_LEFT = -17; //Nc6
    private final static int MOVE_UP_RIGHT = -15; //Ne6
    private final static int MOVE_LEFT_UP = -10; //Nb5
    private final static int MOVE_RIGHT_UP = -6; //Nf5
    private final static int MOVE_LEFT_DOWN = 6; //Nb3
    private final static int MOVE_RIGHT_DOWN = 10; //Nf3
    private final static int MOVE_DOWN_LEFT = 15; //Nc2
    private final static int MOVE_DOWN_RIGHT = 17; //Ne2
    private final static int[] CANDIDATE_MOVE_COORDINATES = {MOVE_UP_LEFT, MOVE_UP_RIGHT,
                                                             MOVE_LEFT_UP, MOVE_RIGHT_UP,
                                                             MOVE_LEFT_DOWN, MOVE_RIGHT_DOWN,
                                                             MOVE_DOWN_LEFT, MOVE_DOWN_RIGHT};

    public Knight(final Alliance pieceAlliance,
                  final int getPiecePosition) {
        super(PieceType.KNIGHT, getPiecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Knight movePiece(Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    //in case Piece is a difficult spot
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == MOVE_UP_LEFT ||
                                                            candidateOffset == MOVE_LEFT_UP ||
                                                            candidateOffset == MOVE_LEFT_DOWN ||
                                                            candidateOffset == MOVE_DOWN_LEFT);
    } //-17, -10, 6, 15

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == MOVE_LEFT_UP ||
                                                             candidateOffset == MOVE_LEFT_DOWN);
    } //-10, 6

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == MOVE_RIGHT_UP ||
                                                              candidateOffset == MOVE_RIGHT_DOWN);
    } //-6, 10

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == MOVE_UP_RIGHT ||
                                                             candidateOffset == MOVE_RIGHT_UP ||
                                                             candidateOffset == MOVE_RIGHT_DOWN ||
                                                             candidateOffset == MOVE_DOWN_RIGHT);
    } // -15, -6, 10, 17
}
