package model;

import com.google.common.collect.ImmutableSet;
import common.Colour;
import common.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static common.Position.*;
import static org.junit.jupiter.api.Assertions.*;

class JesterTest {

    private Board board;
    private Map<Position, BasePiece> boardMap;

    @BeforeEach
    void initBeforeEachBoardTest() {
        board = new Board();
        boardMap = board.boardMap;
    }

    @Test
    void setupDirections_initPieceDirectionsIsEmpty_False(){
        BasePiece jester = new Jester(Colour.BLUE);
        assertNotEquals(0, jester.directions.length);
    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    void isLegalMove_jesterMovesToEmptySquare_True(Colour colour) {
        Board board = new Board();
        boardMap.clear();

        Position jesterPosition = BE2;

        BasePiece jester = new Jester(colour);
        boardMap.put(jesterPosition, jester);

        Set<Position> actualJesterMoves = jester.getHighlightPolygons(boardMap, jesterPosition);
        assertTrue(actualJesterMoves.contains(BF4));
    }

    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_jesterTakesItsColourPiece_False(BasePiece piece) {
        BasePiece jester = new Jester(piece.colour);

        boardMap.put(BE4, jester);
        boardMap.put(BC3, piece);

        Set<Position> actualJesterMoves = jester.getHighlightPolygons(boardMap, BE4);
        assertFalse(actualJesterMoves.contains(BC3));
    }

    @ParameterizedTest
    @MethodSource("model.DataProvider#pieceProvider")
    void isLegalMove_jesterTakesDifferentColourPiece_True(BasePiece piece) {
        BasePiece jester = new Jester(piece.colour.next());
        boardMap.put(BE4, jester);
        boardMap.put(BC3, piece);
        Set<Position> actualJesterMoves = jester.getHighlightPolygons(boardMap, BE4);
        assertTrue(actualJesterMoves.contains(BC3));
    }


    @ParameterizedTest
    @EnumSource(Colour.class)
    void getHighlightPolygons_validPolygons_presentInPolygonList(Colour colour){
        Board board = new Board();
        boardMap.clear();
        Position startPosition = BE4;

        BasePiece jester = new Jester(colour);
        boardMap.put(startPosition, jester);

        Set<Position> expectedJesterMoves = ImmutableSet.of(BG3, BF2, BD2, BC3, GF4, GE3, RB4, RC3, RF4, RE3);
        Set<Position> actualJesterMoves = jester.getHighlightPolygons(boardMap, startPosition);

        assertEquals(expectedJesterMoves, actualJesterMoves);
    }

    @ParameterizedTest
    @EnumSource(Colour.class)
    void toString_initJesterAllColours_correctStringFormat(Colour colour) {
        BasePiece jester = new Jester(colour);
        String expectedFormat = colour.toString() + "J";

        assertEquals(expectedFormat, jester.toString());
    }
}