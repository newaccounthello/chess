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

public class Rook extends Piece {
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDS = {-8, -1, 1, 8};
    public Rook(final Alliance pieceAlliance, final int position) {
        super(PieceType.ROOK, pieceAlliance, position);
    }

    @Override
    public Collection<Move> calcLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateOffset : CANDIDATE_MOVE_VECTOR_COORDS) { // continue with the candidate offset
            int candidateCoordDestination = this.position;
            while (BoardUtils.isValidTileCoord(candidateCoordDestination)) { // As long as the tile is valid
                if (firstColumnExclusion(candidateCoordDestination, candidateOffset)
                        || eighthColumnExclusion(candidateCoordDestination, candidateOffset)) {
                    break;
                }
                candidateCoordDestination += candidateOffset;
                if (BoardUtils.isValidTileCoord(candidateCoordDestination)) {
                    final Tile candidateDestinationTile = board.getTile(candidateCoordDestination);
                    if (!candidateDestinationTile.isTileOccupied()) {
                        legalMoves.add(new MajorMove(board, this, candidateCoordDestination));
                    } else {
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        final Alliance pieceAlliance = pieceAtDestination.getPieceAlliance();
                        if (this.pieceAlliance != pieceAlliance) {
                            legalMoves.add(new AttackMove(board, this, candidateCoordDestination, pieceAtDestination));
                        }
                        break; // Since you can't go past if there is a piece in the way
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Rook movePiece(Move move) {
        return new Rook(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoord());
    }

    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }

    private static boolean firstColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[position] && (candidateOffset == -1);
    }

    private static boolean eighthColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[position] && (candidateOffset == 1);
    }
}
