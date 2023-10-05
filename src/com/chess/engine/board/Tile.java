package com.chess.engine.board;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Tile {

    protected final int tileCoordinate;

    //Map with all possible empty tiles
    private static final Map<Integer, EmptyTile> EMPTY_TILES_CASHE = createAllPossibleEmptyTiles();

    //creates an unmodifiable map with all possible empty tiles
    private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }

        //without guava lib use this from JDK:
        //Collections.unmodifiableMap(emptyTileMap);
        return ImmutableMap.copyOf(emptyTileMap);
    }
    //creates a tile
    public static Tile crateTile(final int tileCoordinate, final Piece piece){
        return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CASHE.get(tileCoordinate);
    }

    //ctor
    private Tile(final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    //abstract methods
    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

    //empty tile
    public static final class EmptyTile extends Tile{
        //ctor
        EmptyTile(final int coordinate){
            super(coordinate);
        }

        //methods
        @Override
        public boolean isTileOccupied(){
            return false;
        }
        @Override
        public Piece getPiece(){
            return null;
        }

    }

    //occupied tile
    public static final class OccupiedTile extends Tile{
        private final Piece pieceOnTile;
        //ctor
         private OccupiedTile(int tileCoordinate, final Piece pieceOnTile){
            super(tileCoordinate);
            this.pieceOnTile = pieceOnTile;
        }

        //methods
        @Override
        public boolean isTileOccupied(){
            return true;
        }
        @Override
        public Piece getPiece(){
            return pieceOnTile;
        }
    }
}
