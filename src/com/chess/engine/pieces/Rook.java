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

public class Rook extends Piece{
    //Imagine Rook is on tile d4:
    //
    //Chess board
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   | * |   |   |   |   |
    // 7|   |   |   | * |   |   |   |   |
    // 6|   |   |   | * |   |   |   |   |
    // 5|   |   |   | * |   |   |   |   |
    // 4| * | * | * | R | * | * | * | * |
    // 3|   |   |   | * |   |   |   |   |
    // 2|   |   |   | * |   |   |   |   |
    // 1|   |   |   | * |   |   |   |   |
    //
    // "R" represents the piece Rook
    // "*" represents it's possible moves

    //VECTOR_VERTICALLY_UP
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   | * |   |   |   |   |
    // 7|   |   |   | * |   |   |   |   |
    // 6|   |   |   | r |   |   |   |   |
    // 5|   |   |   | | |   |   |   |   |
    // 4| * | * | * | R | * | * | * | * |
    // 3|   |   |   | * |   |   |   |   |
    // 2|   |   |   | * |   |   |   |   |
    // 1|   |   |   | * |   |   |   |   |

    //VECTOR_HORIZONTALLY_RIGHT
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   | * |   |   |   |   |
    // 7|   |   |   | * |   |   |   |   |
    // 6|   |   |   | * |   |   |   |   |
    // 5|   |   |   | * |   |   |   |   |
    // 4| * | * | * | R |---|---|>r | * |
    // 3|   |   |   | * |   |   |   |   |
    // 2|   |   |   | * |   |   |   |   |
    // 1|   |   |   | * |   |   |   |   |

    private static final int VECTOR_VERTICALLY_UP = -8; //d5 to d8
    private static final int VECTOR_HORIZONTALLY_LEFT = -1; //c4 to a4
    private static final int VECTOR_HORIZONTALLY_RIGHT = 1; //e4 to h4
    private static final int VECTOR_VERTICALLY_DOWN = 8; //d3 to d1
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {VECTOR_VERTICALLY_UP, VECTOR_HORIZONTALLY_LEFT, VECTOR_HORIZONTALLY_RIGHT, VECTOR_VERTICALLY_DOWN};
    Rook(int getPiecePosition, Alliance pieceAlliance) {
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
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == VECTOR_HORIZONTALLY_LEFT); //-1
    }

    private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == VECTOR_HORIZONTALLY_RIGHT); //1
    }
}
