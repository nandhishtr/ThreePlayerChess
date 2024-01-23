package model;

import common.Colour;
import common.Direction;
import common.InvalidPositionException;
import common.Position;
import utility.Log;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static utility.MovementUtil.step;
import static utility.MovementUtil.stepOrNull;

/**
 * Rook class extends BasePiece. Move directions for the Rook, the polygons
 * to be highlighted, and its legal moves are checked here
 **/
public class Rook extends BasePiece {

    private static final String TAG = "ROOK";

    /**
     * Rook constructor
     * @param colour: Colour of the chess piece being initiated
     * */
    public Rook(Colour colour) {
        super(colour);
        setupDirections();
    }

    /**
     * Method to initialize directions for a chess piece
     **/
    @Override
    protected void setupDirections() {
        this.directions = new Direction[][] {{Direction.BACKWARD},{Direction.LEFT},{Direction.RIGHT},{Direction.FORWARD}};
    }

    /**
     *  To check whether a move is valid
     * @param board: Board class instance representing current game board
     * @param start: Start position of move
     * @param end: End position of move
     * @return True if a move is possible from start to end, else False
     * */
    @Override
    public boolean canMove(Board board, Position start, Position end) {
        Map<Position, BasePiece> boardMap = board.boardMap;
        BasePiece mover = this;

        Direction[][] steps = this.directions;
        for (Direction[] step : steps) {
            try {
                Position tmp = step(mover, step, start);
                while (end != tmp &&
                        (boardMap.get(tmp) == null) || (boardMap.get(tmp) instanceof Wall && boardMap.get(tmp).getColour() == mover.getColour())) {
                    Log.d(TAG, "tmp: " + tmp);
                    tmp = step(mover, step, tmp, tmp.getColour() != start.getColour());
                }
                if (end == tmp) {
                    return true; // when end position is in range of rook and contains a piece
                }
            } catch (InvalidPositionException e) {
                Log.e(TAG, "InvalidPositionException: " + e.getMessage());
            }//do nothing, steps went off board.
        }
        return false;
    }

    /**
     * Fetch all the possible positions where a piece can move on board
     * @param board: Board class instance representing current game board
     * @param start: position of piece on board
     * @return Set of possible positions a piece is allowed to move
     * */
    @Override
    public Set<Position> getHighlightPolygons(Board board, Position start) {
        Map<Position, BasePiece> boardMap = board.boardMap;
        Collection<Position> wallPiecePositions = board.wallPieceMapping.values();
        //List<Position> positions = new ArrayList<>();
        Set<Position> positionSet = new HashSet<>();
        BasePiece mover = this;
        Direction[][] steps = this.directions;

        for (Direction[] step : steps) {
            Position tmp = stepOrNull(mover, step, start);
            while(tmp != null &&
                    (boardMap.get(tmp)==null || (boardMap.get(tmp) instanceof Wall && boardMap.get(tmp).getColour() == mover.getColour()))) {
                Log.d(TAG, "tmp: "+tmp);
                positionSet.add(tmp);
                tmp = stepOrNull(mover, step, tmp, tmp.getColour()!=start.getColour());
            }

            if(tmp!=null) {
                if(boardMap.get(tmp).getColour()!=mover.getColour()) {
                    Log.d(TAG, "Opponent tmp: " + tmp);
                    positionSet.add(tmp);
                } else {
                    Log.d(TAG, "Mine tmp: " + tmp);
                }
            }
        }

        for(Position position: wallPiecePositions) {
            if(positionSet.contains(position)) {
                Log.d(TAG, "Removed a wallPiecePos: "+position);
                positionSet.remove(position);
            }
        }

        return positionSet;
    }

    /**
     * Returns custom string representation of the class
     * @return String
     * */
    @Override
    public String toString() {
        return this.colour.toString()+"R";
    }
}
