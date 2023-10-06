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

public class Queen extends Piece{
    //Imagine Queen is on tile d4:
    //
    //Chess board
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   | * |   |   |   | * |
    // 7| * |   |   | * |   |   | * |   |
    // 6|   | * |   | * |   | * |   |   |
    // 5|   |   | * | * | * |   |   |   |
    // 4| * | * | * | Q | * | * | * | * |
    // 3|   |   | * | * | * |   |   |   |
    // 2|   | * |   | * |   | * |   |   |
    // 1| * |   |   | * |   |   | * |   |
    //
    // "Q" represents the piece Queen
    // "*" represents it's possible moves

    //VECTOR_DIAGONALLY_RIGHT_UP
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   | * |   |   |   | * |
    // 7| * |   |   | * |   |   |>q |   |
    // 6|   | * |   | * |   | / |   |   |
    // 5|   |   | * | * | / |   |   |   |
    // 4| * | * | * | Q | * | * | * | * |
    // 3|   |   | * | * | * |   |   |   |
    // 2|   | * |   | * |   | * |   |   |
    // 1| * |   |   | * |   |   | * |   |

    //VECTOR_HORIZONTALLY_RIGHT
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   | * |   |   |   | * |
    // 7| * |   |   | * |   |   | * |   |
    // 6|   | * |   | * |   | * |   |   |
    // 5|   |   | * | * | * |   |   |   |
    // 4| * | * | * | Q |---|---|>q | * |
    // 3|   |   | * | * | * |   |   |   |
    // 2|   | * |   | * |   | * |   |   |
    // 1| * |   |   | * |   |   | * |   |

    private static final int VECTOR_DIAGONALLY_LEFT_UP = -9; //c5 to a7
    private static final int VECTOR_VERTICALLY_UP = -8; //d5 to d8
    private static final int VECTOR_DIAGONALLY_RIGHT_UP = -7; //e5 to h8
    private static final int VECTOR_HORIZONTALLY_LEFT = -1; //c4 to a4
    private static final int VECTOR_HORIZONTALLY_RIGHT = 1; //e4 to h4
    private static final int VECTOR_DIAGONALLY_LEFT_DOWN = 7; //c3 to a1
    private static final int VECTOR_VERTICALLY_DOWN = 8; //d3 to d1
    private static final int VECTOR_DIAGONALLY_RIGHT_DOWN = 9; //e3 to g1

    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {VECTOR_DIAGONALLY_LEFT_UP, VECTOR_VERTICALLY_UP,
                                                                    VECTOR_DIAGONALLY_RIGHT_UP, VECTOR_HORIZONTALLY_LEFT,
                                                                    VECTOR_HORIZONTALLY_RIGHT, VECTOR_DIAGONALLY_LEFT_DOWN,
                                                                    VECTOR_VERTICALLY_DOWN, VECTOR_DIAGONALLY_RIGHT_DOWN};
    public Queen(final Alliance pieceAlliance,
                 final int getPiecePosition) {
        super(getPiecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                if (isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) ||
                        isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)){
                    break;
                }
                candidateDestinationCoordinate += candidateCoordinateOffset;
                if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if (!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance =pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAlliance){
                            legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                        }
                        break;
                    }

                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == VECTOR_DIAGONALLY_LEFT_UP ||
                                                            candidateOffset == VECTOR_HORIZONTALLY_LEFT ||
                                                            candidateOffset == VECTOR_DIAGONALLY_LEFT_DOWN);
    } //-9, -1, 7

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == VECTOR_DIAGONALLY_RIGHT_UP ||
                                                             candidateOffset == VECTOR_HORIZONTALLY_RIGHT ||
                                                             candidateOffset == VECTOR_DIAGONALLY_RIGHT_DOWN);
    } //-7, 1, 9
}
