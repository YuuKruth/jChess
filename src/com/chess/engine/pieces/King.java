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

public class King extends Piece {
    //Imagine King is on tile d4:
    //
    //Chess board
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   |   |   |   |   |   |
    // 7|   |   |   |   |   |   |   |   |
    // 6|   |   |   |   |   |   |   |   |
    // 5|   |   | * | * | * |   |   |   |
    // 4|   |   | * | K | * |   |   |   |
    // 3|   |   | * | * | * |   |   |   |
    // 2|   |   |   |   |   |   |   |   |
    // 1|   |   |   |   |   |   |   |   |
    //
    // "K" represents the piece King
    // "k" represents the moved piece King
    // "*" represents it's possible moves
    private final static int MOVE_UP_LEFT = -9; //Kc5
    private final static int MOVE_UP = -8; //Kd5
    private final static int MOVE_UP_RIGHT = -7; //Ke5
    private final static int MOVE_LEFT = -1; //Kc4
    private final static int MOVE_RIGHT = 1; //Ke4
    private final static int MOVE_DOWN_LEFT = 7; //Kc3
    private final static int MOVE_DOWN = 8; //Kd3
    private final static int MOVE_DOWN_RIGHT = 9; //Ke3

    private final static int[] CANDIDATE_MOVE_COORDINATE = {MOVE_UP_LEFT, MOVE_UP, MOVE_UP_RIGHT,
                                                                    MOVE_LEFT, MOVE_RIGHT,
                                                            MOVE_DOWN_LEFT, MOVE_DOWN, MOVE_DOWN_RIGHT};

    public King(final Alliance pieceAlliance,
                final int getPiecePosition) {
        super(PieceType.KING, getPiecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;
            if (isFirstColumnExclusion(this.piecePosition, currentCandidateOffset) ||
                    isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
                continue;
            }
            if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                } else {
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != pieceAlliance) {
                        legalMoves.add(new Move.AttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    private static boolean isFirstColumnExclusion(final int currentPosition,
                                                  final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == MOVE_UP_LEFT ||
                                                            candidateOffset == MOVE_LEFT ||
                                                            candidateOffset == MOVE_DOWN_LEFT);
    } //-9, -1, 7

    private static boolean isEighthColumnExclusion(final int currentPosition,
                                                   final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == MOVE_UP_RIGHT ||
                                                             candidateOffset == MOVE_RIGHT ||
                                                             candidateOffset == MOVE_DOWN_RIGHT);
    } //-7, 1, 9

}
