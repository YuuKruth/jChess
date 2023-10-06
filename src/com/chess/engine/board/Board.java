package com.chess.engine.board;

import com.chess.engine.Alliance;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;

public class Board {
    //Chess board 8*8
    //    a   b   c   d   e   f   g   h
    // 8|   |   |   |   |   |   |   |   |
    // 7|   |   |   |   |   |   |   |   |
    // 6|   |   |   |   |   |   |   |   |
    // 5|   |   |   |   |   |   |   |   |
    // 4|   |   |   |   |   |   |   |   |
    // 3|   |   |   |   |   |   |   |   |
    // 2|   |   |   |   |   |   |   |   |
    // 1|   |   |   |   |   |   |   |   |

    private final List<Tile> gameBoard;
    //ctor
    private Board(Builder builder){
        this.gameBoard = createGameBoard(builder);
    }
    public Tile getTile(final int tileCoordinate){
        return null;
    }
    private static List<Tile> createGameBoard(final Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            tiles[i] = Tile.crateTile(i, builder.boardConfig.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }
    //TODO
    public static Board createStandardBoard(){
        return null;
    }


    //BUILDER
    public static class Builder{

        Map<Integer, Piece> boardConfig;
        Alliance nextMoveMaker;

        public Builder(){
        }

        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(), piece);
            return this;
        }
        public Builder setMoveMaker(final Alliance nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }
        public Board build(){
            return new Board(this);
        }
    }
}
