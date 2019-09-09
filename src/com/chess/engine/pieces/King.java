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

public class King extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDS = {-9, -8, -7, -1, 1, 7, 8, 9};

    public King(final Alliance pieceAlliance, final int position) {
        super(PieceType.KING, pieceAlliance, position);
    }

    @Override
    public Collection<Move> calcLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateOffset : CANDIDATE_MOVE_COORDS) {
            final int candidateCoordDestination = this.position + candidateOffset;
            if (firstColumnExclusion(this.position, candidateOffset) ||
                eightColumnExclusion(this.position, candidateOffset)) {
                continue;
            }
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
                }
            }

        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public King movePiece(Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoord());
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    private static boolean firstColumnExclusion(final int position, final int candidateOffSet) {
        return BoardUtils.FIRST_COLUMN[position] && (candidateOffSet == -9 || candidateOffSet == -1
                || candidateOffSet == 7);
    }

    private static boolean eightColumnExclusion(final int position, final int candidateOffSet) {
        return BoardUtils.EIGHTH_COLUMN[position] && (candidateOffSet == 1 || candidateOffSet == 9
                || candidateOffSet == -7);
    }
}
