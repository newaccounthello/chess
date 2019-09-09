package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece {

    private final static int[] CANDIDATE_MOVE_COORDS = {7, 8, 9};

    public Pawn(final Alliance pieceAlliance, final int position) {
        super(PieceType.PAWN, pieceAlliance, position);
    }

    public Collection<Move> calcLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateOffset : CANDIDATE_MOVE_COORDS) {
            final int candidateCoordDestination = this.position + (this.pieceAlliance.getDirection() * candidateOffset);
            if (BoardUtils.isValidTileCoord(candidateCoordDestination)) {
                continue;
            }
            if (candidateOffset == 8 && !board.getTile(candidateCoordDestination).isTileOccupied()) {
                // todo more work
                legalMoves.add(new Move.MajorMove(board, this, candidateCoordDestination));
            } else if (candidateOffset == 16 && this.isFirstMove() &&
                     (BoardUtils.SECOND_ROW[this.position] && this.getPieceAlliance().isBlack()) ||
                     (BoardUtils.SEVENTH_ROW[this.position] && this.getPieceAlliance().isWhite())) {
                // cannot move up 2 squares unless the square behind is unoccupied
                final int behindCandidateDestination = this.position + (this.pieceAlliance.getDirection() * 8);
                if (!board.getTile(behindCandidateDestination).isTileOccupied() &&
                        !board.getTile(candidateCoordDestination).isTileOccupied()) {
                    legalMoves.add(new Move.MajorMove(board, this, candidateCoordDestination));
                }
            } else if (candidateOffset == 7 &&
                        !((BoardUtils.EIGHTH_COLUMN[this.position] && this.pieceAlliance.isWhite()) ||
                        (BoardUtils.FIRST_COLUMN[this.position] && this.pieceAlliance.isBlack()))) {
                if (board.getTile(candidateCoordDestination).isTileOccupied()) {
                    final  Piece pieceOnCandidate = board.getTile(candidateCoordDestination).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        // todo
                        legalMoves.add(new Move.MajorMove(board, this, candidateCoordDestination));
                    }
                }
            } else if (candidateOffset == 9 &&
                        !((BoardUtils.EIGHTH_COLUMN[this.position] && this.pieceAlliance.isBlack()) ||
                        (BoardUtils.FIRST_COLUMN[this.position] && this.pieceAlliance.isWhite()))) {
                if (board.getTile(candidateCoordDestination).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(candidateCoordDestination).getPiece();
                    if (this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
                        // todo
                        legalMoves.add(new Move.MajorMove(board, this, candidateCoordDestination));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoord());
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
}
