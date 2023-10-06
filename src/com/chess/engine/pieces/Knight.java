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

public class Knight extends Piece{
    //Imagine Knight is on tile d4:
    //
    //Chess board
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   |   |   |   |   |   |
    // 7|   |   |   |   |   |   |   |   |
    // 6|   |   | * |   | * |   |   |   |
    // 5|   | * |   |   |   | * |   |   |
    // 4|   |   |   | K |   |   |   |   |
    // 3|   | * |   |   |   | * |   |   |
    // 2|   |   | * |   | * |   |   |   |
    // 1|   |   |   |   |   |   |   |   |
    //
    // "K" represents the piece Knight
    // "*" represents it's possible moves

    //MOVE_UP_LEFT
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   |   |   |   |   |   |
    // 7|   |   |   |   |   |   |   |   |
    // 6|   |   | k<|-- | * |   |   |   |
    // 5|   | * |   | | |   | * |   |   |
    // 4|   |   |   | K |   |   |   |   |
    // 3|   | * |   |   |   | * |   |   |
    // 2|   |   | * |   | * |   |   |   |
    // 1|   |   |   |   |   |   |   |   |

    //MOVE_RIGHT_DOWN
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   |   |   |   |   |   |
    // 7|   |   |   |   |   |   |   |   |
    // 6|   |   | * |   | * |   |   |   |
    // 5|   | * |   |   |   | * |   |   |
    // 4|   |   |   | K-|---|-v |   |   |
    // 3|   | * |   |   |   | k |   |   |
    // 2|   |   | * |   | * |   |   |   |
    // 1|   |   |   |   |   |   |   |   |

    private final static  int MOVE_UP_LEFT = -17; //c6
    private final static  int MOVE_UP_RIGHT = -15; //e6
    private final static  int MOVE_LEFT_UP = -10; //b5
    private final static  int MOVE_RIGHT_UP = -6; //f5
    private final static  int MOVE_LEFT_DOWN = 6; //b3
    private final static  int MOVE_RIGHT_DOWN = 10; //f3
    private final static  int MOVE_DOWN_LEFT = 15; //c2
    private final static  int MOVE_DOWN_RIGHT = 17; //e2
    private final static int[] CANDIDATE_MOVE_COORDINATES = {MOVE_UP_LEFT, MOVE_UP_RIGHT,
                                                            MOVE_LEFT_UP, MOVE_RIGHT_UP,
                                                            MOVE_LEFT_DOWN, MOVE_RIGHT_DOWN,
                                                            MOVE_DOWN_LEFT, MOVE_DOWN_RIGHT};
    public Knight(final Alliance pieceAlliance,
                  final int getPiecePosition) {
        super(getPiecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move>legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)){
                    continue;
                }
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance =pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance){
                        legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    //in case Piece is a difficult spot
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == MOVE_UP_LEFT ||
                                                            candidateOffset == MOVE_LEFT_UP ||
                                                            candidateOffset == MOVE_LEFT_DOWN ||
                                                            candidateOffset == MOVE_DOWN_LEFT);
    } //-17, -10, 6, 15

    private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SECOND_COLUMN[currentPosition] && (candidateOffset == MOVE_LEFT_UP ||
                                                             candidateOffset == MOVE_LEFT_DOWN);
    } //-10, 6

    private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.SEVENTH_COLUMN[currentPosition] && (candidateOffset == MOVE_RIGHT_UP ||
                                                              candidateOffset == MOVE_RIGHT_DOWN);
    } //-6, 10

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == MOVE_UP_RIGHT ||
                                                             candidateOffset == MOVE_RIGHT_UP ||
                                                             candidateOffset == MOVE_RIGHT_DOWN ||
                                                             candidateOffset == MOVE_DOWN_RIGHT);
    } // -15, -6, 10, 17
}
