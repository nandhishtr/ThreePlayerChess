package model;

import abstraction.BasePiece;
import common.Colour;
import common.Direction;
import common.InvalidPositionException;
import common.Position;
import utility.Log;
import utility.Util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static utility.MovementUtil.step;
import static utility.MovementUtil.stepOrNull;

/**
 * Knight class extends BasePiece. Move directions for the Knight, the polygons
 * to be highlighted, and its legal moves are checked here
 **/
public class Knight extends BasePiece {

    private static final String TAG = "KNIGHT";

    /**
     * Knight constructor
     * @param colour: Colour of the chess piece being initiated
     * */
    public Knight(Colour colour) {
        super(colour);
    }

    /**
     * Method to initialize directions for a chess piece
     **/
    @Override
    protected void setupDirections() {
        this.directions = new Direction[][] {{Direction.FORWARD,Direction.FORWARD,Direction.LEFT},
                {Direction.FORWARD,Direction.FORWARD,Direction.RIGHT},{Direction.FORWARD,Direction.LEFT,Direction.LEFT},
                {Direction.FORWARD,Direction.RIGHT,Direction.RIGHT},{Direction.BACKWARD,Direction.BACKWARD,Direction.LEFT},
                {Direction.BACKWARD,Direction.BACKWARD,Direction.RIGHT},{Direction.BACKWARD,Direction.LEFT,Direction.LEFT},
                {Direction.BACKWARD,Direction.RIGHT,Direction.RIGHT},{Direction.LEFT,Direction.LEFT,Direction.FORWARD},
                {Direction.LEFT,Direction.LEFT,Direction.BACKWARD},{Direction.LEFT,Direction.FORWARD,Direction.FORWARD},
                {Direction.LEFT,Direction.BACKWARD,Direction.BACKWARD},{Direction.RIGHT,Direction.RIGHT,Direction.FORWARD},
                {Direction.RIGHT,Direction.RIGHT,Direction.BACKWARD},{Direction.RIGHT,Direction.FORWARD,Direction.FORWARD},
                {Direction.RIGHT,Direction.BACKWARD,Direction.BACKWARD}};
    }

    /**
     *  To check whether a move is valid
     * @param board: Board class instance representing current game board
     * @param start: Start position of move
     * @param end: End position of move
     * @return True if a move is possible from start to end, else False
     * */
    @Override
    public boolean isLegalMove(Board board, Position start, Position end) {
        Map<Position, BasePiece> boardMap = board.boardMap;
        BasePiece mover = this;
        BasePiece target = boardMap.get(end);
        if(mover==null) return false; // No piece present at start position
        Colour moverCol = mover.getColour();
        if(target!= null && moverCol==target.getColour()) return false; // player cannot take it's own piece

        Collection<Position> wallPiecePositions = board.wallPieceMapping.values();
        if(wallPiecePositions.contains(end)) return false;

        Direction[][] steps = this.directions;
        for(int i = 0; i<steps.length; i++){
            try{
                if(end == step(mover, steps[i],start))
                    return true;
            }catch(InvalidPositionException e){}//do nothing, steps went off board.
        }
        return false;
    }

    /**
     * Fetch all the possible positions where a piece can move on board
     * @param board: Board class instance representing current game board
     * @param start: position of piece on board
     * @return List of possible positions a piece is allowed to move
     * */
    @Override
    public List<Position> getHighlightPolygons(Board board, Position start) {
        Map<Position, BasePiece> boardMap = board.boardMap;
        Collection<Position> wallPiecePositions = board.wallPieceMapping.values();

        Set<Position> positionSet = new HashSet<>();
        BasePiece mover = this;
        Direction[][] steps = this.directions;

        for(Direction[] step: steps) {
            Position end = stepOrNull(mover, step, start);

            if(positionSet.contains(end) || wallPiecePositions.contains(end)) continue;

            if(end != null) {
                BasePiece target = boardMap.get(end);

                if(target!=null) {
                    if(target.getColour()!=mover.getColour()) {
                        Log.d(TAG, "position enemy: "+end);
                        positionSet.add(end);
                    }
                } else {
                    Log.d(TAG, "position: "+end);
                    positionSet.add(end);
                }
            }
        }

        return Util.toList(positionSet);
    }

    /**
     * Returns custom string representation of the class
     * @return String
     * */
    @Override
    public String toString() {
        return this.colour.toString()+"N";
    }
}
