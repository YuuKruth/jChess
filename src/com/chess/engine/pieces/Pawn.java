package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Pawn extends Piece {
    //Imagine Pawn is on tile d4 or f2:
    //
    //Chess board
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   |   |   |   |   |   |
    // 7|   |   |   |   |   |   |   |   |
    // 6|   |   |   |   |   |   |   |   |
    // 5|   |   |   | * |   |   |   |   |
    // 4|   |   |   | P |   | * |   |   |
    // 3|   |   |   |   |   | * |   |   |
    // 2|   |   |   |   |   | P |   |   |
    // 1|   |   |   |   |   |   |   |   |
    //
    // "P" represents the piece Pawn
    // "p" represents the moved piece Pawn
    // "*" represents it's possible moves

    //MOVE_UP d5
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   |   |   |   |   |   |
    // 7|   |   |   |   |   |   |   |   |
    // 6|   |   |   |   |   |   |   |   |
    // 5|   |   |   | p |   |   |   |   |
    // 4|   |   |   | P |   |   |   |   |
    // 3|   |   |   |   |   |   |   |   |
    // 2|   |   |   |   |   |   |   |   |
    // 1|   |   |   |   |   |   |   |   |

    //MOVE_JUMP f4
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   |   |   |   |   |   |
    // 7|   |   |   |   |   |   |   |   |
    // 6|   |   |   |   |   |   |   |   |
    // 5|   |   |   |   |   |   |   |   |
    // 4|   |   |   |   |   | p |   |   |
    // 3|   |   |   |   |   | | |   |   |
    // 2|   |   |   |   |   | P |   |   |
    // 1|   |   |   |   |   |   |   |   |

    private final static int MOVE_UP = 8; //d5
    private final static int MOVE_JUMP = 16; //f4
    private final static int ATTACK_MOVE_ONE_SIDE = 7; //c5 or e5
    private final static int ATTACK_MOVE_ANOTHER_SIDE = 9; //e5 or c5
    private final static int[] CANDIDATE_MOVE_COORDINATE = {MOVE_UP, MOVE_JUMP,
                               ATTACK_MOVE_ONE_SIDE, ATTACK_MOVE_ANOTHER_SIDE};

    public Pawn(final Alliance pieceAlliance,
                final int getPiecePosition) {
        super(PieceType.PAWN, getPiecePosition, pieceAlliance);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();
        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
            final int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateOffset);
            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            //Non-Attacking Move
            if (currentCandidateOffset == MOVE_UP && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                //TODO more work here!!!! deal with promotions
                legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
            }//Jump Move
            else if (currentCandidateOffset == MOVE_JUMP && this.isFirstMove() &&
                    (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) ||
                    (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite())) {
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                }
            }//Attack Move one side
            else if (currentCandidateOffset == ATTACK_MOVE_ONE_SIDE &&
                    !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                            (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        //TODO more work here!! deal with promotion
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }

            }//Attack Move another side
            else if (currentCandidateOffset == ATTACK_MOVE_ANOTHER_SIDE &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite() ||
                      (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))) {
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        //TODO more work here!! deal with promotion
                        legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
}
