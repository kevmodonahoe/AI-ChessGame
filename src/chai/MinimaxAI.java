package chai;

import chesspresso.Chess;
import chesspresso.move.IllegalMoveException;
import chesspresso.position.Position;
import javafx.geometry.Pos;

/**
 * Created by kdonahoe on 10/17/16.
 */
public class MinimaxAI implements ChessAI {
    private int MAXDEPTH = 3;
    private int statesVisited;
    private int depthReached;

    @Override
    public short getMove(Position position) throws IllegalMoveException {
        Position positionCopy = position;
        statesVisited = 0;
        ChessMove chosenMove = minimax(positionCopy, 0);
        System.out.println();
        System.out.println("Num States Visited: " + statesVisited);
        System.out.println("Depth Reached: " + depthReached);
        System.out.println("Return Value: " + chosenMove.value);
        return chosenMove.move;
    }

    public ChessMove minimax(Position position, int depth) throws IllegalMoveException {
        short[] possibleMoves = position.getAllMoves();
        ChessMove bestMove = new ChessMove();
        bestMove.value = Short.MIN_VALUE;
        depth++;
        for(short move : possibleMoves) {
            statesVisited++;
            position.doMove(move);
            ChessMove childMove = min_move(position, depth);
            bestMove.value = (short) Math.max(bestMove.value, childMove.value);
            if(bestMove.value == childMove.value) {
                bestMove.move = move;
            }

            position.undoMove();
        }

//        position.doMove(bestMove.move);
//        if(winCheck(position) == 1) {
//            position.undoMove();
//            System.out.println("Game won by: " + position.getToPlay());
//        } else if(winCheck(position) == 2) {
//            position.undoMove();
//            System.out.println("Stale mate!");
//        } else {
//            position.undoMove();
//        }

        return bestMove;
    }

    public ChessMove min_move(Position position, int depth) throws IllegalMoveException {
        if(cutoffTest(depth, position)) {
            ChessMove bestMove = new ChessMove(position.getLastShortMove(), maxUtilityFunction(position));
            depthReached = depth;
            return bestMove;
        }
        depth++;

        ChessMove bestMove = new ChessMove();
        bestMove.value = Short.MAX_VALUE;
        short[] possibleMoves = position.getAllMoves();
        for(short move : possibleMoves) {
            statesVisited++;
            position.doMove(move);
            ChessMove childMove = max_move(position, depth);
            bestMove.value = (short) Math.min(bestMove.value, childMove.value);
            if(bestMove.value == childMove.value) {
                bestMove.move = move;
            }
            position.undoMove();
        }
        return bestMove;
    }

    public ChessMove max_move(Position position, int depth) throws IllegalMoveException {
        if(cutoffTest(depth, position)) {
            ChessMove bestMove = new ChessMove(position.getLastShortMove(), minUtilityFunction(position));
            depthReached = depth;
            return bestMove;
        }
        depth++;

        ChessMove bestMove = new ChessMove();
        bestMove.value = Short.MIN_VALUE;
        short[] possibleMoves = position.getAllMoves();
        for(short move : possibleMoves) {
            statesVisited++;
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

    public int winCheck(Position position) {
        if(position.isMate()) {
            return 1;
        } else if(position.isStaleMate()) {
            return 2;
        }
        return 0;
    }

}
