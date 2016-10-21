package chai;

import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;
import javafx.geometry.Pos;
import java.util.Random;

import java.util.HashMap;

/**
 * Created by kdonahoe on 10/17/16.
 */
public class MinimaxAI implements ChessAI {
    private int MAXDEPTH = 4;

    @Override
    public short getMove(Position position) throws IllegalMoveException {
        Position positionCopy = position;
        ChessMove chosenMove = minimax(positionCopy, 0);
        System.out.println("Return value: " + chosenMove.value);
        return chosenMove.move;
    }

    public ChessMove minimax(Position position, int depth) throws IllegalMoveException {
        short[] possibleMoves = position.getAllMoves();
        ChessMove bestMove = new ChessMove();
        bestMove.value = Short.MIN_VALUE;
        int i=0;
        depth++;
        for(short move : possibleMoves) {
            position.doMove(move);
            ChessMove childMove = min_move(position, depth);
            bestMove.value = (short) Math.max(bestMove.value, childMove.value);
            if(bestMove.value == childMove.value) {
                bestMove.move = move;
            }

            position.undoMove();
        }
        return bestMove;
    }

    public ChessMove min_move(Position position, int depth) throws IllegalMoveException {
        if(cutoffTest(depth, position)) {
            ChessMove bestMove = new ChessMove(position.getLastShortMove(), maxUtilityFunction(position));
            return bestMove;
        }
        depth++;

        ChessMove bestMove = new ChessMove();
        bestMove.value = Short.MAX_VALUE;
        short[] possibleMoves = position.getAllMoves();
        for(short move : possibleMoves) {
            position.doMove(move);
            ChessMove childMove = max_move(position, depth);
            bestMove.value = (short) Math.min(bestMove.value, childMove.value);
            if(bestMove.value == childMove.value) {
                bestMove.move = childMove.move;
            }
            position.undoMove();
        }
        return bestMove;
    }

    public ChessMove max_move(Position position, int depth) throws IllegalMoveException {
        if(cutoffTest(depth, position)) {
            ChessMove bestMove = new ChessMove(position.getLastShortMove(), minUtilityFunction(position));
            return bestMove;
        }
        depth++;

        ChessMove bestMove = new ChessMove();
        bestMove.value = Short.MIN_VALUE;
        short[] possibleMoves = position.getAllMoves();
        for(short move : possibleMoves) {
            position.doMove(move);
            ChessMove childMove = min_move(position, depth);
            bestMove.value = (short) Math.max(bestMove.value, childMove.value);
            if(bestMove.value == childMove.value) {
                bestMove.move = childMove.move;
            }
            position.undoMove();
        }
        return bestMove;
    }



    // The cutoff test for the minimax search - returns true if either the current
    // state is a terminal state, or if the depth equals the max depth.
    public boolean cutoffTest(int depth, Position position) {
        if(position.isTerminal() || depth == MAXDEPTH) {
            return true;
        }

        return false;
    }

    public int maxUtilityFunction(Position position) {
        if(position.isTerminal()) {
            return Short.MAX_VALUE;
        }

        return position.getMaterial();
    }


    public int minUtilityFunction(Position position) {
        if(position.isTerminal()) {
            return Short.MIN_VALUE;
        }

        return position.getMaterial();
    }

}
