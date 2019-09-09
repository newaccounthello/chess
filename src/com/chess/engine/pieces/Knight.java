package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;
//import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Knight extends Piece {

    private final static int[] CANDIDATE_LEGAL_COORDS = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(final Alliance pieceAlliance, final int position) {
        super(PieceType.KNIGHT, pieceAlliance, position);
    }

    @Override
    public Collection<Move> calcLegalMoves(final Board board) {

        int candidateCoordDestination;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateOffset : CANDIDATE_LEGAL_COORDS) {
            candidateCoordDestination = this.position + candidateOffset;
            if (BoardUtils.isValidTileCoord(candidateCoordDestination)) {
                if (firstColumnExclusion(this.position, candidateOffset) ||
                        secondColumnExclusion(this.position, candidateOffset) ||
                        seventhColumnExclusion(this.position, candidateOffset) ||
                        eighthColumnExclusion(this.position, candidateOffset)) {
                    continue;
                }
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
    public Knight movePiece(Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoord());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    private static boolean firstColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.FIRST_COLUMN[position] && (candidateOffset == -17 || candidateOffset == -10
                || candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean secondColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.SECOND_COLUMN[position] && (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean seventhColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.SEVENTH_COLUMN[position] && (candidateOffset == -6 || candidateOffset == 10);
    }

    private static boolean eighthColumnExclusion(final int position, final int candidateOffset) {
        return BoardUtils.EIGHTH_COLUMN[position] && (candidateOffset == -15 || candidateOffset == -6
                || candidateOffset == 10 || candidateOffset == 17);
    }
}
