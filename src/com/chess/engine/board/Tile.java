package com.chess.engine.board;

import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

//import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

        public abstract class Tile {

            protected final int coordinate;

            private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllTiles();

            public static Tile createTile(final int coordinate, final Piece piece) {
                return (piece != null ? new OccupiedTile(coordinate, piece) : EMPTY_TILES_CACHE.get(coordinate));
            }

            private Tile(int coordinate) {
                this.coordinate = coordinate;
            }

            private static Map<Integer, EmptyTile> createAllTiles() {

                final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

                for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
                    emptyTileMap.put(i, new EmptyTile(i));
                }

                // can also use: return Collections.unmodifiableMap(emptyTileMap) rather than guava
                // After construction of the emptyTileMap, it is immutable (guava 21)
                return ImmutableMap.copyOf(emptyTileMap);
            }

            public int getTileCoordinate() {
                return this.coordinate;
            }

            public abstract boolean isTileOccupied();

            public abstract Piece getPiece();

            public static final class EmptyTile extends Tile {

                private EmptyTile(final int coordinate) {
                    super(coordinate);
                }

                @Override
                public String toString() {
                    return "-";
                }

                @Override
                public boolean isTileOccupied() {
                    return false;
                }

                @Override
                public Piece getPiece() {
                    return null;
                }

            }

            public static final class OccupiedTile extends Tile {

                private final Piece piece;

                private OccupiedTile(int coordinate, Piece piece) {
                    super(coordinate);
                    this.piece = piece;
                }

                @Override
                public String toString() {
                    return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
                            getPiece().toString();
                }

        @Override
        public boolean isTileOccupied() {
            return true;
        }

        @Override
        public Piece getPiece() {
            return this.piece;
        }
    }
}
